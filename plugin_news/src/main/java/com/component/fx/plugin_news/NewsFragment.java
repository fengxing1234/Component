package com.component.fx.plugin_news;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_news.adapter.NewsRecycleAdapter;
import com.component.fx.plugin_news.network.NewsEnum;
import com.component.fx.plugin_news.network.NewsRequestManager;
import com.component.fx.plugin_news.network.model.NewsModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {

    private static final String BUNDLE_NEWS_TYPE = "bundle_news_type";
    private String mNewsType;
    private RecyclerView mRecycleView;
    private NewsRecycleAdapter newsAdapter;

    public static NewsFragment getInstance(String type) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NEWS_TYPE, type);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mNewsType = bundle.getString(BUNDLE_NEWS_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_layout_news_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecycleView = (RecyclerView) view.findViewById(R.id.news_recycle_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsRecycleAdapter(this);
        mRecycleView.setAdapter(newsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        NewsRequestManager.getNewsData(NewsEnum.getEnumByName(mNewsType), new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                ToastUtil.toast(mNewsType+"数据请求成功");
                newsAdapter.setNewsModel(response.body());
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                ToastUtil.toast(t.getMessage());
            }
        });
    }
}
