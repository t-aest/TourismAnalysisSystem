package com.taest.tourismdatavisualization.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 入境外国旅游者人数表
 */
@Data
public class NationForeignTourists implements Serializable {

    private static final long serialVersionUID = -3104080392621211207L;

    /**
     * 国籍id
     */
    private String nationality_id;

    /**
     * 国籍
     */
    private String nationality_name;

    /**
     * 游客人数
     */
    private String total;

    /**
     * 入境方式:船舶
     */
    private String ship;

    /**
     * 入境方式:飞机
     */
    private String aeroplane;

    /**
     * 入境方式:火车
     */
    private String train;

    /**
     * 入境方式:汽车
     */
    private String automobile;

    /**
     * 入境方式:徒步
     */
    private String foot;

    /**
     * 年份
     */
    private String year;

}
