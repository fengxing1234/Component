package com.component.fx.plugin_toutiao;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.component.fx.plugin_base.BaseApplication;
import com.component.fx.plugin_base.manager.SPManager;

public class TouTiaoApplication extends BaseApplication {

    private static final String TAG = TouTiaoApplication.class.getSimpleName();
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getAppContext();
        initModuleApp(this);
        initModuleData(this);
        initTheme();
    }

    private void initTheme() {
        if (SPManager.getInstance().getNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);//黑天模式
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//白天模式
        }
    }

    @Override
    public void initModuleApp(Application application) {

    }

    @Override
    public void initModuleData(Application application) {

    }
}
