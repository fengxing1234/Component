package com.component.fx.plugin_toutiao.fragment;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.component.fx.plugin_base.network.TouTiaoRetrofit;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.adapter.TouTiaoNewsRecycleAdapter;
import com.component.fx.plugin_toutiao.api.IMobileNewsApi;
import com.component.fx.plugin_toutiao.base.LazyLoadFragment;
import com.component.fx.plugin_toutiao.bean.MultiNewsArticleBean;
import com.component.fx.plugin_toutiao.bean.MultiNewsArticleBeanData;
import com.google.gson.Gson;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class TouTiaoNewsTabFragment extends LazyLoadFragment {

    public static final String NEWS_CATEGORY_KEY = "news_category_key";
    private String type;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private Gson gson = new Gson();

    private List<MultiNewsArticleBeanData> mList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private TouTiaoNewsRecycleAdapter adapter;

    public static TouTiaoNewsTabFragment getInstance(String type) {
        TouTiaoNewsTabFragment fragment = new TouTiaoNewsTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_CATEGORY_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.toutiao_fragment_item_layout;
    }

    @Override
    protected void initView(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.toutiao_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.toutiao_recycle_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new TouTiaoNewsRecycleAdapter();
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void lazyLoadData() {
        Log.d(TAG, "lazyLoadData: ");
        getNewsData();

    }

    public void getNewsData() {
        IMobileNewsApi mobileNewsApi = TouTiaoRetrofit.getRetrofit().create(IMobileNewsApi.class);
        Observable<MultiNewsArticleBean> observable = mobileNewsApi.getNewsArticle("news_hot", getCurrentTimeStamp());
        observable
                .subscribeOn(Schedulers.io())
                .switchMap(new Function<MultiNewsArticleBean, ObservableSource<MultiNewsArticleBeanData>>() {
                    @Override
                    public ObservableSource<MultiNewsArticleBeanData> apply(MultiNewsArticleBean multiNewsArticleBean) throws Exception {//数据转换  MultiNewsArticleBean中的字符串转成bean对象
                        ArrayList<MultiNewsArticleBeanData> list = new ArrayList<>();
                        for (MultiNewsArticleBean.DataBean bean : multiNewsArticleBean.getData()) {
                            list.add(gson.fromJson(bean.getContent(), MultiNewsArticleBeanData.class));
                        }
                        return Observable.fromIterable(list);
                    }
                })
                .filter(new Predicate<MultiNewsArticleBeanData>() {
                    @Override
                    public boolean test(MultiNewsArticleBeanData data) throws Exception {

                        int time = data.getBehot_time();

                        if (TextUtils.isEmpty(data.getSource())) {
                            return false;
                        }

                        try {
                            // 过滤头条问答新闻
                            if (data.getSource().contains("头条问答")
                                    || data.getTag().contains("ad")
                                    || data.getSource().contains("悟空问答")) {
                                return false;
                            }
                            // 过滤头条问答新闻
                            //阅读数量0  媒体名字 不存在
                            if (data.getRead_count() == 0 || TextUtils.isEmpty(data.getMedia_name())) {

                                String title = data.getTitle();
                                if (title.lastIndexOf("?") == title.length() - 1) {//问号结尾？
                                    return false;
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        //过滤重复新闻

                        for (MultiNewsArticleBeanData beanData : mList) {
                            if (beanData.getTitle().equals(data.getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .map(new Function<List<MultiNewsArticleBeanData>, List<MultiNewsArticleBeanData>>() {
                    @Override
                    public List<MultiNewsArticleBeanData> apply(List<MultiNewsArticleBeanData> list) throws Exception {
                        //过滤重复新闻(与本次刷新的数据比较,因为使用了2个请求,数据会有重复)
                        for (int i = 0; i < list.size() - 1; i++) {
                            for (int j = list.size() - 1; j > i; j--) {
                                if (list.get(j).getTitle().equals(list.get(i).getTitle())) {
                                    list.remove(j);
                                }
                            }
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.<List<MultiNewsArticleBeanData>>autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new Consumer<List<MultiNewsArticleBeanData>>() {
                    @Override
                    public void accept(List<MultiNewsArticleBeanData> list) throws Exception {
                        if (list != null && list.size() > 0) {
                            adapter.setData(list);
                        } else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.toast(throwable.getMessage());
                    }
                });
    }

    public String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

}
