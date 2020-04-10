package com.taest.analysis

import java.util.UUID
import com.taest.analysis.MonthlyIncomeStatistics.storeResultToDataBase
import commons.constant.Constants
import commons.model.{MySqlConfig, PopularTouristSourceResult}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * 热门客源地的统计分析
  */
object PopularTouristSource {

  def main(args: Array[String]): Unit = {

    //获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("PopularTouristSource")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    //获取游客信息
    val visitorInfoRDD: RDD[(String, String)] = getVisitorInfo(sparkSession).cache()
    //获取游客记录信息
    val visitorRecordInfoRDD: RDD[(String, Row)] = getVisitorRecordInfo(sparkSession).cache()

    /**
      * 需求 按月的热门常驻地
      */
    val PopularTouristSourceOfMonthTop10DF: DataFrame = getPopularTouristSourceOfMonthTop10DF(sparkSession, taskUUID, visitorInfoRDD, visitorRecordInfoRDD)
    /**
      * 需求 按年的热门常驻地
      */
    val PopularTouristSourceOfYearTop10DF: DataFrame = getPopularTouristSourceOfYearTop10DF(sparkSession, taskUUID, visitorInfoRDD, visitorRecordInfoRDD)

    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))

    //导入结果到数据库
    storeResultToDataBase(PopularTouristSourceOfMonthTop10DF, Constants.POPULAR_TOURIST_SOURCE_TABLE)
    storeResultToDataBase(PopularTouristSourceOfYearTop10DF, Constants.POPULAR_TOURIST_SOURCE_YEAR_TABLE)

    sparkSession.stop()
  }

  def getPopularTouristSourceTop10DF(sparkSession: SparkSession, taskUUID: String, visitorJoinRecordInfoRDD: RDD[(String, String)]) = {
    import sparkSession.implicits._
    visitorJoinRecordInfoRDD.groupByKey().map {
      case (time, iterableResidence) =>
        val residenceMap: mutable.HashMap[String, Int] = new mutable.HashMap[String, Int]()
        for (residence <- iterableResidence) {
          residenceMap.get(residence) match {
            case None => residenceMap(residence) = 1
            case Some(map) => residenceMap(residence) = residenceMap(residence) + 1
          }
        }
        (time, residenceMap.toList.sortWith(_._2 > _._2).take(10).mkString("|"))
    }.flatMap {
      case (time, str) =>
        var arr = new ArrayBuffer[PopularTouristSourceResult]()
        //        val strings: mutable.ArrayOps[String] = str.substring(1,str.lastIndexOf(")"))split("|")
        val strings: Array[String] = str.split("\\|")
        for (elem <- strings) {
          val residence_count: Array[String] = elem.substring(1, elem.length - 1).split("\\,")
          arr += PopularTouristSourceResult(taskUUID, residence_count(0), time, residence_count(1).toInt)
        }
        arr
    }.toDF()
  }

  def getPopularTouristSourceOfMonthTop10DF(sparkSession: SparkSession, taskUUID: String, visitorInfoRDD: RDD[(String, String)], visitorRecordInfoRDD: RDD[(String, Row)]): DataFrame = {

    val visitorJoinRecordInfoRDD: RDD[(String, String)] = visitorRecordInfoRDD.join(visitorInfoRDD).map {
      case (vid, (item, residence)) =>
        val time: String = item.getAs[String]("time")
        (time, residence)
    }
    getPopularTouristSourceTop10DF(sparkSession, taskUUID, visitorJoinRecordInfoRDD)
  }

  def getPopularTouristSourceOfYearTop10DF(sparkSession: SparkSession, taskUUID: String, visitorInfoRDD: RDD[(String, String)], visitorRecordInfoRDD: RDD[(String, Row)]) = {
    val visitorJoinRecordInfoRDD: RDD[(String, String)] = visitorRecordInfoRDD.join(visitorInfoRDD).map {
      case (vid, (item, residence)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        (time, residence)
    }
    getPopularTouristSourceTop10DF(sparkSession, taskUUID, visitorJoinRecordInfoRDD)
  }

  def getVisitorInfo(sparkSession: SparkSession) = {
    val sql: String = "select vid,residence from visitor_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("vid"), item.getAs[String]("residence"))
    })
  }

  /**
    * 以游客id为key
    *
    * @param sparkSession
    * @return
    */
  def getVisitorRecordInfo(sparkSession: SparkSession) = {
    val sql: String = "select vid,sid,time from visitor_record_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("vid"), item)
    })
  }

  /**
    * 输入时间取年份
    *
    * @param time
    * @return
    */
  def getYearFromDate(time: String): String = {
    return time.substring(0, 4)
  }

}
