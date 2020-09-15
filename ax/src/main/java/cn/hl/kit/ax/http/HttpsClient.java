package cn.hl.kit.ax.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author hyman
 * @date 2019-09-02 16:05:36
 * @version $ Id: MyClient.java, v 0.1  hyman Exp $
 */
public class HttpsClient {
    private static String               TAG       = "ⓘHttpsClient";
    private static String               CHARSET   = "UTF-8";
    private static int                  TIMEOUT   = 1000 * 30;
    private static ThreadLocal<Integer> LOG_LEVEL = ThreadLocal.withInitial(() -> 1);

    /**
     * HTTP请求枚举取值范围为: GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS,TRACE, 常用的是GET和POST请求.
     */
    public enum Method {
        /**获取*/ GET,
        /**推送*/ POST,
        /**删除*/ DELETE
    }

    public static void detailLog() {
        LOG_LEVEL.set(2);
    }

    public static void simpleLog() {
        LOG_LEVEL.set(1);
    }

    public static void hideLog() {
        LOG_LEVEL.set(0);
    }

    private static DefaultHttpClient getHttpsClient() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };
        DefaultHttpClient client = new DefaultHttpClient();
        ctx.init(null, new TrustManager[] {tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx);

        ClientConnectionManager ccm = client.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        // 设置要使用的端口，默认是443
        sr.register(new Scheme("https", 443, ssf));
        return client;
    }

    private static List<NameValuePair> map2NameValuePairList(Object params) {
        List<NameValuePair> list = new ArrayList<>();
        if (params != null && params instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) params;
            if (!map.isEmpty()) {
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    if (map.get(key) != null) {
                        String value = String.valueOf(map.get(key));
                        list.add(new BasicNameValuePair(key, value));
                    }
                }
            }
        }
        return list;
    }

    private static String execute(HttpRequestBase request, String message)
            throws KeyManagementException, NoSuchAlgorithmException, IOException {
        CloseableHttpClient client = getHttpsClient();
        HttpParams clientParams = client.getParams();
        clientParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
        clientParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
        if (StringUtils.isBlank(message) || LOG_LEVEL.get() == 1) {
            URI uri = request.getURI();
            message = TAG + " " + request.getMethod() + " → ";
            message += uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
            message += (StringUtils.isBlank(uri.getQuery()) ? "" : ("?" + uri.getQuery())) + "\n";
        }
        message = "================================================================\n" + message;
        String body = null;
        try {
            CloseableHttpResponse response = client.execute(request);
            try {
                // 处理请求结果
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Header ce = entity.getContentEncoding();
                    if (ce != null) {
                        // 检查压缩模式
                        String cev = ce.getValue();
                        if (StringUtils.containsIgnoreCase(cev, "gzip")) {
                            entity = new GzipDecompressingEntity(entity);
                        } else if (StringUtils.containsIgnoreCase(cev, "deflate")) {
                            entity = new DeflateDecompressingEntity(entity);
                        }
                    }
                    body = EntityUtils.toString(entity, CHARSET);
                    String json = body;
                    if (LOG_LEVEL.get() == 2) {
                        try {
                            json = JSONObject.toJSONString(JSONObject.parseObject(body), true);
                        } catch (Exception e) {
                            try {
                                json = JSONObject.toJSONString(JSONObject.parseArray(body), true);
                            } catch (Exception ee) {
                                json = body;
                            }
                        }
                        message += "➥ Result: " + json;
                    } else {
                        message += json;
                    }
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            client.close();
            if (LOG_LEVEL.get() > 0) {
                System.out.println(message);
            }
        }
        return body;
    }

    public static String execute(String url, Method method, Object params, Map<String, String> header, boolean postJson)
            throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, IOException {
        // 判断请求地址和请求类型是否合法
        if (StringUtils.isBlank(url) || method == null || !StringUtils.startsWithIgnoreCase(url, "http")) {
            return null;
        }
        // 初始化请求对象
        url = url.trim();
        String msg = TAG;
        HttpRequestBase request = null;
        switch (method) {
            case GET:
                msg += " GET ";
                request = new HttpGet(url);
                break;
            case POST:
                msg += " POST" + (postJson ? "{JSON} " : " ");
                request = new HttpPost(url);
                break;
            case DELETE:
                msg += " DELETE ";
                request = new HttpDelete(url);
                break;
            default:
                return null;
        }
        msg += "→ " + url + "\n";
        // 设置超时
        RequestConfig requestConfig = RequestConfig.custom() //
                .setConnectionRequestTimeout(TIMEOUT) //
                .setConnectTimeout(TIMEOUT) //
                .setSocketTimeout(TIMEOUT) //
                .build();
        request.setConfig(requestConfig);
        // 设置Header
        if (header == null) {
            header = new HashMap<>();
        }
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=" + CHARSET);
        if (method == Method.POST && postJson) {
            header.put("Content-Type", "application/json; charset=" + CHARSET);
        }
        for (Iterator<Entry<String, String>> it = header.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> entry = it.next();
            request.setHeader(new BasicHeader(entry.getKey(), entry.getValue()));
        }
        msg += "➥ Header: " + JSONObject.toJSONString(header, true) + "\n";
        // 设置参数
        if (params != null) {
            switch (method) {
                case POST:
                    StringEntity entity;
                    if (postJson) {
                        /*
                        fastjson.JSONObject.toJSONString(Object object, SerializerFeature... features)
                        SerializerFeature有用的一些枚举值
                        QuoteFieldNames         - 输出key时是否使用双引号,默认为true
                        WriteMapNullValue       - 是否输出值为null的字段,默认为false
                        WriteNullNumberAsZero   - 数值字段如果为null,输出为0,而非null
                        WriteNullListAsEmpty    - List字段如果为null,输出为[],而非null
                        WriteNullStringAsEmpty  - 字符类型字段如果为null,输出为”“,而非null
                        WriteNullBooleanAsFalse - Boolean字段如果为null,输出为false,而非null
                         */
                        entity = new StringEntity(JSONObject.toJSONString(params), ContentType.DEFAULT_TEXT.withCharset(CHARSET));
                    } else {
                        entity = new UrlEncodedFormEntity(map2NameValuePairList(params), CHARSET);
                    }
                    ((HttpPost) request).setEntity(entity);
                    break;
                default:
                    String str = EntityUtils.toString(new UrlEncodedFormEntity(map2NameValuePairList(params), CHARSET));
                    String uri = request.getURI().toString();
                    if (uri.indexOf("?") >= 0) {
                        request.setURI(new URI(uri + "&" + str));
                    } else {
                        request.setURI(new URI(uri + "?" + str));
                    }
            }
            msg += "➥ Params: " + JSONObject.toJSONString(params, true) + "\n";
        }
        // 发起请求
        return execute(request, msg);
    }

    public static String doGet(String url, Map<String, Object> params, Map<String, String> header)
            throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, IOException {
        return execute(url, Method.GET, params, header, false);
    }

    public static String doPost(String url, Map<String, Object> params, Map<String, String> header)
            throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, IOException {
        return execute(url, Method.POST, params, header, false);
    }

    public static String doPostJson(String url, Map<String, Object> params, Map<String, String> header)
            throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, IOException {
        String json = null;
        if (params != null && !params.isEmpty()) {
            for (Iterator<Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = it.next();
                Object object = entry.getValue();
                if (object == null) {
                    it.remove();
                }
            }
        }
        return execute(url, Method.POST, params, header, true);
    }

    public static String doDelete(String url, Map<String, Object> params, Map<String, String> header)
            throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, IOException {
        return execute(url, Method.DELETE, params, header, false);
    }

    /*
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.9</version>
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpmime</artifactId>
        <version>4.5.9</version>
    </dependency>
     */
    public static String doUpload(String url, String name, String path)
            throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(name, new File(path));
        return doUpload(url, params);
    }

    public static String doUpload(String url, Map<String, Object> params)
            throws NoSuchAlgorithmException, KeyManagementException, IOException {
        String msg = "Upload File @ " + url + "\n";
        HttpPost httpPost = new HttpPost(url.trim());
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.setCharset(Charset.forName(CHARSET));
        if (params != null && !params.isEmpty()) {
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Object value = params.get(key);
                if (value instanceof File) {
                    FileBody fileBody = new FileBody((File) value);
                    entityBuilder.addPart(key, fileBody);
                } else {
                    entityBuilder.addPart(key, new StringBody(String.valueOf(value), ContentType.DEFAULT_TEXT.withCharset(CHARSET)));
                }
            }
        }
        HttpEntity entity = entityBuilder.build();
        httpPost.setEntity(entity);
        return execute(httpPost, msg);
    }

    public static void doDownload(String url, Map<String, Object> params, String localFileName) throws IOException {
        if (params != null && !params.isEmpty()) {
            url = url.trim();
            String str = EntityUtils.toString(new UrlEncodedFormEntity(map2NameValuePairList(params)));
            if (url.contains("?")) {
                url += "&" + str;
            } else {
                url += "?" + str;
            }
        }
        doDownLoad(url, localFileName);
    }

    public static void doDownLoad(String url, String localFileName) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(new HttpGet(url));
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            System.out.println("The response value of token: " + response.getFirstHeader("token"));
            if (entity != null) {
                long length = entity.getContentLength();
                if (length <= 0) {
                    throw new IOException("Download File not found!");
                }
                byte[] b = EntityUtils.toByteArray(entity);
                OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(localFileName)));
                out.write(b);
                out.flush();
                out.close();
                EntityUtils.consume(entity);
            }
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }
}
