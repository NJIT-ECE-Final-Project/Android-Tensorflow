package com.njit.ece.senior_project.medical_sensor.data.util;

/**
 * Array helper with useful functions for doing math with arrays
 */
public class ArrayHelper {

    public static float[] flatten(float[][][] array) {

        int size = array.length * array[0].length * array[0][0].length;

        float[] out = new float[size];

        int t = 0;
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[0].length; j++) {
                for(int k = 0; k < array[0][0].length; k++) {
                  out[t] = array[i][j][k];
                  t++;
                }
            }
        }

        return out;
    }


    public static int indexOfMax(float[] array) {
        int maxIndex = 0;
        for(int i = 0; i < array.length; i++) {
            if(array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    public static double maxValue(double[] array) {
        double maxVal = Double.MIN_VALUE;

        for(int i = 0; i < array.length; i++) {
            if(array[i] > maxVal) {
                maxVal = array[i];
            }
        }

        return maxVal;
    }


}
