package com.taest.tourismdatavisualization.service;

import com.taest.tourismdatavisualization.domain.ProvinceHotels;

import java.util.List;

/**
 * 全国各省的星级酒店个数
 */
public interface ProvinceHotelsService {

    /**
     * 根据年份查询各省的星级酒店数量
     * @param year
     * @return
     */
    List<ProvinceHotels> findProvinceHotelsByTime(String year);

    /**
     * 根据年份查询各省的总星级酒店数量
     * @param year
     * @return
     */
    ProvinceHotels findProvinceHotelsTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    List<String> getYearCountBy();
}
