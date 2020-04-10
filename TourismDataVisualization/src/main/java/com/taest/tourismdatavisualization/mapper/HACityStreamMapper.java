package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.HACityStream;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 河南省各市的流量
 */
@Mapper
public interface HACityStreamMapper {

    /**
     * 根据时间查询各市的流量
     * @param time
     * @return
     */
    @Select("SELECT city,SUM(count) as count,time FROM `ha_cities_stream` WHERE time like '%${value}%' GROUP BY city")
    List<HACityStream> getHACityStreamByTime(String time);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct SUBSTRING_INDEX(time,'-',1) AS time from ha_cities_stream;")
    List<String> getYearCountBy();
}
