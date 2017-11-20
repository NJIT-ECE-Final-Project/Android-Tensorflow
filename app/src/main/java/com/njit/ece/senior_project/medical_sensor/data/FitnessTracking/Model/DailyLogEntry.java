package com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Model;

import com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Persistance.DailyLogDAO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log the number of steps and time spent walking each day
 */
public class DailyLogEntry {

    private Date date;

    private int numSteps;

    // amount of time windows where the classification was walking
    // (one time window is 20 ms)
    private int timeWalking;

    // private no-arg constructor needed for SnappyDB
    private DailyLogEntry() {

    }

    public DailyLogEntry(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public int getTimeWalking() {
        return timeWalking;
    }

    public void setTimeWalking(int timeWalking) {
        this.timeWalking = timeWalking;
    }

    public void addSteps(int steps) {
        this.numSteps += steps;
    }

    public void addTimeWalking(int timeWalking) {
        this.timeWalking += timeWalking;
    }

    public static String getID(Date date) {
        return getBaseID() + new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String getBaseID() {
        return "DailyLogEntry:";
    }
}
