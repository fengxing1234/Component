package com.component.fx.plugin_base.manager;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.component.fx.plugin_base.base.BaseApplication;

public class IntentManager {

    @TargetApi(Build.VERSION_CODES.O)
    public static void notifyChannelIntent(NotificationChannel channel) {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, BaseApplication.getAppContext().getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
        BaseApplication.getAppContext().startActivity(intent);
    }
}
