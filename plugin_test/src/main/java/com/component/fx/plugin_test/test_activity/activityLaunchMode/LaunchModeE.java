package com.component.fx.plugin_test.test_activity.activityLaunchMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class LaunchModeE extends LaunchModeBase {

    @Override
    protected int getCurrentPageColor() {
        return com.component.fx.plugin_base.R.color.khaki;
    }

    @Override
    protected String getCurrentPageText() {
        return "当前页面E";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void doStartActivity() {
        startActivity(new Intent(this, LaunchModeA.class));
    }
}
