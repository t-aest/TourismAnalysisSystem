package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import com.taest.tourismdatavisualization.mapper.ProvinceOtherEnterpriseEconomicMapper;
import com.taest.tourismdatavisualization.service.ProvinceOtherEnterpriseEconomicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProvinceOtherEnterpriseEconomicServiceImpl implements ProvinceOtherEnterpriseEconomicService {

    @Autowired
    private ProvinceOtherEnterpriseEconomicMapper provinceOtherEnterpriseEconomicMapper;

    @Override
    public List<ProvinceBaseEntity> findProvinceOtherEnterpriseEconomicByTime(String year) {
        return provinceOtherEnterpriseEconomicMapper.getProvinceOtherEnterpriseEconomicByTime(year);
    }

    @Override
    public ProvinceBaseEntity findProvinceOtherEnterpriseEconomicTotal(String year) {
        return provinceOtherEnterpriseEconomicMapper.getProvinceOtherEnterpriseEconomicTotal(year);
    }

    @Override
    public List<String> findYearCountBy() {
        return provinceOtherEnterpriseEconomicMapper.getYearCountBy();
    }

    @Override
    public ProvinceBaseEntity findHAOtherEnterpriseEconomicByYear(String year) {
        return provinceOtherEnterpriseEconomicMapper.getHAOtherEnterpriseEconomicByYear(year);
    }
}
