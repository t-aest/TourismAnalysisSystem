package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;
import com.taest.tourismdatavisualization.mapper.CityHotelsEconomicMapper;
import com.taest.tourismdatavisualization.service.CityHotelsEconomicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CityHotelsEconomicServiceImpl implements CityHotelsEconomicService {

    @Autowired
    private CityHotelsEconomicMapper cityHotelsEconomicMapper;

    @Override
    public List<CityBaseEntity> findCityHotelsEconomicByTime(String year) {
        return cityHotelsEconomicMapper.getCityHotelsEconomicByTime(year);
    }

    @Override
    public CityBaseEntity findCityHotelsEconomicTotal(String year) {
        return cityHotelsEconomicMapper.getCityHotelsEconomicTotal(year);
    }

    @Override
    public List<String> getYearCountBy() {
        return cityHotelsEconomicMapper.getYearCountBy();
    }
}
