package me.hriships.concurrency;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by administrator on 2/8/16.
 */
public class Captain {

    private List<Runnable> schedule;
    private int index;
    private Runnable currentTask;

    public Captain() {
        schedule = new LinkedList<Runnable>();
        index = 0;
    }

    public synchronized void register(Runnable task) {
        schedule.add(task);
        if(currentTask == null)
            currentTask = task;
    }

    public synchronized void imDone() {
        index = (index == schedule.size()-1) ? 0 : index+1;
        currentTask = schedule.get(index);
        notifyAll();
    }

    public synchronized void canI(Runnable task) {
        while(currentTask != task) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
