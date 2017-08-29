package com.njit.ece.senior_project.medical_sensor.data;

/**
 * The 9 signal types used by the tensorflow model
 */
public enum SignalType {
    // high-pass filtered accelerometer data
    body_acc_x,
    body_acc_y,
    body_acc_z,
    // raw gyroscope data
    body_gyro_x,
    body_gyro_y,
    body_gyro_z,
    // raw accelerometer data
    total_acc_x,
    total_acc_y,
    total_acc_z
}
