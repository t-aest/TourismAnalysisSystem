package com.taest.analysis

import java.util.UUID
import commons.constant.Constants
import commons.model.{MonthlyIncomeStatisticsResult, MySqlConfig}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

/**
  * 景区月收入统计分析
  */
object MonthlyIncomeStatistics {

  def main(args: Array[String]): Unit = {

    //获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("MonthlyIncomeStatistics")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    //获取景区信息 sid,item(sid,ticket)
    val scenicInfoRDD: RDD[(String, Row)] = getScenicInfo(sparkSession)

    //获取游客记录信息 sid,item(vid,sid,time)
    val visitorRecordInfoRDD: RDD[(String, Row)] = getVisitorRecordInfo(sparkSession)

    val userRecordJoinScenicRDD: RDD[(String, (String, String, Double))] = visitorRecordInfoRDD.join(scenicInfoRDD).map {
      case (sid, item) =>
        val vid: String = item._1.getAs[String]("vid")
        val time: String = item._1.getAs[String]("time")
        val ticket: Double = item._2.getAs[Double]("ticket")
        (sid, (vid, time, ticket))
    }.cache()

    val scenicPerMonthlyTaking: RDD[(String, String, Double)] = userRecordJoinScenicRDD.map {
      case (sid, item) =>
        ((sid, item._2), item._3)
    }.groupByKey().map {
      case ((sid, time), ticketiter) =>
        val taking: Double = ticketiter.sum.formatted("%.2f").toDouble
        (sid, time, taking)
    }.sortBy(_._3, ascending = false)

    import sparkSession.implicits._

    val scenicPerMonthlyTakingDF: DataFrame = scenicPerMonthlyTaking.map(
      item => MonthlyIncomeStatisticsResult(taskUUID,item._1, item._2, item._3)
    ).toDF()

    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))

    storeResultToDataBase(scenicPerMonthlyTakingDF, Constants.MONTH_INCOME_RESULT_TABLE)

    sparkSession.stop()
  }

  def getScenicInfo(sparkSession: SparkSession) = {
    val sql: String = "select sid,ticket from scenic_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("sid"), item)
    })
  }

  /**
    * 以景区id为key
    * @param sparkSession
    * @return
    */
  def getVisitorRecordInfo(sparkSession: SparkSession) = {
    val sql: String = "select vid,sid,time from visitor_record_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("sid"), item)
    })
  }

  def storeResultToDataBase(df: DataFrame, table_name: String)(implicit mySqlConfig: MySqlConfig): Unit = {
    df.write
      .format("jdbc")
      .option("url", mySqlConfig.url)
      .option("user", mySqlConfig.user)
      .option("password", mySqlConfig.password)
      .option("dbtable", table_name)
      .mode(SaveMode.Append)
      .save()
  }
}
