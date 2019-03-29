package com.component.fx.plugin_login;

import android.app.Application;

import com.component.fx.plugin_base.base.BaseApplication;
import com.component.fx.plugin_component.ServiceFactory;
import com.component.fx.plugin_login.export.ExportAccountService;

public class LoginApplication extends BaseApplication {

    @Override
    public void initModuleApp(Application application) {
        // 将 AccountService 类的实例注册到 ServiceFactory
        ServiceFactory.getInstance().setAccountService(new ExportAccountService());
    }

    @Override
    public void initModuleData(Application application) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initModuleApp(this);
        initModuleData(this);
    }

}
