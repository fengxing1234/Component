package com.component.fx;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.component.fx.plugin_base.AppConfig;
import com.component.fx.plugin_base.BaseApplication;

public class MainApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化


        initModuleApp(this);
        initModuleApp(this);
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 初始化组件 Application
     *
     * @param application
     */
    @Override
    public void initModuleApp(Application application) {
        for (String moduleApp : AppConfig.moduleApps) {
            try {
                Class<?> clazz = Class.forName(moduleApp);
                BaseApplication baseApp = (BaseApplication) clazz.newInstance();
                baseApp.initModuleApp(this);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 所有 Application 初始化后的操作
     *
     * @param application
     */
    @Override
    public void initModuleData(Application application) {
        for (String moduleApp : AppConfig.moduleApps) {
            try {
                Class clazz = Class.forName(moduleApp);
                BaseApplication baseApp = (BaseApplication) clazz.newInstance();
                baseApp.initModuleData(this);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
