package com.njit.ece.senior_project.medical_sensor.tensorflow;

/**
 * A listener which takes in changes to the activity classification
 */
public interface ClassificationListener {

    void onClassificationChanged(ActivityClassifier.Activity newClassification);

}
