package com.taest.tourismdatavisualization.web.controller;

import com.taest.tourismdatavisualization.domain.*;
import com.taest.tourismdatavisualization.domain.request.HAHotCitiesVo;
import com.taest.tourismdatavisualization.domain.request.HAHotResidenceVo;
import com.taest.tourismdatavisualization.service.*;
import com.taest.tourismdatavisualization.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("henan")
public class HAProvinceController {

    @Autowired
    private HAHotResidenceService haHotResidenceService;

    @Autowired
    private HACityStreamService haCityStreamService;

    @Autowired
    private HACityIncomeService haCityIncomeService;

    @Autowired
    private HAHotCitiesService haHotCitiesService;

    @Autowired
    private HAHotScenicInCitiesService haHotScenicInCitiesService;

    @Autowired
    private HAHotScenicService haHotScenicService;

    /**
     * 获取所有年份热门城市
     * @return
     */
    @GetMapping("/hotCities")
    public ModelMap listhotCities(){
        ModelMap map = new ModelMap();

        // 所有年份以及热门城市信息
        ArrayList<Object> completeList = new ArrayList<>();

        // 年份
        List<String> yearList = haHotCitiesService.findYearCountBy();

        for (String year : yearList) {

            //存放每年以及该年热门城市信息
            HashMap<Object, Object> hashMap = new HashMap<>();

            List<HAHotCities> haHotCities = haHotCitiesService.findHAHotCitiesByTime(year);

            // 存放要返回前台的详细城市信息
            List<HAHotCitiesVo> haHotCitiesVos = new ArrayList<HAHotCitiesVo>();

            for (HAHotCities haHotCity : haHotCities) {

                HAHotCitiesVo haHotCitiesVo = new HAHotCitiesVo();

                String city = haHotCity.getCity();

                String lng = JsonUtils.getLalFromCity(city).get(0);
                String lat = JsonUtils.getLalFromCity(city).get(1);

                haHotCitiesVo.setCity(city);
                haHotCitiesVo.setCount(haHotCity.getCount());
                haHotCitiesVo.setLat(lat);
                haHotCitiesVo.setLng(lng);
                haHotCitiesVo.setTime(haHotCity.getTime());

                haHotCitiesVos.add(haHotCitiesVo);
            }
            hashMap.put("year", year);
            hashMap.put("haHotCitiesVos", haHotCitiesVos);
            completeList.add(hashMap);
        }
        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);

