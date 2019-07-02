package com.component.fx.plugin_test.test_activity.activityLaunchMode;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.component.fx.plugin_test.R;

/**
 * standard：标准启动模式（默认启动模式），每次都会启动一个新的activity实例。
 * singleTop：单独使用使用这种模式时，如果Activity实例位于当前任务栈顶，就重用栈顶实例，而不新建，并回调该实例onNewIntent()方法，否则走新建流程。
 * singleTask：这种模式启动的Activity只会存在相应的Activity的taskAffinit任务栈中，同一时刻系统中只会存在一个实例，已存在的实例被再次启动时，会重新唤起该实例，并清理当前Task任务栈该实例之上的所有Activity，同时回调onNewIntent()方法。
 * singleInstance：这种模式启动的Activity独自占用一个Task任务栈，同一时刻系统中只会存在一个实例，已存在的实例被再次启动时，只会唤起原实例，并回调onNewIntent()方法。
 * <p>
 * 上面的场景仅仅适用于Activity启动Activity，并且采用的都是默认Intent，没有额外添加任何Flag
 */
public abstract class LaunchModeBase extends AppCompatActivity {

    private static final String TAG = LaunchModeBase.class.getSimpleName();
    private String mCurrentPageClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentPageClass = getClass().getSimpleName();
        Log.d(TAG, "onCreate: " + mCurrentPageClass + "  hashCode = " + this.hashCode());
        setContentView(R.layout.test_launch_mode_activity);
        findViewById(R.id.btn_next_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStartActivity();
            }
        });

        TextView tvCurrentPage = findViewById(R.id.tv_current_page);
        tvCurrentPage.setText(getCurrentPageText());
        tvCurrentPage.setBackgroundColor(getCurrentPageColor());

        dumpTaskAffinity();
    }

    protected abstract int getCurrentPageColor();

    protected abstract String getCurrentPageText();

    public abstract void doStartActivity();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: " + mCurrentPageClass + "  hashCode = " + this.hashCode());
        dumpTaskAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + mCurrentPageClass);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: " + mCurrentPageClass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + mCurrentPageClass);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + mCurrentPageClass);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + mCurrentPageClass);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + mCurrentPageClass);
    }

    protected void dumpTaskAffinity() {
        PackageManager packageManager = getPackageManager();
        try {
            ActivityInfo activityInfo = packageManager.getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            Log.d(TAG, mCurrentPageClass + "dumpTaskAffinity = " + activityInfo.taskAffinity + "  taskId = " + getTaskId());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注意坑：
     *
     * 1. TaskAffinity 格式 必须以包名的形式填写 否则安装不上 例如 <activity android:name=".test_activity.activityLaunchMode.LaunchModeA" android:taskAffinity="www.zhyen.com" />
     *
     * 2.taskAffinity属性不对standard和singleTop模式有任何影响，即时你指定了该属性为其他不同的值，这两种启动模式下不会创建新的task.
     * 2019-06-25 23:33:14.953 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 189756207
     * 2019-06-25 23:33:14.977 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = www.zhyen.com  taskId = 4951
     * 2019-06-25 23:33:14.981 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-25 23:33:14.982 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-25 23:36:37.486 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-25 23:36:37.513 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeB  hashCode = 68886116
     * 2019-06-25 23:36:37.533 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4951
     * 2019-06-25 23:36:37.537 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-25 23:36:37.538 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-25 23:36:37.834 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     * 2019-06-25 23:36:39.570 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-25 23:36:39.571 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onNewIntent: LaunchModeB  hashCode = 68886116
     * 2019-06-25 23:36:39.573 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4951
     * 2019-06-25 23:36:39.573 14559-14559/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     *
     * 可以看出来  就算指定了taskAffinity taskId还是相同的
     */


    /**
     * 启动模式 standard-默认模式 A-->B-->C-->D-->E
     * <activity android:name=".test_activity.activityLaunchMode.LaunchModeA"/>
     * <activity android:name=".test_activity.activityLaunchMode.LaunchModeB"/>
     * <activity android:name=".test_activity.activityLaunchMode.LaunchModeC"/>
     * <activity android:name=".test_activity.activityLaunchMode.LaunchModeD"/>
     * <activity android:name=".test_activity.activityLaunchMode.LaunchModeE"/>
     * <p>
     * 2019-06-25 23:07:25.517 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 168049438
     * 2019-06-25 23:07:25.538 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4947
     * 2019-06-25 23:07:25.542 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-25 23:07:25.543 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-25 23:07:33.809 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-25 23:07:33.834 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeB  hashCode = 80524844
     * 2019-06-25 23:07:33.857 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4947
     * 2019-06-25 23:07:33.862 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-25 23:07:33.864 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-25 23:07:34.148 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     * 2019-06-25 23:07:36.627 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-25 23:07:36.650 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeC  hashCode = 55631067
     * 2019-06-25 23:07:36.670 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeCdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4947
     * 2019-06-25 23:07:36.673 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeC
     * 2019-06-25 23:07:36.674 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeC
     * 2019-06-25 23:07:37.006 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeB
     * 2019-06-25 23:07:37.869 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeC
     * 2019-06-25 23:07:37.895 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeD  hashCode = 196437297
     * 2019-06-25 23:07:37.914 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeDdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4947
     * 2019-06-25 23:07:37.916 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeD
     * 2019-06-25 23:07:37.917 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeD
     * 2019-06-25 23:07:38.200 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeC
     * 2019-06-25 23:07:39.531 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeD
     * 2019-06-25 23:07:39.554 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeE  hashCode = 148514679
     * 2019-06-25 23:07:39.574 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeEdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4947
     * 2019-06-25 23:07:39.576 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeE
     * 2019-06-25 23:07:39.578 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeE
     * 2019-06-25 23:07:39.862 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeD
     * 2019-06-25 23:07:42.313 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeE
     * 2019-06-25 23:07:42.337 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 263487556
     * 2019-06-25 23:07:42.356 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4947
     * 2019-06-25 23:07:42.358 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-25 23:07:42.359 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-25 23:07:42.652 11491-11491/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeE
     *
     * standard启动模式是默认的启动模式，每次启动一个Activity都会新建一个实例不管栈中是否已有该Activity的实例。
     */


    /**
     * singleTop-栈顶复用模式 A-->B-->B
     * 如果新的activity已经位于栈顶，那么这个Activity不会被重写创建，同时它的onNewIntent方法会被调用.
     * 如果栈顶不存在该Activity的实例，则情况与standard模式相同。
     *
     * <activity android:name=".test_activity.activityLaunchMode.LaunchModeA" />
     * <activity  android:name=".test_activity.activityLaunchMode.LaunchModeB" android:launchMode="singleTop" />
     * <activity android:name=".test_activity.activityLaunchMode.LaunchModeC" />
     * <activity android:name=".test_activity.activityLaunchMode.LaunchModeD" />
     * <activity android:name=".test_activity.activityLaunchMode.LaunchModeE" />
     *
     * 2019-06-25 23:12:32.881 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 236723426
     * 2019-06-25 23:12:32.904 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4948
     * 2019-06-25 23:12:32.907 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-25 23:12:32.908 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-25 23:12:39.859 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-25 23:12:39.879 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeB  hashCode = 213544684
     * 2019-06-25 23:12:39.899 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4948
     * 2019-06-25 23:12:39.902 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-25 23:12:39.903 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-25 23:12:40.189 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     * 2019-06-25 23:13:00.835 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-25 23:13:00.835 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onNewIntent: LaunchModeB  hashCode = 213544684
     * 2019-06-25 23:13:00.837 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4948
     * 2019-06-25 23:13:00.837 11937-11937/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     *
     * 当前栈中已有该Activity的实例并且该实例位于栈顶时，不会新建实例，而是复用栈顶的实例，并且会将Intent对象传入，回调onNewIntent方法
     * 当前栈中已有该Activity的实例但是该实例不在栈顶时，其行为和standard启动模式一样，依然会创建一个新的实例
     * 当前栈中不存在该Activity的实例时，其行为同standard启动模式
     */


    /**
     * singleTask-栈内复用模式 A-->B-->C-->D-->E-->A-->B-->C (未设置 taskAffinity 属性)
     *
     *         <activity
     *             android:name=".test_activity.activityLaunchMode.LaunchModeA"
     *             android:taskAffinity="www.zhyen.com" />
     *         <activity
     *             android:name=".test_activity.activityLaunchMode.LaunchModeB"
     *             android:launchMode="singleTop" />
     *         <activity
     *             android:name=".test_activity.activityLaunchMode.LaunchModeC"
     *             android:launchMode="singleTask" />
     *         <activity android:name=".test_activity.activityLaunchMode.LaunchModeD" />
     *         <activity android:name=".test_activity.activityLaunchMode.LaunchModeE" />
     *
     * 2019-06-25 23:46:48.051 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 208181001
     * 2019-06-25 23:46:48.079 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = www.zhyen.com  taskId = 4953
     * 2019-06-25 23:46:48.083 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-25 23:46:48.084 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-25 23:46:50.480 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-25 23:46:50.516 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeB  hashCode = 240414711
     * 2019-06-25 23:46:50.543 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4953
     * 2019-06-25 23:46:50.547 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-25 23:46:50.548 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-25 23:46:50.863 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     * 2019-06-25 23:46:51.115 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-25 23:46:51.140 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeC  hashCode = 124027309
     * 2019-06-25 23:46:51.161 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeCdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4953
     * 2019-06-25 23:46:51.165 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeC
     * 2019-06-25 23:46:51.166 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeC
     * 2019-06-25 23:46:51.457 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeB
     * 2019-06-25 23:46:52.314 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeC
     * 2019-06-25 23:46:52.333 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeD  hashCode = 150735955
     * 2019-06-25 23:46:52.353 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeDdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4953
     * 2019-06-25 23:46:52.356 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeD
     * 2019-06-25 23:46:52.358 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeD
     * 2019-06-25 23:46:52.651 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeC
     * 2019-06-25 23:46:53.923 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeD
     * 2019-06-25 23:46:53.945 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeE  hashCode = 217221481
     * 2019-06-25 23:46:53.965 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeEdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4953
     * 2019-06-25 23:46:53.967 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeE
     * 2019-06-25 23:46:53.968 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeE
     * 2019-06-25 23:46:54.262 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeD
     * 2019-06-25 23:46:59.403 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeE
     * 2019-06-25 23:46:59.424 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 241089870
     * 2019-06-25 23:46:59.446 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = www.zhyen.com  taskId = 4953
     * 2019-06-25 23:46:59.447 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-25 23:46:59.448 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-25 23:46:59.730 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeE
     * 2019-06-25 23:47:02.788 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-25 23:47:02.808 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeB  hashCode = 97620956
     * 2019-06-25 23:47:02.827 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4953
     * 2019-06-25 23:47:02.829 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-25 23:47:02.830 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-25 23:47:03.153 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     * 2019-06-25 23:47:10.814 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeD
     * 2019-06-25 23:47:10.822 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeE
     * 2019-06-25 23:47:10.830 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeA
     * 2019-06-25 23:47:10.842 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-25 23:47:10.922 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onNewIntent: LaunchModeC  hashCode = 124027309
     * 2019-06-25 23:47:10.923 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeCdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4953
     * 2019-06-25 23:47:10.924 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onRestart: LaunchModeC
     * 2019-06-25 23:47:10.924 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeC
     * 2019-06-25 23:47:10.925 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeC
     * 2019-06-25 23:47:11.208 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeB
     * 2019-06-25 23:47:11.209 16316-16316/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeB
     *
     * 日志有点多，为了体现出杀死C上的所有Activit。可以看出并没有重新创建C 而是复用以前的实例。并按顺序杀死 DEA和B 的实例。
     *
     * 使用命令行查看:
     *
     * adb shell dumpsys activity | grep com.component.fx.plugin_test
     *
     *         TaskRecord{83f8c19 #4953 A=com.component.fx.plugin_test U=0 StackId=13 sz=4}
     *         Run #3: ActivityRecord{a70cd37 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeC t4953}
     *         Run #2: ActivityRecord{5ef4c62 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeB t4953}
     *         Run #1: ActivityRecord{12a525a u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeA t4953}
     *         Run #0: ActivityRecord{d90658 u0 com.component.fx.plugin_test/.test_activity.TestPluginMainActivity t4953}
     *
     * LaunchModeC 之上的Activity都出栈了。
     * 总结 如果此时任务栈内已经存在 LaunchModeC 的实例且未位于栈顶，当启动 LaunchModeC 时，会将 LaunchModeC 上方的实例全部出栈让其位于任务栈顶并 LaunchModeC 中的 onNewIntent() 方法会被调用
     * 并且此时任务栈内并不存在 LaunchModeC 的实例，当启动 LaunchModeC 时，会创建一个崭新的 LaunchModeC 实例在栈顶
     */

    /**
     * singleTask-栈内复用模式 A-->B-->C-->D-->E-->A-->B-->C (设置 taskAffinity 属性)
     *
     * 首先按照这个顺序打开 A-->B-->C-->D-->E-->A-->B
     * 使用命令查看 adb shell dumpsys activity | grep com.component.fx.plugin_test
     *
     *      mIntent=Intent { cmp=com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeB }
     *      #4 ActivityRecord{3080e54 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeB t4962} type=standard mode=fullscreen
     *      #3 ActivityRecord{4a2e4b7 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeA t4962} type=standard mode=fullscreen
     *      #2 ActivityRecord{f442906 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeE t4962} type=standard mode=fullscreen
     *      #1 ActivityRecord{7cc1b2c u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeD t4962} type=standard mode=fullscreen
     *      #0 ActivityRecord{cddd8ff u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeC t4962} type=standard mode=fullscreen
     *      #0 TaskRecord{87208c3 #4961 A=com.component.fx.plugin_test U=0 StackId=21 sz=3} type=standard mode=fullscreen
     *      #2 ActivityRecord{5988b22 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeB t4961} type=standard mode=fullscreen
     *      #1 ActivityRecord{30bdce6 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeA t4961} type=standard mode=fullscreen
     *      #0 ActivityRecord{56565ba u0 com.component.fx.plugin_test/.test_activity.TestPluginMainActivity t4961} type=standard mode=fullscreen
     *
     * 现在有两个任务栈 taskId 4961 以及 taskId 4962
     *
     * 附上日志打印情况
     *
     * 2019-06-26 00:43:30.913 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 181468584
     * 2019-06-26 00:43:30.934 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = www.zhyen.com  taskId = 4961
     * 2019-06-26 00:43:30.936 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-26 00:43:30.937 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-26 00:43:31.713 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-26 00:43:31.735 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeB  hashCode = 231403010
     * 2019-06-26 00:43:31.753 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4961
     * 2019-06-26 00:43:31.755 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-26 00:43:31.756 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-26 00:43:32.047 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     * 2019-06-26 00:43:32.280 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-26 00:43:32.299 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeC  hashCode = 215787251
     * 2019-06-26 00:43:32.318 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeCdumpTaskAffinity = www.zhyen.com.fengxing  taskId = 4962
     * 2019-06-26 00:43:32.320 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeC
     * 2019-06-26 00:43:32.321 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeC
     * 2019-06-26 00:43:32.716 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeB
     * 2019-06-26 00:43:32.779 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeC
     * 2019-06-26 00:43:32.799 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeD  hashCode = 24143376
     * 2019-06-26 00:43:32.819 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeDdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4962
     * 2019-06-26 00:43:32.822 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeD
     * 2019-06-26 00:43:32.824 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeD
     * 2019-06-26 00:43:33.108 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeC
     * 2019-06-26 00:43:33.336 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeD
     * 2019-06-26 00:43:33.356 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeE  hashCode = 264584169
     * 2019-06-26 00:43:33.370 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeEdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4962
     * 2019-06-26 00:43:33.372 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeE
     * 2019-06-26 00:43:33.373 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeE
     * 2019-06-26 00:43:33.657 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeD
     * 2019-06-26 00:43:33.833 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeE
     * 2019-06-26 00:43:33.849 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 184679886
     * 2019-06-26 00:43:33.865 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = www.zhyen.com  taskId = 4962
     * 2019-06-26 00:43:33.866 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-26 00:43:33.867 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-26 00:43:34.138 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeE
     * 2019-06-26 00:43:34.453 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-26 00:43:34.471 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeB  hashCode = 201631439
     * 2019-06-26 00:43:34.487 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4962
     * 2019-06-26 00:43:34.489 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-26 00:43:34.490 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-26 00:43:34.769 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     *
     * 可以看出来开启界面LaunchModeC时候 重新开启一个新的Task，并且在之后开启的Activity也运行在了该Task上。
     *
     *
     * 现在开启 LaunchModeC (从 B-->C)
     *
     *  mIntent=Intent { flg=0x10000000 cmp=com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeC }
     *      #0 ActivityRecord{cddd8ff u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeC t4962} type=standard mode=fullscreen
     *     #0 TaskRecord{87208c3 #4961 A=com.component.fx.plugin_test U=0 StackId=21 sz=3} type=standard mode=fullscreen
     *      #2 ActivityRecord{5988b22 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeB t4961} type=standard mode=fullscreen
     *      #1 ActivityRecord{30bdce6 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeA t4961} type=standard mode=fullscreen
     *      #0 ActivityRecord{56565ba u0 com.component.fx.plugin_test/.test_activity.TestPluginMainActivity t4961} type=standard mode=fullscreen
     *
     *
     * 2019-06-26 00:47:22.014 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeD
     * 2019-06-26 00:47:22.021 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeE
     * 2019-06-26 00:47:22.028 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeA
     * 2019-06-26 00:47:22.038 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-26 00:47:22.046 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onNewIntent: LaunchModeC  hashCode = 215787251
     * 2019-06-26 00:47:22.047 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeCdumpTaskAffinity = www.zhyen.com.fengxing  taskId = 4962
     * 2019-06-26 00:47:22.047 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onRestart: LaunchModeC
     * 2019-06-26 00:47:22.048 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeC
     * 2019-06-26 00:47:22.049 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeC
     * 2019-06-26 00:47:22.320 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeB
     * 2019-06-26 00:47:22.321 19817-19817/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeB
     *
     *
     * 如果默认启动的Activity 设置taskAffinity属性  和LaunchModeC 属性设置相同
     *
     *        <activity
     *             android:name=".test_activity.TestPluginMainActivity"
     *             android:taskAffinity="www.zhyen.com">
     *             <intent-filter>
     *                 <action android:name="android.intent.action.MAIN" />
     *
     *                 <category android:name="android.intent.category.LAUNCHER" />
     *             </intent-filter>
     *         </activity>
     * 会发现 和没有设置taskAffinity属性是一样的。
     *
     * 命令行查看：
     *      mIntent=Intent { flg=0x10000000 cmp=com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeC }
     *      #3 ActivityRecord{328ae53 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeC t4965} type=standard mode=fullscreen
     *      #2 ActivityRecord{cdfc318 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeB t4965} type=standard mode=fullscreen
     *      #1 ActivityRecord{61952a u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeA t4965} type=standard mode=fullscreen
     *      #0 ActivityRecord{167258d u0 com.component.fx.plugin_test/.test_activity.TestPluginMainActivity t4965} type=standard mode=fullscreen
     *
     *  因为虽然TestPluginMainActivity 设置了taskAffinity属性，因为没有Task所以会新建一个Task，在开启  LaunchModeC 时， 会查找这个Task，发现已经存在 就不会新建Task了,所以TaskId相同。
     *
     *
     * 总结：
     * singleTask启动模式启动Activity时，首先会根据taskAffinity去寻找当前是否存在一个对应名字的任务
     *
     * 如果不存在，则会创建一个新的Task，并创建新的Activity实例入栈到新创建的Task中去,并且在之后开启的Activity也运行在了该Task上.
     *
     * 如果存在，则得到该任务栈，查找该任务栈中是否存在该Activity实例
     *                        如果存在实例，则将它上面的Activity实例都出栈，然后回调启动的Activity实例的onNewIntent方法
     *                        如果不存在该实例，则新建Activity，并入栈
     *
     * 此外，我们可以将两个不同App中的Activity设置为相同的taskAffinity，这样虽然在不同的应用中，但是Activity会被分配到同一个Task中去。
     */


    /**
     * singleInstance-全局唯一模式 A-->B-->C-->D-->E  其它app --> E
     *
     * 2019-06-26 09:51:37.416 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: TestPluginMainActivity = dumpTaskAffinity = com.component.fx.plugin_test  taskId = 4986
     * 2019-06-26 09:51:38.331 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 132218896
     * 2019-06-26 09:51:38.353 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4986
     * 2019-06-26 09:51:38.356 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-26 09:51:38.357 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-26 09:51:39.104 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-26 09:51:39.131 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeB  hashCode = 153820443
     * 2019-06-26 09:51:39.154 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4986
     * 2019-06-26 09:51:39.158 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-26 09:51:39.159 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-26 09:51:39.453 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     * 2019-06-26 09:51:40.107 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-26 09:51:40.136 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeC  hashCode = 190472049
     * 2019-06-26 09:51:40.158 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeCdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4986
     * 2019-06-26 09:51:40.162 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeC
     * 2019-06-26 09:51:40.163 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeC
     * 2019-06-26 09:51:40.465 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeB
     * 2019-06-26 09:51:40.806 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeC
     * 2019-06-26 09:51:40.854 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeD  hashCode = 150735955
     * 2019-06-26 09:51:40.877 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeDdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4987
     * 2019-06-26 09:51:40.881 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeD
     * 2019-06-26 09:51:40.882 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeD
     * 2019-06-26 09:51:41.233 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeC
     * 2019-06-26 09:51:43.266 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeD
     * 2019-06-26 09:51:43.309 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeE  hashCode = 217221481
     * 2019-06-26 09:51:43.332 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeEdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4986
     * 2019-06-26 09:51:43.334 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeE
     * 2019-06-26 09:51:43.335 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeE
     * 2019-06-26 09:51:43.660 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeD
     * 2019-06-26 09:51:45.626 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeE
     * 2019-06-26 09:51:46.132 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeE
     * 2019-06-26 09:51:48.982 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onNewIntent: LaunchModeD  hashCode = 150735955
     * 2019-06-26 09:51:48.983 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeDdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4987
     * 2019-06-26 09:51:48.983 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onRestart: LaunchModeD
     * 2019-06-26 09:51:48.984 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeD
     * 2019-06-26 09:51:48.988 31200-31200/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeD
     *
     * 这种模式下的Activity会单独占用一个Task栈，具有全局唯一性，即整个系统中就这么一个实例，由于栈内复用的特性，后续的请求均不会创建新的Activity实例，除非这个特殊的任务栈被销毁了
     *
     *
     * A-->B-->C-->D
     *         <activity android:name=".test_activity.activityLaunchMode.LaunchModeA" />
     *         <activity
     *             android:name=".test_activity.activityLaunchMode.LaunchModeB"
     *             android:launchMode="singleTop" />
     *         <activity
     *             android:name=".test_activity.activityLaunchMode.LaunchModeC"
     *             android:launchMode="singleTask" />
     *         <activity
     *             android:name=".test_activity.activityLaunchMode.LaunchModeD"
     *             android:launchMode="singleInstance">
     *             <intent-filter>
     *
     *                 <action android:name="com.component.fx.plugin_test.action_singleInstance" />
     *
     *                 <category android:name="android.intent.category.DEFAULT" />
     *                 <category android:name="android.intent.category.BROWSABLE" />
     *
     *             </intent-filter>
     *         </activity>
     *         <activity android:name=".test_activity.activityLaunchMode.LaunchModeE" />
     *
     * 2019-06-26 10:01:44.262 31712-31712/? D/LaunchModeBase: TestPluginMainActivity = dumpTaskAffinity = com.component.fx.plugin_test  taskId = 4991
     * 2019-06-26 10:04:11.657 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeA  hashCode = 189173052
     * 2019-06-26 10:04:11.683 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeAdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4991
     * 2019-06-26 10:04:11.686 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-26 10:04:11.688 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-26 10:04:15.565 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-26 10:04:15.592 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeB  hashCode = 240414711
     * 2019-06-26 10:04:15.614 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeBdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4991
     * 2019-06-26 10:04:15.619 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-26 10:04:15.620 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-26 10:04:15.910 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     * 2019-06-26 10:04:16.328 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-26 10:04:16.354 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeC  hashCode = 124027309
     * 2019-06-26 10:04:16.378 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeCdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4991
     * 2019-06-26 10:04:16.382 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeC
     * 2019-06-26 10:04:16.383 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeC
     * 2019-06-26 10:04:16.673 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeB
     * 2019-06-26 10:04:17.209 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeC
     * 2019-06-26 10:04:17.260 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeD  hashCode = 172276112
     * 2019-06-26 10:04:17.282 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeDdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4992
     * 2019-06-26 10:04:17.285 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeD
     * 2019-06-26 10:04:17.287 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeD
     * 2019-06-26 10:04:17.640 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeC
     *
     *    mIntent=Intent { flg=0x10000000 cmp=com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeD }
     *     #0 TaskRecord{8e4f72a #4992 A=com.component.fx.plugin_test U=0 StackId=52 sz=1} type=standard mode=fullscreen
     *      #0 ActivityRecord{d78617b u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeD t4992} type=standard mode=fullscreen
     *     #0 TaskRecord{5ebc091 #4991 A=com.component.fx.plugin_test U=0 StackId=51 sz=4} type=standard mode=fullscreen
     *      #3 ActivityRecord{3bb307c u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeC t4991} type=standard mode=fullscreen
     *      #2 ActivityRecord{553d19e u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeB t4991} type=standard mode=fullscreen
     *      #1 ActivityRecord{90586d u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeA t4991} type=standard mode=fullscreen
     *      #0 ActivityRecord{13d167e u0 com.component.fx.plugin_test/.test_activity.TestPluginMainActivity t4991} type=standard mode=fullscreen
     *
     *      效果和singleTask相同
     *
     * 现在 开启--> E
     * 2019-06-26 10:06:33.910 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeD
     * 2019-06-26 10:06:33.962 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onCreate: LaunchModeE  hashCode = 156807150
     * 2019-06-26 10:06:33.986 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: LaunchModeEdumpTaskAffinity = com.component.fx.plugin_test  taskId = 4991
     * 2019-06-26 10:06:33.988 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeE
     * 2019-06-26 10:06:33.989 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeE
     * 2019-06-26 10:06:34.308 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeD
     *
     * 注意 LaunchModeD taskId的值 taskId还是以前的任务，并没有加入新的task。 这时候看看任务栈的情况.
     *
     * mIntent=Intent { flg=0x10400000 cmp=com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeE }
     *     #0 TaskRecord{5ebc091 #4991 A=com.component.fx.plugin_test U=0 StackId=51 sz=5} type=standard mode=fullscreen
     *      #4 ActivityRecord{53cabf0 u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeE t4991} type=standard mode=fullscreen
     *      #3 ActivityRecord{3bb307c u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeC t4991} type=standard mode=fullscreen
     *      #2 ActivityRecord{553d19e u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeB t4991} type=standard mode=fullscreen
     *      #1 ActivityRecord{90586d u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeA t4991} type=standard mode=fullscreen
     *      #0 ActivityRecord{13d167e u0 com.component.fx.plugin_test/.test_activity.TestPluginMainActivity t4991} type=standard mode=fullscreen
     *     #0 TaskRecord{8e4f72a #4992 A=com.component.fx.plugin_test U=0 StackId=52 sz=1} type=standard mode=fullscreen
     *      #0 ActivityRecord{d78617b u0 com.component.fx.plugin_test/.test_activity.activityLaunchMode.LaunchModeD t4992} type=standard mode=fullscreen
     *
     *  发现 LaunchModeD 在任务栈的最下面。那么现在我们点击返回 应该依次 E-->C-->B-->A-->TestPluginMainActivity-->D
     *
     *  我们来看日志：
     *  2019-06-26 10:11:38.355 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeE
     * 2019-06-26 10:11:38.369 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onRestart: LaunchModeC
     * 2019-06-26 10:11:38.372 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeC
     * 2019-06-26 10:11:38.374 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeC
     * 2019-06-26 10:11:38.667 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeE
     * 2019-06-26 10:11:38.669 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeE
     * 2019-06-26 10:11:40.003 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeC
     * 2019-06-26 10:11:40.022 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onRestart: LaunchModeB
     * 2019-06-26 10:11:40.023 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeB
     * 2019-06-26 10:11:40.024 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeB
     * 2019-06-26 10:11:40.308 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeC
     * 2019-06-26 10:11:40.309 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeC
     * 2019-06-26 10:11:41.614 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeB
     * 2019-06-26 10:11:41.639 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onRestart: LaunchModeA
     * 2019-06-26 10:11:41.641 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeA
     * 2019-06-26 10:11:41.643 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeA
     * 2019-06-26 10:11:41.921 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeB
     * 2019-06-26 10:11:41.923 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeB
     * 2019-06-26 10:11:42.746 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeA
     * 2019-06-26 10:11:43.063 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeA
     * 2019-06-26 10:11:43.064 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeA
     * 2019-06-26 10:11:44.760 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onRestart: LaunchModeD
     * 2019-06-26 10:11:44.761 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStart: LaunchModeD
     * 2019-06-26 10:11:44.764 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onResume: LaunchModeD
     * 2019-06-26 10:11:49.679 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onPause: LaunchModeD
     * 2019-06-26 10:11:50.255 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onStop: LaunchModeD
     * 2019-06-26 10:11:50.258 31712-31712/com.component.fx.plugin_test D/LaunchModeBase: onDestroy: LaunchModeD
     *
     * 和预期的结果相同
     *
     *
     */


    /**
     *
     * 清空任务栈中的所有元素，创建一个新的活动实例并放到一个新的任务栈中
     *
     * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
     *
     * FLAG_ACTIVITY_SINGLE_TOP
     * =  singleTop
     *
     * FLAG_ACTIVITY_CLEAR_TOP
     *
     */
}

