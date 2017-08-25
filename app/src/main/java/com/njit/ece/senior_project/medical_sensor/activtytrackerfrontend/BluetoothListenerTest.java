package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend;

import android.hardware.SensorEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageListener;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProvider;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProviderImpl;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.BluetoothSensorDataProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.DataEvent;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.RawDataListener;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothListenerTest extends AppCompatActivity  implements BluetoothMessageListener, RawDataListener {

    BluetoothMessageProvider messageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_listener_test);

        Bluetooth b = new Bluetooth(this);
        b.enableBluetooth();

        messageProvider = new BluetoothMessageProviderImpl(b);
        messageProvider.addBluetoothMessageListener(this);

        BluetoothSensorDataProvider btDataProvider = new BluetoothSensorDataProvider(b);
        btDataProvider.addRawDataListener(this);


        //b.setCommunicationCallback(messageProvider);

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

    @Override
    public void onRawDataChanged(final DataEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateAccelSensorTest(event.getAccel());
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

}
