package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.HACityIncome;
import com.taest.tourismdatavisualization.mapper.HACityIncomeMapper;
import com.taest.tourismdatavisualization.service.HACityIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HACityIncomeServiceImpl implements HACityIncomeService {

    @Autowired
    private HACityIncomeMapper haCityIncomeMapper;

    @Override
    public List<HACityIncome> findHACityIncomeByTime(String time) {
        return haCityIncomeMapper.getHACityIncomeByTime(time);
    }

    @Override
    public List<String> findYearCountBy() {
        return haCityIncomeMapper.getYearCountBy();
    }
}
