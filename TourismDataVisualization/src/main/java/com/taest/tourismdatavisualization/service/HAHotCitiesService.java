package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.HAHotCities;

import java.util.List;

/**
 * 热门旅游城市查询service层
 */
public interface HAHotCitiesService {

    /**
     * 根据时间查询热门城市
     * @param time
     * @return
     */
    List<HAHotCities> findHAHotCitiesByTime(String time);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> findYearCountBy();
}
