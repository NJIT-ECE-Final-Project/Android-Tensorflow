package com.njit.ece.senior_project.medical_sensor.data.util;

/**
 * A utility class with helpful functions for converting between data types
 */
public class DataHelper {

    public static byte[] hexStringToByteArray(String s) {
        try {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i + 1), 16));
            }
            return data;
        } catch (Exception e) {
            //TODO: why does this happen???
            return new byte[]{0,0,0,0,0,0};
        }
    }


    public static double magnitude(float[] v) {
        int norm = 0;
        for(int i = 0; i < v.length; i++) {
            norm += v[i] * v[i];
        }
        return Math.sqrt(norm);
    }

}
