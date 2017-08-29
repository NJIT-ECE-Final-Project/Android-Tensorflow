package com.njit.ece.senior_project.medical_sensor.data.FallDetector;

import com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier;

import java.util.Date;

/**
 * A bean which encapsulates the detection of a fall
 */
public class FallEvent {


    private Date fallTime;

    private ActivityClassifier.Activity previousActivity;

    public FallEvent(Date fallTime, ActivityClassifier.Activity previousActivity) {
        this.fallTime = fallTime;
        this.previousActivity = previousActivity;
    }

    public Date getFallTime() {
        return fallTime;
    }

    public ActivityClassifier.Activity getPreviousActivity() {
        return previousActivity;
    }
}
