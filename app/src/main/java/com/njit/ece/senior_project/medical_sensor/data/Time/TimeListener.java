package com.njit.ece.senior_project.medical_sensor.data.Time;

import java.util.Date;

/**
 * Interface capable of asynchronously receiving time updates
 */
public interface TimeListener {

    void onTimeChanged(Date currTime);

}
