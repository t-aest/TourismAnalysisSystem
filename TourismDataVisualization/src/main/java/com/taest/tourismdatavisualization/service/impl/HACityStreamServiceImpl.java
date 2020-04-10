package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.HACityStream;
import com.taest.tourismdatavisualization.mapper.HACityStreamMapper;
import com.taest.tourismdatavisualization.service.HACityStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HACityStreamServiceImpl implements HACityStreamService {

    @Autowired
    private HACityStreamMapper haCityStreamMapper;

    @Override
    public List<HACityStream> findHACityStreamByTime(String time) {
        return haCityStreamMapper.getHACityStreamByTime(time);
    }

    @Override
    public List<String> findYearCountBy() {
        return haCityStreamMapper.getYearCountBy();
    }
}
