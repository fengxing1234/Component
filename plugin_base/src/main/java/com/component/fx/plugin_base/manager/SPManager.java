package com.component.fx.plugin_base.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.component.fx.plugin_base.BaseApplication;

public class SPManager {

    public static final String PREFERENCES_Name = "Preferences";
    public static final String IS_NIGHT_MODE = "is_night_mode";

    private SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());

    private SPManager() {
    }

    private static final class Holder {
        private static final SPManager INSTANCE = new SPManager();
    }

    public static SPManager getInstance() {
        return Holder.INSTANCE;
    }

    @Deprecated
    private SharedPreferences getSharedPreferences() {
        return BaseApplication.getAppContext().getSharedPreferences(PREFERENCES_Name, Context.MODE_PRIVATE);
    }

    /**
     * 设置夜间模式
     * true 夜间 false 白天
     */
    public void setNightMode(boolean isNightMode) {
        set(IS_NIGHT_MODE, isNightMode);
    }

    /**
     * 获取夜间模式
     *
     * @return
     */
    public boolean getNightMode() {
        return setting.getBoolean(IS_NIGHT_MODE, false);
    }

    private void set(String name, Object value) {
        SharedPreferences.Editor edit = setting.edit();
        if (value instanceof Boolean) {
            edit.putBoolean(name, (Boolean) value);
        }
        if (value instanceof Integer) {
            edit.putInt(name, (Integer) value);
        }
        if (value instanceof String) {
            edit.putString(name, (String) value);
        }
        if (value instanceof Float) {
            edit.putFloat(name, (Float) value);
        }
        if (value instanceof Long) {
            edit.putLong(name, (long) value);
        }
        edit.apply();
    }

    public void clear() {
        setting.edit().clear().apply();
    }

    public void remove(String name) {
        setting.edit().remove(name).apply();
    }
}
