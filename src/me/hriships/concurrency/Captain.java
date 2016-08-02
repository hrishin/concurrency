package me.hriships.concurrency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by administrator on 2/8/16.
 */
public class Captain {

    private List<String> schedule;
    private int index;

    public Captain(String... data) {
        schedule = new LinkedList<String>(Arrays.asList(data));
        index = 0;
    }

    public synchronized void imDone() {
        index = (index == schedule.size()-1) ? 0 : index+1;
    }

    public synchronized void canI(String name) {
        while(schedule.get(index) != name) {
            try {
                wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
