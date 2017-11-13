package com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Model;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Log the time of each fall
 */
public class FallLogEntry {

    private Date fallTime;

    // private no-arg for SnappyDB
    private FallLogEntry() {

    }

    public FallLogEntry(Date fallTime) {
        this.fallTime = fallTime;
    }

    public Date getFallTime() {
        return fallTime;
    }

    public void setFallTime(Date fallTime) {
        this.fallTime = fallTime;
    }

    public static String getID(Date date) {
        return "FallLogEntry:" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
    }
}
