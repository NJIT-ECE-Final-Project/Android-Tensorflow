package com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider;

import android.content.Context;

import com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Classification provider which uses Tensorflow
 */
public class TensorflowClassificationProvider implements ClassificationProvider {


    private List<ClassificationListener> classificationListenerList;
    private ActivityClassifier classifier;

    public TensorflowClassificationProvider(Context context, String savedModelLocation) {
        classificationListenerList = new ArrayList<>();
        classifier = new ActivityClassifier(context, savedModelLocation);
    }

    @Override
    public void addClassificationListener(ClassificationListener listener) {
        classificationListenerList.add(listener);
    }

    @Override
    public void onDataChanged(float[][][] sensorData) {
        // get the probability vector
        float[] probs = classifier.getProbabilities(sensorData);
        // get the activity classification based on the probability vector
        ActivityClassifier.Activity classification = classifier.classify(probs);

        // update each listener with the new data
        for(ClassificationListener listener : classificationListenerList) {
            // update probability info
            listener.onProbabilitiesChanged(probs);
            // update final classification
            listener.onClassificationChanged(classification);
        }
    }
}
