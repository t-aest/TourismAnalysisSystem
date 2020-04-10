package com.taest.analysis

import java.util.UUID
import com.taest.analysis.MonthlyIncomeStatistics.{getVisitorRecordInfo, storeResultToDataBase}
import commons.constant.Constants
import commons.model.{MonthlyStreamStatisticsResult, MySqlConfig}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
  * 景区月流量统计分析
  */
object MonthlyStreamStatistics {

  def main(args: Array[String]): Unit = {

    //获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("MonthlyStreamStatistics")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    //获取游客记录信息 sid,item(vid,sid,time)
    val visitorRecordInfoRDD: RDD[(String, Row)] = getVisitorRecordInfo(sparkSession)

    /**
      * 景区月流量统计需求二
      */
    import sparkSession.implicits._
    val scenicPerMonthlyCountDF: DataFrame = visitorRecordInfoRDD.map {
      case (sid, row) =>
        ((sid, row.getAs[String]("time")), 1)
    }.reduceByKey(_ + _).sortBy(_._2, ascending = false).map {
      case ((sid, time), count) =>
        MonthlyStreamStatisticsResult(taskUUID, sid, time, count)
    }.toDF()

    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))

    storeResultToDataBase(scenicPerMonthlyCountDF, Constants.MONTH_STREAM_RESULT_TABLE)

    sparkSession.stop()
  }
}
