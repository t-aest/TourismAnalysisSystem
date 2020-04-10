package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import com.taest.tourismdatavisualization.mapper.ProvinceAgencyEconomicMapper;
import com.taest.tourismdatavisualization.service.ProvinceAgencyEconomicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProvinceAgencyEconomicServiceImpl implements ProvinceAgencyEconomicService {

    @Autowired
    private ProvinceAgencyEconomicMapper provinceAgencyEconomicMapper;

    @Override
    public List<ProvinceBaseEntity> findProvinceAgencyEconomicByTime(String year) {
        return provinceAgencyEconomicMapper.getProvinceAgencyEconomicByTime(year);
    }

    @Override
    public ProvinceBaseEntity findProvinceAgencyEconomicTotal(String year) {
        return provinceAgencyEconomicMapper.getProvinceAgencyEconomicTotal(year);
    }

    @Override
    public List<String> getYearCountBy() {
        return provinceAgencyEconomicMapper.getYearCountBy();
    }

    @Override
    public ProvinceBaseEntity findHAAgencyEconomicByYear(String year) {
        return provinceAgencyEconomicMapper.getHAAgencyEconomicByYear(year);
    }
}
