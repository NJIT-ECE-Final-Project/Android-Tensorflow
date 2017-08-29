package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

/**
 * Listens for raw data, which is provided immediately as it is recieved (no buffer)
 */
public interface RawDataListener {

    void onRawDataChanged(DataEvent event);

}
