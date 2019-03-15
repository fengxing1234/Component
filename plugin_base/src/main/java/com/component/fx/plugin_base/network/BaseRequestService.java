package com.component.fx.plugin_base.network;

import com.component.fx.plugin_base.AppConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BaseRequestService {

    public static void getService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConfig.BASE_URL).build();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<String> call = service.getString("");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
