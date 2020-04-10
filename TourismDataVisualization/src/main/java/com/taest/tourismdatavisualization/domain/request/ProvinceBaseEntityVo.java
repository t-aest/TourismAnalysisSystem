package com.taest.tourismdatavisualization.domain.request;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * 封装的返回省经济 结果
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProvinceBaseEntityVo extends ProvinceBaseEntity {
    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;
}
