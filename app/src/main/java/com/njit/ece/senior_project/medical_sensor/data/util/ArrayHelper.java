package com.njit.ece.senior_project.medical_sensor.data.util;

/**
 * Created by hp- on 8/6/2017.
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
}
