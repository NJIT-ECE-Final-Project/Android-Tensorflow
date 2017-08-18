package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

/**
 * Interface implemented by classes which can listen for updates in the data
 */
public interface DataListener {

    void onDataChanged(float[][][] sensorData);

}
