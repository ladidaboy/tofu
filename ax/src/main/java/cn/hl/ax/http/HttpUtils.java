package cn.hl.ax.http;

import cn.hl.ax.data.DataUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

import static cn.hl.ax.data.DataUtils.rightPadEx;

public class HttpUtils {
    public static int DETAIL_REQUEST = 1;
    public static int DETAIL_SESSION = 2;
    public static int DETAIL_HEADER  = 4;

    public static void displayRequestDetails(HttpServletRequest request) {
        displayRequestDetails(request, DETAIL_REQUEST | DETAIL_SESSION | DETAIL_HEADER);
    }

    /**
     * 现实请求上下文详细信息
     *
     * @param type 信息类型: request, session, header
     */
    public static void displayRequestDetails(HttpServletRequest request, int type) {
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n------------------------ Request Details ---------------------------------------------------------------------");
        int maxLen = 33;

        // >> Request Information
        if (DataUtils.statusHasFlag(type, DETAIL_REQUEST)) {
            sb.append("\r\n\t[ Request information ]");
            sb.append("\r\n\t - ").append(rightPadEx("AuthType", maxLen)).append("\t: ").append(request.getAuthType());
            sb.append("\r\n\t - ").append(rightPadEx("CharacterEncoding", maxLen)).append("\t: ").append(request.getCharacterEncoding());
            sb.append("\r\n\t - ").append(rightPadEx("ContentLength", maxLen)).append("\t: ").append(request.getContentLength());
            sb.append("\r\n\t - ").append(rightPadEx("ContentType", maxLen)).append("\t: ").append(request.getContentType());
            sb.append("\r\n\t - ").append(rightPadEx("ContextPath", maxLen)).append("\t: ").append(request.getContextPath());
            sb.append("\r\n\t - ").append(rightPadEx("LocalAddr", maxLen)).append("\t: ").append(request.getLocalAddr());
            sb.append("\r\n\t - ").append(rightPadEx("LocalName", maxLen)).append("\t: ").append(request.getLocalName());
            sb.append("\r\n\t - ").append(rightPadEx("LocalPort", maxLen)).append("\t: ").append(request.getLocalPort());
            sb.append("\r\n\t - ").append(rightPadEx("Method", maxLen)).append("\t: ").append(request.getMethod());
            sb.append("\r\n\t - ").append(rightPadEx("PathInfo", maxLen)).append("\t: ").append(request.getPathInfo());
            sb.append("\r\n\t - ").append(rightPadEx("PathTranslated", maxLen)).append("\t: ").append(request.getPathTranslated());
            sb.append("\r\n\t - ").append(rightPadEx("Protocol", maxLen)).append("\t: ").append(request.getProtocol());
            sb.append("\r\n\t - ").append(rightPadEx("QueryString", maxLen)).append("\t: ").append(request.getQueryString());
            sb.append("\r\n\t - ").append(rightPadEx("RemoteAddr", maxLen)).append("\t: ").append(request.getRemoteAddr());
            sb.append("\r\n\t - ").append(rightPadEx("RemoteHost", maxLen)).append("\t: ").append(request.getRemoteHost());
            sb.append("\r\n\t - ").append(rightPadEx("RemotePort", maxLen)).append("\t: ").append(request.getRemotePort());
            sb.append("\r\n\t - ").append(rightPadEx("RemoteUser", maxLen)).append("\t: ").append(request.getRemoteUser());
            sb.append("\r\n\t - ").append(rightPadEx("RequestedSessionId", maxLen)).append("\t: ").append(request.getRequestedSessionId());
            sb.append("\r\n\t - ").append(rightPadEx("RequestURI", maxLen)).append("\t: ").append(request.getRequestURI());
            sb.append("\r\n\t - ").append(rightPadEx("Scheme", maxLen)).append("\t: ").append(request.getScheme());
            sb.append("\r\n\t - ").append(rightPadEx("ServerName", maxLen)).append("\t: ").append(request.getServerName());
            sb.append("\r\n\t - ").append(rightPadEx("ServerPort", maxLen)).append("\t: ").append(request.getServerPort());
            sb.append("\r\n\t - ").append(rightPadEx("ServletPath", maxLen)).append("\t: ").append(request.getServletPath());
            sb.append("\r\n\t - ").append(rightPadEx("isRequestedSessionIdFromCookie", maxLen)).append("\t: ").append(request.isRequestedSessionIdFromCookie());
            sb.append("\r\n\t - ").append(rightPadEx("isRequestedSessionIdFromUrl", maxLen)).append("\t: ").append(request.isRequestedSessionIdFromUrl());
            sb.append("\r\n\t - ").append(rightPadEx("isRequestedSessionIdFromURL", maxLen)).append("\t: ").append(request.isRequestedSessionIdFromURL());
            sb.append("\r\n\t - ").append(rightPadEx("isRequestedSessionIdValid", maxLen)).append("\t: ").append(request.isRequestedSessionIdValid());
            sb.append("\r\n\t - ").append(rightPadEx("isSecure", maxLen)).append("\t: ").append(request.isSecure());
        }

        String name;
        Object value;
        // >> Session Information
        if (DataUtils.statusHasFlag(type, DETAIL_SESSION)) {
            sb.append("\r\n\t[ Session information ]");
            HttpSession session = request.getSession();
            Enumeration sessionNames = session.getAttributeNames();
            while (sessionNames.hasMoreElements()) {
                name = (String) sessionNames.nextElement();
                value = session.getAttribute(name);
                sb.append("\r\n\t - ").append(rightPadEx(name, maxLen)).append("\t: ").append(value);
            }
        }

        // >> Header Information
        if (DataUtils.statusHasFlag(type, DETAIL_HEADER)) {
            sb.append("\r\n\t[ Header information ]");
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                name = (String) headerNames.nextElement();
                value = request.getHeader(name);
                sb.append("\r\n\t - ").append(rightPadEx(name, maxLen)).append("\t: ").append(value);
            }
        }

