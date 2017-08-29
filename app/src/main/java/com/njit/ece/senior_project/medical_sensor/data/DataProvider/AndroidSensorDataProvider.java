package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.data.Filters.SimpleHighPass;
import com.njit.ece.senior_project.medical_sensor.data.SignalType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Data provider that provides data taken from android sensors
 */
public class AndroidSensorDataProvider implements RawDataProvider, SensorEventListener {

    private int timeSteps = 128;

    // one high-pass filter per accelerometer axis
    private SimpleHighPass[] highPassFilters = new SimpleHighPass[3];

    private SensorManager sensorManager;


    // maintain of queue because gyro is not garunteed to be provided in sync with accel
    Queue<float[]> totalAccelQueue = new LinkedList<>();
    Queue<float[]> bodyAccelQueue = new LinkedList<>();
    Queue<float[]> gyroQueue = new LinkedList<>();

    private List<RawDataListener> listeners = new ArrayList<>();
    //private List<DataListener> dataListners = new ArrayList<>();

    public AndroidSensorDataProvider(SensorManager sensorManager) {

        this.sensorManager = sensorManager;

        for(int i = 0; i < 3; i++) {
            highPassFilters[i] = new SimpleHighPass();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // acceleromter data

            float[] accel = sensorEvent.values;

            float[] accel_filtered = new float[3];

            for(int i = 0; i < 3; i++) {
                accel[i] = accel[i]/9.81f; //normalize units to gs
                Log.d("Acceleration", accel[i]+"");
                accel_filtered[i] = (float) highPassFilters[i].getNextDataPoint(accel[i]);
            }

            // add data to queue as well
            totalAccelQueue.add(accel);
            bodyAccelQueue.add(accel_filtered);

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            float[] gyro = sensorEvent.values;

            gyroQueue.add(gyro);
        }

        if(totalAccelQueue.size() > 0 && bodyAccelQueue.size() > 0 && gyroQueue.size() > 0) {
            DataEvent newData = new DataEvent(
                    totalAccelQueue.remove(),
                    bodyAccelQueue.remove(),
                    gyroQueue.remove());

            provideRawData(newData);
        }
    }


    private void provideRawData(DataEvent newData) {
        for(RawDataListener listener : listeners) {
            listener.onRawDataChanged(newData);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //TODO what to do?
    }


    @Override
    public void addRawDataListener(RawDataListener listener) {
        listeners.add(listener);
    }

    @Override
    public void pause() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void resume() {

        // register a listener for the accelerometer data
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME); // game provides the desired 20 ms delay for 50Hz

        // register another listener for the gyroscope data
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME); // game provides the desired 20 ms delay for 50Hz

    }

    @Override
    public void destroy() {
        pause();
    }

}
