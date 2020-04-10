package com.taest.tourismdatavisualization.domain.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 全国经济指标的通用表
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProvinceBaseEntity extends BaseEntity{

    private static final long serialVersionUID = -4644345891829052815L;
    /**
     * 省id
     */
    private String provinceId;

    /**
     * 省名称
     */
    private String provinceName;

}
