package com.component.fx.plugin_base.utils;

import android.widget.Toast;

import com.component.fx.plugin_base.BaseApplication;

public class ToastUtil {

    public static void toast(String message) {
        Toast.makeText(BaseApplication.getAppContext(), message, Toast.LENGTH_LONG).show();
    }
}
