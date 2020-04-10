package com.taest.tourismdatavisualization.domain.base;


import lombok.Data;

import java.io.Serializable;

/**
 * 抽取经济指标相关表的公有属性
 */
@Data
public class BaseEntity implements Serializable {

    protected static final long serialVersionUID = -5737911228464098461L;
    /**
     * 固定资产
     */
    protected String e_fixed_assets;

    /**
     * 收入
     */
    protected String e_taking;

    /**
     * 企业家数
     */
    protected String e_entrepreneur;

    /**
     * 年份
     */
    protected String year;

}
