package com.taest.tourismdatavisualization.utils;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 获取经纬度
 *
 */
public class GetLatAndLngByBaidu {

    /**
     * 根据地址查询经纬度  通过网络请求 速度慢
     * @param addr
     * 查询的地址
     * @return
     * @throws IOException
     */
    public static Object[] getCoordinate(String addr) throws IOException {
        String lng = null;//经度
        String lat = null;//纬度
        String address = null;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        }catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String key = "DN0RiNQKgASsSe6GeEWIT1nY78E4dyxE";
        String geourl = String .format("https://restapi.amap.com/v3/place/text?s=rsv3&key=8325164e247e15eea68b59e89200988b&keywords=%s&tdsourcetag=s_pctim_aiomsg",address);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(geourl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                while((data= br.readLine())!=null){
//                    System.out.println(data);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
                    JsonArray pois = jsonObject.getAsJsonArray("pois");
                    JsonObject object = pois.get(0).getAsJsonObject();
                    lng = object.get("location").toString().replaceAll("\"", "").split(",")[0];
                    lat = object.get("location").toString().replaceAll("\"", "").split(",")[1];
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        return new Object[]{lng,lat};
    }

    public static void main(String[] args) throws IOException {
        Object[] o =  GetLatAndLngByBaidu.getCoordinate("西安");
        System.out.println(o[0]);//经度
        System.out.println(o[1]);//纬度
    }

}