package com.taest.analysis

import java.util.UUID

import com.taest.analysis.MonthlyIncomeStatistics.storeResultToDataBase
import commons.constant.Constants
import commons.model.MySqlConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * 河南省各个城市的流量
  */
object HACityStream {

  def main(args: Array[String]): Unit = {

    // 获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("HACityStream")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))

    /**
      * 景区表
      */
    val scenicInfoDF: DataFrame = readFromMysql(Constants.SCENIC_INFO_TABLE,sparkSession)

    /**
      * 各景区流量表
      */
    val monthStreamDF: DataFrame = readFromMysql(Constants.MONTH_STREAM_RESULT_TABLE,sparkSession)

    scenicInfoDF.createOrReplaceTempView("t_scenic_info")

    monthStreamDF.createOrReplaceTempView("t_month_stream")

    val sql = "SELECT s.city,i.time,SUM(i.count) as count FROM t_scenic_info s LEFT JOIN t_month_stream i ON s.sid = i.sid group by i.time,s.city"

    val HACityStreamDF: DataFrame = sparkSession.sql(sql)

    storeResultToDataBase(HACityStreamDF, Constants.HA_CITIES_STREAM_TABLE)

    sparkSession.stop()

  }

  /**
    * 从mysql读数据
    * @param dbName
    * @param sparkSession
    * @param mySqlConfig
    */
  def readFromMysql(dbName:String,sparkSession:SparkSession)(implicit mySqlConfig: MySqlConfig)={

    val df: DataFrame = sparkSession.read.format("jdbc")
      .option("url", mySqlConfig.url)
      .option("dbtable", dbName)
      .option("user", mySqlConfig.user)
      .option("password", mySqlConfig.password)
      .load()

    df

  }
}
