package com.njit.ece.senior_project.medical_sensor.data.FallDetector;

import com.njit.ece.senior_project.medical_sensor.data.DataProvider.RawDataListener;

/**
 * Common interface for all fall detection algorithms
 */
public interface FallDetector extends RawDataListener {

    void addFallListener(FallListener listener);

}
