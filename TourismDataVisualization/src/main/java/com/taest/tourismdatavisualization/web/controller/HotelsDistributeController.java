package com.taest.tourismdatavisualization.web.controller;

import com.taest.tourismdatavisualization.domain.ProvinceHotels;
import com.taest.tourismdatavisualization.domain.request.ProvinceHotelsVo;
import com.taest.tourismdatavisualization.service.ProvinceHotelsService;
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

@RestController
@RequestMapping("hotal")
public class HotelsDistributeController {

    @Autowired
    private ProvinceHotelsService provinceHotelsService;

    /**
     * 查询各省星级酒店分布
     *
     * @return
     */
    @GetMapping("/distribute")
    public ModelMap listDistribute() {
        ModelMap map = new ModelMap();

        // 所有年份以及星级酒店分布
        ArrayList<Object> completeList = new ArrayList<>();

        // 年份
        List<String> yearList = provinceHotelsService.getYearCountBy();

        // 存放每年的星级酒店分布
        Map<String, Double> yearMap = new HashMap<>();

        for (String year : yearList) {

            //存放每年以及该年星级酒店分布
            HashMap<Object, Object> hashMap = new HashMap<>();

            List<ProvinceHotels> provinceHotels = provinceHotelsService.findProvinceHotelsByTime(year);

            List<ProvinceHotels> provinceHotelsList = provinceHotels.stream()
                    .filter(item -> JsonUtils.getLalFromProvince(item.getE_regin_name()) != null && !JsonUtils.getLalFromProvince(item.getE_regin_name()).isEmpty())
                    .collect(Collectors.toList());

            // 存放要返回前台的详细星级酒店分布信息
            List<ProvinceHotelsVo> provinceHotelsVos = new ArrayList<ProvinceHotelsVo>();

            for (ProvinceHotels provinceHotel : provinceHotelsList) {

                ProvinceHotelsVo provinceHotelsVo = new ProvinceHotelsVo();

                String province = provinceHotel.getE_regin_name();


                String lng = JsonUtils.getLalFromProvince(province).get(0);
                String lat = JsonUtils.getLalFromProvince(province).get(1);

                provinceHotelsVo.setE_regin_id(provinceHotel.getE_regin_id());
                provinceHotelsVo.setE_regin_name(province);
                provinceHotelsVo.setIncome(provinceHotel.getIncome());
                provinceHotelsVo.setFixed_assets(provinceHotel.getFixed_assets());
                provinceHotelsVo.setHotel_num(provinceHotel.getHotel_num());
                provinceHotelsVo.setYear(provinceHotel.getYear());
                provinceHotelsVo.setLng(lng);
                provinceHotelsVo.setLat(lat);

                provinceHotelsVos.add(provinceHotelsVo);


            }
            hashMap.put("year", String.valueOf(Integer.parseInt(year)-1));
            hashMap.put("provinceHotelsVos", provinceHotelsVos);
            completeList.add(hashMap);
        }
//        map.put("year",year);
        map.put("yearMap", yearMap);
        map.put("yearCount", yearList.size());
        map.put("completeList", completeList);

        return map;
    }
}
