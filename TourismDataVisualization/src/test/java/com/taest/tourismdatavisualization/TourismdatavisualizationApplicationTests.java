package com.taest.tourismdatavisualization;

import com.taest.tourismdatavisualization.domain.*;
import com.taest.tourismdatavisualization.domain.base.BaseEntity;
import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;
import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import com.taest.tourismdatavisualization.mapper.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TourismdatavisualizationApplicationTests {

    @Autowired
    private HAHotResidenceMapper haHotResidenceMapper;

    @Autowired
    private HACityStreamMapper haCityStreamMapper;

    @Autowired
    private HACityIncomeMapper haCityIncomeMapper;

    @Autowired
    private HAHotCitiesMapper haHotCitiesMapper;

    @Autowired
    private HAHotScenicInCitiesMapper haHotScenicInCitiesMapper;

    @Autowired
    private HAHotScenicMapper haHotScenicMapper;

    @Autowired
    private ProvinceEnterpriseEconomicMapper provinceEnterpriseEconomicMapper;

    @Autowired
    private ProvinceAgencyEconomicMapper provinceAgencyEconomicMapper;

    @Autowired
    private CityEnterpriseEconomicMapper cityEnterpriseEconomicMapper;

    @Autowired
    private NationForeignTouristsMapper nationForeignTouristsMapper;

    @Autowired
    private ProvinceHotelsMapper provinceHotelsMapper;

    @Autowired
    private CommentAnalysisMapper commentAnalysisMapper;


    @Test
    public void getHotResidenceByTime() {
        int time = 2016;
        List<HAHotResidence> hotResidenceByTime = haHotResidenceMapper.getHotResidenceByTime(String.valueOf(time));
        hotResidenceByTime.forEach(System.out::println);

    }

    @Test
    public void getHACityStreamByTime() {
        int time = 2016;
        List<HACityStream> hotResidenceByTime = haCityStreamMapper.getHACityStreamByTime(String.valueOf(time));
        hotResidenceByTime.forEach(System.out::println);

    }

    @Test
    public void getHACityIncomeByTime() {
        int time = 2016;
        List<HACityIncome> hotResidenceByTime = haCityIncomeMapper.getHACityIncomeByTime(String.valueOf(time));
        hotResidenceByTime.forEach(System.out::println);

        List<String> yearCountBy = haCityIncomeMapper.getYearCountBy();
        System.out.println("yearCountBy = " + yearCountBy);

    }

    @Test
    public void getHAHotCitiesByTime() {
        int time = 2016;
        List<HAHotCities> haHotCities = haHotCitiesMapper.getHAHotCitiesByTime(String.valueOf(time));
        List<String> yearCountBy = haHotCitiesMapper.getYearCountBy();
        yearCountBy.forEach(System.out::println);
        haHotCities.forEach(System.out::println);

    }

    @Test
    public void getHAHotScenicInCities() {
        int time = 2016;
        String city = "洛阳市";
        List<HAHotScenicInCities> haHotScenicInCities = haHotScenicInCitiesMapper.getHAHotScenicInCitiesByTimeAndCity(String.valueOf(time), city);
        List<String> yearCountBy = haHotScenicInCitiesMapper.getYearCountBy();
        yearCountBy.forEach(System.out::println);
        haHotScenicInCities.forEach(System.out::println);

    }

    @Test
    public void getHAHotScenicByTime() {
        int time = 2016;
        String city = "洛阳市";
        List<HAHotScenic> haHotScenicInCities = haHotScenicMapper.getHAHotScenicByTime(String.valueOf(time));
        List<String> yearCountBy = haHotScenicMapper.getYearCountBy();
        yearCountBy.forEach(System.out::println);
        haHotScenicInCities.forEach(System.out::println);

    }

    @Test
    public void getProvinceEnterpriseEconomicByTime() {
        int time = 2006;
        List<ProvinceBaseEntity> provinceBaseEntities = provinceEnterpriseEconomicMapper.getProvinceEnterpriseEconomicByTime(String.valueOf(time));
        List<String> yearCountBy = provinceEnterpriseEconomicMapper.getYearCountBy();
        System.out.println("provinceEnterpriseEconomicMapper.getProvinceEnterpriseEconomicTotal(String.valueOf(time)) = " + provinceEnterpriseEconomicMapper.getProvinceEnterpriseEconomicTotal(String.valueOf(time)));
        yearCountBy.forEach(System.out::println);
        provinceBaseEntities.forEach(System.out::println);

    }

    @Test
    public void getProvinceAgencyEconomicByTime() {
        int time = 2006;
        List<ProvinceBaseEntity> provinceBaseEntities = provinceAgencyEconomicMapper.getProvinceAgencyEconomicByTime(String.valueOf(time));
        List<String> yearCountBy = provinceAgencyEconomicMapper.getYearCountBy();
        System.out.println("provinceEnterpriseEconomicMapper.getProvinceEnterpriseEconomicTotal(String.valueOf(time)) = " + provinceAgencyEconomicMapper.getProvinceAgencyEconomicTotal(String.valueOf(time)));
        yearCountBy.forEach(System.out::println);
        provinceBaseEntities.forEach(System.out::println);

    }

    @Test
    public void getCityEnterpriseEconomicByTime() {
        int time = 2006;
        List<CityBaseEntity> provinceBaseEntities = cityEnterpriseEconomicMapper.getCityEnterpriseEconomicByTime(String.valueOf(time));
        List<String> yearCountBy = cityEnterpriseEconomicMapper.getYearCountBy();
        System.out.println("provinceEnterpriseEconomicMapper.getProvinceEnterpriseEconomicTotal(String.valueOf(time)) = " + cityEnterpriseEconomicMapper.getCityEnterpriseEconomicTotal(String.valueOf(time)));
        yearCountBy.forEach(System.out::println);
        provinceBaseEntities.forEach(System.out::println);

    }

    @Test
    public void getNationForeignTouristsByTime() {
        int time = 2006;
        List<NationForeignTourists> nationForeignTourists = nationForeignTouristsMapper.getNationForeignTouristsByTime(String.valueOf(time));
        List<String> yearCountBy = nationForeignTouristsMapper.getYearCountBy();
        System.out.println("provinceEnterpriseEconomicMapper.getProvinceEnterpriseEconomicTotal(String.valueOf(time)) = " + nationForeignTouristsMapper.getNationForeignTouristsTotal(String.valueOf(time)));
        yearCountBy.forEach(System.out::println);
        nationForeignTourists.forEach(System.out::println);

    }

    @Test
    public void getProvinceHotelsByTime() {
        int time = 2006;
        List<ProvinceHotels> provinceHotels = provinceHotelsMapper.getProvinceHotelsByTime(String.valueOf(time));
        List<String> yearCountBy = provinceHotelsMapper.getYearCountBy();
        System.out.println("provinceEnterpriseEconomicMapper.getProvinceEnterpriseEconomicTotal(String.valueOf(time)) = " + provinceHotelsMapper.getProvinceHotelsTotal(String.valueOf(time)));
        yearCountBy.forEach(System.out::println);
        provinceHotels.forEach(System.out::println);

        System.out.println(provinceEnterpriseEconomicMapper.getHAEnterpriseEconomicByYear(String.valueOf(time)));

    }

    @Test
    public void testCommentAnalysis() {
        System.out.println(commentAnalysisMapper.getComment());
        System.out.println(commentAnalysisMapper.getWordCloud());
    }
    @Test
    public void testScenicCommentAnalysis() {
        System.out.println(commentAnalysisMapper.getScenicWordCloud("老君山"));
    }


}
