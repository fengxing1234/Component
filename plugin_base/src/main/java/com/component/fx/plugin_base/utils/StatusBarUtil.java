package com.component.fx.plugin_base.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class StatusBarUtil {

    private static final String TAG_FAKE_STATUS_BAR_VIEW = "tag_fake_status_bar_view";

    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    /**
     * @param activity
     */
    public static void setTranslucentColor(Activity activity) {
        setTranslucentStatus(activity, Color.TRANSPARENT, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 设置状态栏颜色
     */
    public static void setStatusColor(Activity activity, @ColorInt int color) {
        setTranslucentStatus(activity, color, 0);
    }

    /**
     * 4.4版本 半透明
     * 针对 4.4版本 状态栏半透明
     */
    public static void setTranslucentStatus(Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {

        //Android5.0以上使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //添加Flag把状态栏设为可绘制模式
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //取消状态栏透明
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏颜色
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, alpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //android 4.4以上使用
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addCustomViewStatusBar(activity, color, alpha);
        }
        autoChangeStatusBarTextColor(activity.getWindow(), color);
    }


    /**
     * 解决布局与状态栏重叠问题
     * <p>
     * 上方添加一个大小和StatusBar大小一样的View,
     * View 的BackgroundColor 为标题栏一样的颜色，
     * 这个View起到一个占位的作用。这个时候，
     * 标题栏就会下移StatusBar的高度，回到正常的位置。
     */

    public static void addCustomViewStatusBar(Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {

        Window window = activity.getWindow();
        ViewGroup decorView = (ViewGroup) window.getDecorView();

        //如果已经存在假状态栏 则移除，防止重复添加
        removeFakeStatusBarViewIfExist(window);
        //添加一个View来作为状态栏的填充
        View statusBarView = createFakeStatusBarView(activity, color, alpha);
        decorView.addView(statusBarView);
        setRootView(activity);
    }

    private static void setRootView(Activity activity) {
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        for (int i = 0; i < contentView.getChildCount(); i++) {
            View child = contentView.getChildAt(i);
            if (child instanceof ViewGroup) {
                child.setFitsSystemWindows(true);
                ((ViewGroup) child).setClipToPadding(true);
            }
        }
    }


    /**
     * 创建虚假的状态栏 防止 布局和状态栏 重叠 当然全图片是不需要的 自己添加状态栏的
     *
     * @param color
     */
    private static View createFakeStatusBarView(Context context, @ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        View view = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(context));
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        view.setBackgroundColor(calculateStatusColor(color, alpha));
        view.setTag(TAG_FAKE_STATUS_BAR_VIEW);
        return view;
    }

    /**
     * 删除虚假的状态栏 防止多次添加
     *
     * @param window
     */
    private static void removeFakeStatusBarViewIfExist(Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View statusView = decorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (statusView != null) {
            decorView.removeView(statusView);
        }
    }

    /**
     * 根据透明度 计算状态栏颜色
     *
     * @param color
     * @param statusBarAlpha
     * @return
     */
    private static int calculateStatusColor(@ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (statusBarAlpha == 0) {
            return color;
        }
        float a = 1 - statusBarAlpha / 255f;

        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;

        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;

    }


    ////////////////////////////////获取状态栏的高度////////////////////////////////


    public static int getStatusBarHeight(Context context) {
        int height = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    public static int getStatusBarHeight2(Context context) {
        int statusBarHeight2 = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight2 = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight2;
    }


    /**
     * 根据颜色的亮暗 自动转化状态栏字体颜色
     *
     * @param window
     * @param color
     */
    public static void autoChangeStatusBarTextColor(Window window, int color) {
        if (!ColorTools.isColorDark(color)) {
            statusBarTextColorBlack(window);
        } else {
            statusBarTextColorWhite(window);
        }
    }


    /**
     * 改变状态栏文字的颜色
     * 设置白底黑字
     *
     * @param isDarkMode
     */
    public static boolean changeStatusBarTextColor(Window window, boolean isDarkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDarkMode) {//黑色字体
                //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                statusBarTextColorBlack(window);
            } else {//白色字体
                //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                statusBarTextColorWhite(window);
            }
        }
        return isDarkMode;
    }

    /**
     * 设置状态栏字体为白色 6.0
     *
     * @param window
     */
    public static void statusBarTextColorWhite(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 设置状态栏字体为黑色 6.0
     *
     * @param window
     */
    public static void statusBarTextColorBlack(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
