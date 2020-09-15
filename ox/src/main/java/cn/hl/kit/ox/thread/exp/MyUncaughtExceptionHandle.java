package cn.hl.kit.ox.thread.exp;

/**
 * @author hyman
 * @date 2019-11-15 17:50:54
 */
public class MyUncaughtExceptionHandle implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String msg = String.format("[%16s] Caught #%s > %s", Thread.currentThread().getName(), t.getName(), e.getMessage());
        System.out.println(msg);
    }
}
