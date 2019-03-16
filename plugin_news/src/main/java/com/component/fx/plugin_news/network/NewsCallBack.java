package com.component.fx.plugin_news.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsCallBack<T> implements Callback<Response<T>> {


    @Override
    public void onResponse(Call<Response<T>> call, Response<Response<T>> response) {

    }

    @Override
    public void onFailure(Call<Response<T>> call, Throwable t) {

    }
}
