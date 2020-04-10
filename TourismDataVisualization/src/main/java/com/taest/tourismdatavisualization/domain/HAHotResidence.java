package com.taest.tourismdatavisualization.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 热门常驻地
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HAHotResidence implements Serializable {

    private static final long serialVersionUID = 6173167223243480662L;
    /**
     * 常驻地
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
