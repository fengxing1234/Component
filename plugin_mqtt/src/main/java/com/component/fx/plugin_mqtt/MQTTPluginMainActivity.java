package com.component.fx.plugin_mqtt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.component.fx.plugin_base.utils.ToastUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class MQTTPluginMainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MQTT_TOPIC = "fengxing";
    private TextView tvResult;

    private MqttAndroidClient client;


    private static String userName = "admin";
    private static String password = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mqtt_activity_plugin_main);
        findViewById(R.id.mqtt_btn_connect).setOnClickListener(this);
        findViewById(R.id.mqtt_btn_subscribe).setOnClickListener(this);
        findViewById(R.id.mqtt_btn_publish).setOnClickListener(this);
        findViewById(R.id.mqtt_btn_is_connect).setOnClickListener(this);
        tvResult = (TextView) findViewById(R.id.mqtt_tv_result);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mqtt_btn_connect:
                try {
                    client = new MqttAndroidClient(this, "tcp://192.168.8.100:61613", UUID.randomUUID().toString());
                    client.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {

                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });//设置回调函数

                    MqttConnectOptions mOptions = new MqttConnectOptions();
                    mOptions.setAutomaticReconnect(false);//断开后，是否自动连接
                    mOptions.setCleanSession(true);//是否清空客户端的连接记录。若为true，则断开后，broker将自动清除该客户端连接信息
                    mOptions.setConnectionTimeout(60);//设置超时时间，单位为秒
                    mOptions.setUserName("admins");//设置用户名。跟Client ID不同。用户名可以看做权限等级
                    mOptions.setPassword("password".toCharArray());//设置登录密码
                    mOptions.setKeepAliveInterval(60);//心跳时间，单位为秒。即多长时间确认一次Client端是否在线
                    mOptions.setMaxInflight(10);//允许同时发送几条消息（未收到broker确认信息）
                    //mOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);//选择MQTT版本
                    client.connect(mOptions, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.d("feng", "onSuccess: ");
                            try {
                                client.subscribe("1111", 1);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.d("feng", "onFailure: ");
                            exception.printStackTrace();
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.mqtt_btn_subscribe:
                new Thread() {
                    @Override
                    public void run() {
                        subscribe();
                    }
                }.start();

                break;
            case R.id.mqtt_btn_publish:
                new Thread() {
                    @Override
                    public void run() {
                        publish();
                    }
                }.start();
                break;
            case R.id.mqtt_btn_is_connect:
                boolean connected = client.isConnected();
                tvResult.setText(connected ? "true" : "false");
                break;
        }
    }

    private void publish() {
        MqttMessage message = new MqttMessage();
        try {
            MqttClient client = new MqttClient("tcp://192.168.8.100:61613", UUID.randomUUID().toString(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);

            MqttTopic topic = client.getTopic(MQTT_TOPIC);

            message.setQos(1);
            message.setRetained(false);
            message.setPayload("message from server".getBytes());
            client.connect(options);

            while (true) {
                MqttDeliveryToken token = topic.publish(message);
                token.waitForCompletion();
                System.out.println("已经发送");
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subscribe() {
        try {
            // host为主机名，clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，
            // MemoryPersistence设置clientid的保存形式，默认为以内存保存
            // 设备id不要太骚气！！！！！！！
            MqttClient mqttClient = new MqttClient("tcp://192.168.8.100:61613", UUID.randomUUID().toString(), new MemoryPersistence());
            // 配置参数信息
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
            // 这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置用户名
            options.setUserName(userName);
            // 设置密码
            options.setPassword(password.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 连接
            mqttClient.connect(options);
            // 订阅
            mqttClient.subscribe(MQTT_TOPIC);
            // 设置回调
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("connectionLost");
                }

                @Override
                public void messageArrived(String s, final MqttMessage mqttMessage) throws Exception {
                    System.out.println("Topic: " + s + " Message: " + mqttMessage.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.toast(mqttMessage.toString());
                        }
                    });

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
