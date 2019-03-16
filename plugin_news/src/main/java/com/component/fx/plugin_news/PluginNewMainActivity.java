package com.component.fx.plugin_news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.component.fx.plugin_news.network.NewsRequestManager;
import com.component.fx.plugin_news.network.NewsType;
import com.component.fx.plugin_news.network.model.NewsModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PluginNewMainActivity extends AppCompatActivity {

    private static final String TAG = PluginNewMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity_main);
        findViewById(R.id.tv_news_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsRequestManager.getNewsData(NewsType.TOP, new Callback<NewsModel>() {
                    @Override
                    public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onFailure(Call<NewsModel> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                    }
                });

            }
        });
    }

    private void aaa() {
//        NewsRequestManager.getNewsData(NewsType.CAI_JING, new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d(TAG, "onResponse: ");
//                try {
//                    String string = response.body().string();
//                    JsonElement parse = new JsonParser().parse(string);
//                    Log.d(TAG, "onResponse: "+parse);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "onFailure: ");
//            }
//        });
    }
}
