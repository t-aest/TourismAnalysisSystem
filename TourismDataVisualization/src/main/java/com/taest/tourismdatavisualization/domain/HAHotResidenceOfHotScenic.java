package com.taest.tourismdatavisualization.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 热门景区的热门常驻地
 */
@Data
public class HAHotResidenceOfHotScenic implements Serializable {

    private static final long serialVersionUID = 6624025270544115682L;

    /**
     * 热门景区id
     */
    private String sid;

    /**
     * 热门景区的热门常驻地id
     */
    private String residence;

    /**
     * 人数
     */
    private String count;

    /**
     * 时间
     */
    private String time;
}
