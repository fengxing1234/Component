package com.component.fx.plugin_base;

import com.component.fx.plugin_base.base.BaseApplication;

import java.io.File;

public class AppConfig {

    public static final String LoginApp = "com.component.fx.plugin_login.LoginApplication";
    public static final String ShareApp = "com.component.fx.plugin_share.ShareApplication";
    public static final String NewsApp = "com.component.fx.plugin_news.NewsApplication";
    public static final String TouTiaoApp = "com.component.fx.plugin_toutiao.TouTiaoApplication";

    //服务器地址
    public static final String BASE_URL = "";
    public static final String BASE_URL_NEWS = "https://v.juhe.cn/";

    public static String[] moduleApps = {
            LoginApp, ShareApp, TouTiaoApp
    };


    public static final File getCacheFile() {
        return BaseApplication.getAppContext().getCacheDir();
    }
}
