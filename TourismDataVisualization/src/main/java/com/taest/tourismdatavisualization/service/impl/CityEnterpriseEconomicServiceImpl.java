package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;
import com.taest.tourismdatavisualization.mapper.CityEnterpriseEconomicMapper;
import com.taest.tourismdatavisualization.service.CityEnterpriseEconomicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CityEnterpriseEconomicServiceImpl implements CityEnterpriseEconomicService {

    @Autowired
    private CityEnterpriseEconomicMapper cityEnterpriseEconomicMapper;

    @Override
    public List<CityBaseEntity> findCityEnterpriseEconomicByTime(String year) {
        return cityEnterpriseEconomicMapper.getCityEnterpriseEconomicByTime(year);
    }

    @Override
    public CityBaseEntity findCityEnterpriseEconomicTotal(String year) {
        return cityEnterpriseEconomicMapper.getCityEnterpriseEconomicTotal(year);
    }

    @Override
    public List<String> getYearCountBy() {
        return cityEnterpriseEconomicMapper.getYearCountBy();
    }
}
