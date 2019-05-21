package com.component.fx.plugin_news.network;

import com.component.fx.plugin_news.network.model.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsRequestService {

    @GET("toutiao/index")
    Call<NewsModel> getNews(@Query("type") String type, @Query("key") String key);

    @GET("1/2/3/3/3")
    Call<NewsModel> error();
}
