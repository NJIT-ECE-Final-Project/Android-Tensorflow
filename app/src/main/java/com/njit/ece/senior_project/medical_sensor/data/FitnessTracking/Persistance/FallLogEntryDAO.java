package com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Persistance;

import android.content.Context;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.data.FitnessTracking.Model.FallLogEntry;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David Etler on 11/13/2017.
 */

public class FallLogEntryDAO {

    private DB snappydb;

    public FallLogEntryDAO(Context context) {
        try {
            snappydb = DBFactory.open(context, "fallog");
        } catch (SnappydbException e) {
            Log.e("DailyLogDAO", "Unable to open fitness log database");
        }
    }

    public void logFall(Date date) {
        try {
            snappydb.put(FallLogEntry.getID(date), new FallLogEntry(date));
        } catch (Exception e) {
            Log.e("FallLogEntryDAO", "Unexpected exception", e);
        }
    }

    public int getDailyFallCount(Date date) {
        try {
            String baseDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            return snappydb.countKeys(baseDate);
        } catch (Exception e) {
            Log.e("FallLogEntryDAO", "Unexpected exception", e);
        }

        return 0;
    }
}
