package com.taest.analysis

import java.util.UUID
import com.taest.analysis.MonthlyIncomeStatistics.{getVisitorRecordInfo, storeResultToDataBase}
import com.taest.analysis.PopularScenic.{getPopularScenicOfMonthTop10DF, getPopularScenicOfYearTop10DF}
import com.taest.analysis.PopularTouristSource.{getVisitorInfo, getYearFromDate}
import commons.constant.Constants
import commons.model.{MySqlConfig, PopularScenicResidenceResult}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * 热门景区的热门客源地统计分析
  */
object PopularResidenceOfPopularScenic {

  def main(args: Array[String]): Unit = {

    // 获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("PopularResidenceOfPopularScenic")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    //获取游客记录信息 sid,item(vid,sid,time)
    val visitorRecordInfoRDD: RDD[(String, Row)] = getVisitorRecordInfo(sparkSession).cache()

    //获取游客信息
    val visitorInfoRDD: RDD[(String, String)] = getVisitorInfo(sparkSession).cache()

    /**
      *  按月热门景区热门客源地
      */
    val PopularScenicResidenceOfMonthTop10DF: DataFrame = getPopularScenicOfMonthTop10DF(sparkSession, taskUUID, visitorRecordInfoRDD)

    val PopularScenicOfMonthTop10RDD: RDD[(String, String)] = PopularScenicResidenceOfMonthTop10DF.rdd.map {
      case item =>
        (item.getAs[String]("sid"), item.getAs[String]("time"))
    }.cache()
    val PopularResidenceOfPopularScenicMonthTop10DF: DataFrame = getPopularResidenceOfPopularScenicMonthTop10DF(taskUUID,sparkSession,visitorRecordInfoRDD,PopularScenicOfMonthTop10RDD,visitorInfoRDD)

    /**
      * 按年热门景区热门客源地
      */
    val PopularScenicOfYearTop10RDD: RDD[(String, String)] = getPopularScenicOfYearTop10DF(sparkSession, taskUUID, visitorRecordInfoRDD).rdd.map{
      case item =>
        (item.getAs[String]("sid"), item.getAs[String]("time"))
    }.cache()

    val PopularResidenceOfPopularScenicYearTop10DF: DataFrame = getPopularResidenceOfPopularScenicYearTop10DF(taskUUID,sparkSession,visitorRecordInfoRDD,PopularScenicOfYearTop10RDD,visitorInfoRDD)



    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))

    //导入结果到数据库
    storeResultToDataBase(PopularResidenceOfPopularScenicMonthTop10DF, Constants.POPULAR_SCENIC_RESIDENCE_TABLE)
    storeResultToDataBase(PopularResidenceOfPopularScenicYearTop10DF, Constants.POPULAR_SCENIC_RESIDENCE_YEAR_TABLE)

    sparkSession.stop()
  }

  def getPopularResidenceOfPopularScenicTop10DF(taskUUID: String, sparkSession: SparkSession, PopularResidenceOfPopularScenicMonthTop10RDD: RDD[((String, String), String)]) = {

    import sparkSession.implicits._
    PopularResidenceOfPopularScenicMonthTop10RDD.groupByKey().map {
      case ((sid,time),iterableResidence) =>
        val residenceMap: mutable.HashMap[String,Int] = new mutable.HashMap[String, Int]()
        for (residence <- iterableResidence) {
          residenceMap.get(residence) match {
            case None => residenceMap(residence) = 1
            case Some(map) => residenceMap(residence) = residenceMap(residence) + 1
          }
        }
        ((sid,time), residenceMap.toList.sortWith(_._2 > _._2).take(10).mkString("|"))
    }.flatMap {
      case ((sid,time), str) =>
        var arr = new ArrayBuffer[PopularScenicResidenceResult]()
        //        val strings: mutable.ArrayOps[String] = str.substring(1,str.lastIndexOf(")"))split("|")
        val strings: Array[String] = str.split("\\|")
        for (elem <- strings) {
          val sid_count: Array[String] = elem.substring(1, elem.length - 1).split("\\,")
          arr += PopularScenicResidenceResult(taskUUID, sid, time, sid_count(0),sid_count(1).toInt)
        }
        arr
    }.toDF()
  }

  def getPopularResidenceOfPopularScenicMonthTop10DF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], PopularScenicOfMonthTop10RDD: RDD[(String, String)], visitorInfoRDD: RDD[(String, String)]) = {
    val PopularResidenceOfPopularScenicMonthTop10RDD: RDD[((String, String), String)] = visitorRecordInfoRDD
      .join(PopularScenicOfMonthTop10RDD).map {
      case (sid, (item, time)) =>
        (item.getAs[String]("vid"), (sid, time))
    }.join(visitorInfoRDD).map {
      case (vid, ((sid, time), residence)) =>
        ((sid, time), residence)
    }
    getPopularResidenceOfPopularScenicTop10DF(taskUUID,sparkSession,PopularResidenceOfPopularScenicMonthTop10RDD)
  }

  def getPopularResidenceOfPopularScenicYearTop10DF(taskUUID: String, sparkSession: SparkSession,visitorRecordInfoRDD: RDD[(String, Row)], PopularScenicOfMonthTop10RDD: RDD[(String, String)], visitorInfoRDD: RDD[(String, String)]) = {
    val PopularResidenceOfPopularScenicMonthTop10RDD: RDD[((String, String), String)] = visitorRecordInfoRDD
      .join(PopularScenicOfMonthTop10RDD).map {
      case (sid, (item, time)) =>
        (item.getAs[String]("vid"), (sid, time))
    }.join(visitorInfoRDD).map {
      case (vid, ((sid, time), residence)) =>
        ((sid, getYearFromDate(time)), residence)
    }
    getPopularResidenceOfPopularScenicTop10DF(taskUUID,sparkSession,PopularResidenceOfPopularScenicMonthTop10RDD)
  }
}
