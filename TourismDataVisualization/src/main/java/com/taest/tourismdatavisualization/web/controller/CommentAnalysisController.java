package com.taest.tourismdatavisualization.web.controller;

import com.taest.tourismdatavisualization.domain.WordCloud;
import com.taest.tourismdatavisualization.mapper.CommentAnalysisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("comment")
public class CommentAnalysisController {

    @Autowired
    private CommentAnalysisMapper commentAnalysisMapper;

    @RequestMapping("/listwordcloud")
    @ResponseBody
    public ModelMap listWordCloud() {
        ModelMap map = new ModelMap();

        List<WordCloud> wordClouds = commentAnalysisMapper.getWordCloud();

        map.put("wordClouds",wordClouds);

        return map;
    }

    @RequestMapping("/listlmwordcloud")
    @ResponseBody
    public ModelMap listLmWordCloud() {
        ModelMap map = new ModelMap();
        String name = "龙门石窟";
        List<WordCloud> wordClouds = commentAnalysisMapper.getScenicWordCloud(name);

        map.put("wordClouds",wordClouds);

        return map;
    }

    @RequestMapping("/listbmwordcloud")
    @ResponseBody
    public ModelMap listBmWordCloud() {
        ModelMap map = new ModelMap();
        String name = "白马寺";
        List<WordCloud> wordClouds = commentAnalysisMapper.getScenicWordCloud(name);

        map.put("wordClouds",wordClouds);

        return map;
    }

    @RequestMapping("/listljwordcloud")
    @ResponseBody
    public ModelMap listLjWordCloud() {
        ModelMap map = new ModelMap();
        String name = "老君山";
        List<WordCloud> wordClouds = commentAnalysisMapper.getScenicWordCloud(name);

        map.put("wordClouds",wordClouds);

        return map;
    }

    @RequestMapping("/listltwordcloud")
    @ResponseBody
    public ModelMap listLtWordCloud() {
        ModelMap map = new ModelMap();
        String name = "龙潭大峡谷";
        List<WordCloud> wordClouds = commentAnalysisMapper.getScenicWordCloud(name);

        map.put("wordClouds",wordClouds);

        return map;
    }

}
