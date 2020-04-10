package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 全国旅游企业各省其他经济指标查询
 */
@Mapper
public interface ProvinceOtherEnterpriseEconomicMapper {

    /**
     * 根据年份查询各省其他经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id provinceId,e_regin_name provinceName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `a_other_enterprise_economic` WHERE `year` =#{year}")
    List<ProvinceBaseEntity> getProvinceOtherEnterpriseEconomicByTime(String year);

    /**
     * 根据年份查询各省总其他经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id provinceId,e_regin_name provinceName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `a_other_enterprise_economic` WHERE `year` =#{year} and e_regin_id=1024")
    ProvinceBaseEntity getProvinceOtherEnterpriseEconomicTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct `year` from a_other_enterprise_economic")
    List<String> getYearCountBy();

    /**
     * 根据年份查询河南省其他经济指标
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id provinceId,e_regin_name provinceName,e_fixed_assets,e_taking,e_entrepreneur,`year` FROM `a_other_enterprise_economic` WHERE `year` =#{year} and e_regin_name='河南'")
    ProvinceBaseEntity getHAOtherEnterpriseEconomicByYear(String year);
}
