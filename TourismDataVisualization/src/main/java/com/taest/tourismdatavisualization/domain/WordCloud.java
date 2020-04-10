package com.taest.tourismdatavisualization.domain;

import lombok.Data;

/**
 * 词云信息
 */
@Data
public class WordCloud {

    /**
     * 单词
     */
    private String word;

    /**
     * 词频
     */
    private Integer count;
}
