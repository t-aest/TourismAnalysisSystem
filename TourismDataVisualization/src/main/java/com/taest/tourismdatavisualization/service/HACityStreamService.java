package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.HACityStream;

import java.util.List;

public interface HACityStreamService {

    /**
     * 根据时间查询各市的流量
     * @param time
     * @return
     */
    List<HACityStream> findHACityStreamByTime(String time);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> findYearCountBy();
}
