package cn.hl.ox.thread.exp;

import java.util.Random;

/**
 * @author hyman
 * @date 2019-11-15 17:39:15
 */
public class ThreadExceptionRunner implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            //
        }
        throw new RuntimeException("ThreadException-Runner-Exception#" + (1000 + new Random().nextInt(9000)));
    }
}
