package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;

import java.util.List;

/**
 * 全国旅行社各省的部分经济指标查询
 */
public interface ProvinceAgencyEconomicService {

    /**
     * 根据年份查询各省的旅行社经济指标
     * @param year
     * @return
     */
    List<ProvinceBaseEntity> findProvinceAgencyEconomicByTime(String year);

    /**
     * 根据年份查询各省的总旅行社经济指标
     * @param year
     * @return
     */
    ProvinceBaseEntity findProvinceAgencyEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> getYearCountBy();

    /**
     * 根据年份查询河南省的旅行社经济指标
     * @param year
     * @return
     */
    ProvinceBaseEntity findHAAgencyEconomicByYear(String year);
}
