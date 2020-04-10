package com.taest.analysis

import java.util.UUID
import com.taest.analysis.MonthlyIncomeStatistics.{getVisitorRecordInfo, storeResultToDataBase}
import com.taest.analysis.PopularTouristSource.getYearFromDate
import commons.constant.Constants
import commons.model.{MySqlConfig, PopularScenicResult}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * 热门景区的统计分析
  */
object PopularScenic {

  def main(args: Array[String]): Unit = {

    // 获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("PopularScenic")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    //获取游客记录信息 sid,item(vid,sid,time)
    val visitorRecordInfoRDD: RDD[(String, Row)] = getVisitorRecordInfo(sparkSession).cache()

    /**
      * 需求 按月热门景区
      */
    val PopularScenicOfMonthTop10DF: DataFrame = getPopularScenicOfMonthTop10DF(sparkSession, taskUUID, visitorRecordInfoRDD)

    /**
      * 需求 按年热门景区
      */
    val PopularScenicOfYearTop10DF: DataFrame = getPopularScenicOfYearTop10DF(sparkSession, taskUUID, visitorRecordInfoRDD)

    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))

    //导入结果到数据库
    storeResultToDataBase(PopularScenicOfMonthTop10DF, Constants.POPULAR_SCENIC_TABLE)
    storeResultToDataBase(PopularScenicOfYearTop10DF, Constants.POPULAR_SCENIC_YEAR_TABLE)

    sparkSession.stop()

    sparkSession.close()

  }

  def getPopularScenicTop10DF(sparkSession: SparkSession, taskUUID: String, PopularScenicOfMonthRDD: RDD[(String, String)]) = {
    import sparkSession.implicits._
    PopularScenicOfMonthRDD.groupByKey().map {
      case (time, iterableSid) =>
        val sidMap: mutable.HashMap[String, Int] = new mutable.HashMap[String, Int]()
        for (sid <- iterableSid) {
          sidMap.get(sid) match {
            case None => sidMap(sid) = 1
            case Some(map) => sidMap(sid) = sidMap(sid) + 1
          }
        }
        (time, sidMap.toList.sortWith(_._2 > _._2).take(10).mkString("|"))
    }.flatMap {
      case (time, str) =>
        var arr = new ArrayBuffer[PopularScenicResult]()
        //        val strings: mutable.ArrayOps[String] = str.substring(1,str.lastIndexOf(")"))split("|")
        val strings: Array[String] = str.split("\\|")
        for (elem <- strings) {
          val sid_count: Array[String] = elem.substring(1, elem.length - 1).split("\\,")
          arr += PopularScenicResult(taskUUID, sid_count(0), time, sid_count(1).toInt)
        }
        arr
    }.toDF()
  }

  def getPopularScenicOfMonthTop10DF(sparkSession: SparkSession, taskUUID: String, visitorRecordInfoRDD: RDD[(String, Row)]) = {
    val HotCitiesOfMonthRDD: RDD[(String, String)] = visitorRecordInfoRDD.map {
      case (sid, item) =>
        val time: String = item.getAs[String]("time")
        (time, sid)
    }
    getPopularScenicTop10DF(sparkSession, taskUUID, HotCitiesOfMonthRDD)
  }

  def getPopularScenicOfYearTop10DF(sparkSession: SparkSession, taskUUID: String, visitorRecordInfoRDD: RDD[(String, Row)]) = {
    val HotCitiesOfMonthRDD: RDD[(String, String)] = visitorRecordInfoRDD.map {
      case (sid, item) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        (time, sid)
    }
    getPopularScenicTop10DF(sparkSession, taskUUID, HotCitiesOfMonthRDD)
  }
}
