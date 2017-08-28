package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend;

import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.util.ViewUtil;
import com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider.ClassificationListener;
import com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider.ClassificationProvider;
import com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider.TensorflowClassificationProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.AndroidSensorDataProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.BufferedDataProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.DataEvent;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.DataProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.RawDataListener;
import com.njit.ece.senior_project.medical_sensor.data.SampleLoader.SampleDataLoader;
import com.njit.ece.senior_project.medical_sensor.data.util.ArrayHelper;
import com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier;

import java.io.File;


public class ActivityClassifierActivity extends AppCompatActivity implements RawDataListener, ClassificationListener {

    private SensorManager sensorManager;

    private ActivityClassifier classifier;

    private AndroidSensorDataProvider rawDataProvider;
    private ClassificationProvider classificationProvider;


    private String classificationProbs[] = new String[ActivityClassifier.Activity.values().length];

    private ArrayAdapter<String> classficationProbsAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classifier = new ActivityClassifier(this, "file:///android_asset/frozen_har_new.pb");
        classificationProvider = new TensorflowClassificationProvider(this,  "file:///android_asset/frozen_har_new.pb");

        // get a sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        rawDataProvider = new AndroidSensorDataProvider();

        // get updates of the sensor values for ourself to update the view
        rawDataProvider.addRawDataListener(this);

        // create a buffered data provider
        DataProvider bufferedDataProvider = new BufferedDataProvider(rawDataProvider);

        // pass buffered data directory to classifier
        bufferedDataProvider.addDataListener(classificationProvider);

        // pass clasification directly to this activity to display
        classificationProvider.addClassificationListener(this);

        // initialize classification list to show a ? for all probs
        for(int i = 0; i < classificationProbs.length; i++) {

            ActivityClassifier.Activity activity = classifier.activityFromInt(i + 1 /*arrays start at 0*/);

            classificationProbs[i] = activity.name() + " (" + i + "): ?";
        }

        // create the adaptor to show probability values in the list view
        final ListView classificationProbView = (ListView) findViewById(R.id.classification_probabilities);
        classficationProbsAdaptor = new ArrayAdapter<>(this, R.layout.layout_list_item, classificationProbs);
        classificationProbView.setAdapter(classficationProbsAdaptor);

    }


    @Override
    public void onResume() {

        super.onResume();

        // register a listener for the accelerometer data
        sensorManager.registerListener(rawDataProvider,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME); // game provides the desired 20 ms delay for 50Hz

        // register another listener for the gyroscope data
        sensorManager.registerListener(rawDataProvider,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME); // game provides the desired 20 ms delay for 50Hz

        // test the classifier with the test data (for debugging)
        try {
            // load sample data
            String sampleDataFile = /*"file:///android_asset/*/"UCI HAR Dataset/test/Inertial Signals";
            SampleDataLoader dataLoader = new SampleDataLoader(this, sampleDataFile);

            // run the classifier a few times to test
            for (int i = 0; i < 10; i++) {
                float[][][] dataSet = dataLoader.getNextRow();

                // run the data through the classifier
                ActivityClassifier.Activity activity = classifier.classify(classifier.getProbabilities(dataSet));

                Log.i("Test Classification", "The activity is: " + activity + " (" + activity.getNum() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Main", e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        // unregister listener (for now don't do the classification while the App is paused)
        super.onPause();
        sensorManager.unregisterListener(rawDataProvider);
    }


    @Override
    public void onClassificationChanged(ActivityClassifier.Activity newClassification) {
        ((TextView) this.findViewById(R.id.activity_label)).setText(newClassification.toString());

        Log.d("Classification", "Classification is: " + newClassification);
    }

    @Override
    public void onProbabilitiesChanged(float[] probabilities) {

        for(int i = 0; i < probabilities.length; i++) {
            ActivityClassifier.Activity activity = classifier.activityFromInt(i + 1 /*arrays start at 0*/);
            classificationProbs[i] = activity.name() + " (" + i + "): " + probabilities[i];
        }

        // refresh the list
        classficationProbsAdaptor.notifyDataSetChanged();

        // make the activity which is most likely bold
        ListView probListView = (ListView) findViewById(R.id.classification_probabilities);
        for(int i = 0; i < probListView.getCount(); i++) {
            TextView itemView = (TextView) ViewUtil.getViewByPosition(i, probListView);

            if(i != ArrayHelper.indexOfMax(probabilities)) {
                itemView.setTypeface(null, Typeface.NORMAL);
            } else {
                itemView.setTypeface(null, Typeface.BOLD);
            }
        }
    }

    @Override
    public void onRawDataChanged(DataEvent event) {

        float[] accel = event.getTotal_accel();
        float[] accel_filtered = event.getBody_accel();
        float[] gyro = event.getGyro();

        ((TextView)this.findViewById(R.id.total_acc_x)).setText(Float.toString(accel[0]));
        ((TextView)this.findViewById(R.id.total_acc_y)).setText(Float.toString(accel[1]));
        ((TextView)this.findViewById(R.id.total_acc_z)).setText(Float.toString(accel[2]));

        ((TextView)this.findViewById(R.id.body_acc_x)).setText(Float.toString(accel_filtered[0]));
        ((TextView)this.findViewById(R.id.body_acc_y)).setText(Float.toString(accel_filtered[1]));
        ((TextView)this.findViewById(R.id.body_acc_z)).setText(Float.toString(accel_filtered[2]));

        ((TextView)this.findViewById(R.id.gyro_x)).setText(Float.toString(gyro[0]));
        ((TextView)this.findViewById(R.id.gyro_y)).setText(Float.toString(gyro[1]));
        ((TextView)this.findViewById(R.id.gyro_z)).setText(Float.toString(gyro[2]));
    }
}
