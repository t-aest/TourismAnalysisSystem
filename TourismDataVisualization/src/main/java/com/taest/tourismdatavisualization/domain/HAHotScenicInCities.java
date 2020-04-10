package com.taest.tourismdatavisualization.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 各个城市的热门景区
 */
@Data
public class HAHotScenicInCities implements Serializable {

    private static final long serialVersionUID = 2679941049961611576L;

    /**
     * 城市名称
     */
    private String city;


    /**
     * 景区名称
     */
    private String name;

    /**
     * 人数
     */
    private String count;

    /**
     * 时间
     */
    private String time;
}
