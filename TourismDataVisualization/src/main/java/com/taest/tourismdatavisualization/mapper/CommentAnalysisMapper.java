package com.taest.tourismdatavisualization.mapper;

import com.taest.tourismdatavisualization.domain.Comment;
import com.taest.tourismdatavisualization.domain.WordCloud;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 对评论分析结果的查询
 */
@Mapper
public interface CommentAnalysisMapper {

    /**
     * 查询评论
     * @return
     */
    @Select("SELECT review,if(predict=0,'负向','正向') as predict FROM `comment`")
    List<Comment> getComment();

    /**
     * 查询词云所需数据
     * @return
     */
    @Select("SELECT all_words word,count FROM word_cloud")
    List<WordCloud> getWordCloud();

    /**
     * 查询洛阳5a景区所需数据
     */
    @Select("SELECT all_words word,count FROM word_scenic_cloud WHERE `name`=#{name}")
    List<WordCloud> getScenicWordCloud(String name);


}
