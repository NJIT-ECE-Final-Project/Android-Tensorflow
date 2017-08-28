package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.pulltorefresh.PullToRefresh;

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