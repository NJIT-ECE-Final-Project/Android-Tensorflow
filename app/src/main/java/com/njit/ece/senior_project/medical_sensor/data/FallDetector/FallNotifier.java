package com.njit.ece.senior_project.medical_sensor.data.FallDetector;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.DebugUtils;
import android.util.Log;

import com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.R;

/**
 * Sends the user a notification when a fall is detected
 */
public class FallNotifier implements FallListener {

    public enum AlertCancelOption {
        CANCEL,
        CONTINUE
    }

    private static final String TAG = FallNotifier.class.getSimpleName();

    private static final int NOTIFICATION_ID = 5;

    private Context applicationContext;

    public FallNotifier(FallDetector fallDetector, Context applicationContext) {

        Log.d(TAG, "Creating fall notification sender");

        this.applicationContext = applicationContext;

        fallDetector.addFallListener(this);
    }


    @Override
    public void onFallDetected(FallEvent event) {

        Log.d(TAG, "Fall detected, sending notification.");

        //TODO: implement intents to actually do something

        Intent cancelIntent = new Intent(applicationContext, NotificationActionService.class)
                .setAction("Cancel");

        PendingIntent cancelPendingIntent = PendingIntent.getService(applicationContext, 0,
                cancelIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent continueIntent = new Intent(applicationContext, NotificationActionService.class)
                .setAction("Continue");

        PendingIntent continuePendingIntent = PendingIntent.getService(applicationContext, 0,
                continueIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(applicationContext)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_warn_not)
//HERE ARE YOUR BUTTONS
                .addAction(R.drawable.ic_fall_alert_cancel, "Cancel Alert", cancelPendingIntent)
                .addAction(R.drawable.ic_fall_alert_ok, "Send Alert", continuePendingIntent)
                .setVibrate((new long[] { 1000, 1000, 1000, 1000, 1000 }))
                .build();

        NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);

    }


    // Android boilerplate to execute a function when a notification button is called
    // thanks, https://stackoverflow.com/questions/11270898/how-to-execute-a-method-by-clicking-a-notification
    public static class NotificationActionService extends IntentService {

        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "Received notification action: " + action);
            if ("Cancel".equals(action)) {
                Log.d(TAG, "Canceling alert");
                FallAlerter.getInstance().onOptionSelected(AlertCancelOption.CANCEL);
            } else if ("Continue".equals(action)) {
                Log.d(TAG, "Continuing alert");
                FallAlerter.getInstance().onOptionSelected(AlertCancelOption.CONTINUE);
            }

            // cancel this notification
            ((NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
        }
    }
}
