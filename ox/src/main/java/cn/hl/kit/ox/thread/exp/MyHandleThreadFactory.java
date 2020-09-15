package cn.hl.kit.ox.thread.exp;

import java.util.concurrent.ThreadFactory;

/**
 * @author hyman
 * @date 2019-11-15 17:53:02
 */
public class MyHandleThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandle());
        String msg = String.format("[%16s] Create #%s & set uncaughtException", Thread.currentThread().getName(), t.getName());
        System.out.println(msg);
        return t;
    }
}
