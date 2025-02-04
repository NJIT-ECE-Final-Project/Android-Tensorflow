package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.Activities;

import android.hardware.SensorEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.R;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageListener;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProvider;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProviderImpl;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.BluetoothSensorDataProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.DataEvent;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.RawDataListener;
import com.njit.ece.senior_project.medical_sensor.data.Time.BigBen;
import com.njit.ece.senior_project.medical_sensor.data.Time.BluetoothTimeListener;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothListenerTestActivity extends AppCompatActivity  implements BluetoothMessageListener, RawDataListener {

    BluetoothMessageProvider messageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_listener_test);

        Bluetooth b = new Bluetooth(this);
        b.enableBluetooth();

        // setup time

        // setup callbacks to get asynchronous updates on connection status and sensor data
        messageProvider = new BluetoothMessageProviderImpl(b);
        messageProvider.addBluetoothMessageListener(this);
        BluetoothSensorDataProvider btDataProvider = new BluetoothSensorDataProvider(messageProvider);
        btDataProvider.addRawDataListener(this);

        //b.setCommunicationCallback(messageProvider);

        // get the paired item
        int pos = getIntent().getExtras().getInt("pos");
        String name = b.getPairedDevices().get(pos).getName();
        // connect to device
        b.connectToDevice(b.getPairedDevices().get(pos));

        Log.d("TimeBroadcast", "Setting up time broadcast");
        BluetoothTimeListener bluetoothTimeListener = new BluetoothTimeListener(b);
        BigBen bigBen = new BigBen();
        bigBen.addTimeListener(bluetoothTimeListener);
        bigBen.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageProvider.destroy();
    }

    @Override
    public void onMessageChanged(final String message) {
        // do nothing
    }

    @Override
    public void onStatusChanged(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateStatusText(status);
            }
        });
    }

    @Override
    public void onRawDataChanged(final DataEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateAccelSensorTest(event.getTotal_accel());
                updateGyroSensorTest(event.getGyro());
            }
        });
    }

    private void updateGyroSensorTest(float[] gyro) {
        ((TextView)this.findViewById(R.id.gyro_x)).setText(Float.toString(gyro[0]));
        ((TextView)this.findViewById(R.id.gyro_y)).setText(Float.toString(gyro[1]));
        ((TextView)this.findViewById(R.id.gyro_z)).setText(Float.toString(gyro[2]));
    }

    private void updateAccelSensorTest(float[] accel) {
        ((TextView)this.findViewById(R.id.total_acc_x)).setText(Float.toString(accel[0]));
        ((TextView)this.findViewById(R.id.total_acc_y)).setText(Float.toString(accel[1]));
        ((TextView)this.findViewById(R.id.total_acc_z)).setText(Float.toString(accel[2]));
    }

    private void updateStatusText(String status) {
        ((TextView) findViewById(R.id.bluetooth_status)).setText(status);
    }

}
