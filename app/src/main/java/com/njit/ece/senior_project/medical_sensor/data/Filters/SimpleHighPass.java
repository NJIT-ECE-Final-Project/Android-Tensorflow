package com.njit.ece.senior_project.medical_sensor.data.Filters;

/**
 * A simple high pass filter with a cuttoff frequency designed to
 * filter gravity out of accelerometer readings.
 */
public class SimpleHighPass {


    private double currVal;
    private double prevData;

    // default alpha to 0.9
    private double ALPHA = 0.9;


    public SimpleHighPass() {

    }

    public SimpleHighPass(double alpha) {
        ALPHA = alpha;
    }


    public double getNextDataPoint(double currData) {

        // high-pass filter recurrence relation
        currVal = ALPHA*(currVal + currData - prevData);

        prevData = currData;

        return currVal;
    }

}