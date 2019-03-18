package com.component.fx.plugin_news;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.component.fx.plugin_base.WebViewActivity;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_news.adapter.NewsRecycleAdapter;
import com.component.fx.plugin_news.network.NewsEnum;
import com.component.fx.plugin_news.network.NewsRequestManager;
import com.component.fx.plugin_news.network.model.NewsModel;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {

    private static final String BUNDLE_NEWS_TYPE = "bundle_news_type";

    private static final String TAG = "NewsFragment";
    private String mNewsType;
    private RecyclerView mRecycleView;
    private NewsRecycleAdapter newsAdapter;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;

    private static final int REFRESH_TYPE = 0x0001;
    private static final int MORE_TYPE = 0x0002;

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
        Log.d(TAG, "onCreate: " + mNewsType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + mNewsType);
        return inflater.inflate(R.layout.news_layout_news_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: " + mNewsType);
        initRecycleView(view);
        initRefreshLayout(view);
        getNewsModel(REFRESH_TYPE);
    }

    private void initRecycleView(@NonNull View view) {
        mRecycleView = (RecyclerView) view.findViewById(R.id.news_recycle_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsRecycleAdapter(this);
        mRecycleView.setAdapter(newsAdapter);
        newsAdapter.setOnNewsItemClickListener(new NewsRecycleAdapter.OnNewsItemClickListener() {
            @Override
            public void onNewsItemClick(View v, NewsRecycleAdapter.NewsViewHolder holder, int layoutPosition, NewsModel.DataBean dataBean) {
                WebViewActivity.startWebViewActivity(getContext(), dataBean.getUrl());
            }
        });

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int lastVisibleItemPosition;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == newsAdapter.getItemCount() && newsAdapter.getItemCount() > 5) {
                    getNewsModel(MORE_TYPE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

    }

    private void initRefreshLayout(@NonNull View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_swipe_refresh);
        // 设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        //refreshLayout.setProgressViewOffset(true, 50, 200);
        // 设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                NewsRequestManager.getNewsData(NewsEnum.getEnumByName(mNewsType), new Callback<NewsModel>() {
                    @Override
                    public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                        refreshLayout.setRefreshing(false);
                        newsAdapter.addNewList(response.body());
                    }

                    @Override
                    public void onFailure(Call<NewsModel> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                        String str = "{\n" +
                                "\t\t\"stat\":\"1\",\n" +
                                "\t\t\"data\":[\n" +
                                "\t\t\t{\n" +
                                "\t\t\t\t\"uniquekey\":\"c4b2e063f02b247ad638875a128bfc35\",\n" +
                                "\t\t\t\t\"title\":\"开心一刻笑话：今天早上穿了件有点露肩的连衣裙，化了妆准备出门\",\n" +
                                "\t\t\t\t\"date\":\"2019-03-18 12:15\",\n" +
                                "\t\t\t\t\"category\":\"头条\",\n" +
                                "\t\t\t\t\"author_name\":\"寂寞内涵\",\n" +
                                "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318121524789.html\",\n" +
                                "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_8950cd57331f403eaf5a27205dc685c0_1724_mwpm_03200403.jpg\",\n" +
                                "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_0f480fc7bfc04c5095afa614f5e53a30_6079_mwpm_03200403.jpg\",\n" +
                                "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_b765e4044b7d46499de8f02a1f9b352a_5577_mwpm_03200403.jpg\"\n" +
                                "\t\t\t},\n" +
                                "\t\t\t{\n" +
                                "\t\t\t\t\"uniquekey\":\"e6877af331c50ea09343b711120af1e8\",\n" +
                                "\t\t\t\t\"title\":\"谢娜聊坐月子洗头，最后有谁注意她的表情变化？网友：找罪受！\",\n" +
                                "\t\t\t\t\"date\":\"2019-03-18 12:14\",\n" +
                                "\t\t\t\t\"category\":\"头条\",\n" +
                                "\t\t\t\t\"author_name\":\"追寻心中的梦想\",\n" +
                                "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318121415975.html\",\n" +
                                "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_27fc6c6a3b074bbf842735ed55836fe3_9340_cover_mwpm_03200403.jpg\",\n" +
                                "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_87f416994a9f415aad6850a29d48a522_9564_cover_mwpm_03200403.jpg\",\n" +
                                "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_f0595d7ba7c44a63b1a4cfce122a4876_4821_cover_mwpm_03200403.jpg\"\n" +
                                "\t\t\t}\n" +
                                "\t\t\t\n" +
                                "\t\t]\n" +
                                "\t}";

                        Gson gson = new Gson();
                        NewsModel newsModel = gson.fromJson(str, NewsModel.class);
                        newsAdapter.addNewList(newsModel);
                    }
                });
            }
        });
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + mNewsType);
    }

    private void getNewsModel(final int type) {
        NewsRequestManager.getNewsData(NewsEnum.getEnumByName(mNewsType), new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                String name = Thread.currentThread().getName();
                ToastUtil.toast(mNewsType + "数据请求成功" + "线程=" + name);
                if (type == MORE_TYPE) {
                    newsAdapter.addMoreNews(response.body());
                } else {
                    newsAdapter.addNewList(response.body());
                }

            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                String str = "{\n" +
                        "\t\t\"stat\":\"1\",\n" +
                        "\t\t\"data\":[\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"c4b2e063f02b247ad638875a128bfc35\",\n" +
                        "\t\t\t\t\"title\":\"开心一刻笑话：今天早上穿了件有点露肩的连衣裙，化了妆准备出门\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 12:15\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"寂寞内涵\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318121524789.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_8950cd57331f403eaf5a27205dc685c0_1724_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_0f480fc7bfc04c5095afa614f5e53a30_6079_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_b765e4044b7d46499de8f02a1f9b352a_5577_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"e6877af331c50ea09343b711120af1e8\",\n" +
                        "\t\t\t\t\"title\":\"谢娜聊坐月子洗头，最后有谁注意她的表情变化？网友：找罪受！\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 12:14\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"追寻心中的梦想\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318121415975.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_27fc6c6a3b074bbf842735ed55836fe3_9340_cover_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_87f416994a9f415aad6850a29d48a522_9564_cover_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_f0595d7ba7c44a63b1a4cfce122a4876_4821_cover_mwpm_03200403.jpg\"\n" +
                        "\t\t\t}\n" +
                        "\t\t\t\n" +
                        "\t\t]\n" +
                        "\t}";

                Gson gson = new Gson();
                NewsModel newsModel = gson.fromJson(str, NewsModel.class);
                if (type == MORE_TYPE) {
                    //newsAdapter.addMoreNews(newsModel);
                    newsAdapter.setMoreState(NewsRecycleAdapter.NO_MORE_NEWS);
                } else {
                    newsAdapter.addNewList(newsModel);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + mNewsType);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: " + mNewsType);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: " + mNewsType);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + mNewsType);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + mNewsType);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: " + mNewsType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: " + mNewsType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + mNewsType);
    }
}
