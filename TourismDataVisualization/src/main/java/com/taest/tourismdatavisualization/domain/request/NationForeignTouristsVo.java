package com.taest.tourismdatavisualization.domain.request;

import com.taest.tourismdatavisualization.domain.NationForeignTourists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 封装的返回客源国人数相关 结果
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NationForeignTouristsVo extends NationForeignTourists {

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;
}
