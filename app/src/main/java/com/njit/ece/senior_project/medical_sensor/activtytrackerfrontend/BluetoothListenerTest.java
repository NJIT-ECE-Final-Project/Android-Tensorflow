package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageListener;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProvider;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProviderImpl;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothListenerTest extends AppCompatActivity  implements BluetoothMessageListener {

    BluetoothMessageProvider messageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_listener_test);

        Bluetooth b = new Bluetooth(this);
        b.enableBluetooth();

        messageProvider = new BluetoothMessageProviderImpl(b);
        messageProvider.addBluetoothMessageListener(this);

        b.setCommunicationCallback(messageProvider);

        // get the paired item
        int pos = getIntent().getExtras().getInt("pos");
        String name = b.getPairedDevices().get(pos).getName();

        b.connectToDevice(b.getPairedDevices().get(pos));

    }

    @Override
    public void onMessageChanged(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.bluetooth_message)).setText(message);
            }
        });
    }
}
