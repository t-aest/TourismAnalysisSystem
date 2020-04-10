package com.taest.analysis

import java.util.UUID

import commons.constant.Constants
import commons.model._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object MainAnalysis {

  def main(args: Array[String]): Unit = {
    // 获取唯一主键
    val taskUUID: String = UUID.randomUUID().toString

    // 创建一个sparkConf
    val sparkConf = new SparkConf().setMaster(Constants.config(Constants.SPARK_CORES)).setAppName("MainAnalysis")

    // 创建一个SparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()


    //获取游客记录信息 sid,item(vid,sid,time)
    val visitorRecordInfoBySidRDD: RDD[(String, Row)] = getVisitorRecordInfoBySid(sparkSession).cache()

    //获取游客记录信息
    val visitorsRecordInfoByVidRDD: RDD[(String, Row)] = getVisitorRecordInfoByVid(sparkSession).cache()

    // 获取景区信息 sid,city
    val ScenicInfoRDD = getScenicInfo(sparkSession).cache()

    //获取景区信息 sid,item(sid,ticket)
    val scenicInfoOnMonthRDD: RDD[(String, Row)] = getScenicInfoOnMonth(sparkSession).cache()

    //获取游客信息
    val visitorInfoRDD: RDD[(String, String)] = getVisitorInfo(sparkSession).cache()
    implicit val mysqlConfig = MySqlConfig(Constants.config(Constants.JDBC_URL), Constants.config(Constants.JDBC_USER), Constants.config(Constants.JDBC_PASSWORD))


    /**
      * 各个城市的热门景区的统计分析
      */
    /**
      * 按月 城市
      */
    val HotsCitiesOfMonthDF: DataFrame = getHotsCitiesOfMonthDF(taskUUID, sparkSession, visitorRecordInfoBySidRDD, ScenicInfoRDD)
    HotsCitiesOfMonthDF.show(10)
    val HotsCitiesOfMonthRDD: RDD[(String, String)] = HotsCitiesOfMonthDF.rdd.map {
      case item =>
        (item.getAs[String]("city"), item.getAs[String]("time"))
    }
    val CitiesOfMonthScenicTop10DF: DataFrame = getCitiesOfMonthScenicTop10DF(taskUUID, sparkSession, visitorRecordInfoBySidRDD, HotsCitiesOfMonthRDD, ScenicInfoRDD)

    /**
      * 按年 城市
      */
    val HotsCitiesOfYearDF: DataFrame = getHotsCitiesOfYearDF(taskUUID, sparkSession, visitorRecordInfoBySidRDD, ScenicInfoRDD)
    HotsCitiesOfYearDF.show(10)
    val HotsCitiesOfYearRDD: RDD[(String, String)] = HotsCitiesOfYearDF.rdd.map {
      case item =>
        (item.getAs[String]("city"), item.getAs[String]("time"))
    }
    val CitiesOfYearScenicTop10DF: DataFrame = getCitiesOfYearScenicTop10DF(taskUUID, sparkSession, visitorRecordInfoBySidRDD, HotsCitiesOfYearRDD, ScenicInfoRDD)
    //导入结果到数据库
    storeResultToDataBase(CitiesOfMonthScenicTop10DF, Constants.CITIES_POPULAR_SCENIC_TABLE)
    storeResultToDataBase(CitiesOfYearScenicTop10DF, Constants.CITIES_POPULAR_SCENIC_YEAR_TABLE)
    /**
      * 热门城市的统计分析
      */
    /**
      * 按月 热门城市
      */
    val HotsCitiesOfMonthTop10DF: DataFrame = getHotsCitiesOfMonthTop10DF(taskUUID, sparkSession, visitorRecordInfoBySidRDD, ScenicInfoRDD)

    /**
      * 按年 热门城市
      */
    val HotsCitiesOfYearTop10DF: DataFrame = getHotsCitiesOfYearTop10DF(taskUUID, sparkSession, visitorRecordInfoBySidRDD, ScenicInfoRDD)
    //导入结果到数据库
    storeResultToDataBase(HotsCitiesOfMonthTop10DF, Constants.HOT_CITIES_TABLE)
    storeResultToDataBase(HotsCitiesOfYearTop10DF, Constants.HOT_CITIES_YEAR_TABLE)


    /**
      * 热门景区的统计分析
      */
    /**
      * 需求 按月热门景区
      */
    val PopularScenicOfMonthTop10DF: DataFrame = getPopularScenicOfMonthTop10DF(sparkSession, taskUUID, visitorRecordInfoBySidRDD)

    /**
      * 需求 按年热门景区
      */
    val PopularScenicOfYearTop10DF: DataFrame = getPopularScenicOfYearTop10DF(sparkSession, taskUUID, visitorRecordInfoBySidRDD)
    //导入结果到数据库
    storeResultToDataBase(PopularScenicOfMonthTop10DF, Constants.POPULAR_SCENIC_TABLE)
    storeResultToDataBase(PopularScenicOfYearTop10DF, Constants.POPULAR_SCENIC_YEAR_TABLE)

    /**
      * 热门景区的热门客源地统计分析
      */
    /**
      *  按月热门景区热门客源地
      */
    val PopularScenicResidenceOfMonthTop10DF: DataFrame = getPopularScenicOfMonthTop10DF(sparkSession, taskUUID, visitorRecordInfoBySidRDD)

    val PopularScenicOfMonthTop10RDD: RDD[(String, String)] = PopularScenicResidenceOfMonthTop10DF.rdd.map {
      case item =>
        (item.getAs[String]("sid"), item.getAs[String]("time"))
    }.cache()
    val PopularResidenceOfPopularScenicMonthTop10DF: DataFrame = getPopularResidenceOfPopularScenicMonthTop10DF(taskUUID,sparkSession,visitorRecordInfoBySidRDD,PopularScenicOfMonthTop10RDD,visitorInfoRDD)

    /**
      * 按年热门景区热门客源地
      */
    val PopularScenicOfYearTop10RDD: RDD[(String, String)] = getPopularScenicOfYearTop10DF(sparkSession, taskUUID, visitorRecordInfoBySidRDD).rdd.map{
      case item =>
        (item.getAs[String]("sid"), item.getAs[String]("time"))
    }.cache()

    val PopularResidenceOfPopularScenicYearTop10DF: DataFrame = getPopularResidenceOfPopularScenicYearTop10DF(taskUUID,sparkSession,visitorRecordInfoBySidRDD,PopularScenicOfYearTop10RDD,visitorInfoRDD)
    //导入结果到数据库
    storeResultToDataBase(PopularResidenceOfPopularScenicMonthTop10DF, Constants.POPULAR_SCENIC_RESIDENCE_TABLE)
    storeResultToDataBase(PopularResidenceOfPopularScenicYearTop10DF, Constants.POPULAR_SCENIC_RESIDENCE_YEAR_TABLE)
    /**
      * 热门客源地的统计分析
      */
    /**
      * 需求 按月的热门常驻地
      */
    val PopularTouristSourceOfMonthTop10DF: DataFrame = getPopularTouristSourceOfMonthTop10DF(sparkSession, taskUUID, visitorInfoRDD, visitorsRecordInfoByVidRDD)
    /**
      * 需求 按年的热门常驻地
      */
    val PopularTouristSourceOfYearTop10DF: DataFrame = getPopularTouristSourceOfYearTop10DF(sparkSession, taskUUID, visitorInfoRDD, visitorsRecordInfoByVidRDD)
    //导入结果到数据库
    storeResultToDataBase(PopularTouristSourceOfMonthTop10DF, Constants.POPULAR_TOURIST_SOURCE_TABLE)
    storeResultToDataBase(PopularTouristSourceOfYearTop10DF, Constants.POPULAR_TOURIST_SOURCE_YEAR_TABLE)

    /**
      * 景区月收入统计分析
      */
    val userRecordJoinScenicRDD: RDD[(String, (String, String, Double))] = visitorRecordInfoBySidRDD.join(scenicInfoOnMonthRDD).map {
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
    storeResultToDataBase(scenicPerMonthlyTakingDF, Constants.MONTH_INCOME_RESULT_TABLE)

    /**
      * 景区月流量统计分析
      */
    val scenicPerMonthlyCountDF: DataFrame = visitorRecordInfoBySidRDD.map {
      case (sid, row) =>
        ((sid, row.getAs[String]("time")), 1)
    }.reduceByKey(_ + _).sortBy(_._2, ascending = false).map {
      case ((sid, time), count) =>
        MonthlyStreamStatisticsResult(taskUUID, sid, time, count)
    }.toDF()
    storeResultToDataBase(scenicPerMonthlyCountDF, Constants.MONTH_STREAM_RESULT_TABLE)

    /**
      * 河南省各个城市的收入
      */
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

    val incomesql = "SELECT s.city,i.time,SUM(i.taking) as taking FROM t_month_income i LEFT JOIN t_scenic_info s ON i.sid = s.sid group by i.time,s.city"

    val HACityIncomeDF: DataFrame = sparkSession.sql(incomesql)
    storeResultToDataBase(HACityIncomeDF, Constants.HA_CITIES_INCOME_TABLE)

    /**
      * 河南省各个城市的流量
      */

    /**
      * 各景区流量表
      */
    val monthStreamDF: DataFrame = readFromMysql(Constants.MONTH_STREAM_RESULT_TABLE,sparkSession)

    monthStreamDF.createOrReplaceTempView("t_month_stream")

    val streamsql = "SELECT s.city,i.time,SUM(i.count) as count FROM t_scenic_info s LEFT JOIN t_month_stream i ON s.sid = i.sid group by i.time,s.city"

    val HACityStreamDF: DataFrame = sparkSession.sql(streamsql)
    storeResultToDataBase(HACityStreamDF, Constants.HA_CITIES_STREAM_TABLE)


    sparkSession.stop()


  }

  /**
    * 各个城市的热门景区的统计分析
    */
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
  /**
    * 热门城市的统计分析
    */
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

  /**
    * 热门景区的统计分析
    */
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

  /**
    * 热门景区的热门客源地统计分析
    */
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

  /**
    * 热门客源地的统计分析
    */
  def getPopularTouristSourceTop10DF(sparkSession: SparkSession, taskUUID: String, visitorJoinRecordInfoRDD: RDD[(String, String)]) = {
    import sparkSession.implicits._
    visitorJoinRecordInfoRDD.groupByKey().map {
      case (time, iterableResidence) =>
        val residenceMap: mutable.HashMap[String, Int] = new mutable.HashMap[String, Int]()
        for (residence <- iterableResidence) {
          residenceMap.get(residence) match {
            case None => residenceMap(residence) = 1
            case Some(map) => residenceMap(residence) = residenceMap(residence) + 1
          }
        }
        (time, residenceMap.toList.sortWith(_._2 > _._2).take(10).mkString("|"))
    }.flatMap {
      case (time, str) =>
        var arr = new ArrayBuffer[PopularTouristSourceResult]()
        //        val strings: mutable.ArrayOps[String] = str.substring(1,str.lastIndexOf(")"))split("|")
        val strings: Array[String] = str.split("\\|")
        for (elem <- strings) {
          val residence_count: Array[String] = elem.substring(1, elem.length - 1).split("\\,")
          arr += PopularTouristSourceResult(taskUUID, residence_count(0), time, residence_count(1).toInt)
        }
        arr
    }.toDF()
  }

  def getPopularTouristSourceOfMonthTop10DF(sparkSession: SparkSession, taskUUID: String, visitorInfoRDD: RDD[(String, String)], visitorRecordInfoRDD: RDD[(String, Row)]): DataFrame = {

    val visitorJoinRecordInfoRDD: RDD[(String, String)] = visitorRecordInfoRDD.join(visitorInfoRDD).map {
      case (vid, (item, residence)) =>
        val time: String = item.getAs[String]("time")
        (time, residence)
    }
    getPopularTouristSourceTop10DF(sparkSession, taskUUID, visitorJoinRecordInfoRDD)
  }

  def getPopularTouristSourceOfYearTop10DF(sparkSession: SparkSession, taskUUID: String, visitorInfoRDD: RDD[(String, String)], visitorRecordInfoRDD: RDD[(String, Row)]) = {
    val visitorJoinRecordInfoRDD: RDD[(String, String)] = visitorRecordInfoRDD.join(visitorInfoRDD).map {
      case (vid, (item, residence)) =>
        val time: String = getYearFromDate(item.getAs[String]("time"))
        (time, residence)
    }
    getPopularTouristSourceTop10DF(sparkSession, taskUUID, visitorJoinRecordInfoRDD)
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

  /**
    * 以景区id为key
    * @param sparkSession
    * @return
    */
  def getVisitorRecordInfoBySid(sparkSession: SparkSession) = {
    val sql: String = "select vid,sid,time from visitor_record_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("sid"), item)
    })
  }

  /**
    * 以游客id为key
    *
    * @param sparkSession
    * @return
    */
  def getVisitorRecordInfoByVid(sparkSession: SparkSession) = {
    val sql: String = "select vid,sid,time from visitor_record_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("vid"), item)
    })
  }

  def getVisitorInfo(sparkSession: SparkSession) = {
    val sql: String = "select vid,residence from visitor_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("vid"), item.getAs[String]("residence"))
    })
  }

  def getScenicInfo(sparkSession: SparkSession) = {
    val sql: String = "select sid,city from scenic_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("sid"), item.getAs[String]("city"))
    })
  }

  def getScenicInfoOnMonth(sparkSession: SparkSession) = {
    val sql: String = "select sid,ticket from scenic_info"
    sparkSession.sql(sql).rdd.map(item => {
      (item.getAs[String]("sid"), item)
    })
  }

  /**
    * 输入时间取年份
    *
    * @param time
    * @return
    */
  def getYearFromDate(time: String): String = {
    return time.substring(0, 4)
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
