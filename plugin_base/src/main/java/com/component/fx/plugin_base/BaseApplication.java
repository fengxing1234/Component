package com.component.fx.plugin_base;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

public abstract class BaseApplication extends Application {

    protected static Context mContext;

    /**
     * Application 初始化
     * <p>
     * 初始化当前组件时需要调用的方法
     *
     * @param application
     */
    public abstract void initModuleApp(Application application);

    /**
     * 所有 Application 初始化后的自定义操作
     *
     * @param application
     */
    public abstract void initModuleData(Application application);

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }


        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }


    public static Context getAppContext() {
        return mContext;
    }
}
