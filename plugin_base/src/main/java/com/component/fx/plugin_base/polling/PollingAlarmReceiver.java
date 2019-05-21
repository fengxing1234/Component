package com.component.fx.plugin_base.polling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class PollingAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "PollingAlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 8.0 崩溃 : Not allowed to start service Intent
         *
         * 在后台运行的服务在几分钟内会被stop掉（模拟器测试在1分钟左右后被kill掉）。在这段时间内，应用仍可以创建和使用服务。
         *
         * 在应用处于后台几分钟后（模拟器测试1分钟左右），应用将不能再通过startService创建后台服务，如果创建则抛出以下异常:Not allowed to start service Intent
         *
         * 应用处于后台时，虽然不能通过startService创建后台服务，但仍可以通过下面的方式创建前台服务。
         *
         * NotificationManager noti = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
         * noti.startServiceInForeground();
         *
         * 后台服务会被kill掉，官方推荐可使用AlarmManager、SyncAdapter、JobScheduler代替后台服务。
         *
         * 广播限制
         *
         * 1.targetSdkVersion为Android N（API level 24）及以上的应用，如果应用在AndroidManifest.xml中静态注册CONNECTIVITY_ACTION这个receiver，应用将不能收到此广播。如果应用使用Context.registerReceiver()动态注册receiver，应用仍可以收到这个广播。
         *
         * 2.运行在Android N及以上设备的应用，无论是targetSdkVersion是否是Android N，应用都不能发送或者接收ACTION_NEW_PICTURE和ACTION_NEW_VIDEO这两个广播。
         *
         * 而Android O执行了更为严格的限制。

         1.动态注册的receiver，可接收任何显式和隐式广播。

         2.targetSdkVersion为Android O（API level 26）及以上的应用，静态注册的receiver将不能收到隐式广播，但可以收到显式广播。
         */
        Log.w(TAG, "onReceive: " + intent.toString());
        Intent i = new Intent(context, PollingBroadcastService.class);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            context.startForegroundService(i);
        } else {
            context.startService(i);
        }

    }
}
