package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

/**
 * Provides raw data, without buffering
 */
public interface RawDataProvider {

    void addRawDataListener(RawDataListener listener);

    void pause();

    void resume();

    void destroy();
}
