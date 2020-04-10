package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.HAHotCities;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 热门旅游城市dao
 */
@Mapper
public interface HAHotCitiesMapper {

    /**
     * 根据年份查询热门城市
     * @param time
     * @return
     */
    @Select("SELECT city,count,time FROM `hot_cities_year` where time =#{time}")
    List<HAHotCities> getHAHotCitiesByTime(String time);


    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct time from hot_cities_year")
    List<String> getYearCountBy();
}
