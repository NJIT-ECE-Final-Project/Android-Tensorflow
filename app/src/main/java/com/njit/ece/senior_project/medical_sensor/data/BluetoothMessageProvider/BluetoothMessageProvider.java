package com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider;

import me.aflak.bluetooth.Bluetooth;

/**
 * Created by David Etler on 8/25/2017.
 */

public interface BluetoothMessageProvider extends Bluetooth.CommunicationCallback {

    void addBluetoothMessageListener(BluetoothMessageListener listener);

    void destroy();
}
