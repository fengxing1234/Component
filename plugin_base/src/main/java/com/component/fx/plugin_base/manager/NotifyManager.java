package com.component.fx.plugin_base.manager;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.component.fx.plugin_base.base.BaseApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotifyManager {

    public static final String CHAT_ChANNEL_ID = "chat_channel_id";
    public static final String NEWS_ChANNEL_ID = "news_channel_id";

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";




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
     * 创建Channel类型
     *
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
     * 判断 是否关闭了通知
     * <p>
     * 是否允许通知
     *
     * @param context
     * @return true 可用 false 不可用
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isNotifyEnable(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ///< 8.0手机以上
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d("feng", "mNm: "+notificationManager);
            if (notificationManager.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo info = context.getApplicationInfo();
            String packageName = context.getPackageName();
            int uid = info.uid;
            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((Integer) checkOpNoThrowMethod.invoke(opsManager, value, uid, packageName) == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;

    }


    /**
     * 判断channel是否可用
     *
     * @param channelId
     * @return true 可用  false 不可用
     */
    public static boolean isChannelEnable(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) BaseApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = nm.getNotificationChannel(channelId);
            return !(channel.getImportance() == NotificationManager.IMPORTANCE_NONE);
        }
        return true;
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
            Log.d("feng", "mNm: openNotifyChannel "+nm);
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
