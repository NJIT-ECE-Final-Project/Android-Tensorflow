package com.njit.ece.senior_project.medical_sensor.data.DataProvider;

/**
 * Created by David Etler on 8/25/2017.
 */

public class DataEvent {

    private float[] total_accel;
    private float[] body_accel;
    private float[] gyro;

    public DataEvent(float[] total_accel, float[] body_accel, float[] gyro) {
        this.total_accel = total_accel;
        this.body_accel = body_accel;
        this.gyro = gyro;
    }

    public float[] getTotal_accel() {
        return total_accel;
    }

    public float[] getBody_accel() {
        return body_accel;
    }

    public float[] getGyro() {
        return gyro;
    }


}
