package com.taest.tourismdatavisualization.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 各市的流量
 */
@Data
@ToString
public class HACityStream implements Serializable {

    private static final long serialVersionUID = -1950546050757158113L;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 人流量
     */
    private String count;

    /**
     * 时间
     */
    private String time;
}
