package com.njit.ece.senior_project.medical_sensor.data.Filters;

/**
 * Smooths data sent to it by averaging over an interval and returning the average once
 * the interval is over
 *
 * Also performs a high pass on the data
 */
public class SimpleHighPass {


    private double currVal;
    private double prevData;


    private final double ALPHA = 0.9;


    public SimpleHighPass() {

    }


    public double getNextDataPoint(double currData) {

        // high-pass filter recurrence relation
        currVal = ALPHA*(currVal + currData - prevData);

        prevData = currData;

        return currVal;
    }


}