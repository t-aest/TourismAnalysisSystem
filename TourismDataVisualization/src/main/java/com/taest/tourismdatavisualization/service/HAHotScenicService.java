package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.HAHotScenic;

import java.util.List;

/**
 * 查询热门景区
 */
public interface HAHotScenicService {

    /**
     * 根据年份查询热门景区
     * @param time
     * @return
     */
    List<HAHotScenic> findHAHotScenicByTime(String time);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> YearCountBy();
}
