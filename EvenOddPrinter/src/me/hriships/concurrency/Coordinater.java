package me.hriships.concurrency;

/**
 * Created by administrator on 5/8/16.
 */
public interface Coordinater {
    void canResume(String taskName);
    void taskDone();
    void register(Thread task);
}
