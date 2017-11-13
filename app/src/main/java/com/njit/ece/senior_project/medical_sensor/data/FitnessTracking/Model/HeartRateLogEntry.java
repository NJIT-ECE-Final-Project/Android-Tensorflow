package com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Model;

/**
 * Log every heart rate entry
 */
public class HeartRateLogEntry {

    private long entryTime;

    private int bpm;

    public HeartRateLogEntry(long entryTime, int bpm) {
        this.entryTime = entryTime;
        this.bpm = bpm;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }
}
