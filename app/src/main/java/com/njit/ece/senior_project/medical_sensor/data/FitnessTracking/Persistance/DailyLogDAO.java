package com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Persistance;

import android.content.Context;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Model.DailyLogEntry;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * DAO for {@link DailyLogEntry}
 */
public class DailyLogDAO {

    private DB snappydb;

    public DailyLogDAO(Context context) {
        try {
            snappydb = DBFactory.open(context, "fitnesslog");
        } catch (SnappydbException e) {
            Log.e("DailyLogDAO", "Unable to open fitness log database");
        }
    }

    public DailyLogEntry getLogEntry(Date date) {
        String key = DailyLogEntry.getID(date);

        try {
            if(snappydb.exists(key)) {
                return snappydb.getObject(key, DailyLogEntry.class);
            } else {
                // no entry exists for this day - we can just create an empty one
                DailyLogEntry logEntry = new DailyLogEntry(date);
                snappydb.put(DailyLogEntry.getID(date), logEntry);
                return logEntry;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DailyLogDAO", "Unexpected exeption", e);
            return null;
        }
    }

    public void updateLogEntry(DailyLogEntry logEntry) {

        String key = DailyLogEntry.getID(logEntry.getDate());

        try {
            snappydb.put(key, logEntry);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DailyLogDAO", "Unexpected exeption", e);
        }
    }

    public List<DailyLogEntry> getAllLogEntries() {

        try {
            String[] keys = snappydb.findKeys(DailyLogEntry.getBaseID());
            List<DailyLogEntry> entries = new ArrayList<>();

            for(String key : keys) {
                entries.add(snappydb.getObject(key, DailyLogEntry.class));
            }

            return entries;
        } catch (SnappydbException e) {
            e.printStackTrace();
            Log.e("DailyLogDAO", "Unexpected exception", e);
            throw new RuntimeException(e);
        }



    }
}
