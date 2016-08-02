package me.hriships.concurrency;

public class Main {
    public static void main(String args[]) throws InterruptedException {
        Scheduler captain = new Scheduler();

        EvenNumbers evenPrinter = new EvenNumbers(100, captain);
        OddNumbers oddPrinter = new OddNumbers(100, captain);
        Addition addPrinter = new Addition(100, captain);

        Thread t1 = new Thread(evenPrinter);
        Thread t2 = new Thread(oddPrinter);
        Thread t3 = new Thread(addPrinter);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("end : " + t3.isAlive());
    }
}
