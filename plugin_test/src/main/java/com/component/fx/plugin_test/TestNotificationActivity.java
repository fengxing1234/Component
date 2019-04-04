package com.component.fx.plugin_test;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.component.fx.plugin_base.manager.NotifyManager;


/**
 * 使用的技能
 * <p>
 * Notification
 * <p>
 * pendingIntent
 * <p>
 * TaskStackBuilder
 */

/**
 * Android 5.0（API 级别 21）中引入的 Material Design 变更尤为重要
 * <p>
 * 必需的通知内容  少一个都报错 图片 标题 内容
 */
public class TestNotificationActivity extends AppCompatActivity {

    private static final String TAG = "TestNotificationActivity";
    private Context context;
    private int id;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyManager;
    private Notification.Builder other;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_notification_activity);

        context = this;

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(context, NotifyManager.CHAT_ChANNEL_ID);


        findViewById(R.id.app_test_btn_send_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotifyManager.openNotifyChannel(NotifyManager.CHAT_ChANNEL_ID);

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //showProgressNotify();
                        // showNotify();


                        mNotifyManager.notify(id++, defaultNotify(context, mBuilder).build());

                        //progressNotify(mBuilder);

                        //mNotifyManager.notify(id++, bigTextStyleNotify(mBuilder).build());

                        //mNotifyManager.notify(id++, inboxStyleNotify(mBuilder).build());

                        //mNotifyManager.notify(id++, bigPictureStyleNotify(mBuilder).build());

                        //mNotifyManager.notify(id++, customBigNotify(mBuilder).build());

                        //mNotifyManager.notify(id++, customNotify(mBuilder).build());

                        //startActivity(new Intent(context, TestWindowNotifyActivity.class));

                        //startService(new Intent(context, TestWindowService.class));

                    }
                }.start();
            }

        });
    }


    public NotificationCompat.Builder customNotify(NotificationCompat.Builder builder) {

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.test_custom_remote_view_dj);


        /**
         * 加入点击事件  setAutoCancel 无效
         */
