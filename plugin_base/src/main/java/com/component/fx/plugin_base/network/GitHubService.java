package com.component.fx.plugin_base.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {

    @GET("users/{user}/repos")
    Call<String> getString(@Path("user") String user);

}
