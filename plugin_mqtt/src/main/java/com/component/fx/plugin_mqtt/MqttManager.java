package com.component.fx.plugin_mqtt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.component.fx.plugin_base.base.BaseApplication;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class MqttManager implements MqttCallback {

    private static final String TAG = "MqttManager";


    private static final String BASE_URL = "tcp://192.168.0.103:61680";
    private static final String CLIENT_ID = "313sfds23234";

    private static MqttManager instance;
    private MqttClient client;
    private MqttAsyncClient asyncClient;
    private MqttAndroidClient androidClient;

    private MqttManager() {

    }

    public static synchronized MqttManager getInstance() {
        if (instance == null) {
            synchronized (MqttManager.class) {
                if (instance == null) {
                    instance = new MqttManager();
                }
            }
        }
        return instance;
    }


    public void getAndroidClient() {
        String client_id = UUID.randomUUID().toString();
        androidClient = new MqttAndroidClient(BaseApplication.getAppContext(), BASE_URL, client_id, new MemoryPersistence());
        try {
            androidClient.connect(getConnectOptions(), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: ");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "onFailure: ");
                    exception.printStackTrace();
                }
            });
            androidClient.setCallback(this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnect() {
        return androidClient.isConnected();
    }

    public MqttManager createAsyncClient() {
        String client_id = UUID.randomUUID().toString();
        try {
            asyncClient = new MqttAsyncClient(BASE_URL, client_id, new MemoryPersistence(), new MqttPingSender() {
                @Override
                public void init(ClientComms comms) {
                    Log.d(TAG, "init: " + comms);
                }

                @Override
                public void start() {
                    Log.d(TAG, "start: ");
                }

                @Override
                public void stop() {
                    Log.d(TAG, "stop: ");
                }

                @Override
                public void schedule(long delayInMilliseconds) {
                    Log.d(TAG, "schedule: " + delayInMilliseconds);
                }
            });
            doAsyncConnect();
            asyncClient.setCallback(this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void doAsyncConnect() {
        if (asyncClient == null) throw new IllegalArgumentException("请先初始化 MqttAsyncClient");
        try {
            asyncClient.connect(getConnectOptions(), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: " + asyncActionToken);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "onFailure: " + asyncActionToken);
                    exception.printStackTrace();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void connectionLost(Throwable cause) {
        Log.d(TAG, "connectionLost: " + cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, "messageArrived: " + topic);
        Log.d(TAG, "messageArrived: " + message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "deliveryComplete: " + token);
    }

    private MqttConnectOptions getConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);//重连不保持状态
//        if(this.userID!=null&&this.userID.length()>0&&this.passWord!=null&&this.passWord.length()>0){
//            options.setUserName(this.userID);//设置服务器账号密码
//            options.setPassword(this.passWord.toCharArray());
//        }
        options.setConnectionTimeout(10);//设置连接超时时间
        options.setKeepAliveInterval(30);//设置保持活动时间，超过时间没有消息收发将会触发ping消息确认

        return options;
    }


    public void disAsyncConnect() {
        if (asyncClient != null) {
            try {
                asyncClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isAsyncConnect() {
        return asyncClient != null && asyncClient.isConnected();
    }


    /**
     * 创建连接
     */
    public void createConnect() {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            client = new MqttClient(BASE_URL, CLIENT_ID, persistence);

//            MqttConnectOptions options = new MqttConnectOptions();
//
//            // 清除缓存
//            options.setCleanSession(true);
//            // 设置超时时间，单位：秒
//            options.setConnectionTimeout(10);
//            // 心跳包发送间隔，单位：秒
//            options.setKeepAliveInterval(20);

//            // 用户名
//            options.setUserName("admin");
//            // 密码
//            options.setPassword("admin".toCharArray());


            //options.setWill("aaa","".getBytes(),1,false);
            client.setCallback(new MqttCallback() {

                /**
                 * 连接丢失 重连
                 * @param cause
                 */
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d(TAG, "connectionLost: ");
                }

                /**
                 * subscribe后得到的消息会执行到这里面
                 * @param topic
                 * @param message
                 * @throws Exception
                 */
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d(TAG, "messageArrived: " + topic);
                    Log.d(TAG, "messageArrived: " + message.toString());
                }

                /**
                 * 	publish后会执行到这里
                 * @param token
                 */
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "deliveryComplete: " + token);
                }
            });
            client.connect();

        } catch (MqttException e) {
            Log.d(TAG, "createConnect: " + e.getReasonCode());
            e.printStackTrace();
        }
    }


    public void doConnect() {
        if (isConnected() && isConnectIsNomarl()) {
            try {
                client.connect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    public void publish() {
        String body = "我的名字是冯星";
        MqttMessage message = new MqttMessage(body.getBytes());
        message.setQos(1);
        message.setPayload(body.getBytes());
        try {
            client.publish("topicName", message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe() {

        try {
            client.subscribe("topicName", 1);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否连接
     */
    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }


    /**
     * 释放单例, 及其所引用的资源
     */
    public void release() {
        try {
            if (instance != null) {
                disConnect();
                instance = null;
            }
        } catch (Exception e) {
            Log.e("MqttManager", "release : " + e.toString());
        }
    }

    /**
     * 取消连接
     */
    public void disConnect() throws MqttException {
        if (isConnected())
            client.disconnect();
    }
}
