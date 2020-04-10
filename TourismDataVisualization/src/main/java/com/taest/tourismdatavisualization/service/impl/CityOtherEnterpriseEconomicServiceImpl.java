package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;
import com.taest.tourismdatavisualization.mapper.CityOtherEnterpriseEconomicMapper;
import com.taest.tourismdatavisualization.service.CityOtherEnterpriseEconomicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CityOtherEnterpriseEconomicServiceImpl implements CityOtherEnterpriseEconomicService {

    @Autowired
    private CityOtherEnterpriseEconomicMapper cityOtherEnterpriseEconomicMapper;

    @Override
    public List<CityBaseEntity> findCityOtherEnterpriseEconomicByTime(String year) {
        return cityOtherEnterpriseEconomicMapper.getCityOtherEnterpriseEconomicByTime(year);
    }

    @Override
    public CityBaseEntity findCityOtherEnterpriseEconomicTotal(String year) {
        return cityOtherEnterpriseEconomicMapper.getCityOtherEnterpriseEconomicTotal(year);
    }

    @Override
    public List<String> getYearCountBy() {
        return cityOtherEnterpriseEconomicMapper.getYearCountBy();
    }
}
