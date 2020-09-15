package cn.hl.kit.ax.log;

import cn.hl.kit.ax.CommonConst;
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
            /*StringWriter w = new StringWriter();
            ths[0].printStackTrace(new PrintWriter(w));
            return w.toString();*/
            return Arrays.stream(ths).map(LogUtils::generateSimpleMessages).collect(Collectors.joining(SP));
        }
    }

    private static String generateSimpleMessages(Throwable th) {
        if (th == null) {
            return null;
        }

        String m = th.getMessage();
        if (m == null || "".equals(m) || NL.equals(m)) {
            return generateSimpleMessages(th.getCause());
        }

        StackTraceElement[] ts = th.getStackTrace();
        if (ts != null && ts.length > 0) {
            StackTraceElement t = ts[0];
            m = String.format("`%s` at %s.%s(%s:%d)", m, t.getClassName(), t.getMethodName(), t.getFileName(), t.getLineNumber());
        }
        return m;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 打印控制台日志

    private static final int MIN_LEN = 32;

    public static void printHeadline(String tag, Object... msg) {
        printHeadlineEx(tag, 0, msg);
    }

    public static void printHeadlineEx(String tag, int len, Object... msg) {
        tag = tag == null ? "^" : tag.trim();
        tag = " " + tag + " ";
        len = Math.max(len, MIN_LEN) - 16;
        int lmin = tag.length(), lmax = (int) (Math.ceil(lmin / 2.0) * 2), smax = Math.max(len, lmax);
        tag = StringUtils.leftPad(tag, lmax + (smax - lmax) / 2, "/");
        tag = StringUtils.rightPad(tag, smax, "\\");
        tag = "____////" + tag + "\\\\\\\\____";
        try {
            Thread.sleep(5);
            System.err.println(tag);
            Thread.sleep(5);
            if (msg.length > 0) {
                for (Object mm : msg) {
                    System.out.println(mm);
                }
            }
            Thread.sleep(5);
        } catch (InterruptedException e) {
            //
        }
    }

    public static void printCornerTitle(String tag, Object... msg) {
        printCornerTitleEx(tag, 0, msg);
    }

    public static void printCornerTitleEx(String tag, int len, Object... msg) {
        tag = tag == null ? "!" : tag.trim();
        len = Math.max(len, MIN_LEN);
        int lmin = tag.length(), lmax = Math.max(lmin + 18, len);
        tag = StringUtils.rightPad("\\" + tag + "/", lmin + 10, "^");
        tag = StringUtils.leftPad(tag, lmax, "^");
        try {
            Thread.sleep(5);
            if (msg.length > 0) {
                for (Object mm : msg) {
                    System.out.println(mm);
                }
            }
            Thread.sleep(5);
            System.err.println(tag);
            Thread.sleep(5);
        } catch (InterruptedException e) {
            //
        }
    }

    public static void printSplitLine() {
        printSplitLine(0);
    }

    public static void printSplitLine(int len) {
        len = Math.max(len, MIN_LEN) / 2 - 12;
        String ss = StringUtils.rightPad("", len, '-');
        String ll = "( S P L I T ~ L I N E )-";
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            ll = "( E R R O R ~ L I N E )-";
        }
        System.err.println(ss + ll + ss);
    }

    public static void printSplitWave() {
        printSplitWave(0);
    }

    public static void printSplitWave(int len) {
        len = Math.max(len, MIN_LEN) / 2 - 12;
        String ss = StringUtils.rightPad("", len, '^');
        String ll = "\\ S P L I T ~ L I N E /^";
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            ll = "\\ E R R O R ~ L I N E /^";
        }
        System.err.println(ss + ll + ss);
    }

    public static String printError(String msg, Exception... ee) {
        msg = msg == null ? "(ToT)" : msg.trim();
        if (ee == null || ee.length == 0) {
            msg = "[WARN] " + msg;
        } else {
            /*StringWriter w = new StringWriter();
            ee[0].printStackTrace(new PrintWriter(w));
            msg = w.toString();*/
            String sp = "\r\n  ";
            String err = Arrays.stream(ee).map(LogUtils::getExceptionMessage).collect(Collectors.joining(sp));
            msg = "[ERROR] " + msg + sp + err;
        }
        System.err.println(msg);
        return msg;
    }

    public static String getExceptionMessage(Throwable th) {
        if (th == null) {
            return null;
        }

        String m = th.getMessage();
        if (m == null || CommonConst.S_NIL.equals(m)) {
            return getExceptionMessage(th.getCause());
        }

        StackTraceElement[] stes = th.getStackTrace();
        if (stes != null && stes.length > 0) {
            StackTraceElement t = stes[0];
            m = '`' + m + String.format("` at %s.%s(%s:%d)", t.getClassName(), t.getMethodName(), t.getFileName(), t.getLineNumber());
        }
        return m;
    }
}
