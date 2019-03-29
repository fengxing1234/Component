package com.component.fx.plugin_base.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.component.fx.plugin_base.swipeback.SwipeBackActivity;
import com.component.fx.plugin_base.utils.StatusBarUtil;

public class BaseActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT,0);
    }
}
