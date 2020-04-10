package com.taest.tourismdatavisualization.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 预测的河南的收入
 */
@Mapper
public interface HaPredictIncomeMapper {

    /**
     * 根据年份查询预测的河南旅游企业收入
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM ha_enterprise_economic_predict where year =#{year}")
    String getPredictEnterpriseIncomeByYear(String year);


    /**
     * 根据年份查询预测的河南旅行社收入
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM ha_agency_economic_predict where year =#{year}")
    String getPredictAgencyIncomeByYear(String year);

    /**
     * 根据年份查询预测的河南旅游星级酒店收入
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM ha_hotal_economic_predict where year =#{year}")
    String getPredictHotalIncomeByYear(String year);

    /**
     * 根据年份查询预测的河南省旅游其他收入
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM ha_other_enterprise_economic_predict where year =#{year}")
    String getPredictOtherEnterpriseIncomeByYear(String year);
}
