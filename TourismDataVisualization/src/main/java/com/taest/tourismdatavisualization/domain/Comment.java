package com.taest.tourismdatavisualization.domain;

import lombok.Data;

/**
 * 评论预测实体类
 */
@Data
public class Comment {

    /**
     * 评论
     */
    private String review;

    /**
     * 正负向预测
     */
    private String predict;
}
