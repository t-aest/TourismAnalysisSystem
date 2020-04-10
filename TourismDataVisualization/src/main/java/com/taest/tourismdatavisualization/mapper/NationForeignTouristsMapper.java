package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.NationForeignTourists;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 入境外国旅游者人数查询
 */
@Mapper
public interface NationForeignTouristsMapper {

    /**
     * 根据年份查询入境外国旅游者人数
     * @param year
     * @return
     */
    @Select("SELECT nationality_id,nationality_name,total,ship,aeroplane,train,automobile,foot,`year`FROM `i_foreign_tourists`WHERE `year`=#{year}")
    List<NationForeignTourists> getNationForeignTouristsByTime(String year);

    /**
     * 根据年份查询入境外国旅游者总人数
     * @param year
     * @return
     */
    @Select("SELECT nationality_id,nationality_name,total,ship,aeroplane,train,automobile,foot,`year`FROM `i_foreign_tourists`WHERE nationality_id = 1024 AND `year`=#{year}")
    NationForeignTourists getNationForeignTouristsTotal(String year);

    /**
     * 查询年份的个数
     * @return
     */
    @Select("SELECT DISTINCT `year` FROM i_foreign_tourists")
    List<String> getYearCountBy();
}
