package com.njit.ece.senior_project.medical_sensor.data.FallDetector;

/**
 * Listens for canceled alerts
 */
public interface AlertCancelListener {

    void onOptionSelected(FallNotifier.AlertCancelOption option);

}
