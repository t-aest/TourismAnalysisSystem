package com.taest.tourismdatavisualization.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 预测的主要城市的总收入
 */
@Mapper
public interface CityPredictIncomeMapper {

    /**
     * 根据年份查询预测的主要城市旅游企业收入总和
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM m_enterprise_economic_predict where year =#{year}")
    String getCityPredictEnterpriseIncomeByYear(String year);


    /**
     * 根据年份查询预测的主要城市旅行社收入总和
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM m_agency_economic_predict where year =#{year}")
    String getCityPredictAgencyIncomeByYear(String year);

    /**
     * 根据年份查询预测的主要城市旅游星级酒店收入总和
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM m_hotal_economic_predict where year =#{year}")
    String getCityPredictHotalIncomeByYear(String year);

    /**
     * 根据年份查询预测的主要城市旅游其他收入总和
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM m_other_enterprise_economic_predict where year =#{year}")
    String getCityPredictOtherEnterpriseIncomeByYear(String year);
}
