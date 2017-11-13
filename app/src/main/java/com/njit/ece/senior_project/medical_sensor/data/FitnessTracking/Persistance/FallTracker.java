package com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Persistance;

import android.content.Context;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.data.FallDetector.FallDetector;
import com.njit.ece.senior_project.medical_sensor.data.FallDetector.FallEvent;
import com.njit.ece.senior_project.medical_sensor.data.FallDetector.FallListener;

/**
 * Created by David Etler on 11/13/2017.
 */

public class FallTracker implements FallListener {

    private FallLogEntryDAO fallLogEntryDAO;

    public FallTracker(Context context) {
        fallLogEntryDAO = new FallLogEntryDAO(context);
    }

    @Override
    public void onFallDetected(FallEvent event) {
        // log this fall
        try {

        } catch (Exception e) {
            Log.e("FallTracker", "Unexpected exception", e);

            

        }
    }
}
