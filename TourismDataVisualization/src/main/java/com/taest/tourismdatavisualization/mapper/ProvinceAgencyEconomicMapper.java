package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 全国旅行社各省的部分经济指标查询
 */
@Mapper
public interface ProvinceAgencyEconomicMapper {

    /**
     * 根据年份查询各省的旅行社经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id provinceId,e_regin_name provinceName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `a_agency_economic` WHERE `year` =#{year}")
    List<ProvinceBaseEntity> getProvinceAgencyEconomicByTime(String year);

    /**
     * 根据年份查询各省的总旅行社经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id provinceId,e_regin_name provinceName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `a_agency_economic` WHERE `year` =#{year} and e_regin_id=1024")
    ProvinceBaseEntity getProvinceAgencyEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct `year` from a_agency_economic")
    List<String> getYearCountBy();

    /**
     * 根据年份查询河南省的旅行社经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id provinceId,e_regin_name provinceName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `a_agency_economic` WHERE `year` =#{year} and e_regin_name='河南'")
    ProvinceBaseEntity getHAAgencyEconomicByYear(String year);


}
