package com.njit.ece.senior_project.medical_sensor.data.Time;

import android.util.Log;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronously sends the current time as an update
 */
public class BigBen implements TimeBroadcaster {

    private static final String prefix = "T:";

    private static final long delay = 1 * 1000;

    private List<TimeListener> timeListeners = new ArrayList<>();


    public BigBen() {
    }

    public void start() {

        Log.d("TimeBroadcast", "Starting new thread to broadcast time");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {

                    Log.d("TimeBroadcast", "Getting current time for broadcast");

                    Date currTime = new Date();
                    broadcastTime(currTime);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private void broadcastTime(Date time) {

        Log.d("TimeBroadcast", "Current time is: " + time);

        for(TimeListener listener : timeListeners) {
            listener.onTimeChanged(time);
        }
    }

    @Override
    public void addTimeListener(TimeListener listener) {
        timeListeners.add(listener);
    }
}
