package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;

import java.util.List;

/**
 * 全国星级饭店各省的经济指标查询
 */
public interface ProvinceHotelsEconomicService {

    /**
     * 根据年份查询各省星级饭店经济指标
     * @param year
     * @return
     */
    List<ProvinceBaseEntity> findProvinceHotelsEconomicByTime(String year);

    /**
     * 根据年份查询各省总星级饭店经济指标
     * @param year
     * @return
     */
    ProvinceBaseEntity findProvinceHotelsEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> getYearCountBy();

    /**
     * 根据年份查询河南省星级饭店经济指标
     * @param year
     * @return
     */
    ProvinceBaseEntity findHAHotelsEconomicByYear(String year);
}
