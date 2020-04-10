package com.taest.tourismdatavisualization.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 预测的各省的总收入
 */
@Mapper
public interface ProvincePredictIncomeMapper {

    /**
     * 根据年份查询预测的各省旅游企业收入总和
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM a_enterprise_economic_predict where year =#{year}")
    String getProvincePredictEnterpriseIncomeByYear(String year);


    /**
     * 根据年份查询预测的各省旅行社收入总和
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM a_agency_economic_predict where year =#{year}")
    String getProvincePredictAgencyIncomeByYear(String year);

    /**
     * 根据年份查询预测的各省旅游星级酒店收入总和
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM a_hotal_economic_predict where year =#{year}")
    String getProvincePredictHotalIncomeByYear(String year);

    /**
     * 根据年份查询预测的各省旅游其他收入总和
     * @param year
     * @return
     */
    @Select("SELECT round(predict,2) predict FROM a_other_enterprise_economic_predict where year =#{year}")
    String getProvincePredictOtherEnterpriseIncomeByYear(String year);
}
