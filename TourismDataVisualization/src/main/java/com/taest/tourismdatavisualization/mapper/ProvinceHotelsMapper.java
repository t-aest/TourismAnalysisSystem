package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.ProvinceHotels;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 全国各省的星级酒店个数
 */
@Mapper
public interface ProvinceHotelsMapper {

    /**
     * 根据年份查询各省的星级酒店数量
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id,e_regin_name,hotel_num,taking as income,fixed_assets,`year`FROM a_star_rated_hotels WHERE `year`=#{year}")
    List<ProvinceHotels> getProvinceHotelsByTime(String year);

    /**
     * 根据年份查询各省的总星级酒店数量
     * @param year
     * @return
     */
    @Select("SELECT e_regin_id,e_regin_name,hotel_num,taking as income,fixed_assets,`year`FROM a_star_rated_hotels WHERE e_regin_id=1024 and `year`=#{year}")
    ProvinceHotels getProvinceHotelsTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("select distinct `year` from a_star_rated_hotels")
    List<String> getYearCountBy();
}
