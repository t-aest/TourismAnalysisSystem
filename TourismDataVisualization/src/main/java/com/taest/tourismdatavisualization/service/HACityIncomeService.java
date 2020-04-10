package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.HACityIncome;

import java.util.List;

public interface HACityIncomeService {

    /**
     * 根据时间查询各市的收入
     * @param time
     * @return
     */
    List<HACityIncome> findHACityIncomeByTime(String time);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> findYearCountBy();
}
