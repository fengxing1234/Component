package com.component.fx.plugin_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.component.fx.plugin_component.ServiceFactory;

public class PluginAppMainActivity extends AppCompatActivity {

    private static final String TAG = PluginAppMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_app_main);
        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump();
            }
        });
        findViewById(R.id.tv_click_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("1", "1");
                bundle.putString("2", "2");
                ServiceFactory.getInstance().getAccountService().newUserFragment(PluginAppMainActivity.this, R.id.fl_container, getSupportFragmentManager(), bundle, TAG);
            }
        });
    }

    private void jump() {
        ARouter.getInstance().build("/plugin_login/login").navigation();
    }
}
