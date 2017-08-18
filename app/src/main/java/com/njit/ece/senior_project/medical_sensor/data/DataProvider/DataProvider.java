package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

import android.hardware.SensorEventListener;

/**
 * Created by hp- on 8/17/2017.
 */
public interface DataProvider {


    public void addDataListener(DataListener listener);

    public void addSensorEventListener(SensorEventListener listener);

}
