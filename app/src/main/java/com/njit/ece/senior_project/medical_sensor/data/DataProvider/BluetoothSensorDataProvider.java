package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.provider.ContactsContract;

import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageListener;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProvider;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProviderImpl;
import com.njit.ece.senior_project.medical_sensor.data.util.DataHelper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;

/**
 * Created by David Etler on 8/25/2017.
 */

public class BluetoothSensorDataProvider implements BluetoothMessageListener, RawDataProvider {

    private static final int NUM_AXES = 6;

    private BluetoothMessageProvider messageProvider;

    List<RawDataListener> dataListenerList = new ArrayList<>();
    //List<SensorEventListener> sensorEventListenerList;

    public BluetoothSensorDataProvider(Bluetooth bluetooth) {
        messageProvider = new BluetoothMessageProviderImpl(bluetooth);
        messageProvider.addBluetoothMessageListener(this);
        bluetooth.setCommunicationCallback(messageProvider);
    }


    /**
     * Optional override to use a different message provider, just for fun
     * @param provider
     */
    public void setMessageProvider(BluetoothMessageProvider provider) {
        this.messageProvider = provider;
        provider.addBluetoothMessageListener(this);
    }

    @Override
    public void onMessageChanged(String message) {
        //TODO: process the message

        String[] splitString = message.split(",");

        if (splitString.length != NUM_AXES) {
            // TODO: how to handle messages other than data messages?
        } else {
            // this matches the format for the serial data
            float dataValues[] = new float[NUM_AXES];

            for (int i = 0; i < NUM_AXES; i++) {

                //convert the hex to float
                byte[] rawData = DataHelper.hexStringToByteArray(splitString[i]);
                float myfloatvalue = ByteBuffer.wrap(rawData).getFloat();
                dataValues[i] = myfloatvalue;
            }

            // create message that sensor data was updated
            float[] accel = new float[3];
            float[] gyro = new float[3];
            for (int i = 0; i < NUM_AXES; i++) {
                if (i < 3) {
                    accel[i] = dataValues[i];
                } else if (i < 6) {
                    gyro[i - 3] = dataValues[i];
                }
            }

            DataEvent dataEvent = new DataEvent(accel, gyro);

            for(RawDataListener listener : dataListenerList) {
                listener.onRawDataChanged(dataEvent);
            }
        }
    }

    @Override
    public void addRawDataListener(RawDataListener listener) {
        dataListenerList.add(listener);
    }
}
