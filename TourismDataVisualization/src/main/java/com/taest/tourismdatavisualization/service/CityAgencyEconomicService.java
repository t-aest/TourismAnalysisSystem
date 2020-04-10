package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;

import java.util.List;

/**
 * 全国旅游企业主要城市旅行社经济指标查询
 */
public interface CityAgencyEconomicService {

    /**
     * 根据年份查询主要城市旅行社经济指标
     * @param year
     * @return
     */
    List<CityBaseEntity> findCityAgencyEconomicByTime(String year);

    /**
     * 根据年份查询主要城市旅行社总经济指标
     * @param year
     * @return
     */
    CityBaseEntity findCityAgencyEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> getYearCountBy();
}
