package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.HAHotResidence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 热门常驻地查询dao层
 */
@Mapper
public interface HAHotResidenceMapper {

    /**
     * 根据时间查询热门常驻地
     * @param time
     * @return
     */
    @Select("select residence,count,time from popular_tourist_source_year where time =#{time}")
    List<HAHotResidence> getHotResidenceByTime(String time);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct SUBSTRING_INDEX(time,'-',1) AS time from popular_tourist_source_year;")
    List<String> getYearCountBy();

}
