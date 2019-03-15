package com.component.fx.plugin_share;

import android.app.Application;

import com.component.fx.plugin_base.BaseApplication;

public class ShareApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initModuleApp(this);
        initModuleData(this);
    }

    @Override
    public void initModuleApp(Application application) {

    }

    @Override
    public void initModuleData(Application application) {

    }
}
