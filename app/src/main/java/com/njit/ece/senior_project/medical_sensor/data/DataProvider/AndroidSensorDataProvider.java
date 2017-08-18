package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.data.Filters.SimpleHighPass;
import com.njit.ece.senior_project.medical_sensor.data.SignalType;

import java.util.ArrayList;
import java.util.List;

/**
 * Data provider that provides data taken from android sensors
 */
public class AndroidSensorDataProvider implements DataProvider, SensorEventListener {

    private int timeSteps = 128;

    // one high-pass filter per accelerometer axis
    private SimpleHighPass[] highPassFilters = new SimpleHighPass[3];

    // incremented until timesteps is reached
    int accel_t = 0;
    int gyro_t = 0;
    float[][][] dataBuffer;


    private List<DataListener> dataListenerList;
    private List<SensorEventListener> eventListenerList;

    public AndroidSensorDataProvider() {

        dataListenerList = new ArrayList<>();
        eventListenerList = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            highPassFilters[i] = new SimpleHighPass();
        }

        dataBuffer = new float[1][timeSteps][SignalType.values().length];
    }

    @Override
    public void addDataListener(DataListener listener) {
        dataListenerList.add(listener);
    }

    @Override
    public void addSensorEventListener(SensorEventListener listener) {
        eventListenerList.add(listener);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Log.d("Timestep", "accel_t: " + accel_t + " gyro_t: " + gyro_t);

        if(accel_t == timeSteps && gyro_t == timeSteps) {
            provideData();
            accel_t = 0;
            gyro_t = 0;
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // acceleromter data

            float[] accel = sensorEvent.values;

            float[] accel_filtered = new float[3];

            for(int i = 0; i < 3; i++) {
                accel[i] = accel[i]/9.81f; //normalize units to gs
                Log.d("Acceleration", accel[i]+"");
                accel_filtered[i] = (float) highPassFilters[i].getNextDataPoint(accel[i]);
            }

            if(accel_t < timeSteps) {
                for(int i = 0; i < 3; i++) {
                    dataBuffer[0][accel_t][i] = accel_filtered[i];
                    dataBuffer[0][accel_t][i + 6] = accel[i];
                }
                accel_t++;
            }

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            float[] gyro = sensorEvent.values;

            if(gyro_t < timeSteps) {
                for(int i = 0; i < 3; i++) {
                    dataBuffer[0][gyro_t][i + 3] = gyro[i];
                }
                gyro_t++;
            }
        }

        // update all child listeners
        for(SensorEventListener listener : eventListenerList) {
            listener.onSensorChanged(sensorEvent);
        }
    }

    private void provideData() {

        Log.d("DataProvider", "Providing data");

        for(DataListener dataListener : dataListenerList) {
            dataListener.onDataChanged(dataBuffer);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // update all child listeners
        for(SensorEventListener listener : eventListenerList) {
            listener.onAccuracyChanged(sensor, i);
        }
    }
}