        return map;
    }

    /**
     * 获取所有年份热门常驻地
     *
     * @return
     */
    @GetMapping("/residence")
    public ModelMap listHotResidence() {

        ModelMap map = new ModelMap();

        // 所有年份以及常驻地信息
        ArrayList<Object> completeList = new ArrayList<>();

        // 年份
        List<String> yearList = haHotResidenceService.findYearCountBy();

        for (String year : yearList) {

            //存放每年以及该年常驻地信息
            HashMap<Object, Object> hashMap = new HashMap<>();

            List<HAHotResidence> hotResidences = haHotResidenceService.findHotResidenceByTime(year);

            // 存放要返回前台的详细常驻地信息
            List<HAHotResidenceVo> haHotResidenceVos = new ArrayList<HAHotResidenceVo>();

            for (HAHotResidence hotResidence : hotResidences) {

                HAHotResidenceVo haHotResidenceVo = new HAHotResidenceVo();

                String residence = hotResidence.getResidence();

                String lng = JsonUtils.getLalFromProvince(residence).get(0);
                String lat = JsonUtils.getLalFromProvince(residence).get(1);

                haHotResidenceVo.setResidence(hotResidence.getResidence());
                haHotResidenceVo.setCount(hotResidence.getCount());
                haHotResidenceVo.setTime(hotResidence.getTime());
                haHotResidenceVo.setLng(lng);
                haHotResidenceVo.setLat(lat);

                haHotResidenceVos.add(haHotResidenceVo);
            }
            hashMap.put("year", year);
            hashMap.put("haHotResidenceVos", haHotResidenceVos);
            completeList.add(hashMap);
        }
//        map.put("year",year);
        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);
        return map;

    }

    /**
     * 根据年份获取热门常驻地信息
     *
     * @param time
     * @return
     */
    @GetMapping("/residence/{time}")
    public ModelMap listHotResidenceByTime(@PathVariable("time") String time) {

        ModelMap map = new ModelMap();

        // 存放要返回前台的详细常驻地信息
        List<HAHotResidenceVo> haHotResidenceVos = new ArrayList<HAHotResidenceVo>();

        List<HAHotResidence> hotResidences = haHotResidenceService.findHotResidenceByTime(time);

        for (HAHotResidence hotResidence : hotResidences) {

            HAHotResidenceVo haHotResidenceVo = new HAHotResidenceVo();

            String residence = hotResidence.getResidence();

            String lng = JsonUtils.getLalFromProvince(residence).get(0);
            String lat = JsonUtils.getLalFromProvince(residence).get(1);

            haHotResidenceVo.setResidence(hotResidence.getResidence());
            haHotResidenceVo.setCount(hotResidence.getCount());
            haHotResidenceVo.setTime(hotResidence.getTime());
            haHotResidenceVo.setLng(lng);
            haHotResidenceVo.setLat(lat);
            haHotResidenceVos.add(haHotResidenceVo);
        }

        map.put("haHotResidenceVos", haHotResidenceVos);

        return map;
    }

    /**
     * 获取所有河南省各市的流量
     * @return
     */
    @GetMapping("/stream")
    public ModelMap listCityStream() {

        ModelMap map = new ModelMap();

        // 所有年份以及各市人流量
        ArrayList<Object> completeList = new ArrayList<>();

        List<String> yearList = haCityStreamService.findYearCountBy();

        for (String year : yearList) {
            //存放每年以及各市人流量信息
            HashMap<Object, Object> hashMap = new HashMap<>();

            List<HACityStream> haCityStreams = haCityStreamService.findHACityStreamByTime(year)
                    .stream()
                    .filter(item -> JsonUtils.isContains(item.getCity())).collect(Collectors.toList());

            hashMap.put("year", year);
            hashMap.put("haCityStreams", haCityStreams);

            completeList.add(hashMap);
        }

        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);

        return map;

    }

    /**
     * 根据年份获取所有河南省各市的流量
     * @param time
     * @return
     */
    @GetMapping("/stream/{time}")
    public ModelMap listCityStreamByTime(@PathVariable("time") String time) {

        ModelMap map = new ModelMap();

        List<HACityStream> haCityStreams = haCityStreamService.findHACityStreamByTime(time)
                .stream()
                .filter(item -> JsonUtils.isContains(item.getCity())).collect(Collectors.toList());

        map.put("haCityStreams", haCityStreams);

        return map;

    }

    /**
     * 获取所有河南省各市的收入
     * @return
     */
    @GetMapping("/income")
    public ModelMap listCityIncome() {

        ModelMap map = new ModelMap();

        // 所有年份以及各市收入
        ArrayList<Object> completeList = new ArrayList<>();

        List<String> yearList = haCityIncomeService.findYearCountBy();

        for (String year : yearList) {
            //存放每年以及各市收入
            HashMap<Object, Object> hashMap = new HashMap<>();

            List<HACityIncome> haCityIncomes = haCityIncomeService.findHACityIncomeByTime(year)
                    .stream()
                    .filter(item -> JsonUtils.isContains(item.getCity())).collect(Collectors.toList());

            hashMap.put("year", year);
            hashMap.put("haCityIncomes", haCityIncomes);

            completeList.add(hashMap);
        }

        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);

        return map;

    }

    /**
     * 根据年份获取所有河南省各市的收入
     * @param time
     * @return
     */
    @GetMapping("/income/{time}")
    public ModelMap listCityIncomeByTime(@PathVariable("time") String time) {

        ModelMap map = new ModelMap();

        List<HACityIncome> haCityIncomes = haCityIncomeService.findHACityIncomeByTime(time)
                .stream()
                .filter(item -> JsonUtils.isContains(item.getCity())).collect(Collectors.toList());

        map.put("haCityIncomes", haCityIncomes);

        return map;

    }

    /**
     * 根据城市查询该城市热门景区
     * @param city
     * @return
     */
    @GetMapping("/hotscenic/{city}")
    public ModelMap listHotscenicByCity(@PathVariable("city") String city){

        ModelMap map = new ModelMap();

        // 所有年份以及各市热门景区
        ArrayList<Object> completeList = new ArrayList<>();

        List<String> yearList = haHotScenicInCitiesService.findYearCountBy();

        for (String year : yearList) {
            // 存放每年以及每年的热门景区
            HashMap<Object, Object> hashMap = new HashMap<>();

            List<HAHotScenicInCities> hotScenicInCities = haHotScenicInCitiesService.findHAHotScenicInCitiesByTimeAndCity(year, city);

            hashMap.put("year", year);
            hashMap.put("hotScenicInCities", hotScenicInCities);

            completeList.add(hashMap);
        }

        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);

        return map;
    }

    /**
     * 查询全国的热门景区
     * @return
     */
    @GetMapping("/hotscenic")
    public ModelMap listHotScenic(){

        ModelMap map = new ModelMap();

        // 所有年份以及热门景区
        ArrayList<Object> completeList = new ArrayList<>();

        List<String> yearList = haHotScenicService.YearCountBy();

        for (String year : yearList) {
            //存放每年以及各市收入
            HashMap<Object, Object> hashMap = new HashMap<>();

            List<HAHotScenic> haHotScenics = haHotScenicService.findHAHotScenicByTime(year);

            hashMap.put("year", year);
            hashMap.put("haHotScenics", haHotScenics);

            completeList.add(hashMap);
        }

        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);

        return map;

    }
}
