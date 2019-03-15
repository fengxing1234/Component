package com.component.fx.plugin_base.swipeback;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.component.fx.plugin_base.R;

public class DemoSwipeBackActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_swipe_back_activity);
    }
}
