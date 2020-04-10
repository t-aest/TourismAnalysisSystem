package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 全国旅游企业主要城市星级酒店经济指标查询
 */
@Mapper
public interface CityHotelsEconomicMapper {

    /**
     * 根据年份查询主要城市星级酒店经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id cityId,e_regin_name cityName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `m_hotal_economic` WHERE `year` =#{year}")
    List<CityBaseEntity> getCityHotelsEconomicByTime(String year);

    /**
     * 根据年份查询主要城市星级酒店总经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id cityId,e_regin_name cityName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `m_hotal_economic` WHERE `year` =#{year} and e_regin_id=1024")
    CityBaseEntity getCityHotelsEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct `year` from m_hotal_economic")
    List<String> getYearCountBy();
}
