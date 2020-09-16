package cn.hl.ox.gis;

import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 获取经纬度通过google
 */
public class LocationAnalyzerOfGoogle2 {
    private static final String SearchUrl = "http://maps.google.cn/maps/api/geocode/json?address=%s&language=%s";

    /**
     * @param address 查询的地址
     * @param isProxyIp 是否使用代理
     * @return
     */
    public static Map<String, Object> getCoordinate(String address, Boolean isProxyIp) {
        String addressKey = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            addressKey = java.net.URLEncoder.encode(address, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        InetSocketAddress socketAddr = new InetSocketAddress("118.97.103.82", 8080);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddr);
        String language = "zh-CN";
        String url = String.format(SearchUrl, addressKey, language);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        }
        try {
            if (isProxyIp) {
                httpsConn = (URLConnection) myURL.openConnection(proxy);// 使用代理
            } else {
                httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            }
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                JsonReader reader = new JsonReader(insr);
                reader.beginObject();
                while (reader.hasNext()) {
                    String tagName = reader.nextName();
                    if (tagName.equals("results")) {
                        reader.beginArray();
                        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                        while (reader.hasNext()) {
                            reader.beginObject();
                            Map<String, String> map_temp = new HashMap<String, String>();
                            while (reader.hasNext()) {
                                tagName = reader.nextName();
                                if (tagName.equals("address_components")) {
                                    reader.skipValue();
                                } else if (tagName.equals("formatted_address")) {
                                    map_temp.put("address", reader.nextString());
                                } else if (tagName.equals("geometry")) {
                                    reader.beginObject();
                                    while (reader.hasNext()) {
                                        tagName = reader.nextName();
                                        if (tagName.equals("location")) {
                                            reader.beginObject();
                                            while (reader.hasNext()) {
                                                map_temp.put(reader.nextName(), reader.nextString());
                                            }
                                            reader.endObject();
                                        } else {
                                            reader.skipValue();
                                        }
                                    }
                                    reader.endObject();
                                } else {
                                    reader.skipValue();
                                }
                            }
                            list.add(map_temp);
                            reader.endObject();
                        }
                        map.put("result", list);
                        reader.endArray();
                    } else if (tagName.equals("status")) {
                        map.put("status", reader.nextString());
                    }
                }
                insr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void main(String[] args) {
        boolean isProxyIp = false;
        String address = "Chuks Ikokwu Street Off Ogombo Road. Ajah. Lekki, Nigeria.";
        Map map = getCoordinate(address, isProxyIp);
        Iterator<Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, String> entry = entries.next();
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ": " + value);
        }

        //String[] latlng = getCoordinate(address);
        //System.out.println(latlng[0] + ", " + latlng[1]);
    }

    public static String[] getCoordinate(String addr) {
        String[] latLng = new String[2];
        String address = null;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        ;
        String output = "csv";
        //密钥可以随便写一个key=abc
        String key = "AIzaSyBIdSyB_td3PE-ur-ISjwFUtBf2O0Uo0Jo";
        String url = "http://maps.google.com/maps/geo?q=" + address + "&output=" + output + "&key=" + key;
        URL googleMapURL = null;
        URLConnection httpsConn = null;
        // 进行转码
        try {
            googleMapURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            httpsConn = (URLConnection) googleMapURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null) {
                    String[] retList = data.split(",");
                    if (retList.length > 2 && ("200".equals(retList[0]))) {
                        latLng[0] = retList[2];
                        latLng[1] = retList[3];
                    }
                }
                insr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}