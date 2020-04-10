package com.taest.tourismdatavisualization.web.controller;

import com.taest.tourismdatavisualization.domain.NationForeignTourists;
import com.taest.tourismdatavisualization.domain.request.NationForeignTouristsVo;
import com.taest.tourismdatavisualization.service.NationForeignTouristsService;
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
 * 各国来华游客统计
 */
@RestController
@RequestMapping("foreign")
public class ForeignTouristsController {

    @Autowired
    private NationForeignTouristsService nationForeignTouristsService;

    /**
     * 查询客源国人数相关信息
     *
     * @return
     */
    @GetMapping("/nation")
    public ModelMap listNation() {
        ModelMap map = new ModelMap();

        // 所有年份以及客源国人数
        ArrayList<Object> completeList = new ArrayList<>();

        // 年份
        List<String> yearList = nationForeignTouristsService.getYearCountBy();

        // 存放每年的总客源国人数相关信息
        Map<String, Double> yearMap = new HashMap<>();

        for (String year : yearList) {

            if ( Integer.parseInt(year) >=2004){
                //存放每年以及该年客源国人数相关信息
                HashMap<Object, Object> hashMap = new HashMap<>();

                List<NationForeignTourists> nationForeignTourists = nationForeignTouristsService.findNationForeignTouristsByTime(year);

                List<NationForeignTourists> foreignTourists = nationForeignTourists.stream()
                        .filter(item -> JsonUtils.getLalFromCountry(getChineseFromCE(item.getNationality_name())) != null)
                        .collect(Collectors.toList());

                // 存放要返回前台的详细客源国人数相关信息
                List<NationForeignTouristsVo> nationForeignTouristsVos = new ArrayList<NationForeignTouristsVo>();

                for (NationForeignTourists nationForeignTourist : foreignTourists) {

                    NationForeignTouristsVo nationForeignTouristsVo = new NationForeignTouristsVo();

                    String country = getChineseFromCE(nationForeignTourist.getNationality_name());

                    String lng = JsonUtils.getLalFromCountry(country).get(0);
                    String lat = JsonUtils.getLalFromCountry(country).get(1);

                    if (Integer.parseInt(year)>2008){
                        nationForeignTouristsVo.setTotal(nationForeignTourist.getTotal());
                    }
                    else {
                        double total = Double.valueOf(nationForeignTourist.getTotal()) / 10000;
                        nationForeignTouristsVo.setTotal(String.valueOf(total));
                    }

                    nationForeignTouristsVo.setNationality_id(nationForeignTourist.getNationality_id());
                    nationForeignTouristsVo.setNationality_name(country);
                    nationForeignTouristsVo.setAeroplane(nationForeignTourist.getAeroplane());
                    nationForeignTouristsVo.setAutomobile(nationForeignTourist.getAutomobile());
                    nationForeignTouristsVo.setFoot(nationForeignTourist.getFoot());
                    nationForeignTouristsVo.setShip(nationForeignTourist.getShip());
                    nationForeignTouristsVo.setTrain(nationForeignTourist.getTrain());
                    nationForeignTouristsVo.setYear(nationForeignTourist.getYear());
                    nationForeignTouristsVo.setLng(lng);
                    nationForeignTouristsVo.setLat(lat);


                    nationForeignTouristsVos.add(nationForeignTouristsVo);

                }
                hashMap.put("year", String.valueOf(Integer.parseInt(year)-1));
                hashMap.put("nationForeignTouristsVos", nationForeignTouristsVos);
                completeList.add(hashMap);
            }

        }
        map.put("yearMap", yearMap);
        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);

        return map;
    }

    /**
     *  查询入境方式
     */
    @GetMapping("/transport")
    public ModelMap listTransport(){
        ModelMap map = new ModelMap();

        // 年份
        List<String> yearList = nationForeignTouristsService.getYearCountBy();

        // 存放入境方式为ship
        List<String> ships = new ArrayList<>();

        // 存放入境方式为aeroplane
        List<String> aeroplanes = new ArrayList<>();

        // 存放入境方式为train
        List<String> trains = new ArrayList<>();

        // 存放入境方式为automobile
        List<String> automobiles = new ArrayList<>();

        // 存放入境方式为foot
        List<String> foots = new ArrayList<>();

        // 存放入境方式为total
        List<String> totals = new ArrayList<>();

        for (String year : yearList) {

            NationForeignTourists totalRecord = nationForeignTouristsService.findNationForeignTouristsTotal(year);

            if (Integer.parseInt(year)<=2008){
                ships.add(String.valueOf(Double.valueOf(totalRecord.getShip())/10000));
                aeroplanes.add(String.valueOf(Double.valueOf(totalRecord.getAeroplane())/10000));
                trains.add(String.valueOf(Double.valueOf(totalRecord.getTrain())/10000));
                automobiles.add(String.valueOf(Double.valueOf(totalRecord.getAutomobile())/10000));
                foots.add(String.valueOf(Double.valueOf(totalRecord.getFoot())/10000));
                totals.add(String.valueOf(Double.valueOf(totalRecord.getTotal())/10000));
            }
            else {
                ships.add(totalRecord.getShip());
                aeroplanes.add(totalRecord.getAeroplane());
                trains.add(totalRecord.getTrain());
                automobiles.add(totalRecord.getAutomobile());
                foots.add(totalRecord.getFoot());
                totals.add(totalRecord.getTotal());
            }


        }

        // 存放各入境类型类型
        List<String> TotalType = new ArrayList<String>() {{
            add("ship");
            add("aeroplane");
            add("train");
            add("automobile");
            add("foot");
            add("total");
        }};
        map.put("yearList", yearList.stream().map(item->Integer.parseInt(item)-1));
        map.put("TotalType", TotalType);
        map.put("ships", ships);
        map.put("aeroplanes", aeroplanes);
        map.put("trains", trains);
        map.put("automobiles", automobiles);
        map.put("foots", foots);
        map.put("totals", totals);

        return map;
    }
}
