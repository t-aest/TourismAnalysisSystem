package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.HAHotScenic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 查询热门景区
 */
@Mapper
public interface HAHotScenicMapper {

    /**
     * 根据年份查询热门景区
     * @param time
     * @return
     */
    @Select("SELECT s.`name`,p.count ,p.time FROM popular_scenic_year p RIGHT JOIN scenic_info s ON p.sid = s.sid WHERE time = #{time} group by name ORDER BY p.count DESC LIMIT 10")
    List<HAHotScenic> getHAHotScenicByTime(String time);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct time from popular_scenic_year")
    List<String> getYearCountBy();
}