        // >> Log
        sb.append("\r\n--------------------------------------------------------------------------------------------------------------");
        System.out.println(sb.toString());
    }

    /**
     * 获取站点的ROOT
     */
    public static String getRequestRoot(HttpServletRequest request, boolean needContextPath) {
        return request.getScheme() + "://" + request.getLocalAddr() + (needContextPath ? request.getContextPath() : "");
    }

    /**
     * 获取站点的DOMAIN
     */
    public static String getDomain(HttpServletRequest request) {
        String domain = request.getServerName();
        if (domain.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
            return request.getScheme() + "://" + domain;
        }
        String[] names = domain.split("\\.");
        return "." + names[names.length - 2] + "." + names[names.length - 1];
    }

    /**
     * 设置COOKIE
     *
     * @param expiry 时效(单位: 秒)
     */
    @SuppressWarnings("deprecation")
    public static boolean setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int expiry) {
        if (request == null || response == null || DataUtils.isInvalid(name)) {
            return false;
        }

        try {
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            value = URLEncoder.encode(value);
        }
        //
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(getDomain(request));
        cookie.setPath("/");
        if (expiry > 0) {
            cookie.setMaxAge(expiry);
        }

        response.addCookie(cookie);
        //
        return true;
    }

    /**
     * 读取COOKIE
     */
    @SuppressWarnings("deprecation")
    public static String readCookie(HttpServletRequest request, String name) {
        if (request == null || DataUtils.isInvalid(name)) {
            return null;
        }
        //
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                String value = cookie.getValue();
                try {
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    value = URLDecoder.decode(value);
                }
                return value;
            }
        }
        return null;
    }

    /**
     * 删除COOKIE
     */
    public static boolean delCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        if (request == null || response == null || DataUtils.isInvalid(name)) {
            return false;
        }
        //
        Cookie cookie = new Cookie(name, null);
        cookie.setDomain(getDomain(request));
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        //
        return true;
    }

    /**
     * 处理HTML特殊字符
     */
    public static String htmlSpecialChars(String text) {
        if (DataUtils.isInvalid(text)) {
            return text;
        }
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        text = text.replaceAll("\"", "&quot;");
        return text;
    }

}
