package me.hriships.concurrency;

/**
 * Created by administrator on 2/8/16.
 */
public class OddNumbers implements Runnable {

    private int limit;
    private Scheduler captain;

    public OddNumbers(int limit, Scheduler captain) {
        this.limit = limit;
        this.captain = captain;
        captain.register(this);
    }

    @Override
    public void run() {
        for(int i =1; i < limit; i=i+2) {
            captain.canI(this);
            System.out.print("[" + i + "]");
            captain.imDone();
        }
    }
}
