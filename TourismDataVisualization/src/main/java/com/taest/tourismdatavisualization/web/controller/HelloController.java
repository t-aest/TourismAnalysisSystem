package com.taest.tourismdatavisualization.web.controller;

import com.taest.tourismdatavisualization.domain.Comment;
import com.taest.tourismdatavisualization.domain.Entrepreneur;
import com.taest.tourismdatavisualization.domain.Hotel;
import com.taest.tourismdatavisualization.domain.ProvinceHotels;
import com.taest.tourismdatavisualization.domain.base.CityBaseEntity;
import com.taest.tourismdatavisualization.domain.base.ProvinceBaseEntity;
import com.taest.tourismdatavisualization.mapper.CommentAnalysisMapper;
import com.taest.tourismdatavisualization.service.*;
import com.taest.tourismdatavisualization.utils.JsonUtils;
import com.taest.tourismdatavisualization.utils.SystemUtils;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    private ProvinceAgencyEconomicService provinceAgencyEconomicService;

    @Autowired
    private ProvinceHotelsEconomicService provinceHotelsEconomicService;

    @Autowired
    private ProvinceOtherEnterpriseEconomicService provinceOtherEnterpriseEconomicService;

    @Autowired
    private CityAgencyEconomicService cityAgencyEconomicService;

    @Autowired
    private CityHotelsEconomicService cityHotelsEconomicService;

    @Autowired
    private CityOtherEnterpriseEconomicService cityOtherEnterpriseEconomicService;

    @Autowired
    private ProvinceHotelsService provinceHotelsService;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private CommentAnalysisMapper commentAnalysisMapper;


    @RequestMapping("/main")
    public String main(Map<String, Object> map) {

        List<Entrepreneur> provinceList = new ArrayList<>();
        List<Entrepreneur> cityList = new ArrayList<>();
        List<Hotel> hotelList = new ArrayList<>();

        List<String> yearList = new ArrayList<String>() {{
            add("2017");
            add("2016");
            add("2014");
            add("2012");
            add("2009");

        }};
        for (String year : yearList) {
            /**
             * 各省企业家
             */
            ProvinceBaseEntity provinceAgencyEconomicTotal = provinceAgencyEconomicService.findProvinceAgencyEconomicTotal(year);
            ProvinceBaseEntity hotelsEconomicTotal = provinceHotelsEconomicService.findProvinceHotelsEconomicTotal(year);
            ProvinceBaseEntity provinceOtherEnterpriseEconomicTotal = provinceOtherEnterpriseEconomicService.findProvinceOtherEnterpriseEconomicTotal(year);

            Entrepreneur province = new Entrepreneur();

            double p_fixed_assets = (Double.valueOf(provinceAgencyEconomicTotal.getE_fixed_assets()) + Double.valueOf(hotelsEconomicTotal.getE_fixed_assets()) + Double.valueOf(provinceOtherEnterpriseEconomicTotal.getE_fixed_assets())) / 10000;
            double p_income = (Double.valueOf(provinceAgencyEconomicTotal.getE_taking()) + Double.valueOf(hotelsEconomicTotal.getE_taking()) + Double.valueOf(provinceOtherEnterpriseEconomicTotal.getE_taking())) / 10000;
            int p_count = Integer.parseInt(provinceAgencyEconomicTotal.getE_entrepreneur()) + Integer.parseInt(hotelsEconomicTotal.getE_entrepreneur()) + Integer.parseInt(provinceOtherEnterpriseEconomicTotal.getE_entrepreneur());

            province.setFixed_assets(Double.valueOf(String.format("%.2f", p_fixed_assets)));
            province.setIncome(Double.valueOf(String.format("%.2f", p_income)));
            province.setCount(p_count);
            province.setYear(Integer.parseInt(year) - 1);

            provinceList.add(province);

            /**
             * 各市企业家
             */
            CityBaseEntity cityAgencyEconomicTotal = cityAgencyEconomicService.findCityAgencyEconomicTotal(year);
            CityBaseEntity cityHotelsEconomicTotal = cityHotelsEconomicService.findCityHotelsEconomicTotal(year);
            CityBaseEntity cityOtherEnterpriseEconomicTotal = cityOtherEnterpriseEconomicService.findCityOtherEnterpriseEconomicTotal(year);

            Entrepreneur city = new Entrepreneur();

            double c_fixed_assets = (Double.valueOf(cityAgencyEconomicTotal.getE_fixed_assets()) + Double.valueOf(cityHotelsEconomicTotal.getE_fixed_assets()) + Double.valueOf(cityOtherEnterpriseEconomicTotal.getE_fixed_assets())) / 10000;
            double c_income = (Double.valueOf(cityAgencyEconomicTotal.getE_taking()) + Double.valueOf(cityHotelsEconomicTotal.getE_taking()) + Double.valueOf(cityOtherEnterpriseEconomicTotal.getE_taking())) / 10000;
            int c_count = Integer.parseInt(cityAgencyEconomicTotal.getE_entrepreneur()) + Integer.parseInt(cityHotelsEconomicTotal.getE_entrepreneur()) + Integer.parseInt(cityOtherEnterpriseEconomicTotal.getE_entrepreneur());

            city.setFixed_assets(Double.valueOf(String.format("%.2f", c_fixed_assets)));
            city.setIncome(Double.valueOf(String.format("%.2f", c_income)));
            city.setCount(c_count);
            city.setYear(Integer.parseInt(year) - 1);

            cityList.add(city);

            /**
             * 各省星级酒店分布
             */
            ProvinceHotels provinceHotelsTotal = provinceHotelsService.findProvinceHotelsTotal(year);

            Hotel hotel = new Hotel();
            hotel.setFixed_assets(Double.valueOf(String.format("%.2f", Double.valueOf(provinceHotelsTotal.getFixed_assets()) / 10000)));
            hotel.setIncome(Double.valueOf(String.format("%.2f", Double.valueOf(provinceHotelsTotal.getIncome()) / 10000)));
            hotel.setCount(Integer.parseInt(provinceHotelsTotal.getHotel_num()));
            hotel.setYear(Integer.parseInt(year) - 1);

            hotelList.add(hotel);

        }
        map.put("provinceList", provinceList);
        map.put("cityList", cityList);
        map.put("hotelList", hotelList);

        return "index";
    }

    @RequestMapping("/page/index")
    public String page(Map<String, Object> map) {

        List<Comment> comments = commentAnalysisMapper.getComment();
        map.put("comments", comments);

        return "page/index";
    }

    @RequestMapping("/page/scenic")
    public String scenic() {
        return "page/scenic";
    }


    /**
     * 获取世界地图需要json
     *
     * @return
     * @throws FileNotFoundException
     */
    @RequestMapping("/getworldJson")
    @ResponseBody
    public String getWorldJson() throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/world.json");
        String file = JsonUtils.readFile(inputStream);

        return file;
    }


    /**
     * 获取中国地图需要json
     *
     * @return
     * @throws FileNotFoundException
     */
    @RequestMapping("/getchinaJson")
    @ResponseBody
    public String getChinaJson() throws IOException {

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/china.json");
        String file = JsonUtils.readFile(inputStream);

        return file;

    }

    /**
     * 获取河南地图需要json
     *
     * @return
     * @throws FileNotFoundException
     */
    @RequestMapping("/gethenanJson")
    @ResponseBody
    public String getHenanJson() throws IOException {
//        readFile(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/LalJson"));
//        File path = new File(ResourceUtils.getURL("classpath:json/henan.json").getPath());
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/henan.json");
        String file = JsonUtils.readFile(inputStream);

        return file;
    }

    @RequestMapping("/getonlines")
    @ResponseBody
    public ModelMap getOnlines(HttpServletRequest httpServletRequest) {

        ModelMap map = new ModelMap();

        HttpSession session = httpServletRequest.getSession();
        String ip = (String) session.getAttribute("ip");

        Map<String, List<HttpSession>> userMap = (Map<String, List<HttpSession>>) session.getServletContext().getAttribute("userMap");

        List<HttpSession> httpSessions = userMap.get(ip);

        map.put("ip", ip);
        map.put("httpSessions", httpSessions.size());

        return map;
    }

    @RequestMapping("/getsystem")
    @ResponseBody
    public ModelMap getSystem(HttpServletRequest httpServletRequest) {

        ModelMap map = new ModelMap();

        int memory = SystemUtils.memoryLoad();

        if (memory > 70) {
            //封装邮件内容
            SimpleMailMessage message = new SimpleMailMessage();

            //邮件主题
            message.setSubject("系统警告");
            message.setText("系统内存占用过大，请及时维护！");

            message.setFrom("876239615@qq.com");
            message.setTo("1666564497@qq.com");

            javaMailSender.send(message);

        }

        map.put("memory", memory);

        return map;
    }


}
