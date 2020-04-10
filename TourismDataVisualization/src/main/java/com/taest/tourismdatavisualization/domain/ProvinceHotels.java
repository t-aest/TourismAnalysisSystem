package com.taest.tourismdatavisualization.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 全国星级饭店地区分布表
 */
@Data
@ToString
public class ProvinceHotels implements Serializable {

    private static final long serialVersionUID = 5288651228971953741L;
    /**
     *  地区(省)id
     */
    private String e_regin_id;

    /**
     *  地区(省)名称
     */
    private String e_regin_name;

    /**
     *  饭店数量
     */
    private String hotel_num;

    /**
     * 营业收入
     */
    private String income;

    /**
     *  固定资产 支出
     */
    private String fixed_assets;

    /**
     * 年份
     */
    private String year;
}
