package com.component.fx.plugin_base.network;

import com.component.fx.plugin_base.AppConfig;
import com.component.fx.plugin_base.BuildConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TouTiaoRetrofit {

    public static final String TOUTIAO_HTTP_CACHE = "ToutiaoHttpCache";
    public static final int MAX_SIZE = 1024 * 1024 * 50;
    public static final int TIMEOUT = 15;
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (TouTiaoRetrofit.class) {
                if (retrofit == null) {

                    Cache cache = new Cache(new File(AppConfig.getCacheFile(), TOUTIAO_HTTP_CACHE), MAX_SIZE);


                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                            .cache(cache);

                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        builder.addInterceptor(loggingInterceptor);
                    }

                    retrofit = new Retrofit.Builder()
                            .client(builder.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl("http://is.snssdk.com/")
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }


        return retrofit;
    }
}
