package com.component.fx.plugin_news.network;

import android.support.annotation.NonNull;

import com.component.fx.plugin_base.network.BaseRequestRetrofit;
import com.component.fx.plugin_news.network.model.NewsModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * 新闻接口管理类
 */
public class NewsRequestManager {

    private static final String NEWS_KEY = "aad6635d30611d3899be0cefe1a4efb3";

    public static final String TOP = "top";


//    public static void getNewsData(@NonNull NewsEnum newsEnum, @NonNull Callback<ResponseBody> callback) {
//        Retrofit retrofit = BaseRequestRetrofit.getNewsRetrofit();
//        NewsRequestService newsRequestService = retrofit.create(NewsRequestService.class);
//        Call<ResponseBody> call = newsRequestService.getNews(newsEnum.getType(), NEWS_KEY);
//        call.enqueue(callback);
//    }

    public static void getNewsData(@NewsType String type, @NonNull Callback<NewsModel> callback) {
        Retrofit retrofit = BaseRequestRetrofit.getNewsRetrofit();
        NewsRequestService newsRequestService = retrofit.create(NewsRequestService.class);
        Call<NewsModel> call = newsRequestService.getNews(type, NEWS_KEY);
        call.enqueue(callback);
    }

//    public static void getNewsResponse(@NewsType String type,NewsCallBack callBack){
//        Retrofit retrofit = BaseRequestRetrofit.getNewsRetrofit();
//        NewsRequestService newsRequestService = retrofit.create(NewsRequestService.class);
//        Call<ResponseBody> call = newsRequestService.getNews(type, NEWS_KEY);
//        call.enqueue(callBack);
//    }
}
