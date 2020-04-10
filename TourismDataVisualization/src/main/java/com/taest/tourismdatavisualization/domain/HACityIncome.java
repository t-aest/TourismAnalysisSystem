package com.taest.tourismdatavisualization.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 各市的收入
 */
@Data
@ToString
public class HACityIncome implements Serializable {

    private static final long serialVersionUID = 1698101159314132400L;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 收入
     */
    private String taking;

    /**
     * 时间
     */
    private String time;
}
