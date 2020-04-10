package com.taest.tourismdatavisualization.domain.request;

import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CityBaseEntityVo extends CityBaseEntity {

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;
}
