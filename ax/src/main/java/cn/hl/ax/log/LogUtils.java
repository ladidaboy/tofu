package cn.hl.ax.log;

import cn.hutool.core.util.ArrayUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author hyman
 * @date 2020-09-14 14:38:34
 * @version $ Id: LogUtils.java, v 0.1  hyman Exp $
 */
public class LogUtils {
    private static final String SP = "\r\n  ";
    private static final String NL = "null";

    /**
     * 获取简单错误日志
     * @param ths 错误对象
     * @return 错误日志
     */
    public static String getSimpleMessages(Throwable... ths) {
        if (ths == null || ths.length == 0) {
            return "(N/A)";
        } else {
            return Arrays.stream(ths).map(LogUtils::generateSimpleMessages).collect(Collectors.joining(SP));
        }
    }

    private static String generateSimpleMessages(Throwable th) {
        if (th == null) {
            return null;
        }

        String m = th.getMessage(), c = th.getClass().getSimpleName();
        if (m == null || "".equals(m) || NL.equals(m)) {
            return generateSimpleMessages(th.getCause());
        }

        StackTraceElement[] ts = th.getStackTrace();
        if (ts != null && ts.length > 0) {
            StackTraceElement t = ts[0];
            m = String.format("%s(`%s`) at %s.%s(%s:%d)", c, m, t.getClassName(), t.getMethodName(), t.getFileName(), t.getLineNumber());
        }
        return m;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 打印控制台日志

    private static final int    MIN_LEN   = 40;
    private static final String STYLE_TAG = "\033[93;40;0m";
    private static final String STYLE_LIN = "\033[36;0m";
    private static final String STYLE_LBL = "\033[34;0m";
    private static final String STYLE_WRN = "\033[33;0m";
    private static final String STYLE_ERR = "\033[31;0m";
    private static final String STYLE_CLR = "\033[0m";

    public static void printLine(int len) {
        System.out.println(StringUtils.rightPad("", len, '='));
    }

    public static void printSplitLine() {
        printSplitLine(0);
    }

    public static void printSplitLine(int len) {
        len = Math.max(len, MIN_LEN) / 2 - 12;
        String ss = StringUtils.rightPad("", len, '-');
        System.out.println(STYLE_LIN + ss + "( S P L I T ~~ L I N E )" + ss + STYLE_CLR);
    }

    public static void printWave(int len) {
        System.out.println(StringUtils.rightPad("", len, '~'));
    }

    public static void printSplitWave() {
        printSplitWave(0);
    }

    public static void printSplitWave(int len) {
        len = Math.max(len, MIN_LEN) / 2 - 12;
        String ss = StringUtils.rightPad("", len, '^');
        System.out.println(STYLE_LIN + ss + "\\ S P L I T ~~ W A V E /" + ss + STYLE_CLR);
    }

    public static void printHeadline(String tag, Object... msg) {
        printHeadlineEx(tag, 0, msg);
    }

    public static void printHeadlineEx(String tag, int len, Object... msg) {
        tag = " " + (tag == null ? "^" : tag.trim()) + " ";
        len = Math.max(len, MIN_LEN) - 16;
        int min = tag.length(), max = (int) (Math.ceil(min / 2.0) * 2), mmx = Math.max(len, max);
        tag = StringUtils.leftPad(tag, max + (mmx - max) / 2, "/");
        tag = StringUtils.rightPad(tag, mmx, "\\");
        tag = "____////" + tag + "\\\\\\\\____";
        printlnMessages(tag, msg);
    }

    public static void printCornerTitle(String tag, Object... msg) {
        printCornerTitleEx(tag, 0, msg);
    }

    public static void printCornerTitleEx(String tag, int len, Object... msg) {
        tag = tag == null ? "!" : tag.trim();
        len = Math.max(len, MIN_LEN);
        int min = tag.length(), max = Math.max(min + 18, len);
        tag = StringUtils.rightPad("\\" + tag + "/", min + 10, "^");
        tag = StringUtils.leftPad(tag, max, "^");
        printlnMessages(tag, msg);
    }

    private static void printlnMessages(String tag, Object... msg) {
        int i = 0;
        String[] ms = new String[msg.length + 1];
        ms[i++] = STYLE_TAG + tag + STYLE_CLR;
        if (msg.length > 0) {
            for (Object mm : msg) {
                ms[i++] = mm.toString();
            }
        }
        System.out.println(ArrayUtil.join(ms, "\n"));
    }

    public static void printLabel(String msg) {
        System.out.println(STYLE_LBL + msg + STYLE_CLR);
    }

    public static String printError(String msg, Exception... ee) {
        msg = msg == null ? "(ToT)" : msg.trim();
        if (ee == null || ee.length == 0) {
            msg = "[WARN] " + msg;
            System.out.println(STYLE_WRN + msg + STYLE_CLR);
        } else {
            /*StringWriter w = new StringWriter();
            ee[0].printStackTrace(new PrintWriter(w));
            msg = w.toString();*/
            String sp = "\r\n  ";
            String err = Arrays.stream(ee).map(LogUtils::generateSimpleMessages).collect(Collectors.joining(sp));
            msg = "[ERROR] " + msg + sp + err;
            System.out.println(STYLE_ERR + msg + STYLE_CLR);
        }

        return msg;
    }
}
