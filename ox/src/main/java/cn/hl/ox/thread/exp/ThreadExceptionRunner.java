package cn.hl.ox.thread.exp;

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
        throw new RuntimeException("ThreadException-Runner-Exception");
    }
}
