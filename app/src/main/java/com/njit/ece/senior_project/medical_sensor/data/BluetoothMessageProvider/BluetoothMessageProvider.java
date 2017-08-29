package com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider;

import me.aflak.bluetooth.Bluetooth;

/**
 * Provides buffered messages from the Bluetooth module
 */

public interface BluetoothMessageProvider extends Bluetooth.CommunicationCallback {

    void addBluetoothMessageListener(BluetoothMessageListener listener);

    void destroy();
}
