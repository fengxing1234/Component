package com.component.fx.plugin_base.network;

import com.component.fx.plugin_base.AppConfig;
import com.component.fx.plugin_base.network.converter.NewsGsonConvert;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 6种解析类型
 * Gson: com.squareup.retrofit2:converter-gson
 * Jackson: com.squareup.retrofit2:converter-jackson
 * Moshi: com.squareup.retrofit2:converter-moshi
 * Protobuf: com.squareup.retrofit2:converter-protobuf
 * Wire: com.squareup.retrofit2:converter-wire
 * Simple XML: com.squareup.retrofit2:converter-simplexml
 * Scalars (primitives, boxed, and String): com.squareup.retrofit2:converter-scalars
 */
public class BaseRequestRetrofit {

    public static Retrofit getNewsRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL_NEWS)
                .addConverterFactory(NewsGsonConvert.create())//根据聚合数据返回数据类型 自定义解析
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static void getNewsRequestServce() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConfig.BASE_URL_NEWS).build();
    }

}
