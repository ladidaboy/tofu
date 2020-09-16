package cn.hl.ox.gis;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author hyman
 * @date 2019-08-08 11:55:31
 * @version $ Id: GetLatAndLngByBaidu.java, v 0.1  hyman Exp $
 */
public class LocationAnalyzerOfBaidu {
    private static final String MAP_AK  = "Puzsuhx0wmeNTQFoHgfDEf9U";
    private static final String MAP_URL = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=" + MAP_AK;

    /**
     * 解析位置信息
     * @param location 位置对象
     * @param toAddress true: 经纬度==>详细地址,  false: 详细地址==>经纬度
     * @return true: 解析成功, false: 解析失败
     */
    public static boolean analyze(Location location, boolean toAddress) {
        URL url = null;
        String params = null;
        URLConnection conn = null;
        BufferedReader reader = null;
        try {
            if (toAddress) {
                params = "&location=" + location.lat + "," + location.lng;
            } else {
                String address = location.address;
                if (address == null || address.trim().equals("")) {
                    return false;
                }
                try {
                    address = java.net.URLEncoder.encode(address, "UTF-8");
                } catch (UnsupportedEncodingException uee) {
                    uee.printStackTrace();
                }
                params = "&address=" + address;
            }
            url = new URL(MAP_URL + params);
            conn = url.openConnection();
            conn.setDoOutput(true);
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            StringBuilder text = new StringBuilder("");
            while ((line = reader.readLine()) != null) {
                text.append(line.trim());
            }
            JSONObject response = JSONObject.parseObject(text.toString());
            if (response != null && response.getIntValue("status") == 0) {
                JSONObject result = response.getJSONObject("result");

                location.formattedAddress = result.getString("formatted_address");
                location.lng = result.getJSONObject("location").getDoubleValue("lng");
                location.lat = result.getJSONObject("location").getDoubleValue("lat");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        Location location = new Location(121.430996, 31.2938918);
        System.out.println(analyze(location, true));
        System.out.println(location);

        location = new Location("美国纽约市");//lat: 40.6974034, lng: -74.1197634
        System.out.println(analyze(location, false));
        System.out.println(location);

        location = new Location("美国迈阿密");//lat: 25.7823907, lng: -80.2994983
        System.out.println(analyze(location, false));
        System.out.println(location);

        location = new Location("中国大庆市");//lat: 46.6606087, lng: 124.7414925
        System.out.println(analyze(location, false));
        System.out.println(location);

        location = new Location("中国成都市");//lat: 30.6584534, lng: 103.935463
        System.out.println(analyze(location, false));
        System.out.println(location);

        location = new Location("海口市");//lat: 20.0119319, lng: 110.248144
        System.out.println(analyze(location, false));
        System.out.println(location);
    }
}
