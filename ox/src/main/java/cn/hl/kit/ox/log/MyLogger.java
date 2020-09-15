package cn.hl.kit.ox.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLogger implements Serializable {
    private static final long serialVersionUID = -3042686055658047285L;

    private transient Object              backtrace;
    private           String              detailMessage;
    private           MyLogger            cause = this;
    private           StackTraceElement[] stackTrace;

    public MyLogger() {
        fillInStackTrace();
    }

    public MyLogger(String message) {
        fillInStackTrace();
        detailMessage = message;
    }

    public MyLogger(String message, MyLogger cause) {
        fillInStackTrace();
        detailMessage = message;
        this.cause = cause;
    }

    public MyLogger(MyLogger cause) {
        fillInStackTrace();
        detailMessage = (cause == null ? null : cause.toString());
        this.cause = cause;
    }

    public String getMessage() {
        return detailMessage;
    }

    public String getLocalizedMessage() {
        return getMessage();
    }

    public MyLogger getCause() {
        return (cause == this ? null : cause);
    }

    public synchronized MyLogger initCause(MyLogger cause) {
        if (this.cause != this) {
            throw new IllegalStateException("Can't overwrite cause");
        }
        if (cause == this) {
            throw new IllegalArgumentException("Self-causation not permitted");
        }
        this.cause = cause;
        return this;
    }

    public String toString() {
        String clz = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (clz + ": " + message) : clz;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream s) {
        s.println(this);
        StackTraceElement[] trace = getOurStackTrace();
        for (int i = 0; i < trace.length; i++) {
            s.println("\tat " + trace[i]);
        }
        MyLogger ourCause = getCause();
        if (ourCause != null) {
            ourCause.printStackTraceAsCause(s, trace);
        }
    }

    private void printStackTraceAsCause(PrintStream s, StackTraceElement[] causedTrace) {
        StackTraceElement[] trace = getOurStackTrace();
        int m = trace.length - 1, n = causedTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }
        int framesInCommon = trace.length - 1 - m;

        s.println("Caused by: " + this);
        for (int i = 0; i <= m; i++) {
            s.println("\tat " + trace[i]);
        }
        if (framesInCommon != 0) {
            s.println("\t... " + framesInCommon + " more");
        }
        MyLogger ourCause = getCause();
        if (ourCause != null) {
            ourCause.printStackTraceAsCause(s, trace);
        }
    }

    private void printStackTraceAsCause(PrintWriter s, StackTraceElement[] causedTrace) {
        StackTraceElement[] trace = getOurStackTrace();
        int m = trace.length - 1, n = causedTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }
        int framesInCommon = trace.length - 1 - m;

        s.println("Caused by: " + this);
        for (int i = 0; i <= m; i++) {
            s.println("\tat " + trace[i]);
        }
        if (framesInCommon != 0) {
            s.println("\t... " + framesInCommon + " more");
        }

        MyLogger ourCause = getCause();
        if (ourCause != null) {
            ourCause.printStackTraceAsCause(s, trace);
        }
    }

    public void printStackTrace(PrintWriter s) {
        synchronized (s) {
            s.println(this);
            StackTraceElement[] trace = getOurStackTrace();
            for (int i = 0; i < trace.length; i++) {
                s.println("\tat " + trace[i]);
            }

            MyLogger ourCause = getCause();
            if (ourCause != null) {
                ourCause.printStackTraceAsCause(s, trace);
            }
        }
    }

    public synchronized native Throwable fillInStackTrace();

    public StackTraceElement[] getStackTrace() {
        return (StackTraceElement[]) getOurStackTrace().clone();
    }

    private synchronized StackTraceElement[] getOurStackTrace() {
        if (stackTrace == null) {
            int depth = getStackTraceDepth();
            stackTrace = new StackTraceElement[depth];
            for (int i = 0; i < depth; i++) {
                stackTrace[i] = getStackTraceElement(i);
            }
        }
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        StackTraceElement[] defensiveCopy = (StackTraceElement[]) stackTrace.clone();
        for (int i = 0; i < defensiveCopy.length; i++) {
            if (defensiveCopy[i] == null) {
                throw new NullPointerException("stackTrace[" + i + "]");
            }
        }

        this.stackTrace = defensiveCopy;
    }

    private native int getStackTraceDepth();

    private native StackTraceElement getStackTraceElement(int index);

    private synchronized void writeObject(java.io.ObjectOutputStream s) throws IOException {
        getOurStackTrace();
        s.defaultWriteObject();
    }

    // --------------------------------------------------------------------------------------------------------

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static void log2Console(String msg) {
        System.out.println(sdf.format(new Date()) + " [" + Thread.currentThread().getName() + "] - " + msg);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        /*MyLogger log = new MyLogger("NULL Pointer");
        log.printStackTrace();*/

        log2Console("Test LogUtil.");

        Log logger = LogFactory.getLog(MyLogger.class);
        logger.debug("窗前明月光");
        logger.info("疑是地上霜");
        logger.warn("举头望明月");
        logger.error("低头思故乡");

    }
}
