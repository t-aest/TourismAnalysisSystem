package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.HAHotScenicInCities;

import java.util.List;

/**
 * 查询各城市热门景区service层
 */
public interface HAHotScenicInCitiesService {

    /**
     * 查询热门景区
     * @param time 时间
     * @param city 城市名称
     * @return
     */
    List<HAHotScenicInCities> findHAHotScenicInCitiesByTimeAndCity(String time, String city);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> findYearCountBy();

}
