package com.taest.tourismdatavisualization.web.controller;

import com.taest.tourismdatavisualization.domain.HAHotResidence;
import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;
import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import com.taest.tourismdatavisualization.domain.request.CityBaseEntityVo;
import com.taest.tourismdatavisualization.domain.request.ProvinceBaseEntityVo;
import com.taest.tourismdatavisualization.mapper.CityPredictIncomeMapper;
import com.taest.tourismdatavisualization.mapper.HaPredictIncomeMapper;
import com.taest.tourismdatavisualization.mapper.ProvincePredictIncomeMapper;
import com.taest.tourismdatavisualization.service.*;
import com.taest.tourismdatavisualization.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.taest.tourismdatavisualization.utils.StringUtils.getChineseFromCE;

/**
 * 经济指标controller层
 */
@RestController
@RequestMapping("economic")
public class EconomicIndicatorsController {

    @Autowired
    private ProvinceEnterpriseEconomicService provinceEnterpriseEconomicService;

    @Autowired
    private ProvinceAgencyEconomicService provinceAgencyEconomicService;

    @Autowired
    private ProvinceHotelsEconomicService provinceHotelsEconomicService;

    @Autowired
    private ProvinceOtherEnterpriseEconomicService provinceOtherEnterpriseEconomicService;

    @Autowired
    private CityAgencyEconomicService cityAgencyEconomicService;

    @Autowired
    private CityEnterpriseEconomicService cityEnterpriseEconomicService;

    @Autowired
    private CityHotelsEconomicService cityHotelsEconomicService;

    @Autowired
    private CityOtherEnterpriseEconomicService cityOtherEnterpriseEconomicService;

    @Autowired
    private HaPredictIncomeMapper haPredictIncomeMapper;

    @Autowired
    private ProvincePredictIncomeMapper provincePredictIncomeMapper;

    @Autowired
    private CityPredictIncomeMapper cityPredictIncomeMapper;

    /**
     * 查询各省旅游企业经济指标
     *
     * @return
     */
    @GetMapping("/provinceenterprise")
    public ModelMap listProvinceEnterprise() {
        ModelMap map = new ModelMap();

        // 所有年份以及旅游企业经济指标
        ArrayList<Object> completeList = new ArrayList<>();

        // 年份
        List<String> yearList = provinceEnterpriseEconomicService.getYearCountBy();

        // 存放每年的总旅游企业经济指标
        Map<String, Double> yearMap = new HashMap<>();

        for (String year : yearList) {

            //存放每年以及该年旅游企业经济指标
            HashMap<Object, Object> hashMap = new HashMap<>();

            List<ProvinceBaseEntity> provinceBaseEntities = provinceEnterpriseEconomicService.findProvinceEnterpriseEconomicByTime(year);

//            provinceBaseEntities.stream().filter(item->item.)

            // 存放要返回前台的详细旅游企业经济指标信息
            List<ProvinceBaseEntityVo> provinceBaseEntityVos = new ArrayList<ProvinceBaseEntityVo>();

            for (ProvinceBaseEntity provinceBaseEntity : provinceBaseEntities) {

                ProvinceBaseEntityVo provinceBaseEntityVo = new ProvinceBaseEntityVo();

                String province = provinceBaseEntity.getProvinceName();
                if (Integer.parseInt(provinceBaseEntity.getProvinceId()) != 1024) {

                    String lng = JsonUtils.getLalFromProvince(province).get(0);
                    String lat = JsonUtils.getLalFromProvince(province).get(1);

                    provinceBaseEntityVo.setProvinceId(provinceBaseEntity.getProvinceId());
                    provinceBaseEntityVo.setProvinceName(getChineseFromCE(provinceBaseEntity.getProvinceName()));
                    provinceBaseEntityVo.setE_entrepreneur(provinceBaseEntity.getE_entrepreneur());
                    provinceBaseEntityVo.setE_fixed_assets(provinceBaseEntity.getE_fixed_assets());
                    provinceBaseEntityVo.setE_taking(provinceBaseEntity.getE_taking());
                    provinceBaseEntityVo.setYear(provinceBaseEntity.getYear());
                    provinceBaseEntityVo.setLng(lng);
                    provinceBaseEntityVo.setLat(lat);

                    provinceBaseEntityVos.add(provinceBaseEntityVo);
                } else {
                    yearMap.put(String.valueOf(Integer.parseInt(year) - 1), Double.parseDouble(provinceBaseEntity.getE_taking()));
                }
            }
            hashMap.put("year", String.valueOf(Integer.parseInt(year) - 1));
            hashMap.put("provinceBaseEntityVos", provinceBaseEntityVos);
            completeList.add(hashMap);
        }
//        map.put("year",year);
        map.put("yearMap", yearMap);
        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);