//        views.setOnClickPendingIntent(R.id.test_iv_large_img,getPendingIntent("onClickLargeImage"));
//        views.setOnClickPendingIntent(R.id.test_iv_src_right,getPendingIntent("onClickRightImage"));
//
//        views.setOnClickPendingIntent(R.id.test_iv_src,getPendingIntent("onClickLeftImage"));
//        views.setOnClickPendingIntent(R.id.test_tv_title,getPendingIntent("onClickTextTitle"));

        return builder
                .setSmallIcon(R.drawable.test_baseline_school_black_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.test_baseline_school_black_24))
                .setContentTitle("自定义")
                .setContentText("RemoteViews")
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(getTaskStackPending())
                .setCustomContentView(views)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)//锁屏通知
                .setOngoing(true)//true不可以滑动删除 false 可以滑动删除  使用这个属性 通知栏默认展开状态 说的不是很明白 可以自己试一下
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder customBigNotify(NotificationCompat.Builder builder) {

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.test_custom_remote_view_dj);


        views.setOnClickPendingIntent(R.id.test_iv_large_img, getPendingIntent("onClickLargeImage"));
        views.setOnClickPendingIntent(R.id.test_iv_src_right, getPendingIntent("onClickRightImage"));

        views.setOnClickPendingIntent(R.id.test_iv_src, getPendingIntent("onClickLeftImage"));
        views.setOnClickPendingIntent(R.id.test_tv_title, getPendingIntent("onClickTextTitle"));

        return builder
                .setSmallIcon(R.drawable.test_baseline_school_black_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.test_baseline_school_black_24))
                .setContentTitle("自定义")
                .setContentText("RemoteViews")
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(getTaskStackPending())
                .setCustomBigContentView(views)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)//true不可以滑动删除 false 可以滑动删除  使用这个属性 通知栏默认展开状态 说的不是很明白 可以自己试一下
                .setAutoCancel(true);
    }


    public NotificationCompat.Builder bigPictureStyleNotify(NotificationCompat.Builder builder) {
        return builder
                .setSmallIcon(R.drawable.test_baseline_school_black_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.test_baseline_school_black_24))
                .setContentTitle("折叠式:BigPicture")
                .setContentText("对折叠样式的说明")
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.test_baseline_school_black_24))
                )
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(getTaskStackPending())
                // .setOngoing(true)//true不可以滑动删除 false 可以滑动删除  使用这个属性 通知栏默认展开状态 说的不是很明白 可以自己试一下
                .setAutoCancel(true);
    }


    public NotificationCompat.Builder inboxStyleNotify(NotificationCompat.Builder builder) {
        return builder
                .setSmallIcon(R.drawable.test_baseline_school_black_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.test_baseline_school_black_24))
                .setContentTitle("折叠式:Inbox")
                .setContentText("对折叠样式的说明")
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("第一行数据")
                        .addLine("第二行数据第二行数据第二行数据第二行数据第二行数据第二行数据第二行数据第二行数据第二行数据第二行数据第二行数据第二行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                        .addLine("第一行数据")
                )
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(getTaskStackPending())
                //.setOngoing(true)//true不可以滑动删除 false 可以滑动删除  使用这个属性 通知栏默认展开状态 说的不是很明白 可以自己试一下
                .setAutoCancel(true);
    }


    public NotificationCompat.Builder bigTextStyleNotify(NotificationCompat.Builder builder) {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

        bigTextStyle.bigText("Android4.1+之后出现几种折叠样式，均需要使用setStyle()方法设定 setStyle()传递一个NotificationCompat.Style对象，它是一个抽象类，Android为我们提供了三个实现类，用于显示不同的场景。Android4.1+之后出现几种折叠样式，均需要使用setStyle()方法设定 setStyle()传递一个NotificationCompat.Style对象，它是一个抽象类，Android为我们提供了三个实现类，用于显示不同的场景。Android4.1+之后出现几种折叠样式，均需要使用setStyle()方法设定 setStyle()传递一个NotificationCompat.Style对象，它是一个抽象类，Android为我们提供了三个实现类，用于显示不同的场景。");
        bigTextStyle.setBigContentTitle("折叠式:BigText"); //替换掉了 setContentTitle
        bigTextStyle.setSummaryText("摘要");
        return builder
                .setSmallIcon(R.drawable.test_baseline_school_black_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.test_baseline_school_black_24))
                .setContentTitle("折叠式:BigText")
                .setContentText("对折叠样式的说明")
                .setStyle(bigTextStyle)
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(getTaskStackPending())
                // .setOngoing(true)//true不可以滑动删除 false 可以滑动删除  使用这个属性 通知栏默认展开状态 说的不是很明白 可以自己试一下
                .setAutoCancel(true);
    }


    /**
     * 默认样式的通知栏
     *
     * @param context
     * @param builder
     * @return
     */
    public NotificationCompat.Builder defaultNotify(Context context, NotificationCompat.Builder builder) {

        builder.setContentTitle("梦里挑灯看剑")
                .setContentText("江山如此多娇，引无数英雄竞折腰")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.test_baseline_school_black_24))// 右侧显示大图标
                .setSmallIcon(R.drawable.test_baseline_school_black_24)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)//显示时间 默认true
                .setDefaults(NotificationCompat.DEFAULT_ALL)//默认提示 铃声 震动
                .setPriority(NotificationCompat.PRIORITY_MAX)//设置优先级 和默认提示 搭配使用 可实现 悬浮提示效果 注意：部分机型不可用
                .setContentIntent(getTaskStackPending())
                //.setContentInfo("ContentInfo")
                .setNumber(5)
                .setOngoing(true)//true不可以滑动删除 false 可以滑动删除
                .setAutoCancel(true)
        ;
        return builder;
    }


    /**
     * 进度条通知栏
     *
     * @param builder
     * @return
     */
    public NotificationCompat.Builder progressNotify(final NotificationCompat.Builder builder) {
        builder.setContentTitle("正在下载:")
                .setAutoCancel(false)
                .setOngoing(true)
                .setShowWhen(false)//显示时间 默认true
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.test_baseline_school_black_24))
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.test_baseline_school_black_24);

        new Thread() {
            @Override
            public void run() {
                int progress = 0;

                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress += 10;
                    builder.setProgress(100, progress, false);
                    builder.setContentTitle("正在下载:" + progress + "%");
                    builder.setContentInfo(progress + "%");
                    mNotifyManager.notify(500, builder.build());
                }
                builder.setContentTitle("下载完成:");
                mNotifyManager.notify(500, builder.build());
                //mNotifyManager.cancel(500);

            }
        }.start();
        return builder;
    }


    public PendingIntent getTaskStackPending() {
        //创建 Intent 以启动 Activity。
        Intent resultIntent = new Intent(this, TestReceiveActivity.class);
        //创建堆栈生成器
        TaskStackBuilder builder = TaskStackBuilder.create(this);
        //将返回栈添加到堆栈生成器。 对于在清单文件中所定义层次结构内的每个 Activity，返回栈均包含可启动 Activity 的 Intent 对象。此方法还会添加一些可在全新任务中启动堆栈的标志。
        builder.addParentStack(TestReceiveActivity.class);
        builder.addNextIntent(resultIntent);//添加可从通知中启动 Activity 的 Intent。 将在第一步中创建的 Intent 作为 addNextIntent() 的参数传递
        /**
         * 如需，请通过调用 TaskStackBuilder.editIntentAt() 向堆栈中的 Intent 对象添加参数。有时，需要确保目标 Activity 在用户使用“返回”导航回它时会显示有意义的数据。
         */
        //通过调用 getPendingIntent() 获得此返回栈的 PendingIntent。 然后，您可以使用此 PendingIntent 作为 setContentIntent() 的参数。
        return builder.getPendingIntent(200, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public PendingIntent getPendingIntent(Context context, int requestCode, Intent intent) {
        return PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    /**
     * 必需的通知内容
     * 少一个都报错
     */
    @SuppressLint("LongLogTag")
    private void showNotify() {

        Uri internalContentUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = new String[]{"1", "2", "3", "4", "5", "6"};
        inboxStyle.setBigContentTitle("Event tracker details:");
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        other = new Notification.Builder(this);
        other.setContentTitle("替换")
                .setContentText("替换内容")
                .setSmallIcon(R.drawable.test_ic_launcher_background);


        mBuilder = new NotificationCompat.Builder(this);

        Intent intent = new Intent("abcd");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 12, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setContentText("今天中午吃什么?" + System.currentTimeMillis())
                .setSmallIcon(R.drawable.test_ic_launcher_background)
                .setContentTitle("我是标题")
                //无效
                //.setTicker("悬浮通知")//设置收到通知时在顶部显示的文字信息
                //无效
                .setWhen(System.currentTimeMillis())//设置通知时间 一般设置的是收到通知时的System.currentTimeMillis()
                .setAutoCancel(true)//用户点击Notification点击面板后是否让通知取消(默认不取消)
                //闪光灯 无效
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、 使用默认（defaults）属性，可以组合多个属性，
                .setPriority(Notification.PRIORITY_MAX) //设置优先级
                /**
                 Notification.DEFAULT_VIBRATE(添加默认震动提醒)；
                 Notification.DEFAULT_SOUND(添加默认声音提醒)；
                 Notification.DEFAULT_LIGHTS(添加默认三色灯提醒)
                 Notification.DEFAULT_ALL(添加默认以上3种全部提醒)
                 */
                //.setVibrate(new long[]{0, 300, 500, 700, 500, 1000, 500, 1000})//设置自定义震动 延迟0ms，然后振动300ms，在延迟500ms， 接着再振动700ms，
                // TODO: 2019/4/2 无效
                //.setLights(0xffff0000, 1000, 0)//设置三色灯，参数依次是：灯光颜色， 亮持续时间，暗的时间，不是所有颜色都可以，这跟设备有关，有些手机还不带三色灯； 另外，还需要为Notification设置flags为Notification.FLAG_SHOW_LIGHTS才支持三色灯提醒！
                // TODO: 2019/4/2 带测试
                //.setSound((Uri.withAppendedPath(internalContentUri, "0")))//设置接收到通知时的铃声，可以用系统的，也可以自己设置  获取Android多媒体库内的铃声
                //.setSound(Uri.parse("file:///sdcard/a.mp3"))// 有效
                //.setSound(Uri.parse("android.resource://com.aa.cn.bb/" + R.raw.hah));

                //.setOngoing(true)//：设置为ture，表示它为一个正在进行的通知。他们通常是用来表示 一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载, 同步操作,主动网络连接)
                //.setProgress()

                //.setStyle(inboxStyle)


                /**
                 * 控制在安全锁定屏幕上显示的通知中可见的详细级别。 调用 setVisibility()

                 VISIBILITY_PUBLIC 显示通知的完整内容。
                 VISIBILITY_SECRET 不会在锁定屏幕上显示此通知的任何部分。
                 VISIBILITY_PRIVATE 显示通知图标和内容标题等基本信息，但是隐藏通知的完整内容。
                 */
                //.setVisibility(Notification.VISIBILITY_PRIVATE)
                // TODO: 2019/4/2 无效
                //设置 VISIBILITY_PRIVATE 后，您还可以提供其中隐藏了某些详细信息的替换版本通知内容。
                // 例如，短信 应用可能会显示一条通知，指出“您有 3 条新短信”，但是隐藏了短信内容和发件人。
                // 要提供此替换版本的通知，请先使用 NotificationCompat.Builder 创建替换通知。创建专用通知对象时，请通过 setPublicVersion() 方法为其附加替换通知。
                //.setPublicVersion(other.build())

                //无效
                //.setCategory("im")


                .setFullScreenIntent(null, true)

        //.setContentIntent(getTaskStackPending("onClick"))//设置意图

        //.setDeleteIntent(getPendingIntent("onDelete"))//设置删除意图

        ;


        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.d(TAG, "showNotify: " + id++);
        mNotifyManager.notify(id, mBuilder.build());

    }

    private PendingIntent getPendingIntent(String action) {
        Intent intent = new Intent(this, TestReceiveActivity.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 200, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        /**
         FLAG_ONE_SHOT 表示返回的PendingIntent仅能执行一次，执行完后自动取消
         FLAG_NO_CREATE 表示如果描述的PendingIntent不存在，并不创建相应的PendingIntent，而是返回NULL
         FLAG_CANCEL_CURRENT 表示相应的PendingIntent已经存在，则取消前者，然后创建新的PendingIntent， 这个有利于数据保持为最新的，可以用于即时通信的通信场景
         FLAG_UPDATE_CURRENT 表示更新的PendingIntent
         */
        return pendingIntent;
    }


    public void am(PendingIntent pendingIntent) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 3000, pendingIntent);
    }


    public void showProgressNotify() {
        mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.test_ic_launcher_background);
// Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr += 5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, incr, true);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(0, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        mNotifyManager.notify(id++, mBuilder.build());
                    }
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();
    }

    public void setCustomView(String packageName, int layoutId) {
        mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        RemoteViews views = new RemoteViews(packageName, layoutId);

        mBuilder.setContent(views);
    }
}
