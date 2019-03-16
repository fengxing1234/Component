package com.component.fx.plugin_base.network.intercepto;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ParameterInterceptor implements Interceptor {

    private static ParameterInterceptor mInterceptor;

    private ParameterInterceptor() {

    }

    public static ParameterInterceptor getInstance() {
        if (mInterceptor == null) {
            synchronized (ParameterInterceptor.class) {
                if (mInterceptor == null) {
                    return mInterceptor = new ParameterInterceptor();
                }
            }
        }
        return mInterceptor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        HttpUrl.Builder builder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter("key", "aad6635d30611d3899be0cefe1a4efb3")
                //.addQueryParameter("parameter", "")
                //.addQueryParameter("parameter", "")
                ;

        Request request = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .addHeader("header1", "header1")
                .addHeader("header2", "header2")
                .addHeader("header3", "header3")
                .url(builder.build())
                .build();

        return chain.proceed(request);
    }
}
