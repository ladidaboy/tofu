package cn.hl.ox.thread;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestScheduledThreadPoolExecutor {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        // 每隔一段时间就触发
        exec.scheduleAtFixedRate(() -> System.out.println(new Date() + " [INTERVAL]"), 1000, 5000, TimeUnit.MILLISECONDS);
        // 每隔一段时间打印系统时间，证明两者是互不影响的
        exec.scheduleAtFixedRate(() -> System.out.println(new Date() + " [RECORDER] - " + System.nanoTime()), 1000, 2000, TimeUnit.MILLISECONDS);

        // exec.shutdown();
    }

}
