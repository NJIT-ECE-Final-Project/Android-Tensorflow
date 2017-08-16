package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.data.SampleLoader.SampleDataLoader;
import com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private File signalsFolder;

    private int timeSteps = 128;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onResume() {

        super.onResume();

        try {
            // create a classifier
            ActivityClassifier classifier = new ActivityClassifier(this, "file:///android_asset/frozen_har.pb");

            AssetManager am = getAssets();


            // load sample data
            String sampleDataFile = /*"file:///android_asset/*/"UCI HAR Dataset/test/Inertial Signals";
            SampleDataLoader dataLoader = new SampleDataLoader(this, sampleDataFile);

            // run the classifier for each test set
            //TODO: this while loop terminates with a nullpointerexception
            while (dataLoader.hasNext()) {
                float[][][] dataSet = dataLoader.getNextRow();

                // run the data through the classifier
                ActivityClassifier.Activity activity = classifier.classify(dataSet);

                Log.i("Classification", "The activity is: " + activity + " (" + activity.getNum() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Main", e.getMessage());
        }
    }

}
