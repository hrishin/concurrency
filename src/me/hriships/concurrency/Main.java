package me.hriships.concurrency;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String args[]) throws InterruptedException {
        Captain captain = new Captain("t1", "t2", "t3");
        Thread t1 = new Thread(new EvenNumbers(100, captain));
        Thread t2 = new Thread(new OddNumbers(100, captain));
        Thread t3 = new Thread(new Addition(100, captain));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("end : " + t3.isAlive());
    }
}
