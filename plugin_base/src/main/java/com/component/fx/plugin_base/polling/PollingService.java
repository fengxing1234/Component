package com.component.fx.plugin_base.polling;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * alarmManager.setRepeating(,,,);
 * <p>
 * 第一个参数表示闹钟类型：一般为 AlarmManager.ELAPSED_REALTIME_WAKEUP 或者 AlarmManager.RTC_WAKEUP 。它们之间的区别就是前者是从手机开机后的时间，包含了手机睡眠时间；而后者使用的就是手机系统设置中的时间。所以如果设置为 AlarmManager.RTC_WAKEUP ，那么可以通过修改手机系统的时间来提前触发定时事件。另外，对于相似的 AlarmManager.ELAPSED_REALTIME 和 AlarmManager.RTC 来说，它们不会唤醒 CPU 。所以使用的频率较少；
 * 第二个参数表示任务首次执行时间：与第一个参数密切相关。第一个参数若为 AlarmManager.ELAPSED_REALTIME_WAKEUP ，那么当前时间就为 SystemClock.elapsedRealtime() ；若为 AlarmManager.RTC_WAKEUP ，那么当前时间就为 System.currentTimeMillis() ；
 * 第三个参数表示两次执行的间隔时间：这个参数没什么好讲的，一般为常量；
 * 第四个参数表示对应的响应动作：一般都是去发送广播，然后在广播接收 onReceive(Context context, Intent intent) 中做相关操作。
 * <p>
 * set(int type，long startTime，PendingIntent pi)；
 * <p>
 * 该方法用于设置一次性闹钟，
 * 第一个参数表示闹钟类型，
 * 第二个参数表示闹钟执行时间，
 * 第三个参数表示闹钟响应动作。
 * <p>
 * int type：闹钟的类型，常用的有5个值：
 * AlarmManager.ELAPSED_REALTIME、AlarmManager.ELAPSED_REALTIME_WAKEUP、AlarmManager.RTC、AlarmManager.RTC_WAKEUP、AlarmManager.POWER_OFF_WAKEUP。
 * <p>
 * AlarmManager.ELAPSED_REALTIME表示闹钟在手机睡眠状态下不可用，该状态下闹钟使用相对时间（相对于系统启动开始），状态值为3；
 * <p>
 * AlarmManager.ELAPSED_REALTIME_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟也使用相对时间，状态值为2；
 * <p>
 * AlarmManager.RTC表示闹钟在睡眠状态下不可用，该状态下闹钟使用绝对时间，即当前系统时间，状态值为1；
 * <p>
 * AlarmManager.RTC_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟使用绝对时间，状态值为0；
 * <p>
 * <p>
 * <p>
 * PendingIntent Flag
 * <p>
 * FLAG_ONE_SHOT 表示返回的PendingIntent仅能执行一次，执行完后自动取消
 * FLAG_NO_CREATE 表示如果描述的PendingIntent不存在，并不创建相应的PendingIntent，而是返回NULL
 * FLAG_CANCEL_CURRENT 表示相应的PendingIntent已经存在，则取消前者，然后创建新的PendingIntent， 这个有利于数据保持为最新的，可以用于即时通信的通信场景
 * FLAG_UPDATE_CURRENT 如果要创建的PendingIntent已经存在了，那么在保留原先PendingIntent的同时，将原先PendingIntent封装的Intent中的extra部分替换为现在新创建的PendingIntent的intent中extra的内容
 * <p>
 * <p>
 * <p>
 * <p>
 * Service Flag 返回值
 * <p>
 * START_STICKY
 * 当Service因内存不足而被系统kill后，一段时间后内存再次空闲时，系统将会尝试重新创建此Service，一旦创建成功后将回调onStartCommand方法，但其中的Intent将是null，除非有挂起的Intent，如pendingintent，这个状态下比较适用于不执行命令、但无限期运行并等待作业的媒体播放器或类似服务。
 * START_NOT_STICKY
 * 当Service因内存不足而被系统kill后，即使系统内存再次空闲时，系统也不会尝试重新创建此Service。除非程序中再次调用startService启动此Service，这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务。
 * START_REDELIVER_INTENT
 * 当Service因内存不足而被系统kill后，则会重建服务，并通过传递给服务的最后一个 Intent 调用 onStartCommand()，任何挂起 Intent均依次传递。与START_STICKY不同的是，其中的传递的Intent将是非空，是最后一次调用startService中的intent。这个值适用于主动执行应该立即恢复的作业（例如下载文件）的服务。
 */

public class PollingService extends Service {

    private static final int REPEATING_TIME = 1000 * 60 * 30;
    private static final int TEST_REPEATING_TIME = 1000 * 10;

    private static final int REQUEST_CODE = 0x12;

    private static final String NAME = "com.component.fx.plugin_base";
    private static PowerManager.WakeLock wakeLock;
    public static final String ACTION_QUERY = "ACTION_QUERY";


    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, NAME);
            wakeLock.setReferenceCounted(true);
        }
        return wakeLock;
    }

    public static void startAlarmPolling(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, PollingService.class);
        intent.setAction(ACTION_QUERY);
        long time = SystemClock.elapsedRealtime();
        PendingIntent pendingIntent = PendingIntent.getService(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, time + TEST_REPEATING_TIME, TEST_REPEATING_TIME, pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time + TEST_REPEATING_TIME, pendingIntent);
    }

    public static void stopAlarmPolling(Context context) {
        Intent intent = new Intent(context, PollingService.class);
        intent.setAction(PollingService.ACTION_QUERY);
        PendingIntent sender = PendingIntent.getService(context, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w("fengxing_polling", "onStartCommand" + this);
        if (intent == null || ACTION_QUERY.equals(intent.getAction())) {
            PowerManager.WakeLock lock = getLock(getApplicationContext());
            if (!lock.isHeld()) {
                Log.w("fengxing_polling", "lock.acquire()");
                lock.acquire();
            }
            doQuery();
        }
        return START_STICKY;
    }

    private void doQuery() {
        new Thread() {
            @Override
            public void run() {
                Log.w("fengxing_polling", "开始轮询数据了");
                PollingService.startAlarmPolling(PollingService.this);
                releaseLock();
            }
        }.start();
    }

    private void releaseLock() {
        PowerManager.WakeLock lock = getLock(getApplicationContext());
        if (lock.isHeld()) {
            lock.release();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("fengxing_polling", "onDestroy: ");
    }
}
