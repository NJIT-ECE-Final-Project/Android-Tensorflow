package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import me.aflak.bluetooth.Bluetooth;

/**
 * Created by David Etler on 8/25/2017.
 */

public class BluetoothSensorDataProvider implements Bluetooth.CommunicationCallback {

    private Bluetooth bluetooth;

    public BluetoothSensorDataProvider(Bluetooth bluetooth) {
        this.bluetooth = bluetooth;
    }

    private static final String TAG = BluetoothSensorDataProvider.class.getSimpleName();

    @Override
    public void onConnect(BluetoothDevice device) {
        Log.i(TAG, "Device connected: " + device.getName());
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        Log.i(TAG, "Device disconnected: " + device.getName() + "\nMessage: " + message);
        Log.i(TAG, "Attempting to reconnect");
        bluetooth.connectToDevice(device);
    }

    @Override
    public void onMessage(String message) {
        Log.i(TAG, "Device disconnected: " + message);
        //TODO parse method and return data to all listeners
    }

    @Override
    public void onError(String message) {
        Log.w(TAG, "Error with bluetooth: " + message);
    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {
        Log.e(TAG, "Connection error: " + message + " for device " + device.getName());

        // TODO: reconnect
    }

}
