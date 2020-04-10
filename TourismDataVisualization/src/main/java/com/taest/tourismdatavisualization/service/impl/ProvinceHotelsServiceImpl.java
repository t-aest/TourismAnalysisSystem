package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.ProvinceHotels;
import com.taest.tourismdatavisualization.mapper.ProvinceHotelsMapper;
import com.taest.tourismdatavisualization.service.ProvinceHotelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceHotelsServiceImpl implements ProvinceHotelsService {

    @Autowired
    private ProvinceHotelsMapper provinceHotelsMapper;

    @Override
    public List<ProvinceHotels> findProvinceHotelsByTime(String year) {
        return provinceHotelsMapper.getProvinceHotelsByTime(year);
    }

    @Override
    public ProvinceHotels findProvinceHotelsTotal(String year) {
        return provinceHotelsMapper.getProvinceHotelsTotal(year);
    }

    @Override
    public List<String> getYearCountBy() {
        return provinceHotelsMapper.getYearCountBy();
    }
}
