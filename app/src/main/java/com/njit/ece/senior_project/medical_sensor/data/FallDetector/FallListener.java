package com.njit.ece.senior_project.medical_sensor.data.FallDetector;

/**
 * Created by David Etler on 8/29/2017.
 */

public interface FallListener {

    void onFallDetected(FallEvent event);

}
