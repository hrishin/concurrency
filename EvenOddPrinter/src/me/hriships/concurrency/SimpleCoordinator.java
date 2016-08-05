package me.hriships.concurrency;

import java.util.*;

/**
 * Created by administrator on 2/8/16.
 */

public class SimpleCoordinator implements Coordinater {
    private Queue<Thread> schedule;
    private Thread currentTask;

    public SimpleCoordinator() {
        schedule = new LinkedList<Thread>();
    }

    @Override
    public synchronized void canResume(String taskName) {
        while(!currentTask.getName().equalsIgnoreCase(taskName)) {
            holdCurrentThread();
        }
    }

    void holdCurrentThread() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void taskDone() {
        scheduleNextTask();
        notifyAll();
    }

    private void scheduleNextTask() {
        enqueueCurrentTask();
        pickNextTask();
    }

    private void enqueueCurrentTask() {
        if(currentTask.isAlive()) {
            schedule.add(currentTask);
        }
    }

    private void pickNextTask() {
        currentTask = schedule.remove();
    }

    public synchronized void register(Thread task) {
        schedule.add(task);
        if(currentTask == null)
            pickNextTask();
    }

}
