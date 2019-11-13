package cn.hl.ax.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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

    public static final String PATTERN_DATE     = "yyyy-MM-dd";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    private static DateTimeFormatter DTF_D  = DateTimeFormatter.ofPattern(PATTERN_DATE);
    private static DateTimeFormatter DTF_DT = DateTimeFormatter.ofPattern(PATTERN_DATETIME);
    private static SimpleDateFormat  SDF_DT = new SimpleDateFormat(PATTERN_DATETIME);

    public static Date random() {
        Random rd = new Random();
        LocalDateTime dateTime = LocalDateTime.of(2000 + rd.nextInt(20), 1 + rd.nextInt(12), 1 + rd.nextInt(27), //
                rd.nextInt(24), rd.nextInt(60), rd.nextInt(60));
        return Date.from(dateTime.toInstant(ZoneOffset.UTC));
    }

    public static Date now() {
        LocalDateTime dateTime = LocalDateTime.now(Clock.systemUTC());
        String localTime = DTF_DT.format(dateTime);
        try {
            return SDF_DT.parse(localTime);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String getNow() {
        LocalDateTime dateTime = LocalDateTime.now(Clock.systemUTC());
        return dateTime.format(DTF_DT);
    }

    public static long getNowMillis() {
        return Instant.now().toEpochMilli();
    }

    public static long getNowSeconds() {
        return Instant.now().getEpochSecond();
    }

    public static String getTime(Date time) {
        assert time == null : "time is null";

        return SDF_DT.format(time);
    }

    public static long getTimeMillis(String time) {
        assert time == null : "time is null";

        LocalDateTime dataTime = LocalDateTime.parse(time, DTF_DT);
        Instant instant = dataTime.toInstant(ZoneOffset.UTC);
        return instant.toEpochMilli();
    }

    public static long getTimeSeconds(String time) {
        assert time == null : "time is null";

        LocalDateTime dataTime = LocalDateTime.parse(time, DTF_DT);
        return dataTime.toEpochSecond(ZoneOffset.UTC);
    }

    public static String fromMillis(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
        return dateTime.format(DTF_DT);
    }

    public static String fromSeconds(long seconds) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneOffset.UTC);
        return dateTime.format(DTF_DT);
    }

    public static Date fromDateTime(String datetime) {
        assert datetime == null : "datetime is null";

        try {
            return SDF_DT.parse(datetime);
        } catch (ParseException e) {
            return null;
        }
    }

}
