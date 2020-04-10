package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.HAHotScenic;
import com.taest.tourismdatavisualization.mapper.HAHotScenicMapper;
import com.taest.tourismdatavisualization.service.HAHotScenicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HAHotScenicServiceImpl implements HAHotScenicService {

    @Autowired
    private HAHotScenicMapper haHotScenicMapper;

    @Override
    public List<HAHotScenic> findHAHotScenicByTime(String time) {
        return haHotScenicMapper.getHAHotScenicByTime(time);
    }

    @Override
    public List<String> YearCountBy() {
        return haHotScenicMapper.getYearCountBy();
    }
}
