package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;
import com.taest.tourismdatavisualization.mapper.CityAgencyEconomicMapper;
import com.taest.tourismdatavisualization.service.CityAgencyEconomicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityAgencyEconomicServiceImpl implements CityAgencyEconomicService {

    @Autowired
    private CityAgencyEconomicMapper cityAgencyEconomicMapper;

    @Override
    public List<CityBaseEntity> findCityAgencyEconomicByTime(String year) {
        return cityAgencyEconomicMapper.getCityAgencyEconomicByTime(year);
    }

    @Override
    public CityBaseEntity findCityAgencyEconomicTotal(String year) {
        return cityAgencyEconomicMapper.getCityAgencyEconomicTotal(year);
    }

    @Override
    public List<String> getYearCountBy() {
        return cityAgencyEconomicMapper.getYearCountBy();
    }
}
