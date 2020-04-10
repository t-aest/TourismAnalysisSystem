/*
 * Copyright (c) 2018. Atguigu Inc. All Rights Reserved.
 */

package commons.constant

/**
 * 常量接口
 */
object Constants {

	/**
	 * 项目配置相关的常量
	 */

	val JDBC_DATASOURCE_SIZE = "jdbc.datasource.size"
	val JDBC_URL = "jdbc.url"
	val JDBC_USER = "jdbc.user"
	val JDBC_PASSWORD = "jdbc.password"
	val SPARK_CORES = "spark.cores"

	val KAFKA_TOPICS = "kafka.topics"
	val config = Map(
		"spark.cores" -> "local[*]",
		"jdbc.url" -> "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8",
		"jdbc.user" -> "root",
		"jdbc.password" -> "980518"
	)

	/**
		* 数据的加载路径
		*/
	val SCENIC_DATA_PATH = "E:\\data\\data\\scenic_full_city_info"
	val VISITOR_DATA_PATH = "E:\\data\\data\\user_info"
	val VISITOR_RECORD_DATA_PATH = "E:\\data\\data\\scenic\\"
	val TEST = "E:\\data\\scenic\\*"

	/**
		* Spark的表名常量
		*/

	val SCENIC_INFO_TABLE = "scenic_info"
	val VISITOR_INFO_TABLE = "visitor_info"
	val VISITOR_RECORD_INFO_TABLE = "visitor_record_info"


	val POPULAR_TOURIST_SOURCE_TABLE = "popular_tourist_source"
	val POPULAR_TOURIST_SOURCE_YEAR_TABLE = "popular_tourist_source_year"

	val MONTH_INCOME_RESULT_TABLE = "monthly_income_result"

	val CITIES_POPULAR_SCENIC_TABLE = "cities_popular_scenic"
	val CITIES_POPULAR_SCENIC_YEAR_TABLE = "cities_popular_scenic_year"

	val HOT_CITIES_TABLE = "hot_cities"
	val HOT_CITIES_YEAR_TABLE = "hot_cities_year"

	val MONTH_STREAM_RESULT_TABLE = "monthly_stream_result"

	val POPULAR_SCENIC_RESIDENCE_TABLE = "popular_scenic_residence"
	val POPULAR_SCENIC_RESIDENCE_YEAR_TABLE = "popular_scenic_residence_year"

	val POPULAR_SCENIC_TABLE = "popular_scenic"
	val POPULAR_SCENIC_YEAR_TABLE = "popular_scenic_year"

	val HA_CITIES_INCOME_TABLE = "ha_cities_income"
	val HA_CITIES_INCOME_YEAR_TABLE = "hot_cities_income_year"

	val HA_CITIES_STREAM_TABLE = "ha_cities_stream"
	val HA_CITIES_STREAM_YEAR_TABLE = "ha_cities_stream_year"

	//
//	/**
//	 * task.params.json中限制条件对应的常量字段
//	 */
//	val TASK_PARAMS = "task.params.json"
//	val PARAM_START_DATE = "startDate"
//	val PARAM_END_DATE = "endDate"
//	val PARAM_START_AGE = "startAge"
//	val PARAM_END_AGE = "endAge"
//	val PARAM_PROFESSIONALS = "professionals"
//	val PARAM_CITIES = "cities"
//	val PARAM_SEX = "sex"
//	val PARAM_KEYWORDS = "keywords"
//	val PARAM_CATEGORY_IDS = "categoryIds"
//	val PARAM_TARGET_PAGE_FLOW = "targetPageFlow"
	
}
