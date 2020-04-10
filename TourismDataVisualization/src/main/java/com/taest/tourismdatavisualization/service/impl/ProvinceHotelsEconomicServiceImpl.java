package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import com.taest.tourismdatavisualization.mapper.ProvinceHotelsEconomicMapper;
import com.taest.tourismdatavisualization.service.ProvinceHotelsEconomicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProvinceHotelsEconomicServiceImpl implements ProvinceHotelsEconomicService {

    @Autowired
    private ProvinceHotelsEconomicMapper provinceHotelsEconomicMapper;

    @Override
    public List<ProvinceBaseEntity> findProvinceHotelsEconomicByTime(String year) {
        return provinceHotelsEconomicMapper.getProvinceHotelsEconomicByTime(year);
    }

    @Override
    public ProvinceBaseEntity findProvinceHotelsEconomicTotal(String year) {
        return provinceHotelsEconomicMapper.getProvinceHotelsEconomicTotal(year);
    }

    @Override
    public List<String> getYearCountBy() {
        return provinceHotelsEconomicMapper.getYearCountBy();
    }

    @Override
    public ProvinceBaseEntity findHAHotelsEconomicByYear(String year) {
        return provinceHotelsEconomicMapper.getHAHotelsEconomicByYear(year);
    }
}
