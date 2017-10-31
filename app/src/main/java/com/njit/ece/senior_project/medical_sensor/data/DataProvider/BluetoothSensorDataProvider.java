package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.provider.ContactsContract;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageListener;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProvider;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProviderImpl;
import com.njit.ece.senior_project.medical_sensor.data.Filters.SimpleHighPass;
import com.njit.ece.senior_project.medical_sensor.data.util.DataHelper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;

/**
 * Provides raw data taken from the Bluetooth module, by parsing each of the messages sent via
 * the bluetooth module
 */
public class BluetoothSensorDataProvider implements BluetoothMessageListener, RawDataProvider, HeartRateProvider {

    private static final int NUM_AXES = 6;

    private BluetoothMessageProvider messageProvider;

    private List<RawDataListener> dataListenerList = new ArrayList<>();
    //List<SensorEventListener> sensorEventListenerList;

    private List<HeartRateListener> heartRateListenerList = new ArrayList<>();

    private SimpleHighPass[] accelHighPass = new SimpleHighPass[3];
    private SimpleHighPass[] gyroHighPass = new SimpleHighPass[3];  // get rid of drift


    public BluetoothSensorDataProvider(BluetoothMessageProvider provider) {
        provider.addBluetoothMessageListener(this);

        for(int i = 0; i < accelHighPass.length; i++) {
            accelHighPass[i] = new SimpleHighPass();
            gyroHighPass[i] = new SimpleHighPass();
        }

        this.messageProvider = provider;
    }


    // TODO: garbage data
    @Override
    public void onMessageChanged(String message) {
        //TODO: process the message
        String messageType = message.split(":")[0];

        Log.d("BluetoothSensor", message);

        if(messageType.equals("S")) {

            String[] splitString = message.split(":")[1].split(",");

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
                        accel[i] = dataValues[i]/9.81f;
                    } else if (i < 6) {
                        gyro[i - 3] = dataValues[i];
                    }
                }

                float[] accelFiltered = new float[3];
                for(int i = 0; i < 3; i++) {
                    accelFiltered[i] = (float) accelHighPass[i].getNextDataPoint(accel[i]);
                    gyro[i] = (float) gyroHighPass[i].getNextDataPoint(gyro[i]);
                }

                //TODO filter
                DataEvent dataEvent = new DataEvent(accel, accelFiltered, gyro);

                for (RawDataListener listener : dataListenerList) {
                    listener.onRawDataChanged(dataEvent);
                }
            }
        } else if(messageType.equals("H")) {

            String heartRateString = message.split(":")[1];

            double hr = Double.parseDouble(heartRateString);

            for(HeartRateListener listener : heartRateListenerList) {
                listener.onHeartRateChanged(hr);
            }

        }
    }

    @Override
    public void onStatusChanged(String status) {
        // do nothing for now
    }

    @Override
    public void addRawDataListener(RawDataListener listener) {
        dataListenerList.add(listener);
    }



    @Override
    public void pause() {
        //TODO implement pause
    }

    @Override
    public void resume() {
        //TODO implement resume
    }

    @Override
    public void destroy() {
        messageProvider.destroy();
    }


    @Override
    public void addHeartRateListener(HeartRateListener heartRateListener) {
        heartRateListenerList.add(heartRateListener);
    }
}
