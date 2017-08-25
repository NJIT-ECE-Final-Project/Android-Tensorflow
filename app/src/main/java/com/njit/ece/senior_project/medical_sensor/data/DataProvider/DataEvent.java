package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

/**
 * Created by David Etler on 8/25/2017.
 */

public class DataEvent {

    private float[] accel;
    private float[] gyro;

    public DataEvent(float[] accel, float[] gyro) {
        this.accel = accel;
        this.gyro = gyro;
    }

    public float[] getAccel() {
        return accel;
    }

    public float[] getGyro() {
        return gyro;
    }


}
