package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.HACityIncome;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 河南省各市的收入
 */
@Mapper
public interface HACityIncomeMapper {

    /**
     * 根据时间查询各市的收入
     * @param time
     * @return
     */
    @Select("SELECT city,ROUND(SUM(taking),3) as taking,time FROM `ha_cities_income` WHERE time like '%${value}%' GROUP BY city")
    List<HACityIncome> getHACityIncomeByTime(String time);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct SUBSTRING_INDEX(time,'-',1) AS time from ha_cities_income;")
    List<String> getYearCountBy();
}
