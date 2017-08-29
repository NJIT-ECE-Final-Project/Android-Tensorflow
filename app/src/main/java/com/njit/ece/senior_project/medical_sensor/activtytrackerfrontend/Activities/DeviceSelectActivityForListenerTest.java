package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.Activities;

import android.content.Intent;

/**
 * Created by Omar on 16/07/2015.
 */
public class DeviceSelectActivityForListenerTest extends DeviceSelectActivity {

    private boolean registered=false;


    protected void onDeviceSelected(int pos) {
        Intent i = new Intent(DeviceSelectActivityForListenerTest.this, BluetoothListenerTestActivity.class);
        i.putExtra("pos", pos);
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
        startActivity(i);
        finish();
    }
}