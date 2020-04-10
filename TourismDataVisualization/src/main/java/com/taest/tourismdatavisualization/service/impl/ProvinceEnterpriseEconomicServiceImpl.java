package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import com.taest.tourismdatavisualization.mapper.ProvinceEnterpriseEconomicMapper;
import com.taest.tourismdatavisualization.service.ProvinceEnterpriseEconomicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceEnterpriseEconomicServiceImpl implements ProvinceEnterpriseEconomicService {

    @Autowired
    private ProvinceEnterpriseEconomicMapper provinceEnterpriseEconomicMapper;

    @Override
    public List<ProvinceBaseEntity> findProvinceEnterpriseEconomicByTime(String year) {
        return provinceEnterpriseEconomicMapper.getProvinceEnterpriseEconomicByTime(year);
    }

    @Override
    public ProvinceBaseEntity findProvinceEnterpriseEconomicTotal(String year) {
        return provinceEnterpriseEconomicMapper.getProvinceEnterpriseEconomicTotal(year);
    }

    @Override
    public List<String> getYearCountBy() {
        return provinceEnterpriseEconomicMapper.getYearCountBy();
    }

    @Override
    public ProvinceBaseEntity findHAEnterpriseEconomicByYear(String year) {
        return provinceEnterpriseEconomicMapper.getHAEnterpriseEconomicByYear(year);
    }
}
