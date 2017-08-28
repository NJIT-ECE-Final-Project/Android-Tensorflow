package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend;

import android.content.Intent;

/**
 * Created by David Etler on 8/28/2017.
 */

public class DeviceSelectActivityForActivityClassifier extends DeviceSelectActivity {

    private boolean registered=false;


    protected void onDeviceSelected(int pos) {

        //TODO create a bluetooth data provider and pass in via the bundle

        Intent i = new Intent(DeviceSelectActivityForActivityClassifier.this, ActivityClassifierActivity.class);
        i.putExtra("pos", pos);
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
        startActivity(i);
        finish();
    }

}
