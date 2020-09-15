package cn.hl.kit.ox.log;

import cn.hl.kit.ax.log.LogUtils;

import java.io.File;
import java.io.PrintStream;

/**
 * 自定义日志的输出测试
 *
 * <li><b>ClassName :</b> TestLog</li>
 * <li><b>Author : </b> Half.Lee</li>
 * <li><b>Date : </b> 2015年11月20日</li>
 * <style>*{color:#145b7d;}li{color:#00a6ac;list-style-type:square;}li
 * b{color:#2585a6;}</style>
 */
public class Tester {
    // 输入到文件
    private static final String OUTPUT_TYPE_FILE    = "FILE";
    // 输出到控制台
    private static final String OUTPUT_TYPE_CONSOLE = "CONSOLE";

    // 输出到控制台位置
    private static final PrintStream OUT;
    private static final PrintStream ERR;

    static {
        OUT = System.out;
        ERR = System.err;
        try {
            // 日志输出路径
            String logPath = System.getProperty("user.dir");
            OUT.println("LOG PATH: " + logPath);
            System.setOut(new PrintStream(new File(logPath + "/zen/logs/Tester.log")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义日志的输出路径
     */
    public static void test0() {
        System.out.println("自定义日志的输出测试");
        System.out.println("自定义日志的输出路径");
    }

    /**
     * 输出日志到控制台
     */
    public static void test1() {
        OUT.println("我输出到控制台");
    }

    /**
     * 根据日志输出位置输出日志
     */
    public static void test2(String type) {
        if (OUTPUT_TYPE_FILE.equals(type)) {
            System.out.println("根据日志输出位置输出日志>" + OUTPUT_TYPE_FILE);
        } else if (OUTPUT_TYPE_CONSOLE.equals(type)) {
            OUT.println("根据日志输出位置输出日志>" + OUTPUT_TYPE_CONSOLE);
        } else {

        }
    }

    /**
     * 输出到控制台并且字体为红色
     */
    public static void test3() {
        ERR.println("输出到控制台并且字体为红色");
    }

    /**
     * 输出到控制台加追踪记录(追踪元素)
     */
    public static void test4() {
        StackTraceElement[] traces = new Throwable().getStackTrace();
        for (int i = 0; i < traces.length; i++) {
            ERR.println(traces[i].toString());
        }
    }

    public static void main(String[] args) {
        // 测试输出到文件
        test0();

        // 测试输出到控制台
        test1();

        // 测试输出目标为控制台和文件
        test2(OUTPUT_TYPE_FILE);
        test2(OUTPUT_TYPE_CONSOLE);

        // 输出到控制台并且字体为红色
        test3();

        test4();

        //
        OUT.println("cn.hl.ox.log.Tester.main(Tester.java:101)");

        LogUtils.debug("窗前明月光");
        LogUtils.info("疑是地上霜");
        LogUtils.warn("举头望明月");
        LogUtils.error("低头思故乡");

        //test::LogUtils
        /*MyLogger log = new MyLogger("test::LogUtils:中文测试");
        MyLogger log1 = new MyLogger("日志消息", log);
        log1.printStackTrace(System.err);*/
    }
}
