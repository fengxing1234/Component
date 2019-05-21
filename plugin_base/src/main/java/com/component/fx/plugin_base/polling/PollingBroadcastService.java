package com.component.fx.plugin_base.polling;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static com.component.fx.plugin_base.manager.NotifyManager.TEST_CHANNEL_ID;

public class PollingBroadcastService extends Service {

    private static final String TAG = "PollingBroadcastService";

    public static void startService(Context context) {
        Intent intent = new Intent(context, PollingBroadcastService.class);
        context.startService(intent);
    }

    public void startAlarm(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        int anHour = 10 * 1000; // 10秒
        //int anHour = 1000; // 1秒
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        //要跳转的广播
        Intent i = new Intent(context, PollingAlarmReceiver.class);
        //通过PendingIntent跳转  这里不多做解释
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        Log.w(TAG, "startAlarm: ");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(PollingBroadcastService.this, TEST_CHANNEL_ID)
                .setContentTitle("My notification")
                .setContentText("Hello World!");
        startForeground(-1, builder.build());
        stopSelf();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand: " + this);
        new Thread() {
            @Override
            public void run() {
                Log.w(TAG, "run: 轮询");
            }
        }.start();
        startAlarm(PollingBroadcastService.this);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy: ");
        startAlarm(this);
    }
}
