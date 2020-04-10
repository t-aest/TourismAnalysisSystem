package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;

import java.util.List;

/**
 * 全国旅游企业各省的经济指标查询
 */
public interface ProvinceEnterpriseEconomicService {

    /**
     * 根据年份查询各省的经济指标
     * @param year
     * @return
     */
    List<ProvinceBaseEntity> findProvinceEnterpriseEconomicByTime(String year);

    /**
     * 根据年份查询各省总经济指标
     * @param year
     * @return
     */
    ProvinceBaseEntity findProvinceEnterpriseEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> getYearCountBy();

    /**
     * 根据年份查询河南省的经济指标
     * @param year
     * @return
     */
    ProvinceBaseEntity findHAEnterpriseEconomicByYear(String year);
}
