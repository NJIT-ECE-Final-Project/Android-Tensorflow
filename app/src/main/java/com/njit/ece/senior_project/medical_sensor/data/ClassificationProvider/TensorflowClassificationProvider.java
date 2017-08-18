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
        ActivityClassifier.Activity classification = classifier.classify(sensorData);

        for(ClassificationListener listener : classificationListenerList) {
            listener.onClassificationChanged(classification);
        }
    }
}
