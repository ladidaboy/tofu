package cn.hl.kit.ox.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestFixedThreadPool {
    public static void main(String[] args) {
        // 创建固定大小的线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);
        // new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        for (int i = 0; i < 50; i++) {
            // 创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
            Thread th = new Worker("User" + (i + 1));
            // 将线程放入池中进行执行
            pool.execute(th);
        }
        // 关闭线程池
        pool.shutdown();
    }

}
