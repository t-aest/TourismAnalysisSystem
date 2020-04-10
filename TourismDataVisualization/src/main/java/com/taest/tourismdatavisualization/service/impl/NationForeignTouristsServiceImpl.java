package com.taest.tourismdatavisualization.service.impl;

import com.taest.tourismdatavisualization.domain.NationForeignTourists;
import com.taest.tourismdatavisualization.mapper.NationForeignTouristsMapper;
import com.taest.tourismdatavisualization.service.NationForeignTouristsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NationForeignTouristsServiceImpl implements NationForeignTouristsService {

    @Autowired
    private NationForeignTouristsMapper nationForeignTouristsMapper;

    @Override
    public List<NationForeignTourists> findNationForeignTouristsByTime(String year) {
        return nationForeignTouristsMapper.getNationForeignTouristsByTime(year);
    }

    @Override
    public NationForeignTourists findNationForeignTouristsTotal(String year) {
        return nationForeignTouristsMapper.getNationForeignTouristsTotal(year);
    }

    @Override
    public List<String> getYearCountBy() {
        return nationForeignTouristsMapper.getYearCountBy();
    }
}
