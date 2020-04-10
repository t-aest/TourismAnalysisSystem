package com.taest.analysis

import commons.constant.Constants
import commons.model.{MySqlConfig, Scenic, Visitor, VisitorRecord}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}


/**
  * 读取txt数据到hive
  */
object DataLoader {

  val randomTicket = Array("31.5", "70", "120", "100", "43.9", "50", "25.8", "56", "62.8", "115", "39.8", "135", "96.5")

  def main(args: Array[String]): Unit = {
    //创建Spark配置
    val sparkConf: SparkConf = new SparkConf().setAppName("DataLoader").setMaster(Constants.config(Constants.SPARK_CORES))

    //创建Spark sql客户端
    val spark = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    import spark.implicits._
//    val VISITOR_RECORD_DATA_PATH = "D:\\data\\scenic\\"
    //加载数据
    val scenicRDD: RDD[String] = spark.sparkContext.textFile(Constants.SCENIC_DATA_PATH)
    val visitorRDD: RDD[String] = spark.sparkContext.textFile(Constants.VISITOR_DATA_PATH)
    val visitorRecordRDD: RDD[String] = spark.sparkContext.textFile(Constants.VISITOR_RECORD_DATA_PATH+args(0)+"\\*")
//    val visitorRecordRDD: RDD[String] = spark.sparkContext.textFile(Constants.TEST)

    val scenicDF: DataFrame = scenicRDD.filter(_.split("\\ ").length == 10).map(
      item => {
        val attr: Array[String] = item.split("\\ ")
        if (attr(5) == "nan")
          attr(5) = randomTicket(scala.util.Random.nextInt(randomTicket.length - 1))
        Scenic(attr(0).trim, attr(1).trim, attr(3).trim, attr(5).toDouble, attr(7).trim, attr(9).trim)
      }
    ).filter {
      case item =>
        item.city.length == 3 && item.city.toString.reverse.take(1) == "市"
    }.toDF()
    val visitorDF: DataFrame = visitorRDD.map(
      item => {
        val attr: Array[String] = item.split("\\ ")
        Visitor(attr(0).trim, attr(1).trim)
      }
    ).toDF()
    val visitorRecordDF: DataFrame = visitorRecordRDD.map(
      item => {
        val attr: Array[String] = item.split("\\ ")
        VisitorRecord(attr(0).trim, attr(1).trim, attr(2).trim)
      }
    ).toDF()

    //导入数据到hive
    insertHive(spark, Constants.SCENIC_INFO_TABLE, scenicDF)
    insertHive(spark, Constants.VISITOR_INFO_TABLE, visitorDF)
    insertHive(spark, Constants.VISITOR_RECORD_INFO_TABLE, visitorRecordDF)

    //导入景区表到mysql
    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))
    storeResultToDataBase(scenicDF, Constants.SCENIC_INFO_TABLE)
    spark.close
  }

  /**
    * 将DataFrame插入到Hive表中
    *
    * @param spark     SparkSQL客户端
    * @param tableName 表名
    * @param dataDF    DataFrame
    */
  private def insertHive(spark: SparkSession, tableName: String, dataDF: DataFrame): Unit = {
    spark.sql("DROP TABLE IF EXISTS " + tableName)
    dataDF.write.saveAsTable(tableName)
  }

  /**
    * 将DataFrame插入到mysql表中
    *
    * @param df         DataFrame
    * @param table_name 表名
    *
    */
  private def storeResultToDataBase(df: DataFrame, table_name: String)(implicit mySqlConfig: MySqlConfig): Unit = {
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
