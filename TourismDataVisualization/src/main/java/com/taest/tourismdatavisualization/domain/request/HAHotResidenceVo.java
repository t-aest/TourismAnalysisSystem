package com.taest.tourismdatavisualization.domain.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装的返回热门常驻地 结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HAHotResidenceVo implements Serializable {

    private static final long serialVersionUID = 4723340216418167987L;

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

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

}
