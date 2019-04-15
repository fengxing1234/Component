package com.component.fx.plugin_mqtt;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.component.fx.plugin_base.utils.ToastUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;

/**
 * https://github.com/tokudu/AndroidPushNotificationsDemo/blob/master/src/com/tokudu/demo/PushService.java
 */
public class MqttPushService extends Service {


    // TODO: 2019/4/13 未测试



    private static final String TAG = "MqttPushService";
    private long mStartTime;
    private ConnectionLog mLog;
    private ConnectivityManager mConnMan;
    private NotificationManager mNotifMan;
    private SharedPreferences mPrefs;
    private boolean mStarted;
    private MQTTConnection mConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // MQTT client ID, which is given the broker. In this example, I also use this for the topic header.
    // You can use this to run push notifications for multiple apps with one MQTT broker.
    public static String MQTT_CLIENT_ID = "mqtt_client_id";


    // These are the actions for the service (name are descriptive enough)
    private static final String ACTION_START = MQTT_CLIENT_ID + ".START";
    private static final String ACTION_STOP = MQTT_CLIENT_ID + ".STOP";
    private static final String ACTION_KEEPALIVE = MQTT_CLIENT_ID + ".KEEP_ALIVE";
    private static final String ACTION_RECONNECT = MQTT_CLIENT_ID + ".RECONNECT";


    //无论服务是否已经启动，都存储在preferences中
    public static final String PREF_STARTED = "isStarted";

    public static final String PREF_DEVICE_ID = "deviceID";

    public static final String PREF_RETRY = "retryInterval";


    private static final String MQTT_HOST = "209.124.50.174";
    private static int MQTT_BROKER_PORT_NUM = 1883;


    // Retry intervals, when the connection is lost.
    private static final long INITIAL_RETRY_INTERVAL = 1000 * 10;
    private static final long MAXIMUM_RETRY_INTERVAL = 1000 * 60 * 30;
    // This the application level keep-alive interval, that is used by the AlarmManager to keep the connection active, even when the device goes to sleep.
    //这是应用程序级别保持活动间隔，即使设备进入休眠状态，AlarmManager也会使用该间隔保持连接处于活动状态。
    private static final long KEEP_ALIVE_INTERVAL = 1000 * 60 * 28;


    // Set quality of services to 0 (at most once delivery), since we don't want push notifications more than once. However, this means that some messages might get lost (delivery is not guaranteed)
    //将服务质量设置为0（最多一次交付），因为我们不希望推送通知多次到达。 但是，这意味着某些消息可能会丢失（无法保证交付）
    private static int[] MQTT_QUALITIES_OF_SERVICE = {0};
    private static int MQTT_QUALITY_OF_SERVICE = 0;


    // The broker should not retain any messages.
    //代理不应该保留任何信息
    private static boolean MQTT_RETAINED_PUBLISH = false;

