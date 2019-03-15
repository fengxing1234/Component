package com.component.fx.plugin_share;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.component.fx.plugin_component.ServiceFactory;

@Route(path = "/plugin_share/share")
public class PluginShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity_main);

        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoIntent();
            }
        });

    }

    private void demoIntent() {
        boolean login = ServiceFactory.getInstance().getAccountService().isLogin();
        if (login) {
            Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "分享失败：用户未登录", Toast.LENGTH_SHORT).show();
        }

        String accountId = ServiceFactory.getInstance().getAccountService().getAccountId();
        Toast.makeText(this, "用户账号:" + accountId, Toast.LENGTH_SHORT).show();

        ARouter.getInstance().build("/plugin_login/login").withString("content", "回到登录页面").navigation();
    }

}
