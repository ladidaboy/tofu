package cn.hl.ax.data;

import cn.hl.ax.CommonConst;
import cn.hl.ax.CommonConst.FormatPrettify;
import cn.hl.ax.JavaBean;
import cn.hl.ax.log.LogUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author hyman
 * @date 2020-09-16 11:06:33
 */
public class DataUtilsTests {
    @Test
    public void test4isValid() {
        JavaBean bean = new JavaBean();
        bean.setId("No.99");
        System.out.println(bean + " - Valid: " + DataUtils.isValidBean(bean));
        bean.setAge(9999);
        System.out.println(bean + " - Valid: " + DataUtils.isValidBean(bean));
    }

    @Test
    public void test4status() {
        System.out.println("ⓘ statusHasFlag(12, 2) → " + DataUtils.statusHasFlag(12, 2));
        System.out.println("ⓘ isFullStatus(15, 4) → " + DataUtils.isFullStatus(15, 4));
        System.out.println("ⓘ isMonoStatus(9) → " + DataUtils.isMonoStatus(9));
    }

    @Test
    public void test4random() {
        System.out.println("randomInt(8) ➣ " + DataUtils.randomInt(8));
        String sp;
        for (int i = 0; i < 22; i++) {
            sp = i / 10 % 2 == 0 ? " ➢ " : " ➣ ";
            System.out.println("randomInt(10, MONO)" + sp + DataUtils.randomInt(10, "MONO"));
        }
        System.out.println("randomInt(256, 512) ➣ " + DataUtils.randomInt(256, 512));
    }

    @Test
    public void test4value() {
        int maxSize = 200, lenSize = 152;
        try {
            LogUtils.printSplitLine(lenSize);
            List<Double> dbs = new ArrayList<>();
            for (int i = 0; i < maxSize; i++) {
                dbs.add(DataUtils.getScale(Math.random() * 10000, 2));
            }
            Double indexValue = DataUtils.getPercentIndex(dbs, 78.9);
            System.out.println("\033[33;1mIndex(78.9%) = " + indexValue + "\033[0m");
            printIndexMap(dbs, indexValue, "  [%3d] %7.2f", 10);
        } catch (Exception e) {
            LogUtils.printError("getPercentIndex", e);
        }

        try {
            LogUtils.printSplitLine(lenSize);
            Long[] dbs = new Long[maxSize];
            for (int i = 0; i < maxSize; i++) {
                dbs[i] = (long) (Math.random() * 10000);
            }
            Long indexValue = DataUtils.getPercentIndex(dbs, 88.8);
            System.out.println("\033[33;1mIndex(88.8%) = " + indexValue + "\033[0m");
            printIndexMap(dbs, indexValue, "  [%3d] %4d", 10);
        } catch (Exception e) {
            LogUtils.printError("getPercentIndex", e);
        }
    }

    @Test
    public void test4camel() {
        System.out.println("humpToLine(`userName`) " + DataUtils.humpToLine("userName"));
        System.out.println("humpToLine(`UserName`) " + DataUtils.humpToLine("UserName"));
        System.out.println("lineToHump(`user_name`) " + DataUtils.lineToHump("user_name"));
        System.out.println("lineToHump(`_user_name`) " + DataUtils.lineToHump("_user_name"));
    }

    @Test
    public void test4ip() {
        String ip = "192.168.65.8", segment;
        System.out.println("isInternalIp(" + ip + ") >> " + DataUtils.isInternalIp(ip));
        ip += "/23";
        System.out.println("isInternalIp(" + ip + ") >> " + DataUtils.isInternalIp(ip));
        ip = "192.168.128.0/23";
        System.out.println("isInternalIp(" + ip + ") >> " + DataUtils.isInternalIp(ip));

        ip = "192.168.65.234";
        segment = "192.168.64.0/23";
        System.out.println("ipInSegment(" + ip + ", " + segment + ") >> " + DataUtils.ipInSegment(ip, segment));
        ip = "192.168.65.234";
        segment = "192.168.128.0/19";
        System.out.println("ipInSegment(" + ip + ", " + segment + ") >> " + DataUtils.ipInSegment(ip, segment));

        DataUtils.getIpSegmentRange("10.10.0.0/19");
        DataUtils.getIpSegmentRange("10.10.0.0/29");
        DataUtils.getIpSegmentRange("10.10.0.8/30");
        DataUtils.getIpSegmentRange("10.10.0.0/31");
        DataUtils.getIpSegmentRange("10.10.0.0/32");
    }

    @Test
    public void test4match() {
        System.out.println("matchOne: " + DataUtils.matchOne(FormatPrettify.ENUM, FormatPrettify.NONE, FormatPrettify.ENUM));
        System.out.println("enum.findIn: " + FormatPrettify.ENUM.findIn(FormatPrettify.NONE, FormatPrettify.DATE));

        Date now = new Date();
        System.out.println("isSame: " + DataUtils.isSame(now, "2020-06-30 13:17:"));
        System.out.println("isSame: " + DataUtils.isSame(now, CommonConst.SDF_YMD_HMS_MS.format(now)));
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static <T> void printIndexMap(T[] array, T indexValue, String format, int colSize) {
        printIndexMap(Arrays.asList(array), indexValue, format, colSize);
    }

    private static <T> void printIndexMap(List<T> array, T indexValue, String format, int colSize) {
        //ReentrantLock lock = new ReentrantLock(true);
        for (int i = 0; i < array.size(); i++) {
            //lock.lock();
            int ii = i + 1;
            T thisValue = array.get(i);
            String msg = String.format(format, ii, thisValue);
            if (thisValue == indexValue) {
                System.out.print("\033[31;0m" + msg + "\033[0m");
            } else {
                System.out.print(msg);
            }
            if (ii % colSize == 0) {
                System.out.println();
            }
            //lock.unlock();
        }
    }
}
