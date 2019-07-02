package com.component.fx.plugin_test.test_activity.activityLaunchMode;

import android.content.Intent;

public class LaunchModeA extends LaunchModeBase {

    @Override
    protected int getCurrentPageColor() {
        return com.component.fx.plugin_base.R.color.Pink;
    }

    @Override
    protected String getCurrentPageText() {
        return "当前页面A";
    }

    public void doStartActivity(){
        startActivity(new Intent(this,LaunchModeB.class));
    }
}
