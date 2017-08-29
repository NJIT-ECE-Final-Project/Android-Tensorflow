package com.njit.ece.senior_project.medical_sensor.data.FallDetector;

import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider.ClassificationListener;
import com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider.ClassificationProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.DataEvent;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.RawDataProvider;
import com.njit.ece.senior_project.medical_sensor.data.util.ArrayHelper;
import com.njit.ece.senior_project.medical_sensor.data.util.DataHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier.*;

/**
 * Uses activity classification data to detect falls
 */
public class ActivityClassificationFallDetector implements FallDetector, ClassificationListener {

    private String TAG = ActivityClassificationFallDetector.class.getSimpleName();

    private List<FallListener> fallListenerList = new ArrayList<>();

    private final int ACCEL_MAG_TIMESTEPS = 50 * 5; // 5s @ 50 samples/s
    // circular buffer to store historical acceleration values
    private double accelMagBuffer[] = new double[ACCEL_MAG_TIMESTEPS];
    private int accelMagIndex = 0;

    // only record falls when there was a 2.0g (~20 m/s^2) impact force
    // otherwise, the user probable just fell down
    private static double IMPACT_THRESHOLD = 2.0;


    private Activity lastClassification = Activity.LAYING;


    public ActivityClassificationFallDetector(ClassificationProvider classificationProvider, RawDataProvider rawDataProvider) {
        classificationProvider.addClassificationListener(this);
        rawDataProvider.addRawDataListener(this);
    }

    @Override
    public void onRawDataChanged(DataEvent event) {
        // buffer accelerometer magnitude
        double currMag = DataHelper.magnitude(event.getBody_accel());
        accelMagBuffer[accelMagIndex++] = currMag;

        if(accelMagIndex >= accelMagBuffer.length) {
            Log.d(TAG, "Accel buffer full, looping back");
            accelMagIndex = 0;
        }

        Log.d(TAG, "Accel max value: " + ArrayHelper.maxValue(accelMagBuffer));
    }

    @Override
    public void onClassificationChanged(Activity newClassification) {

        Log.d(TAG, "Got new classification: " + newClassification);
        Log.d(TAG, "Last classification: " + lastClassification);

        // a fall is detected when the user is now laying, and they were not before
        if(newClassification == Activity.LAYING && lastClassification != Activity.LAYING) {
            //FALL DETECTED!
            Log.d(TAG, "Fall detected!!!");

            // make sure there was a hard impact (otherwise ignore)
            if(ArrayHelper.maxValue(accelMagBuffer) >= IMPACT_THRESHOLD) {

                Log.d(TAG, "Hard fall detected! Impact force:"
                        + ArrayHelper.maxValue(accelMagBuffer));


                FallEvent fall = new FallEvent(new Date(), lastClassification);
                broadcastFall(fall);
            } else {
                Log.d(TAG, "Ignoring fall because impact force ("
                        + ArrayHelper.maxValue(accelMagBuffer)
                        + ") was too low.");
            }

        }

        lastClassification = newClassification;

    }

    private void broadcastFall(FallEvent fall) {

        Log.d(TAG, "Broadcasting fall event to all listeners");

        for(FallListener listener : fallListenerList) {
            listener.onFallDetected(fall);
        }
    }

    @Override
    public void onProbabilitiesChanged(float[] probabilities) {
        //Do nothing with the probs
    }

    @Override
    public void addFallListener(FallListener listener) {

        Log.d(TAG, "Registering fall listener");

        fallListenerList.add(listener);
    }
}
