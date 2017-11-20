package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectorActivity extends AppCompatActivity {

    private static Map<String, Class<? extends Activity>> activityList = new HashMap<>();
    private static List<String> activityNames;


    static {
        // populate a list of all the activities we can launch from this activity
        activityList.put("Activity Classification (Phone Sensor Data)", ActivityClassifierActivity.class);
        activityList.put("Activity Classification (Bluetooth Sensor Data)", DeviceSelectActivityForActivityClassifier.class);
        activityList.put("Serial Console", DeviceSelectActivity.class);
        activityList.put("Message Listener Test", DeviceSelectActivityForListenerTest.class);
        activityList.put("Daily Log View", DailyLog.class);
        // get a list of all the names of those activies, to drive the list view
        activityNames = new ArrayList<>(activityList.keySet());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecter);

        // get the list view to put the application list in
        final ListView activityListView = (ListView) findViewById(R.id.activity_list);

        // create an array adaptor to show all the activity names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_list_item, activityNames);
        activityListView.setAdapter(adapter);

        // set the on click listener to use the list view
        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the name of the selection
                String selection = (String) activityListView.getItemAtPosition(i);
                // log the selection name
                Log.d("SelectorActivity", selection + " selected in activity list view.");

                // use the hash map to get the activity corresponding to the selected item
                Class<? extends Activity> selectedActivity = activityList.get(selection);

                if (selectedActivity != null) {
                    Log.d("SelectorActivity", "Starting activity: " + selection);

                    Intent launcherIntent = new Intent(SelectorActivity.this, selectedActivity);

                    if(selectedActivity == ActivityClassifierActivity.class) {
                        // default data source to the android phone sensors
                        launcherIntent.putExtra("Source", "Android");
                    }

                    // if the selection was valid, start the activity corresponding with the selection
                    startActivity(launcherIntent);
                } else {
                    Log.e("SelectorActivity", selection + " does not have a corresponding entry!!!");
                }
            }
        });
    }
}
