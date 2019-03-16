package com.component.fx.plugin_base.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("users/{user}/repos")
    Call<String> getString(@Path("user") String user);

    @GET("toutiao/index")
    Call<String> getNews(@Query("type") String type, @Query("key") String key);

}
