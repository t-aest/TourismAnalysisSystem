package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.HAHotResidence;
import com.taest.tourismdatavisualization.mapper.HAHotResidenceMapper;
import com.taest.tourismdatavisualization.service.HAHotResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HAHotResidenceServiceImpl implements HAHotResidenceService {

    @Autowired
    private HAHotResidenceMapper haHotResidenceMapper;

    @Override
    public List<HAHotResidence> findHotResidenceByTime(String time) {
        return haHotResidenceMapper.getHotResidenceByTime(time);
    }

    @Override
    public List<String> findYearCountBy() {
        return haHotResidenceMapper.getYearCountBy();
    }
}
