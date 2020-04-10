package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 全国星级饭店各省的经济指标查询
 */
@Mapper
public interface ProvinceHotelsEconomicMapper {

    /**
     * 根据年份查询各省星级饭店经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id provinceId,e_regin_name provinceName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `a_hotal_economic` WHERE `year` =#{year}")
    List<ProvinceBaseEntity> getProvinceHotelsEconomicByTime(String year);

    /**
     * 根据年份查询各省总星级饭店经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id provinceId,e_regin_name provinceName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `a_hotal_economic` WHERE `year` =#{year} and e_regin_id=1024")
    ProvinceBaseEntity getProvinceHotelsEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct `year` from a_hotal_economic")
    List<String> getYearCountBy();

    /**
     * 根据年份查询河南省星级饭店经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id provinceId,e_regin_name provinceName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `a_hotal_economic` WHERE `year` =#{year} and e_regin_name='河南'")
    ProvinceBaseEntity getHAHotelsEconomicByYear(String year);

}
