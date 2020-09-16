package cn.hl.ox.thread.exp;

import cn.hl.ax.log.LogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hyman
 * @date 2019-11-15 17:42:32
 */
public class ThreadExceptionDemo {
    private static void join2M(Thread th) {
        try {
            th.join();
        } catch (InterruptedException e) {
            System.out.println("Thread.join error:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int max = 92;
        String tag = "↑\n↑", msg = "↓\n↓";
        //^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\\
        LogUtils.printHeadlineEx("test run ThreadException", max, msg);
        Thread thread = new Thread(new ThreadExceptionRunner());
        thread.start();
        //join2M(thread);
        System.out.println(tag);
        //^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\\
        LogUtils.printHeadlineEx("test catch ThreadException", max, msg);
        try {
            thread = new ThreadExceptionThread();
            thread.start();
            join2M(thread);
        } catch (Exception e) {
            String exp = String.format("[%16s] Caught -> %s", Thread.currentThread().getName(), e.getMessage());
            System.out.println(exp);
        }
        System.out.println(tag);
        //^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\\
        LogUtils.printHeadlineEx("test MyHandleThreadFactory", max, msg);
        ExecutorService exec = Executors.newCachedThreadPool(new MyHandleThreadFactory());
        /* ↑↑ 上面的方式是设置每一个线程执行时候的异常处理。*/
        /* ↓↓ 如果每一个线程的异常处理相同，我们可以用如下的方式进行处理，使用Thread的静态方法。*/
        //Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandle());
        //ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new ThreadExceptionRunner());
        exec.execute(new ThreadExceptionThread());
        exec.shutdown();
        System.out.println(tag);
        //
    }
}
