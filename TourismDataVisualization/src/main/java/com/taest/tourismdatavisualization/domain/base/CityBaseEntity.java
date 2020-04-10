package com.taest.tourismdatavisualization.domain.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 主要城市经济指标的通用表
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CityBaseEntity extends BaseEntity{

    private static final long serialVersionUID = -5060196799535906133L;
    /**
     * 城市id
     */
    private String cityId;

    /**
     * 城市名称
     */
    private String cityName;
}
