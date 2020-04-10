package com.taest.tourismdatavisualization.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 热门景区
 */
@Data
public class HAHotScenic implements Serializable {

    private static final long serialVersionUID = 6693746047092071473L;

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
