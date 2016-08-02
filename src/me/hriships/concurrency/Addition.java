package me.hriships.concurrency;

/**
 * Created by administrator on 2/8/16.
 */
public class Addition implements Runnable{
    private int limit;
    private Captain captain;
    private int total;

    public Addition(int limit, Captain captain) {
        this.limit = limit;
        this.total = 0;
        this.captain = captain;
    }

    @Override
    public void run() {
        for (int i = 0; i <  limit; i++) {
            captain.canI("t3");
            total += i;
            System.out.print("[A:" + total + "]");
            captain.imDone();
        }
    }
}
