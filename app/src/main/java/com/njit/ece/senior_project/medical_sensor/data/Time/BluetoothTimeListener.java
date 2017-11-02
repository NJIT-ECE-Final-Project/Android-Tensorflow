package com.njit.ece.senior_project.medical_sensor.data.Time;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.aflak.bluetooth.Bluetooth;

/**
 * Sends time over bluetooth
 */
public class BluetoothTimeListener implements TimeListener {

    private Bluetooth bluetooth;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");

    public BluetoothTimeListener(Bluetooth bluetooth) {
        this.bluetooth = bluetooth;
    }

    @Override
    public void onTimeChanged(Date currTime) {
        String theTime = dateFormat.format(currTime);

        try {
            Log.d("TimeProvider", "Sending current time to device");
            bluetooth.send("T:" + theTime + ".");
        } catch(Exception e) {
            Log.w("TimeProvider", "Could not send time update to device due to an error", e);
        }
    }
}
