package me.hriships.concurrency;

import com.sun.xml.internal.bind.v2.runtime.Coordinator;

import java.io.IOException;

public class Main {

    public static final int LIMIT = 3;

    public static void main(String args[]) throws InterruptedException, IOException {
        Coordinater coordinator = new SimpleCoordinator();

        Thread evenPrinter = new Thread(new EvenNumbers(LIMIT, coordinator));
        Thread oddPrinter = new Thread(new OddNumbers(LIMIT, coordinator));
        Thread additionPrinter = new Thread(new Addition(LIMIT, coordinator));

        coordinator.register(evenPrinter);
        coordinator.register(oddPrinter);
        coordinator.register(additionPrinter);

        evenPrinter.start();
        oddPrinter.start();
        additionPrinter.start();

        evenPrinter.join();
        oddPrinter.join();
        additionPrinter.join();

        System.out.println("\nMain task done");

    }
}
