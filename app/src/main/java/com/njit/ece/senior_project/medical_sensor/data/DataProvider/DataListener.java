package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

/**
 * Interface implemented by classes which can listen for updates in the data
 *
 * Recieves buffered data once every batch (currently 128 time steps)
 */
public interface DataListener {

    void onDataChanged(float[][][] sensorData);

}
