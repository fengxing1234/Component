package com.component.fx.plugin_login;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.component.fx.plugin_base.base.BaseActivity;

@Route(path = "/plugin_login/login")
public class PluginLoginMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);
        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump();
            }
        });
    }

    private void jump() {
        ARouter.getInstance().build("/plugin_share/share").withString("content","分享数据到微博").navigation();
    }
}
