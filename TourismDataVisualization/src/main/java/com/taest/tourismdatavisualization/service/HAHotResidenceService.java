package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.HAHotResidence;

import java.util.List;

/**
 * 热门常驻地查询service层
 */
public interface HAHotResidenceService {

    /**
     * 根据时间查询热门常驻地
     * @param time
     * @return
     */
    List<HAHotResidence> findHotResidenceByTime(String time);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> findYearCountBy();
}
