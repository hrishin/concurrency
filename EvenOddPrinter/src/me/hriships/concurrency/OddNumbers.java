package me.hriships.concurrency;

/**
 * Created by administrator on 2/8/16.
 */
public class OddNumbers implements Runnable {

    private int limit;
    private Coordinater coordinater;

    public OddNumbers(int limit, Coordinater coordinater) {
        this.limit = limit;
        this.coordinater = coordinater;
    }

    @Override
    public void run() {
        for(int i =1; i <= limit; i=i+2) {
            coordinater.canResume(Thread.currentThread().getName());
            System.out.print("[" + i + "]");
            coordinater.taskDone();
        }
    }
}
