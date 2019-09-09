/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ox.gis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 获取经纬度通过google
 */
public class LocationAnalyzerOfGoogle {
    private static final String CLINET_ID = "103103762276883129613";
    private static final String MAP_AK    = "AIzaSyBiPz736G_l7yIps99GqI6IezYSQzHijNw";
    private static final String MAP_URL   = "https://maps.googleapis.com/maps/api/geocode/json?key=" + MAP_AK;

    public static boolean analyze(Location location, boolean toAddress) {
        URL url = null;
        String params = null;
        URLConnection conn = null;
        BufferedReader reader = null;
        try {
            if (toAddress) {
                //params = "&location=" + location.lat + "," + location.lng;
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
            if (response != null && "OK".equalsIgnoreCase(response.getString("status"))) {
                JSONArray results = response.getJSONArray("results");
                JSONObject result = results.getJSONObject(0);
                if (toAddress) {
                    //
                } else {
                    JSONObject geometry = result.getJSONObject("geometry");
                    location.lng = geometry.getJSONObject("location").getDoubleValue("lng");
                    location.lat = geometry.getJSONObject("location").getDoubleValue("lat");
                }
                return true;
            } else {
                System.out.println(response.getString("status") + " -- " + response.getString("error_message"));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        Location location = new Location(114.07065439904879, 22.519647444317396);
        //System.out.println(analyze(location, true));
        System.out.println(location);

        location = new Location("浙江省杭州市西湖区");
        System.out.println(analyze(location, false));
        System.out.println(location);
    }
}