    public static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, MqttPushService.class);
        i.setAction(ACTION_START);
        ctx.startService(i);
    }

    // Static method to stop the service
    public static void actionStop(Context ctx) {
        Intent i = new Intent(ctx, MqttPushService.class);
        i.setAction(ACTION_STOP);
        ctx.startService(i);
    }


    // Static method to send a keep alive message 发送包活消息
    public static void actionPing(Context ctx) {
        Intent i = new Intent(ctx, MqttPushService.class);
        i.setAction(ACTION_KEEPALIVE);
        ctx.startService(i);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStartTime = System.currentTimeMillis();
        try {
            mLog = new ConnectionLog();
            Log.i(TAG, "Opened log at " + mLog.getPath());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open log", e);
            e.printStackTrace();
        }

        mPrefs = getSharedPreferences(TAG, MODE_PRIVATE);
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mNotifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        /* If our process was reaped by the system for any reason we need
         * to restore our state with merely a call to onCreate.  We record
         * the last "started" value and restore it here if necessary. */
        handleCrashedService();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("Service started with intent=" + intent);
        // Do an appropriate action based on the intent.
        if (ACTION_STOP.equals(intent.getAction())) {
            stop();
            stopSelf();
        } else if (ACTION_START.equals(intent.getAction())) {
            start();
        } else if (ACTION_KEEPALIVE.equals(intent.getAction())) {
            keepAlive();
        } else if (ACTION_RECONNECT.equals(intent.getAction())) {
            if (isNetworkAvailable()) {
                reconnectIfNecessary();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private synchronized void reconnectIfNecessary() {
        if (mStarted && mConnection == null) {
            log("Reconnecting...");
            connect();
        }
    }

    private synchronized void keepAlive() {

        try {
            // Send a keep alive, if there is a connection.
            if (mStarted == true && mConnection != null) {
                mConnection.sendKeepAlive();
            }
        } catch (MqttException e) {
            log("MqttException: " + (e.getMessage() != null ? e.getMessage() : "NULL"), e);
            e.printStackTrace();
            mConnection.disconnect();
            mConnection = null;
            cancelReconnect();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        log("Service destroyed (started=" + mStarted + ")");
        // Stop the services, if it has been started
        if (mStarted) {
            stop();
        }
        try {
            if (mLog != null)
                mLog.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        // Do nothing, if the service is not running.
        if (!mStarted) {
            Log.w(TAG, "Attempt to stop connection not active.");
            return;
        }

        // Save stopped state in the preferences
        setStarted(false);

        // Remove the connectivity receiver
        unregisterReceiver(mConnectivityChanged);


        // Any existing reconnect timers should be removed, since we explicitly stopping the service.
        //应删除任何现有的重新连接计时器，因为我们明确停止了服务
        cancelReconnect();

        // Destroy the MQTT connection if there is one
        if (mConnection != null) {
            mConnection.disconnect();
            mConnection = null;
        }

    }

    //如果服务器已被系统销毁然后重新启动，则此方法可以满足任何必要的清理需求
    // This method does any necessary clean-up need in case the server has been destroyed by the system and then restarted
    private void handleCrashedService() {
        if (wasStarted() == true) {
            log("Handling crashed service...");

            // stop the keep alives
            stopKeepAlives();

            // Do a clean start
            start();
        }
    }

    private synchronized void start() {
        log("Starting service...");
        //如果服务已在运行，则不执行任何操作。
        if (mStarted == true) {
            Log.w(TAG, "Attempt to start connection that is already active");
            return;
        }
        //建立MQTT连接
        connect();
        //注册连接侦听器
        registerReceiver(mConnectivityChanged, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private synchronized void connect() {
        log("Connecting...");
        String deviceID = mPrefs.getString(PREF_DEVICE_ID, null);
        if (deviceID == null) {
            log("Device ID not found.");
        } else {

            try {
                mConnection = new MQTTConnection(this, MQTT_HOST, deviceID);
            } catch (MqttException e) {
                // Schedule a reconnect, if we failed to connect
                log("MqttException: " + (e.getMessage() != null ? e.getMessage() : "NULL"));
                if (isNetworkAvailable()) {
                    scheduleReconnect(mStartTime);
                }
            }
            setStarted(true);
        }
    }

    // We schedule a reconnect based on the starttime of the service
    private void scheduleReconnect(long startTime) {
        // the last keep-alive interval
        long interval = mPrefs.getLong(PREF_RETRY, INITIAL_RETRY_INTERVAL);

        // Calculate the elapsed time since the start
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;

        if (elapsed < interval) {
            interval = Math.min(interval * 4, MAXIMUM_RETRY_INTERVAL);
        } else {
            interval = INITIAL_RETRY_INTERVAL;
        }

        log("Rescheduling connection in " + interval + "ms.");

        mPrefs.edit().putLong(PREF_RETRY, interval).apply();

        // Schedule a reconnect using the alarm manager.
        Intent i = new Intent();
        i.setClass(this, MqttPushService.class);
        i.setAction(ACTION_RECONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, now + interval, pi);
    }

    // Check if we are online
    private boolean isNetworkAvailable() {
        NetworkInfo info = mConnMan.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }

    // Remove the scheduled reconnect
    private void cancelReconnect() {
        Intent i = new Intent();
        i.setClass(this, MqttPushService.class);
        i.setAction(ACTION_RECONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    private void startKeepAlives() {
        Intent intent = new Intent();
        intent.setClass(this, MqttPushService.class);
        intent.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + KEEP_ALIVE_INTERVAL,
                KEEP_ALIVE_INTERVAL, pi);
    }

    private void stopKeepAlives() {
        Intent intent = new Intent();
        intent.setClass(this, MqttPushService.class);
        intent.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    private void setStarted(boolean started) {
        mPrefs.edit().putBoolean(PREF_STARTED, started).apply();
        mStarted = started;
    }


    private boolean wasStarted() {
        return mPrefs.getBoolean(PREF_STARTED, false);
    }

    // log helper function
    private void log(String message) {
        log(message, null);
    }

    private void log(String message, Throwable e) {
        if (e != null) {
            Log.e(TAG, message, e);

        } else {
            Log.i(TAG, message);
        }

        if (mLog != null) {
            try {
                mLog.println(message);
            } catch (IOException ex) {
            }
        }
    }

    private void showNotification(String s) {
        ToastUtil.toast("收到新消息 :  " + s);
    }

    //This receiver listeners for network changes and updates the MQTT connection accordingly
    //此接收器侦听器用于网络更改并相应地更新MQTT连接
    private BroadcastReceiver mConnectivityChanged = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            boolean hasConnectivity = (info != null && info.isConnected());
            log("Connectivity changed: connected=" + hasConnectivity);
            if (hasConnectivity) {
                reconnectIfNecessary();
            } else if (mConnection != null) {
                // if there no connectivity, make sure MQTT connection is destroyed
                //如果没有连接，请确保销毁MQTT连接
                mConnection.disconnect();
                cancelReconnect();
                mConnection = null;
            }

        }
    };

    private class MQTTConnection {


        private MqttAndroidClient mqttClient;

        public MQTTConnection(Context context, String mqttHost, String initTopic) throws MqttException {
            String mqttConnSpec = "tcp://" + mqttHost + ":" + MQTT_BROKER_PORT_NUM;

            String clientID = MQTT_CLIENT_ID + "/" + mPrefs.getString(PREF_DEVICE_ID, "");

            // Create the client and connect
            mqttClient = new MqttAndroidClient(context, mqttConnSpec, clientID, new MemoryPersistence());
            mqttClient.connect(getOptions(), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: ");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "onFailure: ");
                }
            });

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d(TAG, "connectionLost: ");
                    log("Loss of connection" + "connection downed");

                    stopKeepAlives();
                    mConnection = null;

                    if (isNetworkAvailable()) {
                        reconnectIfNecessary();
                    }

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d(TAG, "messageArrived: ");
                    String s = new String(message.getPayload());
                    showNotification(s);
                    log("Got message: " + s);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "deliveryComplete: ");
                }
            });

            initTopic = MQTT_CLIENT_ID + "/" + initTopic;

            subscribeToTopic(initTopic);

            log("Connection established to " + mqttHost + " on topic " + initTopic);

            // Save start time
            mStartTime = System.currentTimeMillis();
            // Star the keep-alives
            startKeepAlives();

        }

        /*
         * Send a request to the message broker to be sent messages published with
         *  the specified topic name. Wildcards are allowed.
         */
        private void subscribeToTopic(String initTopic) throws MqttException {
            if (mqttClient == null || !mqttClient.isConnected()) {
                //quick sanity check - don't try and subscribe if we don't have a connection
                log("Connection error" + "No connection");
            } else {
                String[] topics = {initTopic};
                mqttClient.subscribe(topics, MQTT_QUALITIES_OF_SERVICE);
            }
        }

        /*
         * Sends a message to the message broker, requesting that it be published
         *  to the specified topic.
         */

        public void publishToTopic(String topicName, String message) throws MqttException {
            if ((mqttClient == null) || (!mqttClient.isConnected())) {
                log("No connection to public to");
            } else {
                mqttClient.publish(topicName, message.getBytes(), MQTT_QUALITY_OF_SERVICE, MQTT_RETAINED_PUBLISH);
            }
        }

        public void disconnect() {
            try {
                stopKeepAlives();
                mqttClient.disconnect();
            } catch (MqttException e) {
                log("MqttException" + (e.getMessage() != null ? e.getMessage() : " NULL"), e);
                e.printStackTrace();
            }
        }

        public void sendKeepAlive() throws MqttException {
            log("Sending keep alive");
            // publish to a keep-alive topic
            publishToTopic(MQTT_CLIENT_ID + "/keepalive", mPrefs.getString(PREF_DEVICE_ID, ""));
        }

        private MqttConnectOptions getOptions() {
            return new MqttConnectOptions();
        }
    }
}


