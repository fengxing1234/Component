package com.component.fx.plugin_base.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 开机广播
 * Intent.ACTION_LOCKED_BOOT_COMPLETED
 * Intent.ACTION_BOOT_COMPLETED
 * "保留原因：这些广播只在首次启动时发送一次，并且许多应用都需要接收此广播以便进行作业、闹铃等事项的安排。"
 * <p>
 * <p>
 * <p>
 * 增删用户
 * Intent.ACTION_USER_INITIALIZE
 * "android.intent.action.USER_ADDED"
 * "android.intent.action.USER_REMOVED"
 * "保留原因：这些广播只有拥有特定系统权限的app才能监听，因此大多数正常应用都无法接收它们。"
 * <p>
 * <p>
 * 时区、ALARM变化
 * "android.intent.action.TIME_SET"
 * Intent.ACTION_TIMEZONE_CHANGED
 * AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED
 * "保留原因：时钟应用可能需要接收这些广播，以便在时间或时区变化时更新闹铃"
 * <p>
 * 语言区域变化
 * Intent.ACTION_LOCALE_CHANGED
 * "保留原因：只在语言区域发生变化时发送，并不频繁。 应用可能需要在语言区域发生变化时更新其数据。"
 * <p>
 * <p>
 * Usb相关
 * UsbManager.ACTION_USB_ACCESSORY_ATTACHED
 * UsbManager.ACTION_USB_ACCESSORY_DETACHED
 * UsbManager.ACTION_USB_DEVICE_ATTACHED
 * UsbManager.ACTION_USB_DEVICE_DETACHED
 * "保留原因：如果应用需要了解这些 USB 相关事件的信息，目前尚未找到能够替代注册广播的可行方案"
 * <p>
 * <p>
 * <p>
 * 蓝牙状态相关
 * BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED
 * BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED
 * BluetoothDevice.ACTION_ACL_CONNECTED
 * BluetoothDevice.ACTION_ACL_DISCONNECTED
 * "保留原因：应用接收这些蓝牙事件的广播时不太可能会影响用户体验"
 * <p>
 * <p>
 * <p>
 * Telephony相关
 * CarrierConfigManager.ACTION_CARRIER_CONFIG_CHANGED
 * TelephonyIntents.ACTION_*_SUBSCRIPTION_CHANGED
 * TelephonyIntents.SECRET_CODE_ACTION
 * TelephonyManager.ACTION_PHONE_STATE_CHANGED
 * TelecomManager.ACTION_PHONE_ACCOUNT_REGISTERED
 * TelecomManager.ACTION_PHONE_ACCOUNT_UNREGISTERED
 * "保留原因：设备制造商 (OEM) 电话应用可能需要接收这些广播"
 * <p>
 * <p>
 * <p>
 * 账号相关
 * AccountManager.LOGIN_ACCOUNTS_CHANGED_ACTION
 * "保留原因：一些应用需要了解登录帐号的变化，以便为新帐号和变化的帐号设置计划操作"
 * <p>
 * <p>
 * <p>
 * 应用数据清除
 * Intent.ACTION_PACKAGE_DATA_CLEARED
 * "保留原因：只在用户显式地从 Settings 清除其数据时发送，因此广播接收器不太可能严重影响用户体验"
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * 软件包被移除
 * Intent.ACTION_PACKAGE_FULLY_REMOVED
 * "保留原因：一些应用可能需要在另一软件包被移除时更新其存储的数据；对于这些应用，尚未找到能够替代注册此广播的可行方案"
 * <p>
 * <p>
 * 外拨电话
 * Intent.ACTION_NEW_OUTGOING_CALL
 * "保留原因：执行操作来响应用户打电话行为的应用需要接收此广播"
 * <p>
 * <p>
 * 当设备所有者被设置、改变或清除时发出
 * DevicePolicyManager.ACTION_DEVICE_OWNER_CHANGED
 * <p>
 * <p>
 * <p>
 * "保留原因：此广播发送得不是很频繁；一些应用需要接收它，以便知晓设备的安全状态发生了变化"
 * 日历相关
 * CalendarContract.ACTION_EVENT_REMINDER
 * "保留原因：由日历provider发送，用于向日历应用发布事件提醒。因为日历provider不清楚日历应用是什么，所以此广播必须是隐式广播。"
 * <p>
 * <p>
 * <p>
 * 安装或移除存储相关广播
 * Intent.ACTION_MEDIA_MOUNTED
 * Intent.ACTION_MEDIA_CHECKING
 * Intent.ACTION_MEDIA_EJECT
 * Intent.ACTION_MEDIA_UNMOUNTED
 * Intent.ACTION_MEDIA_UNMOUNTABLE
 * Intent.ACTION_MEDIA_REMOVED
 * Intent.ACTION_MEDIA_BAD_REMOVAL
 * "保留原因：这些广播是作为用户与设备进行物理交互的结果：安装或移除存储卷或当启动初始化时（当可用卷被装载）的一部分发送的，因此它们不是很常见，并且通常是在用户的掌控下"
 * <p>
 * <p>
 * <p>
 * 短信、WAP PUSH相关
 * Telephony.Sms.Intents.SMS_RECEIVED_ACTION
 * Telephony.Sms.Intents.WAP_PUSH_RECEIVED_ACTION
 * <p>
 * <p>
 * <p>
 * 注意：需要申请以下权限才可以接收
 * "android.permission.RECEIVE_SMS"
 * "android.permission.RECEIVE_WAP_PUSH"
 * "保留原因：SMS短信应用需要接收这些广播"
 */


public class SystemReceiver extends BroadcastReceiver {

    private static final String TAG = "SystemReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        Log.d(TAG, "收到的广播类型" + action);
    }
}

