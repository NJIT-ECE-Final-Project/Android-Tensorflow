package com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;

/**
 * Created by David Etler on 8/25/2017.
 */

public class BluetoothMessageProviderImpl implements BluetoothMessageProvider {

    private Bluetooth bluetooth;

    List<BluetoothMessageListener> listenerList = new ArrayList<>();

    public BluetoothMessageProviderImpl(Bluetooth bluetooth) {
        Log.d(TAG, "Creating new message listener for: " + bluetooth.getDevice().getName());
        this.bluetooth = bluetooth;
        bluetooth.setCommunicationCallback(this);
    }

    private static final String TAG = BluetoothMessageProviderImpl.class.getSimpleName();

    @Override
    public void onConnect(BluetoothDevice device) {
        Log.i(TAG, "Device connected: " + device.getName());

        updateStatus("Connected");
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        Log.i(TAG, "Device disconnected: " + device.getName() + "\nMessage: " + message);
        Log.i(TAG, "Attempting to reconnect");
        bluetooth.connectToDevice(device);

        updateStatus("Disconnected");
    }

    @Override
    public void onMessage(String message) {
        Log.i(TAG, "Got message: " + message);

        for(BluetoothMessageListener listener : listenerList) {
            listener.onMessageChanged(message);
        }
    }

    @Override
    public void onError(String message) {
        Log.w(TAG, "Error with bluetooth: " + message);

        updateStatus("Error");
    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {
        Log.e(TAG, "Connection error: " + message + " for device " + device.getName());

        updateStatus("ConnectError");

        // TODO: reconnect
    }

    @Override
    public void addBluetoothMessageListener(BluetoothMessageListener listener) {
        this.listenerList.add(listener);
    }

    private void updateStatus(String status) {
        for(BluetoothMessageListener listener : listenerList) {
            listener.onStatusChanged(status);
        }
    }
}