        return map;
    }

    /**
     * 查询省旅游企业各收入
     *
     * @return
     */
    @GetMapping("/provinceeconomictotal")
    public ModelMap listProvinceEconomicTotal() {
        ModelMap map = new ModelMap();

        // 年份
        List<String> yearList = provinceEnterpriseEconomicService.getYearCountBy();

        // 存放旅行社每年收入
        List<String> provinceAgencyEconomicTotalIncome = new ArrayList<>();

        // 存放星级酒店每年收入
        List<String> provinceHotelsEconomicTotalIncome = new ArrayList<>();

        // 存放其他旅游企业每年收入
        List<String> provinceOtherEnterpriseEconomicTotalIncome = new ArrayList<>();

        // 存放旅行企业每年总收入
        List<String> provinceEnterpriseEconomicTotalIncome = new ArrayList<>();

        // 存放预测的旅行社收入
        List<String> provincePredictAgencyEconomicTotalIncome = new ArrayList<>();

        // 存放预测的星级酒店收入
        List<String> provincePredictHotelsEconomicTotalIncome = new ArrayList<>();

        // 存放预测的其他旅游企业收入
        List<String> provincePredictOtherEnterpriseEconomicTotalIncome = new ArrayList<>();

        // 存放预测的旅行企业总收入
        List<String> provincePredictEnterpriseEconomicTotalIncome = new ArrayList<>();

        for (String year : yearList) {

            // 该年旅行社收入
            ProvinceBaseEntity provinceAgencyEconomicTotal = provinceAgencyEconomicService.findProvinceAgencyEconomicTotal(year);
            provinceAgencyEconomicTotalIncome.add(provinceAgencyEconomicTotal.getE_taking());

            // 该年星级酒店收入
            ProvinceBaseEntity provinceHotelsEconomicTotal = provinceHotelsEconomicService.findProvinceHotelsEconomicTotal(year);
            provinceHotelsEconomicTotalIncome.add(provinceHotelsEconomicTotal.getE_taking());

            // 该年其他收入
            ProvinceBaseEntity provinceOtherEnterpriseEconomicTotal = provinceOtherEnterpriseEconomicService.findProvinceOtherEnterpriseEconomicTotal(year);
            provinceOtherEnterpriseEconomicTotalIncome.add(provinceOtherEnterpriseEconomicTotal.getE_taking());

            // 该年总收入
            ProvinceBaseEntity provinceEnterpriseEconomicTotal = provinceEnterpriseEconomicService.findProvinceEnterpriseEconomicTotal(year);
            provinceEnterpriseEconomicTotalIncome.add(provinceEnterpriseEconomicTotal.getE_taking());

            // 预测的该年旅行社收入
            provincePredictAgencyEconomicTotalIncome.add(provincePredictIncomeMapper.getProvincePredictAgencyIncomeByYear(year));

            // 预测的该年星级酒店收入
            provincePredictHotelsEconomicTotalIncome.add(provincePredictIncomeMapper.getProvincePredictHotalIncomeByYear(year));

            // 预测的该年其他收入
            provincePredictOtherEnterpriseEconomicTotalIncome.add(provincePredictIncomeMapper.getProvincePredictOtherEnterpriseIncomeByYear(year));

            // 预测的该年总收入
            provincePredictEnterpriseEconomicTotalIncome.add(provincePredictIncomeMapper.getProvincePredictEnterpriseIncomeByYear(year));

        }

        // 存放旅游企业各收入类型
        List<String> provinceEconomicTotalType = new ArrayList<String>() {{
            add("Agency");
            add("Hotels");
            add("other");
            add("total");
            add("Predict-Agency");
            add("Predict-Hotels");
            add("Predict-other");
            add("Predict-total");
        }};
        map.put("yearList", yearList.stream().map(item -> Integer.parseInt(item) - 1));
        map.put("provinceAgencyEconomicTotalIncome", provinceAgencyEconomicTotalIncome);
        map.put("provinceHotelsEconomicTotalIncome", provinceHotelsEconomicTotalIncome);
        map.put("provinceOtherEnterpriseEconomicTotalIncome", provinceOtherEnterpriseEconomicTotalIncome);
        map.put("provinceEnterpriseEconomicTotalIncome", provinceEnterpriseEconomicTotalIncome);

        map.put("provincePredictAgencyEconomicTotalIncome", provincePredictAgencyEconomicTotalIncome);
        map.put("provincePredictHotelsEconomicTotalIncome", provincePredictHotelsEconomicTotalIncome);
        map.put("provincePredictOtherEnterpriseEconomicTotalIncome", provincePredictOtherEnterpriseEconomicTotalIncome);
        map.put("provincePredictEnterpriseEconomicTotalIncome", provincePredictEnterpriseEconomicTotalIncome);

        map.put("provinceEconomicTotalType", provinceEconomicTotalType);
        return map;
    }

    /**
     * 查询各省的旅行社经济指标
     *
     * @return
     */
    @GetMapping("/provinceagency")
    public ModelMap listProvinceAgency() {
        ModelMap map = new ModelMap();
        // 所有年份以及旅行社经济指标
        ArrayList<Object> completeList = new ArrayList<>();

        // 年份
        List<String> yearList = provinceEnterpriseEconomicService.getYearCountBy();

        // 存放每年的总旅行社经济指标
        Map<String, Double> yearMap = new HashMap<>();

        for (String year : yearList) {
            System.out.println("year = " + year);

            //存放每年以及该年旅行社经济指标
            HashMap<Object, Object> hashMap = new HashMap<>();

            List<ProvinceBaseEntity> provinceBaseEntities = provinceAgencyEconomicService.findProvinceAgencyEconomicByTime(year);

            // 存放要返回前台的详细旅行社经济指标信息
            List<ProvinceBaseEntityVo> provinceBaseEntityVos = new ArrayList<ProvinceBaseEntityVo>();

            for (ProvinceBaseEntity provinceBaseEntity : provinceBaseEntities) {

                ProvinceBaseEntityVo provinceBaseEntityVo = new ProvinceBaseEntityVo();

                String province = provinceBaseEntity.getProvinceName();
                if (Integer.parseInt(provinceBaseEntity.getProvinceId()) != 1024) {

                    String lng = JsonUtils.getLalFromProvince(province).get(0);
                    String lat = JsonUtils.getLalFromProvince(province).get(1);

                    provinceBaseEntityVo.setProvinceId(provinceBaseEntity.getProvinceId());
                    provinceBaseEntityVo.setProvinceName(provinceBaseEntity.getProvinceName());
                    provinceBaseEntityVo.setE_entrepreneur(provinceBaseEntity.getE_entrepreneur());
                    provinceBaseEntityVo.setE_fixed_assets(provinceBaseEntity.getE_fixed_assets());
                    provinceBaseEntityVo.setE_taking(provinceBaseEntity.getE_taking());
                    provinceBaseEntityVo.setYear(provinceBaseEntity.getYear());
                    provinceBaseEntityVo.setLng(lng);
                    provinceBaseEntityVo.setLat(lat);

                    provinceBaseEntityVos.add(provinceBaseEntityVo);
                } else {
                    yearMap.put(String.valueOf(Integer.parseInt(year) - 1), Double.parseDouble(provinceBaseEntity.getE_taking()));
                }
            }
            hashMap.put("year", String.valueOf(Integer.parseInt(year) - 1));
            hashMap.put("provinceBaseEntityVos", provinceBaseEntityVos);
            completeList.add(hashMap);
        }
//        map.put("year",year);
        map.put("yearMap", yearMap);
        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);


        return map;
    }

    /**
     * 查询各省的星级酒店经济指标
     *
     * @return
     */
    @GetMapping("/provincehotels")
    public ModelMap listProvinceHotels() {
        ModelMap map = new ModelMap();


        return map;
    }

    /**
     * 查询各省的其他经济指标
     *
     * @return
     */
    @GetMapping("/provinceother")
    public ModelMap listProvinceOther() {
        ModelMap map = new ModelMap();


        return map;
    }

    /**
     * 查询主要城市旅游企业各收入
     *
     * @return
     */
    @GetMapping("/cityeconomictotal")
    public ModelMap listCityEconomicTotal() {
        ModelMap map = new ModelMap();

        // 年份
        List<String> yearList = provinceEnterpriseEconomicService.getYearCountBy();

        // 存放旅行社每年收入
        List<String> CityAgencyEconomicTotalIncome = new ArrayList<>();

        // 存放星级酒店每年收入
        List<String> CityHotelsEconomicTotalIncome = new ArrayList<>();

        // 存放其他旅游企业每年收入
        List<String> CityOtherEnterpriseEconomicTotalIncome = new ArrayList<>();

        // 存放旅行企业每年总收入
        List<String> CityEnterpriseEconomicTotalIncome = new ArrayList<>();

        // 存放预测的旅行社收入
        List<String> CityPredictAgencyEconomicTotalIncome = new ArrayList<>();

        // 存放预测的星级酒店收入
        List<String> CityPredictHotelsEconomicTotalIncome = new ArrayList<>();

        // 存放预测的其他旅游企业收入
        List<String> CityPredictOtherEnterpriseEconomicTotalIncome = new ArrayList<>();

        // 存放预测的旅行企业总收入
        List<String> CityPredictEnterpriseEconomicTotalIncome = new ArrayList<>();

        for (String year : yearList) {

            // 该年旅行社收入
            CityBaseEntity cityAgencyEconomicTotal = cityAgencyEconomicService.findCityAgencyEconomicTotal(year);
            CityAgencyEconomicTotalIncome.add(cityAgencyEconomicTotal.getE_taking());

            // 该年星级酒店收入
            CityBaseEntity cityHotelsEconomicTotal = cityHotelsEconomicService.findCityHotelsEconomicTotal(year);
            CityHotelsEconomicTotalIncome.add(cityHotelsEconomicTotal.getE_taking());

            // 该年其他收入
            CityBaseEntity cityOtherEnterpriseEconomicTotal = cityOtherEnterpriseEconomicService.findCityOtherEnterpriseEconomicTotal(year);
            CityOtherEnterpriseEconomicTotalIncome.add(cityOtherEnterpriseEconomicTotal.getE_taking());

            // 该年总收入
            CityBaseEntity cityEnterpriseEconomicTotal = cityEnterpriseEconomicService.findCityEnterpriseEconomicTotal(year);
            CityEnterpriseEconomicTotalIncome.add(cityEnterpriseEconomicTotal.getE_taking());

            // 预测的该年旅行社收入
            CityPredictAgencyEconomicTotalIncome.add(cityPredictIncomeMapper.getCityPredictAgencyIncomeByYear(year));

            // 预测的该年星级酒店收入
            CityPredictHotelsEconomicTotalIncome.add(cityPredictIncomeMapper.getCityPredictHotalIncomeByYear(year));

            // 预测的该年其他收入
            CityPredictOtherEnterpriseEconomicTotalIncome.add(cityPredictIncomeMapper.getCityPredictOtherEnterpriseIncomeByYear(year));

            // 预测的该年总收入
            CityPredictEnterpriseEconomicTotalIncome.add(cityPredictIncomeMapper.getCityPredictEnterpriseIncomeByYear(year));

        }

        // 存放旅游企业各收入类型
        List<String> cityEconomicTotalType = new ArrayList<String>() {{
            add("Agency");
            add("Hotels");
            add("other");
            add("total");
            add("Predict-Agency");
            add("Predict-Hotels");
            add("Predict-other");
            add("Predict-total");
        }};
        map.put("yearList", yearList.stream().map(item -> Integer.parseInt(item) - 1));
        map.put("CityAgencyEconomicTotalIncome", CityAgencyEconomicTotalIncome);
        map.put("CityHotelsEconomicTotalIncome", CityHotelsEconomicTotalIncome);
        map.put("CityOtherEnterpriseEconomicTotalIncome", CityOtherEnterpriseEconomicTotalIncome);
        map.put("CityEnterpriseEconomicTotalIncome", CityEnterpriseEconomicTotalIncome);

        map.put("CityPredictAgencyEconomicTotalIncome", CityPredictAgencyEconomicTotalIncome);
        map.put("CityPredictHotelsEconomicTotalIncome", CityPredictHotelsEconomicTotalIncome);
        map.put("CityPredictOtherEnterpriseEconomicTotalIncome", CityPredictOtherEnterpriseEconomicTotalIncome);
        map.put("CityPredictEnterpriseEconomicTotalIncome", CityPredictEnterpriseEconomicTotalIncome);

        map.put("cityEconomicTotalType", cityEconomicTotalType);

        return map;
    }

    /**
     * 查询主要城市旅游企业经济指标
     *
     * @return
     */
    @GetMapping("/cityenterprise")
    public ModelMap listCityEnterprise() {
        ModelMap map = new ModelMap();

        // 所有年份以及主要城市旅游企业经济指标
        ArrayList<Object> completeList = new ArrayList<>();

        // 年份
        List<String> yearList = provinceEnterpriseEconomicService.getYearCountBy();

        // 存放每年的总主要城市旅游企业经济指标
        Map<String, Double> yearMap = new HashMap<>();

        for (String year : yearList) {
            //存放每年以及该年主要城市旅游企业经济指标
            HashMap<Object, Object> hashMap = new HashMap<>();
            List<CityBaseEntity> cityBaseEntities = cityEnterpriseEconomicService.findCityEnterpriseEconomicByTime(year);

            List<CityBaseEntity> cityBaseEntityist = cityBaseEntities.stream()
                    .filter(item -> JsonUtils.getLalFromCity(item.getCityName()) != null && !JsonUtils.getLalFromCity(item.getCityName()).isEmpty())
                    .collect(Collectors.toList());

            // 存放要返回前台的详细主要城市旅游企业经济指标信息
            List<CityBaseEntityVo> cityBaseEntityVos = new ArrayList<CityBaseEntityVo>();

            for (CityBaseEntity cityBaseEntity : cityBaseEntityist) {

                CityBaseEntityVo cityBaseEntityVo = new CityBaseEntityVo();

                String cityName = cityBaseEntity.getCityName();

                String lng = JsonUtils.getLalFromCity(cityName).get(0);
                String lat = JsonUtils.getLalFromCity(cityName).get(1);

                cityBaseEntityVo.setCityId(cityBaseEntity.getCityId());
                cityBaseEntityVo.setCityName(cityBaseEntity.getCityName());
                cityBaseEntityVo.setE_entrepreneur(cityBaseEntity.getE_entrepreneur());
                cityBaseEntityVo.setE_fixed_assets(cityBaseEntity.getE_fixed_assets());
                cityBaseEntityVo.setE_taking(cityBaseEntity.getE_taking());
                cityBaseEntityVo.setYear(cityBaseEntity.getYear());
                cityBaseEntityVo.setLng(lng);
                cityBaseEntityVo.setLat(lat);

                cityBaseEntityVos.add(cityBaseEntityVo);

            }
            hashMap.put("year", String.valueOf(Integer.parseInt(year) - 1));
            hashMap.put("cityBaseEntityVos", cityBaseEntityVos);
            completeList.add(hashMap);
        }
        map.put("yearMap", yearMap);
        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);

        return map;
    }

    /**
     * 查询主要城市的旅行社经济指标
     *
     * @return
     */
    @GetMapping("/cityagency")
    public ModelMap listCityAgency() {
        ModelMap map = new ModelMap();


        return map;
    }

    /**
     * 查询主要城市的星级酒店经济指标
     *
     * @return
     */
    @GetMapping("/cityhotels")
    public ModelMap listCityHotels() {
        ModelMap map = new ModelMap();


        return map;
    }

    /**
     * 查询主要城市的其他经济指标
     *
     * @return
     */
    @GetMapping("/cityother")
    public ModelMap listCityOther() {
        ModelMap map = new ModelMap();


        return map;
    }

    /**
     * 查询河南省旅游企业各收入
     *
     * @return
     */
    @GetMapping("/henaneconomic")
    public ModelMap listHenanEconomic() {
        ModelMap map = new ModelMap();

        // 年份
        List<String> yearList = provinceEnterpriseEconomicService.getYearCountBy();

        // 存放旅行社收入
        List<String> provinceAgencyEconomicTotalIncome = new ArrayList<>();

        // 存放星级酒店收入
        List<String> provinceHotelsEconomicTotalIncome = new ArrayList<>();

        // 存放其他旅游企业收入
        List<String> provinceOtherEnterpriseEconomicTotalIncome = new ArrayList<>();

        // 存放旅行企业总收入
        List<String> provinceEnterpriseEconomicTotalIncome = new ArrayList<>();

        // 存放预测的旅行社收入
        List<String> provincePredictAgencyEconomicTotalIncome = new ArrayList<>();

        // 存放预测的星级酒店收入
        List<String> provincePredictHotelsEconomicTotalIncome = new ArrayList<>();

        // 存放预测的其他旅游企业收入
        List<String> provincePredictOtherEnterpriseEconomicTotalIncome = new ArrayList<>();

        // 存放预测的旅行企业总收入
        List<String> provincePredictEnterpriseEconomicTotalIncome = new ArrayList<>();

        for (String year : yearList) {

            // 该年旅行社收入
            ProvinceBaseEntity provinceAgencyEconomicTotal = provinceAgencyEconomicService.findHAAgencyEconomicByYear(year);
            provinceAgencyEconomicTotalIncome.add(provinceAgencyEconomicTotal.getE_taking());

            // 该年星级酒店收入
            ProvinceBaseEntity provinceHotelsEconomicTotal = provinceHotelsEconomicService.findHAHotelsEconomicByYear(year);
            provinceHotelsEconomicTotalIncome.add(provinceHotelsEconomicTotal.getE_taking());

            // 该年其他收入
            ProvinceBaseEntity provinceOtherEnterpriseEconomicTotal = provinceOtherEnterpriseEconomicService.findHAOtherEnterpriseEconomicByYear(year);
            provinceOtherEnterpriseEconomicTotalIncome.add(provinceOtherEnterpriseEconomicTotal.getE_taking());

            // 该年总收入
            ProvinceBaseEntity provinceEnterpriseEconomicTotal = provinceEnterpriseEconomicService.findHAEnterpriseEconomicByYear(year);
            provinceEnterpriseEconomicTotalIncome.add(provinceEnterpriseEconomicTotal.getE_taking());

            // 预测的该年旅行社收入
            provincePredictAgencyEconomicTotalIncome.add(haPredictIncomeMapper.getPredictAgencyIncomeByYear(year));

            // 预测的该年星级酒店收入
            provincePredictHotelsEconomicTotalIncome.add(haPredictIncomeMapper.getPredictHotalIncomeByYear(year));

            // 预测的该年其他收入
            provincePredictOtherEnterpriseEconomicTotalIncome.add(haPredictIncomeMapper.getPredictOtherEnterpriseIncomeByYear(year));

            // 预测的该年总收入
            provincePredictEnterpriseEconomicTotalIncome.add(haPredictIncomeMapper.getPredictEnterpriseIncomeByYear(year));

        }

        // 存放旅游企业各收入类型
        List<String> provinceEconomicTotalType = new ArrayList<String>() {{
            add("Agency");
            add("Hotels");
            add("other");
            add("total");
            add("Predict-Agency");
            add("Predict-Hotels");
            add("Predict-other");
            add("Predict-total");
        }};
        map.put("yearList", yearList.stream().map(item -> Integer.parseInt(item) - 1));
        map.put("provinceAgencyEconomicTotalIncome", provinceAgencyEconomicTotalIncome);
        map.put("provinceHotelsEconomicTotalIncome", provinceHotelsEconomicTotalIncome);
        map.put("provinceOtherEnterpriseEconomicTotalIncome", provinceOtherEnterpriseEconomicTotalIncome);
        map.put("provinceEnterpriseEconomicTotalIncome", provinceEnterpriseEconomicTotalIncome);
        map.put("provincePredictAgencyEconomicTotalIncome", provincePredictAgencyEconomicTotalIncome);
        map.put("provincePredictHotelsEconomicTotalIncome", provincePredictHotelsEconomicTotalIncome);
        map.put("provincePredictOtherEnterpriseEconomicTotalIncome", provincePredictOtherEnterpriseEconomicTotalIncome);
        map.put("provincePredictEnterpriseEconomicTotalIncome", provincePredictEnterpriseEconomicTotalIncome);
        map.put("provinceEconomicTotalType", provinceEconomicTotalType);

        return map;
    }
}
