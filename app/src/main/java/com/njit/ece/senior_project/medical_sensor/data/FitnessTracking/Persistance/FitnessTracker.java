package com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Persistance;

import android.content.Context;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider.ClassificationListener;
import com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Model.DailyLogEntry;
import com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier;

import java.util.Date;

import static com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier.Activity.WALKING;
import static com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier.Activity.WALKING_DOWNSTAIRS;
import static com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier.Activity.WALKING_UPSTAIRS;

/**
 * A class which listens for activity tracking updates and logs the entry
 */
public class FitnessTracker implements ClassificationListener {

    DailyLogDAO dailyLogDAO;

    public FitnessTracker(Context context) {
        dailyLogDAO = new DailyLogDAO(context);
    }

    @Override
    public void onClassificationChanged(ActivityClassifier.Activity newClassification) {
        Date currDate = new Date();

        if(newClassification == WALKING
                || newClassification == WALKING_DOWNSTAIRS
                || newClassification == WALKING_UPSTAIRS) {

            // update the number of walking timesteps
            try {
                DailyLogEntry currEntry = dailyLogDAO.getLogEntry(currDate);
                currEntry.addTimeWalking(1);
                dailyLogDAO.updateLogEntry(currEntry);
                Log.d("FitnessTracker", "User has now walked for " + currEntry.getTimeWalking() + " time steps");
            } catch (Exception e) {
                Log.e("FitnessTracker", "Unexpected exception", e);
            }
        }
    }

    @Override
    public void onProbabilitiesChanged(float[] probabilities) {
        // ignore this
    }
}
