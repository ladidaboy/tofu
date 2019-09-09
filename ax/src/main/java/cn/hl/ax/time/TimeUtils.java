package cn.hl.ax.time;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils extends DateUtils {
    /**
     * 格式化字符串毫秒数成日期字符串
     * @param longTime -- 待转换字符串毫秒数
     * @param pattern -- 日期格式
     * @return String
     */
    public static String formatTime(String longTime, String pattern) throws Exception {
        long time = Long.parseLong(longTime);
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 将时间字符串转换成毫秒数
     * 转换出错时返回0L
     * @param date -- 待转换时间字符串
     * @param pattern -- 待转换时间字符串的格式
     * @return long
     */
    public static long formatDate(String date, String pattern) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(date).getTime();
    }

    /**
     * yyyy.MM.dd G 'at' HH:mm:ss z  -  2001.07.04 AD at 12:08:56 PDT
     * EEE, MMM d, ''yy              -  Wed, Jul 4, '01
     * h:mm a                        -  12:08 PM
     * hh 'o''clock' a, zzzz         -  12 o'clock PM, Pacific Daylight Time
     * K:mm a, z                     -  0:08 PM, PDT
     * yyyyy.MMMMM.dd GGG hh:mm aaa  -  02001.July.04 AD 12:08 PM
     * EEE, d MMM yyyy HH:mm:ss Z    -  Wed, 4 Jul 2001 12:08:56 -0700
     * yyMMddHHmmssZ                 -  010704120856-0700
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ    -  2001-07-04T12:08:56.235-0700
     */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        System.out.println(sdf.format(new Date()));
        System.out.println(String.format("%1$tF %1$tT", System.currentTimeMillis()));

        //
        Date date;
        long fragment;
        boolean isEquals;
        int truncatedCompare;

        /**
         * String转换成Date
         * arg0 : 日期字符串 String
         * arg1 : 特定的地理，政治和文化地区.可以传null
         * arg3 : 日期格式.与arg0格式一致 String
         * 该方法对日期和时间的解释是宽松的
         * 宽松的解释日期（如 1996 年 2 月 42 日）将被视为等同于 1996 年 2 月 1 日后的第 41 天
         * 如果是严格的解释，此类日期就会引发异常
         */
        Date date1 = parseDate("20190101 00:00:00,000", Locale.CANADA_FRENCH, "yyyyMMdd HH:mm:ss,SSS");
        Date date2 = parseDate("20191010 12:34:56,789", Locale.SIMPLIFIED_CHINESE, "yyyyMMdd HH:mm:ss,SSS");

        /**
         * String转换成Date 严格的
         * arg0 : 日期字符串 String
         * arg1 : 特定的地理，政治和文化地区.可以传null
         * arg3 : 日期格式.与arg0格式一致 String
         * 该方法对日期和时间的解释是严格的
         */
        Date date3 = parseDateStrictly("20190712", Locale.TRADITIONAL_CHINESE, "yyyyMMdd");
        Date date4 = parseDateStrictly("20190712", Locale.SIMPLIFIED_CHINESE, "yyyyMMdd");

        /**
         * 判断两个日期是否是同一天
         * arg0 arg1 数据类型 : Date Calendar
         * 比较arg0 arg1的
         * ERA = 0 年代
         * YEAR = 1 年
         * DAY_OF_YEAR = 6 年中的第几天
         */
        isEquals = isSameDay(date1, date2);
        System.out.println("isSameDay       = " + isEquals);

        /**
         * 判断两个日期是不是同一毫秒
         * arg0 arg1 数据类型 : Date Calendar
         * 自1970年1月1日00:00:00 GMT 的毫秒数是否相等
         */
        isEquals = isSameInstant(date3, date4);
        System.out.println("isSameInstant   = " + isEquals);

        /**
         * 判断是否是同一个本地时间
         * arg0 arg1 数据类型 : Calendar
         * 比较arg0 arg1的
         * 数据类型
         * ERA = 0 年代
         * YEAR = 1 年
         * DAY_OF_YEAR = 6 年中的第几天
         * HOUR_OF_DAY = 11 天中的第几个小时
         * MINUTE = 12 分钟
         * SECOND = 13 秒
         * MILLISECOND = 14 毫秒
         */
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        isEquals = isSameLocalTime(cal1, cal2);
        System.out.println("isSameLocalTime = " + isEquals);

        System.out.println("----------------------------------------------");
        System.out.println("OriginalDate         = " + sdf.format(date1));

        /**
         * 获取指定日期前后arg1年
         * arg0 : 指定日期 Date类型
         * arg1 : int型,正数向后天数,0当天,负数向前天数
         */
        date = addYears(date1, 4);
        System.out.println("addYears(4)          = " + sdf.format(date));

        /**
         * 获取指定日期前后arg1月
         * arg0 : 指定日期 Date类型
         * arg1 : int型,正数向后天数,0当天,负数向前天数
         */
        date = addMonths(date1, 4);
        System.out.println("addMonths(4)         = " + sdf.format(date));

        /**
         * 获取指定日期前后arg1周
         * arg0 : 指定日期 Date类型
         * arg1 : int型,正数向后天数,0当天,负数向前天数
         */
        date = addWeeks(date1, 4);
        System.out.println("addWeeks(4)          = " + sdf.format(date));

        /**
         * 获取指定日期前后arg1天
         * arg0 : 指定日期 Date类型
         * arg1 : int型,正数向后天数,0当天,负数向前天数
         */
        date = addDays(date1, 4);
        System.out.println("addDays(4)           = " + sdf.format(date));

        /**
         * 获取指定日期前后arg1小时
         * arg0 : 指定日期 Date类型
         * arg1 : int型,正数向后天数,0当天,负数向前天数
         */
        date = addHours(date1, 4);
        System.out.println("addHours(4)          = " + sdf.format(date));

        /**
         * 获取指定日期前后arg1分钟
         * arg0 : 指定日期 Date类型
         * arg1 : int型,正数向后天数,0当天,负数向前天数
         */
        date = addMinutes(date1, 4);
        System.out.println("addMinutes(4)        = " + sdf.format(date));

        /**
         * 获取指定日期前后arg1秒
         * arg0 : 指定日期 Date类型
         * arg1 : int型,正数向后天数,0当天,负数向前天数
         */
        date = addSeconds(date1, 4);
        System.out.println("addSeconds(4)        = " + sdf.format(date));

        /**
         * 获取指定日期前后arg1毫秒
         * arg0 : 指定日期 Date类型
         * arg1 : int型,正数向后天数,0当天,负数向前天数
         */
        date = addMilliseconds(date1, 4);
        System.out.println("addMilliseconds(4)   = " + sdf.format(date));

        /**
         * 指定日期年的值
         * arg0 : 日期 Date类型
         * arg1 : int型
         */
        date = setYears(date1, 2008);
        System.out.println("setYears(2008)       = " + sdf.format(date));

        /**
         * 指定日期月的值
         * arg0 : 日期 Date类型
         * arg1 : int型 范围在 0-11
         */
        date = setMonths(date1, 9);
        System.out.println("setMonths(9)         = " + sdf.format(date));

        /**
         * 指定日期天的值
         * arg0 : 日期 Date类型
         * arg1 : int型 范围在 1-31(不同月份值略有不同)
         */
        date = setDays(date1, 15);
        System.out.println("setDays(15)          = " + sdf.format(date));

        /**
         * 指定日期小时的值
         * arg0 : 日期 Date类型
         * arg1 : int型 范围在1-23
         */
        date = setHours(date1, 12);
        System.out.println("setHours(12)         = " + sdf.format(date));

        /**
         * 指定日期分钟的值
         * arg0 : 日期 Date类型
         * arg1 : int型 范围在1-59
         */
        date = setMinutes(date1, 34);
        System.out.println("setMinutes(34)       = " + sdf.format(date));

        /**
         * 指定日期秒的值
         * arg0 : 日期 Date类型
         * arg1 : int型 范围在1-59
         */
        date = setSeconds(date1, 56);
        System.out.println("setSeconds(56)       = " + sdf.format(date));

        /**
         * 指定日期毫秒的值
         * arg0 : 日期 Date类型
         * arg1 : int型
         */
        date = setMilliseconds(date1, 789);
        System.out.println("setMilliseconds(789) = " + sdf.format(date));

        /**
         * 获取时区
         * timeZone 系统默认
         * timeZone1 系统默认时区
         * timeZone2 设置时区
         */
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();
        TimeZone timeZone1 = TimeZone.getDefault();
        TimeZone timeZone2 = TimeZone.getTimeZone("Europe/Copenhagen");
        System.out.println("> timeZone = " + (timeZone.getDisplayName() == timeZone1.getDisplayName()));

        /**
         * 相当于
         * Calendar cal3 = Calendar.getInstance();
         * cal3.setTime(date);
         * 得到的cal
         */
        Calendar cal3 = toCalendar(date1);
        System.out.println("toCalendar = " + cal3.getTime() + " " + timeZone1.getDisplayName());

        /**
         * Date 转换成 Calendar 带时区
         * arg0 : 日期 Date类型
         * arg1 : 时区
         */
        Calendar cal4 = toCalendar(date1, timeZone2);
        System.out.println("toCalendar = " + cal4.getTime() + " " + timeZone2.getDisplayName());

        System.out.println("---------------------------------------------------");
        System.out.println("getFragments (base on MONTH)");
        System.out.println("OriginalDate              = " + sdf.format(date2));
        /**
         * 获取指定日期中从指定位置起的毫秒数
         * arg0 : 指定的日期 Date类型 或 Calendar类型
         * arg1 : 指定从什么位置开始 int类型:建议使用 Calendar.YEAR Calendar.MONTH 等常量
         */
        fragment = getFragmentInMilliseconds(date2, Calendar.MONTH);
        System.out.println("getFragmentInMilliseconds = " + fragment);

        /**
         * 获取指定日期中从指定位置起的秒数
         * arg0 : 指定的日期 Date类型 或 Calendar类型
         * arg1 : 指定从什么位置开始 int类型:建议使用 Calendar.YEAR Calendar.MONTH 等常量
         */
        fragment = getFragmentInSeconds(date2, Calendar.MONTH);
        System.out.println("getFragmentInSeconds      = " + fragment);

        /**
         * 获取指定日期中从指定位置起的分钟数
         * arg0 : 指定的日期 Date类型 或 Calendar类型
         * arg1 : 指定从什么位置开始 int类型:建议使用 Calendar.YEAR Calendar.MONTH 等常量
         */
        fragment = getFragmentInMinutes(date2, Calendar.MONTH);
        System.out.println("getFragmentInMinutes      = " + fragment);

        /**
         * 获取指定日期中从指定位置起的小时数
         * arg0 : 指定的日期 Date类型 或 Calendar类型
         * arg1 : 指定从什么位置开始 int类型:建议使用 Calendar.YEAR Calendar.MONTH 等常量
         */
        fragment = getFragmentInHours(date2, Calendar.MONTH);
        System.out.println("getFragmentInHours        = " + fragment);

        /**
         * 获取指定日期中从指定位置起的天数
         * arg0 : 指定的日期 Date类型 或 Calendar类型
         * arg1 : 指定从什么位置开始 int类型:建议使用 Calendar.YEAR Calendar.MONTH 等常量
         */
        fragment = getFragmentInDays(date2, Calendar.MONTH);
        System.out.println("getFragmentInDays         = " + fragment);

        System.out.println("------------------------------------------");
        System.out.println("OriginalDate     = " + sdf.format(date2));

        date = round(date2, Calendar.MINUTE);
        System.out.println("round(MINUTE)    = " + sdf.format(date));

        date = round(date2, Calendar.DATE);
        System.out.println("round(DATE)      = " + sdf.format(date));

        date = ceiling(date2, Calendar.MINUTE);
        System.out.println("ceiling(MINUTE)  = " + sdf.format(date));

        date = ceiling(date2, Calendar.DATE);
        System.out.println("ceiling(DATE)    = " + sdf.format(date));

        date = truncate(date2, Calendar.MINUTE);
        System.out.println("truncate(MINUTE) = " + sdf.format(date));

        date = truncate(date2, Calendar.DATE);
        System.out.println("truncate(DATE)   = " + sdf.format(date));

        System.out.println("------------------------------------------");

        /**
         * 判断两个时间在指定的位置之上是否相等
         * arg0 : 时间1 Date类型 或 Calendar类型
         * arg1 : 时间2 Date类型 或 Calendar类型
         * arg2 : 指定在位置上开始比较 int类型:建议使用 Calendar.YEAR Calendar.MONTH 等常量
         */
        isEquals = truncatedEquals(date1, date2, Calendar.MONDAY);
        System.out.println("truncatedEquals    = " + isEquals);

        /**
         * 比较arg0与arg1两个时间在指定的位置上的时间差值
         * arg0 : 时间1 Date类型 或 Calendar类型
         * arg1 : 时间2 Date类型 或 Calendar类型
         * arg2 : 指定在位置上开始比较 int类型:建议使用 Calendar.YEAR Calendar.MONTH 等常量
         */
        truncatedCompare = truncatedCompareTo(date1, date2, Calendar.MONDAY);
        System.out.println("truncatedCompareTo = " + truncatedCompare);
    }

}
