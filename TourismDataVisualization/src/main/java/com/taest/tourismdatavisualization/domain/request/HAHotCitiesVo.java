package com.taest.tourismdatavisualization.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装的返回热门城市 结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HAHotCitiesVo implements Serializable {

    /**
     * 城市
     */
    private String city;

    /**
     * 城市流量
     */
    private String count;

    /**
     * 时间
     */
    private String time;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

}
