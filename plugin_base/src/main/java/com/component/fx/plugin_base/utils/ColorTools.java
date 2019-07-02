package com.component.fx.plugin_base.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;

import java.util.Random;

public class ColorTools {

    /**
     * 判断颜色是否是亮色
     * https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     * 判断颜色的亮暗
     *
     * @param color
     * @return
     */
    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (color == 0) {
            return false;
        }
        if (darkness < 0.5) {
            return false; // It's a light color
        } else {
            return true; // It's a dark color
        }
    }

    /**
     * 同上
     *
     * @param color
     * @return
     */
    public static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    public static int randomColor() {
        return randomColor(0);
    }

    /**
     * 随机获取一个颜色 自定义透明度
     *
     * @param alpha
     * @return
     */
    public static int randomColor(int alpha) {
        Random rnd = new Random();
        alpha = Math.min(Math.max(1, alpha), 255);
        return Color.argb(alpha, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return "#"+r + g + b;
    }
}