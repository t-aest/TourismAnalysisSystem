package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.HAHotScenicInCities;
import com.taest.tourismdatavisualization.mapper.HAHotScenicInCitiesMapper;
import com.taest.tourismdatavisualization.service.HAHotScenicInCitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HAHotScenicInCitiesServiceImpl implements HAHotScenicInCitiesService {

    @Autowired
    private HAHotScenicInCitiesMapper haHotScenicInCitiesMapper;

    @Override
    public List<HAHotScenicInCities> findHAHotScenicInCitiesByTimeAndCity(String time, String city) {
        return haHotScenicInCitiesMapper.getHAHotScenicInCitiesByTimeAndCity(time,city);
    }

    @Override
    public List<String> findYearCountBy() {
        return haHotScenicInCitiesMapper.getYearCountBy();
    }
}
