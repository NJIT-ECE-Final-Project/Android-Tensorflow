package com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider;

import com.njit.ece.senior_project.medical_sensor.data.DataProvider.DataListener;

/**
 * Created by hp- on 8/17/2017.
 */
public interface ClassificationProvider extends DataListener {

    public void addClassificationListener(ClassificationListener listener);

}
