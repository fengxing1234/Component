package com.component.fx.plugin_test.test_activity.activityLaunchMode;

import android.content.Intent;

public class LaunchModeB extends LaunchModeBase {

    public void doStartActivity() {
        //测试singleTop模式
        //startActivity(new Intent(this, LaunchModeB.class));
        startActivity(new Intent(this, LaunchModeC.class));
    }

    @Override
    protected int getCurrentPageColor() {
        return com.component.fx.plugin_base.R.color.Light_Blue;
    }

    @Override
    protected String getCurrentPageText() {
        return "当前页面B";
    }
}
