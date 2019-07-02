package com.component.fx.plugin_base.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class UiSizeUtils {

    public static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    //形式不通


    public static int dip2Px(Context context, int dp) {
        float density = getScreenDensity(context);
        return (int) (dp * density + 0.5f);
    }

    public static int px2Dip(Context context, int px) {
        float density = getScreenDensity(context);
        return (int) (px / density + 0.5f);
    }

    public static int sp2Px(Context context, int sp) {
        float density = getScreenScaledDensity(context);
        return (int) (sp * density + 0.5f);
    }

    public static int px2Sp(Context context, int px) {
        float density = getScreenScaledDensity(context);
        return (int) (px / density + 0.5f);
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            defaultDisplay.getMetrics(metrics);
            return metrics.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

    public static float getScreenScaledDensity(Context context) {
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            defaultDisplay.getMetrics(metrics);
            return metrics.scaledDensity;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

}
