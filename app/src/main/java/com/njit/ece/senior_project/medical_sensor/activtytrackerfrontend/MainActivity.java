package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.njit.ece.senior_project.medical_sensor.data.Filters.SimpleHighPass;
import com.njit.ece.senior_project.medical_sensor.data.SampleLoader.SampleDataLoader;
import com.njit.ece.senior_project.medical_sensor.data.SignalType;
import com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier;

import java.io.File;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private File signalsFolder;

    private int timeSteps = 128;

    private SensorManager sensorManager;
    private ActivityClassifier classifier;


    // one high-pass filter per accelerometer axis
    private SimpleHighPass[] highPassFilters = new SimpleHighPass[3];


    // incremented until timesteps is reached
    int accel_t = 0;
    int gyro_t = 0;
    float[][][] dataBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get a sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // initialize the classifier with the trained model
        classifier = new ActivityClassifier(this, "file:///android_asset/frozen_har.pb");

        for(int i = 0; i < 3; i++) {
            highPassFilters[i] = new SimpleHighPass();
        }

        dataBuffer = new float[1][timeSteps][SignalType.values().length];
    }


    @Override
    public void onResume() {

        super.onResume();

        // register a listener for the accelerometer data
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME); // game provides the desired 20 ms delay for 50Hz

        // register another listener for the gyroscope data
        sensorManager.registerListener(this,
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
                ActivityClassifier.Activity activity = classifier.classify(dataSet);

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
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.d("Timestep", "accel_t: " + accel_t + " gyro_t: " + gyro_t);

        if(accel_t == timeSteps && gyro_t == timeSteps) {
            classifyData();
            accel_t = 0;
            gyro_t = 0;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // acceleromter data
            updateAccelSensorText(event);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            updateGyroSensorTest(event);
        }
    }

    private void classifyData() {

        // run the classifier
        ActivityClassifier.Activity classification = classifier.classify(dataBuffer);

        ((TextView) this.findViewById(R.id.activity_label)).setText(classification.toString());

        Log.d("Classification", "Classification is: " + classification);
    }

    private void updateGyroSensorTest(SensorEvent event) {

        float[] gyro = event.values;

        ((TextView)this.findViewById(R.id.gyro_x)).setText(Float.toString(gyro[0]));
        ((TextView)this.findViewById(R.id.gyro_y)).setText(Float.toString(gyro[1]));
        ((TextView)this.findViewById(R.id.gyro_z)).setText(Float.toString(gyro[2]));

        // add gyro values to data buffer
        if(gyro_t < timeSteps) {
            for(int i = 0; i < 3; i++) {
                dataBuffer[0][gyro_t][i + 3] = gyro[i];
            }
            gyro_t++;
        }
    }

    private void updateAccelSensorText(SensorEvent event) {

        float[] accel = event.values;

        float[] accel_filtered = new float[3];

        for(int i = 0; i < 3; i++) {
            accel[i] = accel[i]/9.81f; //normalize units to gs
            Log.d("Acceleration", accel[i]+"");
            accel_filtered[i] = (float) highPassFilters[i].getNextDataPoint(accel[i]);
        }

        ((TextView)this.findViewById(R.id.total_acc_x)).setText(Float.toString(accel[0]));
        ((TextView)this.findViewById(R.id.total_acc_y)).setText(Float.toString(accel[1]));
        ((TextView)this.findViewById(R.id.total_acc_z)).setText(Float.toString(accel[2]));

        ((TextView)this.findViewById(R.id.body_acc_x)).setText(Float.toString(accel_filtered[0]));
        ((TextView)this.findViewById(R.id.body_acc_y)).setText(Float.toString(accel_filtered[1]));
        ((TextView)this.findViewById(R.id.body_acc_z)).setText(Float.toString(accel_filtered[2]));

        if(accel_t < timeSteps) {
            for(int i = 0; i < 3; i++) {
                dataBuffer[0][accel_t][i] = accel_filtered[i];
                dataBuffer[0][accel_t][i + 6] = accel[i];
            }
            accel_t++;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // TODO: now sure what causes this to happen
        Log.w("Sensors", "Accuraccy changed on " + sensor.getName());
        Log.w("Sensors", "i = " + i);
    }

}
