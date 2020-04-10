package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;

import java.util.List;

/**
 * 全国旅游企业各省其他经济指标查询
 */
public interface ProvinceOtherEnterpriseEconomicService {

    /**
     * 根据年份查询各省其他经济指标
     * @param year
     * @return
     */
    List<ProvinceBaseEntity> findProvinceOtherEnterpriseEconomicByTime(String year);

    /**
     * 根据年份查询各省总其他经济指标
     * @param year
     * @return
     */
    ProvinceBaseEntity findProvinceOtherEnterpriseEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> findYearCountBy();

    /**
     * 根据年份查询河南省其他经济指标
     * @param year
     * @return
     */
    ProvinceBaseEntity findHAOtherEnterpriseEconomicByYear(String year);
}
