package com.taest.analysis

import java.util.UUID

import com.taest.analysis.HACityStream.readFromMysql
import com.taest.analysis.MonthlyIncomeStatistics.storeResultToDataBase
import commons.constant.Constants
import commons.model.MySqlConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * 河南省各个城市的收入
  */
object HACityIncome {

  def main(args: Array[String]): Unit = {

    // 获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("HACityIncome")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))
    /**
      * 景区表
      */
    val scenicInfoDF: DataFrame = readFromMysql(Constants.SCENIC_INFO_TABLE,sparkSession)

    /**
      * 各景区收入表
      */
    val monthIncomeDF: DataFrame = readFromMysql(Constants.MONTH_INCOME_RESULT_TABLE,sparkSession)

    scenicInfoDF.createOrReplaceTempView("t_scenic_info")
    monthIncomeDF.createOrReplaceTempView("t_month_income")

    val sql = "SELECT s.city,i.time,SUM(i.taking) as taking FROM t_month_income i LEFT JOIN t_scenic_info s ON i.sid = s.sid group by i.time,s.city"

    val HACityIncomeDF: DataFrame = sparkSession.sql(sql)

    storeResultToDataBase(HACityIncomeDF, Constants.HA_CITIES_INCOME_TABLE)

    sparkSession.stop()

  }
}
