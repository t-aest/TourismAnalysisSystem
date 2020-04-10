package com.taest.analysis

import java.util.UUID
import commons.constant.Constants
import com.taest.analysis.HotCities.getScenicInfo
import com.taest.analysis.MonthlyIncomeStatistics.{getVisitorRecordInfo, storeResultToDataBase}
import com.taest.analysis.PopularTouristSource.getYearFromDate
import commons.model.{CitiesPopularScenicResult, HotCitiesResult, MySqlConfig}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * 各个城市的热门景区的统计分析
  */
object CitiesPopularScenic {

  def main(args: Array[String]): Unit = {

    // 获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("CitiesPopularScenic")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    //获取游客记录信息 sid,item(vid,sid,time)
    val visitorRecordInfoRDD: RDD[(String, Row)] = getVisitorRecordInfo(sparkSession)

    // 获取景区信息 sid,city
    val ScenicInfoRDD = getScenicInfo(sparkSession)

    /**
      * 按月 城市
      */
    val HotsCitiesOfMonthDF: DataFrame = getHotsCitiesOfMonthDF(taskUUID, sparkSession, visitorRecordInfoRDD, ScenicInfoRDD)
    HotsCitiesOfMonthDF.show(10)
    val HotsCitiesOfMonthRDD: RDD[(String, String)] = HotsCitiesOfMonthDF.rdd.map {
      case item =>
        (item.getAs[String]("city"), item.getAs[String]("time"))
    }
    val CitiesOfMonthScenicTop10DF: DataFrame = getCitiesOfMonthScenicTop10DF(taskUUID, sparkSession, visitorRecordInfoRDD, HotsCitiesOfMonthRDD, ScenicInfoRDD)

    /**
      * 按年 城市
      */
    val HotsCitiesOfYearDF: DataFrame = getHotsCitiesOfYearDF(taskUUID, sparkSession, visitorRecordInfoRDD, ScenicInfoRDD)
    HotsCitiesOfYearDF.show(10)
    val HotsCitiesOfYearRDD: RDD[(String, String)] = HotsCitiesOfYearDF.rdd.map {
      case item =>
        (item.getAs[String]("city"), item.getAs[String]("time"))
    }
    val CitiesOfYearScenicTop10DF: DataFrame = getCitiesOfYearScenicTop10DF(taskUUID, sparkSession, visitorRecordInfoRDD, HotsCitiesOfYearRDD, ScenicInfoRDD)

    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))

    //导入结果到数据库
    storeResultToDataBase(CitiesOfMonthScenicTop10DF, Constants.CITIES_POPULAR_SCENIC_TABLE)
    storeResultToDataBase(CitiesOfYearScenicTop10DF, Constants.CITIES_POPULAR_SCENIC_YEAR_TABLE)

    sparkSession.close()
    sparkSession.stop()


  }

  def getCitiesPopularScenicTop10DF(taskUUID: String, sparkSession: SparkSession, CitiesScenicRDD: RDD[((String, String), String)]) = {
    import sparkSession.implicits._
    CitiesScenicRDD.groupByKey().map {
      case ((city, time), iterableSid) =>
        val sidMap: mutable.HashMap[String, Int] = new mutable.HashMap[String, Int]()
        for (sid <- iterableSid) {
          sidMap.get(sid) match {
            case None => sidMap(sid) = 1
            case Some(map) => sidMap(sid) = sidMap(sid) + 1
          }
        }
        ((city, time), sidMap.toList.sortWith(_._2 > _._2).take(10).mkString("|"))
    }.flatMap {
      case ((city, time), str) =>
        var arr = new ArrayBuffer[CitiesPopularScenicResult]()
        //        val strings: mutable.ArrayOps[String] = str.substring(1,str.lastIndexOf(")"))split("|")
        val strings: Array[String] = str.split("\\|")
        for (elem <- strings) {
          val sid_count: Array[String] = elem.substring(1, elem.length - 1).split("\\,")
          arr += CitiesPopularScenicResult(taskUUID, city, time, sid_count(0), sid_count(1).toInt)
        }
        arr
    }.toDF()
  }

  def getCitiesOfMonthScenicTop10DF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], HotsCitiesOfMonthRDD: RDD[(String, String)], ScenicInfoRDD: RDD[(String, String)]) = {
    val CitiesOfMonthScenicRDD: RDD[((String, String), String)] = ScenicInfoRDD.map(_.swap).join(HotsCitiesOfMonthRDD).map {
      case (city, (sid, time)) =>
        (sid, (city, time))
    }.join(visitorRecordInfoRDD).map {
      case (sid, ((city, time), item)) =>
        ((city, time), sid)
    }
    getCitiesPopularScenicTop10DF(taskUUID, sparkSession, CitiesOfMonthScenicRDD)
  }

  def getCitiesOfYearScenicTop10DF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], HotsCitiesOfYearRDD: RDD[(String, String)], ScenicInfoRDD: RDD[(String, String)]) = {
    val CitiesOfYearScenicRDD: RDD[((String, String), String)] = ScenicInfoRDD.map(_.swap).join(HotsCitiesOfYearRDD).map {
      case (city, (sid, time)) =>
        (sid, (city, time))
    }.join(visitorRecordInfoRDD).map {
      case (sid, ((city, time), item)) =>
        ((city, getYearFromDate(time)), sid)
    }
    getCitiesPopularScenicTop10DF(taskUUID, sparkSession, CitiesOfYearScenicRDD)
  }

  def getHotsCitiesDF(taskUUID: String, sparkSession: SparkSession, HotsCitiesRDD: RDD[(String, String)]) = {
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
        (time, cityMap.toList.sortWith(_._2 > _._2).mkString("|"))
    }.flatMap {
      case (time, str) =>
        var arr = new ArrayBuffer[HotCitiesResult]()
        val strings: Array[String] = str.split("\\|")
        for (elem <- strings) {
          val city_count: Array[String] = elem.substring(1, elem.length - 1).split("\\,")
          arr += HotCitiesResult(taskUUID, city_count(0), time, city_count(1).toInt)
        }
        arr
    }.toDF()
  }

  def getHotsCitiesOfMonthDF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], ScenicInfoRDD: RDD[(String, String)]) = {
    val HotsCitiesRDD: RDD[(String, String)] = visitorRecordInfoRDD.join(ScenicInfoRDD).map {
      case (sid, (item, city)) =>
        val time: String = item.getAs[String]("time")
        (time, city)
    }
    getHotsCitiesDF(taskUUID, sparkSession, HotsCitiesRDD)
  }

  def getHotsCitiesOfYearDF(taskUUID: String, sparkSession: SparkSession, visitorRecordInfoRDD: RDD[(String, Row)], ScenicInfoRDD: RDD[(String, String)]) = {
    val HotsCitiesRDD: RDD[(String, String)] = visitorRecordInfoRDD.join(ScenicInfoRDD).map {
      case (sid, (item, city)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        (time, city)
    }
    getHotsCitiesDF(taskUUID, sparkSession, HotsCitiesRDD)
  }
}
