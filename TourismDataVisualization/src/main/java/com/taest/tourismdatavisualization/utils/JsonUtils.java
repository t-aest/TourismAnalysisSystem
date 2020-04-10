package com.taest.tourismdatavisualization.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtils {

    static String path = null;
    static String worldpath = null;
    static String lalJson = null;
    static String worldJson = null;

    static {
        try {
//            path = new File(ResourceUtils.getURL("classpath:json/LalJson").getPath()).getAbsolutePath();
//            path = new ClassPathResource("json/LalJson").getFile().getPath();
//            worldpath = new ClassPathResource("json/worldCoordData").getFile().getPath();
//            worldpath = new File(ResourceUtils.getURL("classpath:json/worldCoordData").getPath()).getAbsolutePath();
            lalJson = readFile(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/LalJson"));
            worldJson = readFile(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/worldCoordData"));
//            worldJson = readJsonFile(worldpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static final ArrayList<String> cities = new ArrayList<String>() {
        {
            add("开封");
            add("洛阳");
            add("郑州");
            add("平顶山");
            add("安阳");
            add("鹤壁");
            add("新乡");
            add("焦作");
            add("濮阳");
            add("许昌");
            add("漯河");
            add("三门峡");
            add("商丘");
            add("周口");
            add("南阳");
            add("信阳");
            add("济源");
            add("驻马店");

        }

        ;
    };


    /**
     * 读取json文件，返回json串
     *
     * @param fileName
     * @return
     */
    public static String readJsonFile(String fileName) throws FileNotFoundException {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 输入国家名 返回其经纬度
     *
     * @param country 国家
     */
    public static List<String> getLalFromCountry(String country) {

        List<String> lals = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(worldJson).getAsJsonArray();
        Object element = null;
        for (JsonElement jsonElement : jsonArray) {
            element = jsonElement.getAsJsonObject();
            if (((JsonObject) element).get("name").toString().contains(country)) {
                String value = ((JsonObject) element).get("value").toString().replaceAll("\\[", "").replaceAll("]", "");
                lals = Arrays.asList(value.split(","));
                break;
            }
            else {
                lals = null;
            }
        }
        return lals;
    }


    /**
     * 输入省份 返回其经纬度
     *
     * @param province 省份
     */
    public static List<String> getLalFromProvince(String province) {
        List<String> lals = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(lalJson).getAsJsonArray();
        Object element = null;
        for (JsonElement jsonElement : jsonArray) {
            element = jsonElement.getAsJsonObject();
            if (((JsonObject) element).get("name").toString().contains(province)) {
                lals.add(((JsonObject) element).get("log").toString().replaceAll("\"", ""));
                lals.add(((JsonObject) element).get("lat").toString().replaceAll("\"", ""));
            }
        }
        return lals;
    }

    /**
     * 输入城市 返回其经纬度
     *
     * @param city
     * @return
     */
    public static List<String> getLalFromCity(String city) {

        List<String> lals = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(lalJson).getAsJsonArray();


        Object element = null;

        for (JsonElement jsonElement : jsonArray) {
            element = jsonElement.getAsJsonObject();

            JsonArray children = ((JsonObject) element).get("children").getAsJsonArray();

            for (JsonElement child : children) {
                if (city.contains(child.getAsJsonObject().get("name").toString().replaceAll("\"", ""))) {
                    lals.add(child.getAsJsonObject().get("log").toString().replaceAll("\"", ""));
                    lals.add(child.getAsJsonObject().get("lat").toString().replaceAll("\"", ""));
                }

            }
        }
        return lals;

    }

    /**
     * 判断是否为河南省城市
     *
     * @param city
     * @return
     */
    public static boolean isContains(String city) {

        boolean flag = false;
        if (city == null) {
            return false;
        }
        System.out.println(city);
        for (String real_city : cities) {
            if (city.contains(real_city)) {
                System.out.println(real_city);
                flag = true;
                break;
            } else {
                flag = false;
            }
        }

        return flag;
    }


    /*
     * 读取文件内容
     */
    public static String readFile ( InputStream inputStream ) throws IOException {

        StringBuilder builder = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(inputStream , "UTF-8" );
            BufferedReader bfReader = new BufferedReader( reader );
            String tmpContent = null;
            while ( ( tmpContent = bfReader.readLine() ) != null ) {
                builder.append( tmpContent );
            }
            bfReader.close();
        } catch ( UnsupportedEncodingException e ) {
            // 忽略
        }
        return filter( builder.toString());
    }
    // 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
    public static String filter ( String input ) {
        return input.replaceAll( "/\\*[\\s\\S]*?\\*/", "" );
    }


    public static void main(String[] args) throws IOException {
//        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/LalJson");
//        String file = readFile(inputStream);
        System.out.println(getLalFromProvince("台湾"));




    }
}
