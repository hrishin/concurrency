package me.hriships.concurrency;

/**
 * Created by administrator on 2/8/16.
 */
public class Addition implements Runnable{
    private int limit;
    private Coordinater coordinater;
    int total;

    public Addition(int limit, Coordinater coordinater) {
        this.limit = limit;
        this.total = 0;
        this.coordinater = coordinater;
    }

    @Override
    public void run() {
        for (int i = 0; i <= limit/2; i++) {
            coordinater.canResume(Thread.currentThread().getName());
            total += i;
            System.out.print("[A:" + total + "]");
            coordinater.taskDone();
        }

    }
}
