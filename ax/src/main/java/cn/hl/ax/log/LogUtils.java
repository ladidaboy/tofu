package cn.hl.ax.log;

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
}
