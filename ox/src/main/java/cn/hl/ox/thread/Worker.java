package cn.hl.ox.thread;

public class Worker extends Thread {
    private String user;

    public Worker(String user) {
        this.user = user;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " > " + user);
    }
}
