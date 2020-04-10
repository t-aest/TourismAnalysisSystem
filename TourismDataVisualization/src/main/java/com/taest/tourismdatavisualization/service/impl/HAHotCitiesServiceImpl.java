package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.HAHotCities;
import com.taest.tourismdatavisualization.mapper.HAHotCitiesMapper;
import com.taest.tourismdatavisualization.service.HAHotCitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HAHotCitiesServiceImpl implements HAHotCitiesService {

    @Autowired
    private HAHotCitiesMapper haHotCitiesMapper;

    @Override
    public List<HAHotCities> findHAHotCitiesByTime(String time) {
        return haHotCitiesMapper.getHAHotCitiesByTime(time);
    }

    @Override
    public List<String> findYearCountBy() {
        return haHotCitiesMapper.getYearCountBy();
    }
}
