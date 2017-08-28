package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

import android.hardware.SensorEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Buffers 128 time steps of data to provide to the classifier in one batch
 */
public class BufferedDataProvider implements RawDataListener, DataProvider {

    float[][][] dataBuffer;
    private int timeSteps = 128;

    // incremented until timesteps is reached
    int t = 0;

    private List<DataListener> dataListeners = new ArrayList<>();

    public BufferedDataProvider(RawDataProvider dataProvider) {
        dataProvider.addRawDataListener(this);

        dataBuffer = new float[1][timeSteps][9];
    }

    @Override
    public void onRawDataChanged(DataEvent event) {


        if(t == timeSteps) {
            provideData();
            t = 0;
        }

        for(int i = 0; i < 9; i++) {
            if(i < 3) {
                dataBuffer[0][t][i] = event.getBody_accel()[i];
            } else if(i < 6) {
                dataBuffer[0][t][i] = event.getGyro()[i - 3];
            } else if(i < 9) {
                dataBuffer[0][t][i] = event.getTotal_accel()[i - 6];
            }
        }

        t++;

    }

    private void provideData() {
        for(DataListener listener : dataListeners) {
            listener.onDataChanged(dataBuffer);
        }
    }


    @Override
    public void addDataListener(DataListener listener) {
        dataListeners.add(listener);
    }

    @Override
    public void addSensorEventListener(SensorEventListener listener) {
        //Do nothing
    }

}
