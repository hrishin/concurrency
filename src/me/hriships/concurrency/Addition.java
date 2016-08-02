package me.hriships.concurrency;

/**
 * Created by administrator on 2/8/16.
 */
public class Addition implements Runnable{
    private int limit;
    private Scheduler captain;
    private int total;

    public Addition(int limit, Scheduler captain) {
        this.limit = limit;
        this.total = 0;
        this.captain = captain;
        captain.register(this);
    }

    @Override
    public void run() {
        for (int i = 0; i <  limit; i++) {
            captain.canI(this);
            total += i;
            System.out.print("[A:" + total + "]");
            captain.imDone();
        }
    }
}
