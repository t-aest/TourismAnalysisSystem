package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.NationForeignTourists;

import java.util.List;

/**
 * 入境外国旅游者人数查询
 */
public interface NationForeignTouristsService {

    /**
     * 根据年份查询入境外国旅游者人数
     * @param year
     * @return
     */
    List<NationForeignTourists> findNationForeignTouristsByTime(String year);

    /**
     * 根据年份查询入境外国旅游者总人数
     * @param year
     * @return
     */
    NationForeignTourists findNationForeignTouristsTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> getYearCountBy();
}
