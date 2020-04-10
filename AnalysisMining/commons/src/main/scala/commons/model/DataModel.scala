package commons.model

//***************** 配置类 *********************

/**
  * mysql配置类
  * @param url
  * @param user
  * @param password
  */
case class MySqlConfig(url: String, user: String, password: String)



//***************** 输出表 *********************
/**
  * 定义的各城市的热门景区统计结果对象
  *
  * @param taskUUID           任务id
  * @param city               城市名称
  * @param time               时间
  * @param sid                景区id
  * @param count              景区流量
  */
case class CitiesPopularScenicResult(taskUUID: String,
                                     city: String,
                                     time: String,
                                     sid: String,
                                     count: Int)


/**
  * 定义的热门城市统计结果对象
  *
  * @param taskUUID
  * @param city
  * @param time
  * @param count              热门城市流量
  */
case class HotCitiesResult(taskUUID: String,
                           city: String,
                           time: String,
                           count: Int)


/**
  * 定义的景区月收入统计结果对象
  * @param taskUUID
  * @param sid
  * @param time
  * @param taking             景区月收入
  */
case class MonthlyIncomeStatisticsResult(taskUUID:String,
                                         sid: String,
                                         time: String,
                                         taking: Double)


/**
  * 定义的热门客源地统计结果对象
  *
  * @param taskUUID
  * @param residence
  * @param time
  * @param count              客源地游客数量
  */
case class PopularTouristSourceResult(taskUUID: String,
                                      residence: String,
                                      time: String,
                                      count: Int)


/**
  * 定义的景区月流量统计结果对象
  *
  * @param taskUUID
  * @param sid
  * @param time
  * @param count                景区游客数量
  */
case class MonthlyStreamStatisticsResult(taskUUID: String,
                                         sid: String,
                                         time: String,
                                         count: Int)


/**
  * 定义的热门景区的热门客源地统计结果对象
  *
  * @param taskUUID
  * @param sid
  * @param time
  * @param residence            游客客源地
  * @param count                热门景区的客源地的游客数量
  */
case class PopularScenicResidenceResult(taskUUID: String,
                                        sid: String,
                                        time: String,
                                        residence: String,
                                        count: Int)


/**
  * 定义的热门景区统计结果对象
  *
  * @param taskUUID
  * @param sid
  * @param time
  * @param count                景区的游客数量
  */
case class PopularScenicResult(taskUUID: String,
                               sid: String,
                               time: String,
                               count: Int)


/**
  * 定义的河南省各个城市的收入的结果
  * @param taskUUID
  * @param city
  * @param time
  * @param taking
  */
case class HACityIncomeResult(taskUUID: String,
                              city: String,
                              time:String,
                              taking:String)

//***************** 输入表 *********************

/**
  * 河南省景区数据集
  *
  * @param sid      景区id
  * @param name     景区名称
  * @param addr     景区地址
  * @param ticket   景区门票
  * @param province 景区所在省
  * @param city     景区所在市
  */
case class Scenic(sid: String,
                  name: String,
                  addr: String,
                  ticket: Double,
                  province: String,
                  city: String)

/**
  * 游客信息
  *
  * @param vid       游客的id
  * @param residence 游客的常驻地
  */
case class Visitor(vid: String,
                   residence: String)

/**
  * 游客记录信息
  *
  * @param vid  游客的id
  * @param sid  景区的id
  * @param time 记录产生的年月时间
  */
case class VisitorRecord(vid: String,
                         sid: String,
                         time: String)