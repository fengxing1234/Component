package com.component.fx.plugin_base.manager;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.component.fx.plugin_base.base.BaseApplication;

public class NotifyManager {

    public static final String CHAT_ChANNEL_ID = "chat_channel_id";
    public static final String NEWS_ChANNEL_ID = "news_channel_id";


    /**
     * 创建一个Channel类型
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static void createChannel() {

        String channelName = "聊天消息";
        int importance = NotificationManager.IMPORTANCE_MAX;
        setChannel(CHAT_ChANNEL_ID, channelName, importance);

        channelName = "新闻";
        importance = NotificationManager.IMPORTANCE_DEFAULT;
        setChannel(NEWS_ChANNEL_ID, channelName, importance);
    }


    /**
     * @param id
     * @param name
     * @param importance
     */
    @TargetApi(Build.VERSION_CODES.O)
    private static void setChannel(String id, String name, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) BaseApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm == null) {
                return;
            }
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setShowBadge(true);
            nm.createNotificationChannel(channel);
        }
    }


    /**
     * 如果用户关闭了Channel通道可以根据这个方法跳转到设置页面
     *
     * @param channelId
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static void openNotifyChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) BaseApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = nm.getNotificationChannel(channelId);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, BaseApplication.getAppContext().getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                BaseApplication.getAppContext().startActivity(intent);
            }
        }
    }
}
