package cn.hl.ax.time;

import cn.hl.ax.CommonConst;

import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Random;

/**
 * @author Halfman
 * @date 2019-09-23 10:58:08
 */
public class UTCTimeUtils {
    /**
     * Clock格式化：
     * %Y_%m_%d%n%H:%M:%S
     *
     * UTC格式：
     * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     */

    public static Date now() {
        LocalDateTime dateTime = LocalDateTime.now(Clock.systemUTC());
        String localTime = CommonConst.DTF_YMD_HMS.format(dateTime);
        try {
            return CommonConst.SDF_YMD_HMS.parse(localTime);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String getNow() {
        LocalDateTime dateTime = LocalDateTime.now(Clock.systemUTC());
        return dateTime.format(CommonConst.DTF_YMD_HMS);
    }

    public static long getNowMillis() {
        return Instant.now().toEpochMilli();
    }

    public static long getNowSeconds() {
        return Instant.now().getEpochSecond();
    }

    public static String getTime(Date time) {
        assert time == null : "time is null";

        return CommonConst.SDF_YMD_HMS.format(time);
    }

    public static long getTimeMillis(String time) {
        assert time == null : "time is null";

        LocalDateTime dataTime = LocalDateTime.parse(time, CommonConst.DTF_YMD_HMS);
        Instant instant = dataTime.toInstant(ZoneOffset.UTC);
        return instant.toEpochMilli();
    }

    public static long getTimeSeconds(String time) {
        assert time == null : "time is null";

        LocalDateTime dataTime = LocalDateTime.parse(time, CommonConst.DTF_YMD_HMS);
        return dataTime.toEpochSecond(ZoneOffset.UTC);
    }

    public static String fromMillis(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
        return dateTime.format(CommonConst.DTF_YMD_HMS);
    }

    public static String fromSeconds(long seconds) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneOffset.UTC);
        return dateTime.format(CommonConst.DTF_YMD_HMS);
    }

    public static Date fromDateTime(String datetime) {
        assert datetime == null : "datetime is null";

        try {
            return CommonConst.SDF_YMD_HMS.parse(datetime);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date random() {
        Random rd = new Random();
        LocalDateTime dateTime = LocalDateTime.of(2000 + rd.nextInt(20), 1 + rd.nextInt(12), 1 + rd.nextInt(27), //
                rd.nextInt(24), rd.nextInt(60), rd.nextInt(60));
        return Date.from(dateTime.toInstant(ZoneOffset.UTC));
    }
}
