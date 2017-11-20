package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.R;
import com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Model.DailyLogEntry;
import com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Persistance.DailyLogDAO;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

public class DailyLog extends AppCompatActivity {

    private List<DailyLogEntry> logEntries = null;

    private DailyLogDAO dailyLogDAO = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log);

        dailyLogDAO = new DailyLogDAO(this);

        logEntries = dailyLogDAO.getAllLogEntries();

        for(DailyLogEntry logEntry : logEntries) {
            Log.d("DailyLog", "Got log entry for " + logEntry.getDate());
            Log.d("DailyLog", "Spent " + logEntry.getTimeWalking() + " minutes walking");
        }

        ((ListView) findViewById(R.id.daily_log))
                .setAdapter(new ProductAdapter(getApplicationContext(),
                        R.layout.view_daily_log_entry, R.id.daily_steps, logEntries));
    }

    public class ProductAdapter extends ArrayAdapter<DailyLogEntry> {

        private List<DailyLogEntry> Products;

        public ProductAdapter(Context context, int resource,
                              int textViewResourceId, List<DailyLogEntry> objects) {
            super(context, resource, textViewResourceId, objects);
            this.Products = objects;

        }

        @Override
        public int getCount() {
            return Products.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProductHolder Holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.view_daily_log_entry,
                        null);
                Holder = new ProductHolder();
                Holder.Date = (TextView) convertView.findViewById(R.id.log_date);
                Holder.TimeWalked = (TextView) convertView.findViewById(R.id.walking_time);
                Holder.Steps = (TextView) convertView.findViewById(R.id.daily_steps);
                convertView.setTag(Holder);
            } else {
                Holder = (ProductHolder) convertView.getTag();
            }
            DailyLogEntry product = Products.get(position);
            if (product != null) {
                Holder.Date.setText(new SimpleDateFormat("yyyy-MM-dd").format(product.getDate()));
                Holder.Steps.setText(Integer.toString(product.getNumSteps()));
                Holder.TimeWalked.setText(Integer.toString(product.getTimeWalking()));
            }
            return convertView;
        }
    }



    public static class ProductHolder {
        public TextView Date;
        public TextView TimeWalked;
        public TextView Steps;
    }
}
