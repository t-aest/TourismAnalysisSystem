package com.taest.analysis

import java.util.UUID
import com.taest.analysis.MonthlyIncomeStatistics.{getVisitorRecordInfo, storeResultToDataBase}
import com.taest.analysis.PopularTouristSource.getYearFromDate
import commons.constant.Constants
import commons.model.{HotCitiesResult, MySqlConfig}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * 热门城市的统计分析
  */
object HotCities {

  def main(args: Array[String]): Unit = {

    // 获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("HotCities")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    //获取游客记录信息 sid,item(vid,sid,time)
    val visitorRecordInfoRDD: RDD[(String, Row)] = getVisitorRecordInfo(sparkSession)

    // 获取景区信息 sid,city
    val ScenicInfoRDD = getScenicInfo(sparkSession)

    /**
      * 按月 热门城市
      */
    val HotsCitiesOfMonthTop10DF: DataFrame = getHotsCitiesOfMonthTop10DF(taskUUID, sparkSession, visitorRecordInfoRDD, ScenicInfoRDD)

    /**
      * 按年 热门城市
      */
    val HotsCitiesOfYearTop10DF: DataFrame = getHotsCitiesOfYearTop10DF(taskUUID, sparkSession, visitorRecordInfoRDD, ScenicInfoRDD)

    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))

    //导入结果到数据库
    storeResultToDataBase(HotsCitiesOfMonthTop10DF, Constants.HOT_CITIES_TABLE)
    storeResultToDataBase(HotsCitiesOfYearTop10DF, Constants.HOT_CITIES_YEAR_TABLE)

    sparkSession.stop()
  }

  def getHotsCitiesTop10DF(taskUUID: String, sparkSession: SparkSession, HotsCitiesRDD: RDD[(String, String)]) = {
    import sparkSession.implicits._
    HotsCitiesRDD.groupByKey().map {
      case (time, iterableCity) =>
        val cityMap: mutable.HashMap[String, Int] = new mutable.HashMap[String, Int]()
        for (sid <- iterableCity) {
          cityMap.get(sid) match {
            case None => cityMap(sid) = 1
            case Some(map) => cityMap(sid) = cityMap(sid) + 1
          }
        }
        (time, cityMap.toList.sortWith(_._2 > _._2).take(10).mkString("|"))
    }.flatMap {
      case (time, str) =>
        var arr = new ArrayBuffer[HotCitiesResult]()
        //        val strings: mutable.ArrayOps[String] = str.substring(1,str.lastIndexOf(")"))split("|")
        val strings: Array[String] = str.split("\\|")
        for (elem <- strings) {
          val city_count: Array[String] = elem.substring(1, elem.length - 1).split("\\,")
          arr += HotCitiesResult(taskUUID, city_count(0), time, city_count(1).toInt)
        }
        arr
    }.toDF()
  }

  def getHotsCitiesOfMonthTop10DF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], ScenicInfoRDD: RDD[(String, String)]) = {
    val HotsCitiesRDD: RDD[(String, String)] = visitorRecordInfoRDD.join(ScenicInfoRDD).map {
      case (sid, (item, city)) =>
        val time: String = item.getAs[String]("time")
        (time, city)
    }
    getHotsCitiesTop10DF(taskUUID, sparkSession, HotsCitiesRDD)
  }

  def getHotsCitiesOfYearTop10DF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], ScenicInfoRDD: RDD[(String, String)]) = {
    val HotsCitiesRDD: RDD[(String, String)] = visitorRecordInfoRDD.join(ScenicInfoRDD).map {
      case (sid, (item, city)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        (time, city)
    }
    getHotsCitiesTop10DF(taskUUID, sparkSession, HotsCitiesRDD)
  }

  def getScenicInfo(sparkSession: SparkSession) = {
    val sql: String = "select sid,city from scenic_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("sid"), item.getAs[String]("city"))
    })
  }
}
