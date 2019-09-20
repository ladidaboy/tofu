package cn.hl.ox.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadPoolExecutor的完整构造方法的签名是：
 * ThreadPoolExecutor(
 *     int corePoolSize,                  池中所保存的线程数，包括空闲线程。
 *     int maximumPoolSize,               池中允许的最大线程数。
 *     long keepAliveTime,                当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
 *     TimeUnit unit,                     keepAliveTime参数的时间单位。
 *     BlockingQueue<Runnable> workQueue, 执行前用于保持任务的队列。此队列仅保持由 execute方法提交的 Runnable任务。
 *     ThreadFactory threadFactory,       执行程序创建新线程时使用的工厂。
 *     RejectedExecutionHandler handler   由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序。
 * ).
 * ThreadPoolExecutor是Executors类的底层实现。
 */
public class TestSingleThreadExecutor {
    public static void main(String[] args) {
        // 创建一个单线程的线程池
        ExecutorService pool = Executors.newSingleThreadExecutor();
        // new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new
        // LinkedBlockingQueue<Runnable>()));
        for (int i = 0; i < 10; i++) {
            // 创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
            Thread th = new Worker("User" + (i + 1));
            // 将线程放入池中进行执行
            pool.execute(th);
        }
        // 关闭线程池
        pool.shutdown();
    }
}
