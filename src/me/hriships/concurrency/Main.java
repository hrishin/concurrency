package me.hriships.concurrency;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String args[]) {
        Captain captain = new Captain("t1", "t2");
        Thread t1 = new Thread(new EvenNumbers(10, captain));
        Thread t2 = new Thread(new OddNumbers(10, captain));

        t1.start();
        t2.start();
    }
}
