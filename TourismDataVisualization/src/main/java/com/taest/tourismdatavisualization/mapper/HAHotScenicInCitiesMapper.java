package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.HAHotScenicInCities;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 各城市的热门景区
 */
@Mapper
public interface HAHotScenicInCitiesMapper {

    /**
     * 查询热门景区
     * @param time 时间
     * @param city 城市名称
     * @return
     */
    @Select("SELECT c.city,s.`name`,c.time,c.count FROM cities_popular_scenic_year c LEFT JOIN scenic_info s ON c.sid=s.sid where time = #{time} and c.city=#{city} GROUP BY s.`name`")
    List<HAHotScenicInCities> getHAHotScenicInCitiesByTimeAndCity(@Param("time") String time, @Param("city")String city);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct time from popular_tourist_source_year;")
    List<String> getYearCountBy();
}
