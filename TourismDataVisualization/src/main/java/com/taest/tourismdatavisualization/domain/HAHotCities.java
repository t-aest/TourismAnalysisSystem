package com.taest.tourismdatavisualization.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 热门城市
 */
@Data
public class HAHotCities implements Serializable {
    private static final long serialVersionUID = 6566737206221666882L;

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
}
