package com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider;

/**
 * Listens for messages from the bluetooth module
 */

public interface BluetoothMessageListener {

    void onMessageChanged(String message);

    void onStatusChanged(String status);

}
