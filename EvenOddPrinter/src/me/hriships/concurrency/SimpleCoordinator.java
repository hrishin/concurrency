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
    public void canResume(String taskName) {
        synchronized (this) {
            while (!currentTask.getName().equalsIgnoreCase(taskName)) {
                holdCurrentThread();
            }
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
    public void taskDone() {
        synchronized (this) {
            scheduleNextTask();
            notifyAll();
        }
    }

    private void scheduleNextTask() {
        enqueueCurrentTask();
        pickNextTask();
    }

    private void enqueueCurrentTask() {
        if (currentTask.isAlive()) {
            schedule.add(currentTask);
        }
    }

    private void pickNextTask() {
        currentTask = schedule.poll();
    }

    public void register(Thread task) {
        synchronized (this) {
            schedule.add(task);
            if (currentTask == null)
                pickNextTask();
        }
    }

}
