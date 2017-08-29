package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.Activities;

import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.R;
import com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.util.ViewUtil;
import com.njit.ece.senior_project.medical_sensor.data.BluetoothMessageProvider.BluetoothMessageProviderImpl;
import com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider.ClassificationListener;
import com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider.ClassificationProvider;
import com.njit.ece.senior_project.medical_sensor.data.ClassificationProvider.TensorflowClassificationProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.AndroidSensorDataProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.BluetoothSensorDataProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.BufferedDataProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.DataEvent;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.DataProvider;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.RawDataListener;
import com.njit.ece.senior_project.medical_sensor.data.DataProvider.RawDataProvider;
import com.njit.ece.senior_project.medical_sensor.data.FallDetector.ActivityClassificationFallDetector;
import com.njit.ece.senior_project.medical_sensor.data.FallDetector.FallAlerter;
import com.njit.ece.senior_project.medical_sensor.data.FallDetector.FallDetector;
import com.njit.ece.senior_project.medical_sensor.data.FallDetector.FallEvent;
import com.njit.ece.senior_project.medical_sensor.data.FallDetector.FallListener;
import com.njit.ece.senior_project.medical_sensor.data.FallDetector.FallNotifier;
import com.njit.ece.senior_project.medical_sensor.data.SampleLoader.SampleDataLoader;
import com.njit.ece.senior_project.medical_sensor.data.util.ArrayHelper;
import com.njit.ece.senior_project.medical_sensor.tensorflow.ActivityClassifier;

import me.aflak.bluetooth.Bluetooth;


public class ActivityClassifierActivity extends AppCompatActivity implements RawDataListener, ClassificationListener, FallListener {

    private ActivityClassifier classifier;

    private RawDataProvider rawDataProvider;
    private ClassificationProvider classificationProvider;


    private String classificationProbs[] = new String[ActivityClassifier.Activity.values().length];

    private ArrayAdapter<String> classficationProbsAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classifier = new ActivityClassifier(this, "file:///android_asset/frozen_har_new.pb");
        classificationProvider = new TensorflowClassificationProvider(this,  "file:///android_asset/frozen_har_new.pb");


        String source = getIntent().getExtras().getString("Source");
        if(source.equals("Android")) {
            rawDataProvider = new AndroidSensorDataProvider((SensorManager) getSystemService(SENSOR_SERVICE));
        } else if(source.equals("Bluetooth")) {
            Bluetooth b = new Bluetooth(this);
            b.enableBluetooth();

            // get the paired item
            int pos = getIntent().getExtras().getInt("pos");
            String name = b.getPairedDevices().get(pos).getName();
            // connect to device
            b.connectToDevice(b.getPairedDevices().get(pos));

            rawDataProvider = new BluetoothSensorDataProvider(new BluetoothMessageProviderImpl(b));

        } else {
            throw new IllegalStateException("You need to select a data source!");
        }


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


        // create a fall detector which uses the classification provider and raw data provider
        FallDetector fallDetector = new ActivityClassificationFallDetector(classificationProvider, rawDataProvider);
        fallDetector.addFallListener(this);

        // create a listener to generate notifications when a fall is detected
        FallNotifier fallNotifier = new FallNotifier(fallDetector, this);

        // set up the fall alerter to automatically send alerts when a fall is detected
        FallAlerter fallAlerter = FallAlerter.getInstance();
        fallDetector.addFallListener(fallAlerter);


    }


    @Override
    public void onResume() {

        super.onResume();

        rawDataProvider.resume();

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

        rawDataProvider.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        rawDataProvider.destroy();
    }

    @Override
    public void onClassificationChanged(final ActivityClassifier.Activity newClassification) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.activity_label)).setText(newClassification.toString());
            }
        });

        Log.d("Classification", "Classification is: " + newClassification);
    }

    @Override
    public void onProbabilitiesChanged(final float[] probabilities) {

        for(int i = 0; i < probabilities.length; i++) {
            ActivityClassifier.Activity activity = classifier.activityFromInt(i + 1 /*arrays start at 0*/);
            classificationProbs[i] = activity.name() + " (" + i + "): " + probabilities[i];
        }

        // refresh the list
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });

    }

    @Override
    public void onRawDataChanged(DataEvent event) {

        final float[] accel = event.getTotal_accel();
        final float[] accel_filtered = event.getBody_accel();
        final float[] gyro = event.getGyro();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.total_acc_x)).setText(Float.toString(accel[0]));
                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.total_acc_y)).setText(Float.toString(accel[1]));
                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.total_acc_z)).setText(Float.toString(accel[2]));

                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.body_acc_x)).setText(Float.toString(accel_filtered[0]));
                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.body_acc_y)).setText(Float.toString(accel_filtered[1]));
                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.body_acc_z)).setText(Float.toString(accel_filtered[2]));

                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.gyro_x)).setText(Float.toString(gyro[0]));
                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.gyro_y)).setText(Float.toString(gyro[1]));
                ((TextView) ActivityClassifierActivity.this.findViewById(R.id.gyro_z)).setText(Float.toString(gyro[2]));
            }
        });
    }

    @Override
    public void onFallDetected(FallEvent event) {
        ((TextView) findViewById(R.id.last_fall_time)).setText(event.getFallTime().toString());
    }
}
