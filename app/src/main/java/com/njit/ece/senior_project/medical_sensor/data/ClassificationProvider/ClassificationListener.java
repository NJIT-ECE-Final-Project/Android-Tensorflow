package com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider;

import com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier;

/**
 * A listener which takes in changes to the activity classification
 */
public interface ClassificationListener {

    void onClassificationChanged(ActivityClassifier.Activity newClassification);

    void onProbabilitiesChanged(float[] probabilities);

}
