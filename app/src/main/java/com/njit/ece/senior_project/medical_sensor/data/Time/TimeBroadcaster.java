package com.njit.ece.senior_project.medical_sensor.data.Time;

/**
 * Broadcasts time to all time listeners
 */
public interface TimeBroadcaster {

    void addTimeListener(TimeListener listener);

}
