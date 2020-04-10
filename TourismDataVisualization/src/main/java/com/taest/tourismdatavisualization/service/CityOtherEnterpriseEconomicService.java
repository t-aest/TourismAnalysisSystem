package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;

import java.util.List;

/**
 * 全国旅游企业主要城市其他经济指标查询
 */
public interface CityOtherEnterpriseEconomicService {

    /**
     * 根据年份查询主要城市其他经济指标
     * @param year
     * @return
     */
    List<CityBaseEntity> findCityOtherEnterpriseEconomicByTime(String year);

    /**
     * 根据年份查询主要城市其他总经济指标
     * @param year
     * @return
     */
    CityBaseEntity findCityOtherEnterpriseEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> getYearCountBy();
}
