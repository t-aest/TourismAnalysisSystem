package com.taest.tourismdatavisualization.domain.request;

import com.taest.tourismdatavisualization.domain.ProvinceHotels;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 封装的返回省星级酒店分布 结果
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProvinceHotelsVo extends ProvinceHotels {

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;
}
