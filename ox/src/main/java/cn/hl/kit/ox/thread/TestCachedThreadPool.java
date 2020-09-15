package cn.hl.kit.ox.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCachedThreadPool {
    public static void main(String[] args) {
        // 创建一个可缓存的线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        // new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
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