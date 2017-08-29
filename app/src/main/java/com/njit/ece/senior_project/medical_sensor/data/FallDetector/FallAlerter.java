package com.njit.ece.senior_project.medical_sensor.data.FallDetector;

import android.os.Handler;
import android.util.Log;

/**
 * Sends an alert when a fall is detected and the alert is not canceled
 */
public class FallAlerter implements FallListener, AlertCancelListener {

    private static final String TAG = FallAlerter.class.getSimpleName();

    private static final FallAlerter ourInstance = new FallAlerter();

    public static FallAlerter getInstance() {
        return ourInstance;
    }

    private FallAlerter() {
    }

    private static final int TIMEOUT = 10_000; // 10 s timeout for canceling


    private Handler handler = new Handler();

    private Runnable delayedTask = null;

    @Override
    public void onFallDetected(final FallEvent event) {
        if(delayedTask == null) {

            Log.d(TAG, "Fall detected... sending alert in " + TIMEOUT/1000. + " seconds");

            delayedTask = new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Fall detected at: " + event.getFallTime());
                    delayedTask = null;
                }
            };

            handler.postDelayed(delayedTask, TIMEOUT);
        } else {
            Log.d(TAG, "Fall alert already pending... will not send twice");
        }
    }

    @Override
    public void onOptionSelected(FallNotifier.AlertCancelOption option) {
        if(option == FallNotifier.AlertCancelOption.CANCEL) {
            Log.d(TAG, "Fall alert canceled");

            if(delayedTask != null) {
                handler.removeCallbacks(delayedTask);
            }

            delayedTask = null;

        } else if(option == FallNotifier.AlertCancelOption.CONTINUE) {
            //send alert immediately
            if(delayedTask != null) {
                handler.removeCallbacks(delayedTask);
                delayedTask.run();
            }
            delayedTask = null;
        }
    }


}
