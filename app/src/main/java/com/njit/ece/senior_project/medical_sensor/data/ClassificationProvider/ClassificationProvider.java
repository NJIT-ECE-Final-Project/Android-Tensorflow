package com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider;

import com.njit.ece.senior_project.medical_sensor.data.DataProvider.DataListener;

/**
 * Listens for activity classifications from the activity classifier
 */
public interface ClassificationProvider extends DataListener {

    public void addClassificationListener(ClassificationListener listener);

}
