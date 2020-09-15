package cn.hl.kit.ax.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

/**
 * @author hyman
 * @date 2019-09-02 14:23:16
 * @version $ Id: HttpsConsumer.java, v 0.1  hyman Exp $
 */
public class HttpsConsumer {
    private static final Log    logger            = LogFactory.getLog(HttpsConsumer.class);
    private static final String ACCPET_TEXT       = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    private static final String ACCPET_JSON       = "application/json, text/plain, */*";
    private static final String CONTENT_TYPE_TEXT = "application/x-www-form-urlencoded; charset=UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private static final String CHARSET           = "UTF-8";

    static {
        logger.debug("Trust All Hosts......");
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            logger.info("Trust All Hosts DONE!");
        } catch (Exception e) {
            logger.error("Trust All Hosts ERROR!", e);
        }
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();
            char[] chars = new char[256];
            int count;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * 发起请求
     * @param uri 地址
     * @param params 参数
     * @param method GET | POST | DELETE
     * @param headDate 时间戳
     * @param headHash 哈希值
     * @return responseText
     * @throws IOException 打开远程地址过程中会发生IOException
     */
    public static String request(String uri, Object params, String method, String headDate, String headHash) throws Exception {
        try {
            method = method == null ? "POST" : method.toUpperCase();
            String contentType = CONTENT_TYPE_TEXT;
            String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
            if (params instanceof JSONObject) {
                contentType = CONTENT_TYPE_JSON;
            }
            logger.debug("发起请求:\r\n" + method + " : " + uri);

            URL url = new URL(uri);// 创建连接
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(method); // 设置请求方式
            connection.setRequestProperty("Accept", accept); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", contentType); // 设置发送数据的格式
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("Origin", "https://cloud.zenworks.net");
            connection.setRequestProperty("Referer", "https://cloud.zenworks.net");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/42.0.2311.154 Safari/537.36 LBBROWSER");
            if (StringUtils.isNotBlank(headDate)) {
                connection.setRequestProperty("X-Auth-Datetime", headDate);
            }
            if (StringUtils.isNotBlank(headHash)) {
                connection.setRequestProperty("Authorization", headHash);
            }

            String message = "\r\n➤ 入参数:\r\n";
            // 建立输出流，并写入数据
            if (params != null) {
                String requestBody = params.toString();
                if (params instanceof JSONObject) {
                    requestBody = ((JSONObject) params).toJSONString();
                } else if (params instanceof String) {
                    requestBody = (String) params;
                }
                message += requestBody;
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes());
                outputStream.close();
            }
            message += "\r\n➤ 出参数:\r\n";
            // 读取响应
            InputStream is = null;
            int code = connection.getResponseCode();
            if (code > 300) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }
            String encode = connection.getContentEncoding();
            if (encode != null && encode.contains("gzip")) {
                is = new GZIPInputStream(is);
            }
            String responseText = getStreamAsString(is, "UTF-8");
            message += responseText;
            logger.info(message);
            connection.disconnect();
            return responseText;
        } catch (IOException e) {
            logger.error(e);
            return "";
        }
    }
}
