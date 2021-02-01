package fr.openium.examplepam.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import fr.openium.examplepam.R;
import fr.openium.examplepam.database.AppDatabase;
import fr.openium.examplepam.model.Call;

public class CallService extends Service {
    public static final int start = 0xa;
    public static final int stop = 0xb;

    public static final String KEY_COMMAND = "KEY_COMMAND";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String CHANNEL_ID = "callnotification";
    public static final int NOTIFICATION_ID = 0x78;

    private Call call = null;
    private long callStartTime = 0;


    // Used to dialog with the rest of the app, like an Activity, won't be used here
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra(KEY_COMMAND, stop);
        switch (command) {
            case start: {
                String name = intent.getStringExtra(KEY_NAME);
                PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0xaa, getStopIntent(getApplicationContext()), PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_call_end, getString(R.string.dismiss), pendingIntent).build();
                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle(getString(R.string.calling_format, name))
                        .setSmallIcon(R.drawable.ic_notif_call)
                        .setUsesChronometer(true)
                        .setLocalOnly(true)
                        .addAction(action)
                        .build();

                callStartTime = System.currentTimeMillis();
                call = new Call(name, callStartTime);
                startForeground(NOTIFICATION_ID, notification);
                return START_STICKY;
            }
            case stop: {
                call.length = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - callStartTime);
                AppDatabase database = AppDatabase.getInstance(getApplicationContext());
                // required to insert on another thread
                AsyncTask.execute(() -> database.callDao().insertAll(call));

                stopSelf();
                break;
            }
        }

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_title);
            String description = getString(R.string.notification_channel_content);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static Intent getStartIntent(Context context, String name) {
        Intent intent = new Intent(context, CallService.class);
        intent.putExtra(KEY_NAME, name);
        intent.putExtra(KEY_COMMAND, start);
        return intent;
    }

    public static Intent getStopIntent(Context context) {
        Intent intent = new Intent(context, CallService.class);
        intent.putExtra(KEY_COMMAND, stop);
        return intent;
    }
}
