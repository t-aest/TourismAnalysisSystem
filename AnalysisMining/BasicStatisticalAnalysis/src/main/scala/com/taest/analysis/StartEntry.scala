package com.taest.analysis

import java.util.UUID

import com.taest.analysis.HotCities.getScenicInfo
import com.taest.analysis.MainAnalysis.{getVisitorInfo, getVisitorRecordInfoByVid}
import com.taest.analysis.MonthlyIncomeStatistics.{getVisitorRecordInfo, storeResultToDataBase}
import com.taest.analysis.PopularTouristSource.getYearFromDate
import commons.constant.Constants
import commons.model.{CitiesPopularScenicResult, HotCitiesResult, MySqlConfig}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object StartEntry {
  def main(args: Array[String]): Unit = {

    // 获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("DG")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    //获取游客记录信息 sid,item(vid,sid,time)
    val visitorRecordInfoRDD: RDD[(String, Row)] = getVisitorRecordInfo(sparkSession)

    // 获取景区信息 sid,city
    val ScenicInfoRDD = getScenicInfo(sparkSession)

    val ScenicInfoByTakeRDD = getScenicInfoByTake(sparkSession)
    //获取游客信息
    val visitorInfoRDD: RDD[(String, String)] = getVisitorInfo(sparkSession).cache()

    //获取游客记录信息
    val visitorsRecordInfoByVidRDD: RDD[(String, Row)] = getVisitorRecordInfoByVid(sparkSession).cache()

    /**
      * 按年 城市
      */
    //    val HotsCitiesOfYearDF: DataFrame = getHotsCitiesOfYearDF(taskUUID, sparkSession, visitorRecordInfoRDD, ScenicInfoRDD)
    //    val HotsCitiesDF: DataFrame = getHotsCitiesDF(taskUUID, sparkSession, visitorRecordInfoRDD, ScenicInfoRDD)
    //    HotsCitiesOfYearDF.show(10)
    //    HotsCitiesDF.show(10)
//    val HotsScenicDF: DataFrame = getHotsScenicDF(taskUUID, sparkSession, visitorRecordInfoRDD, ScenicInfoRDD)
//    val HotsResidenceDF: DataFrame = getHotsResidenceDF(taskUUID, sparkSession, visitorsRecordInfoByVidRDD, visitorInfoRDD)
//    val CitesIncomeDF: DataFrame = getCitesIncomeDF(taskUUID, sparkSession, visitorRecordInfoRDD, ScenicInfoByTakeRDD)
    val CitesStreamDF: DataFrame = getCitesStreamDF(taskUUID, sparkSession, visitorRecordInfoRDD, ScenicInfoByTakeRDD)

    CitesStreamDF.show(10)
    sparkSession.stop()

  }


  def getHotsCitiesOfYearDF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], ScenicInfoRDD: RDD[(String, String)]) = {
    import sparkSession.implicits._
    val HotsCitiesRDD: RDD[((String, String, String), Integer)] = visitorRecordInfoRDD.join(ScenicInfoRDD).map {
      case (sid, (item, city)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        ((sid, time, city), 1)
    }
    HotsCitiesRDD.reduceByKey(_ + _).toDF()
  }

  /**
    * 热门城市
    *
    * @param taskUUID
    * @param sparkSession
    * @param visitorRecordInfoRDD
    * @param ScenicInfoRDD
    * @return
    */
  def getHotsCitiesDF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], ScenicInfoRDD: RDD[(String, String)]) = {
    import sparkSession.implicits._
    val HotsCitiesRDD: RDD[((String, String), Integer)] = visitorRecordInfoRDD.join(ScenicInfoRDD).map {
      case (sid, (item, city)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        ((time, city), 1)
    }
    HotsCitiesRDD.reduceByKey(_ + _).sortBy(_._2, false).toDF
  }

  /**
    * 热门景区
    *
    * @param taskUUID
    * @param sparkSession
    * @param visitorRecordInfoRDD
    * @param ScenicInfoRDD
    * @return
    */
  def getHotsScenicDF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], ScenicInfoRDD: RDD[(String, String)]) = {
    import sparkSession.implicits._
    val HotsCitiesRDD: RDD[((String, String), Integer)] = visitorRecordInfoRDD.join(ScenicInfoRDD).map {
      case (sid, (item, city)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        ((sid, time), 1)
    }
    HotsCitiesRDD.reduceByKey(_ + _).sortBy(_._2, false).toDF
  }

  /**
    * 热门常驻地
    * @param taskUUID
    * @param sparkSession
    * @param visitorRecordInfoRDD
    * @param ScenicInfoRDD
    * @return
    */
  def getHotsResidenceDF(taskUUID: String, sparkSession: SparkSession, visitorsRecordInfoByVidRDD: RDD[(String, Row)], visitorInfoRDD: RDD[(String, String)]) = {
    import sparkSession.implicits._
    val HotsResidencesRDD: RDD[((String, String), Integer)] = visitorsRecordInfoByVidRDD.join(visitorInfoRDD).map {
      case (vid, (item, residence)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        ((residence, time), 1)
    }
    HotsResidencesRDD.reduceByKey(_ + _).sortBy(_._2, false).toDF
  }

  /**
    * 各城市景区收入分析
    * @param taskUUID
    * @param sparkSession
    * @param visitorRecordInfoRDD
    * @param ScenicInfoRDD
    * @return
    */
  def getCitesIncomeDF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], ScenicInfoRDD: RDD[(String, Row)]) = {
    import sparkSession.implicits._
    val CitesIncomeRDD: RDD[((String, String), Double)] = visitorRecordInfoRDD.join(ScenicInfoRDD).map {
      case (sid, (item, scenicitem)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        val city: String = scenicitem.getAs[String]("city")
        val ticket: Double = scenicitem.getAs[String]("ticket").formatted("%.2f").toDouble
        ((city, time), ticket)
    }
    CitesIncomeRDD.reduceByKey(_+_).sortBy(_._2, false).toDF
  }

  /**
    * 各城市游客流量分析
    * @param taskUUID
    * @param sparkSession
    * @param visitorRecordInfoRDD
    * @param ScenicInfoRDD
    * @return
    */
  def getCitesStreamDF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], ScenicInfoRDD: RDD[(String, Row)]) = {
    import sparkSession.implicits._
    val CitesStreamRDD: RDD[((String, String), Integer)] = visitorRecordInfoRDD.join(ScenicInfoRDD).map {
      case (sid, (item, scenicitem)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        val city: String = scenicitem.getAs[String]("city")
        ((city, time), 1)
    }
    CitesStreamRDD.reduceByKey(_+_).sortBy(_._2, false).toDF
  }

  def getScenicInfoByTake(sparkSession: SparkSession) = {
    val sql: String = "select sid,city,ticket from scenic_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("sid"), item)
    })
  }
}
