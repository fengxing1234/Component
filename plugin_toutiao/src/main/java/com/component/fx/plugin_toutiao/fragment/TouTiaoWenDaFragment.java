package com.component.fx.plugin_toutiao.fragment;

import android.arch.lifecycle.Lifecycle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.component.fx.plugin_base.network.TouTiaoRetrofit;
import com.component.fx.plugin_base.utils.LogUtils;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.adapter.TouTiaoMultiWendaAdapter;
import com.component.fx.plugin_toutiao.api.IMobileWenDaApi;
import com.component.fx.plugin_toutiao.base.LazyLoadFragment;
import com.component.fx.plugin_toutiao.bean.WendaArticleBean;
import com.component.fx.plugin_toutiao.bean.WendaArticleBeanData;
import com.component.fx.plugin_toutiao.widget.OnRecycleViewScrollListener;
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

public class TouTiaoWenDaFragment extends LazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Gson gson = new Gson();
    private List<WendaArticleBeanData> mList = new ArrayList<>();
    private TouTiaoMultiWendaAdapter multiNewsAdapter;

    public static TouTiaoWenDaFragment getInstance() {

        return new TouTiaoWenDaFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.toutiao_fragment_item_layout;
    }

    @Override
    protected void lazyLoadData() {
        getNewsData();
    }

    @Override
    protected void initView(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.toutiao_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.toutiao_recycle_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        multiNewsAdapter = new TouTiaoMultiWendaAdapter();
        multiNewsAdapter.addItemType(WendaArticleBeanData.WENDA_TEXT_TYPE, R.layout.toutiao_fragment_wenda_text_content);
        multiNewsAdapter.addItemType(WendaArticleBeanData.WENDA_THREE_IMG_TYPE, R.layout.toutiao_fragment_wenda_three_image_content);
        multiNewsAdapter.addItemType(WendaArticleBeanData.WENDA_ONE_IMG_TYPE, R.layout.toutiao_fragment_wenda_one_image_content);
        multiNewsAdapter.addFooter(getLayoutInflater().inflate(R.layout.toutiao_fragment_news_foot_view, (ViewGroup) recyclerView.getParent(), false));
        recyclerView.setAdapter(multiNewsAdapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.toutiao_colorPrimary));
        recyclerView.addOnScrollListener(new OnRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                LogUtils.d(TAG, "onLoadMore: ");
                getNewsData();
            }
        });
    }

    public void getNewsData() {
        refreshLayout.setRefreshing(true);

        IMobileWenDaApi mobileNewsApi = TouTiaoRetrofit.getRetrofit().create(IMobileWenDaApi.class);
        Observable<WendaArticleBean> observable = mobileNewsApi.getWendaArticle(getCurrentTimeStamp());
        observable
                .subscribeOn(Schedulers.io())
                .switchMap(new Function<WendaArticleBean, ObservableSource<WendaArticleBeanData>>() {
                    @Override
                    public ObservableSource<WendaArticleBeanData> apply(WendaArticleBean wendaArticleBean) throws Exception {
                        List<WendaArticleBean.DataBean> beanList = wendaArticleBean.getData();
                        final ArrayList<WendaArticleBeanData> list = new ArrayList<>();
                        for (WendaArticleBean.DataBean dataBean : beanList) {
                            WendaArticleBeanData data = gson.fromJson(dataBean.getContent(), WendaArticleBeanData.class);
                            list.add(data);
                        }
                        return Observable.fromIterable(list);
                    }
                })
                .filter(new Predicate<WendaArticleBeanData>() {
                    @Override
                    public boolean test(WendaArticleBeanData wendaArticleBeanData) throws Exception {
                        return !TextUtils.isEmpty(wendaArticleBeanData.getQuestion());
                    }
                })
                .map(new Function<WendaArticleBeanData, WendaArticleBeanData>() {
                    @Override
                    public WendaArticleBeanData apply(WendaArticleBeanData wendaArticleBeanData) throws Exception {
                        WendaArticleBeanData.ExtraBean extraBean = gson.fromJson(wendaArticleBeanData.getExtra(), WendaArticleBeanData.ExtraBean.class);
                        WendaArticleBeanData.QuestionBean questionBean = gson.fromJson(wendaArticleBeanData.getQuestion(), WendaArticleBeanData.QuestionBean.class);
                        WendaArticleBeanData.AnswerBean answerBean = gson.fromJson(wendaArticleBeanData.getAnswer(), WendaArticleBeanData.AnswerBean.class);
                        wendaArticleBeanData.setExtraBean(extraBean);
                        wendaArticleBeanData.setQuestionBean(questionBean);
                        wendaArticleBeanData.setAnswerBean(answerBean);

                        String time = wendaArticleBeanData.getBehot_time();
                        return wendaArticleBeanData;
                    }
                })
                .filter(new Predicate<WendaArticleBeanData>() {
                    @Override
                    public boolean test(WendaArticleBeanData wendaArticleBeanData) throws Exception {
                        for (WendaArticleBeanData bean : mList) {
                            if (bean.getQuestionBean().getTitle().equals(wendaArticleBeanData.getQuestionBean().getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.<List<WendaArticleBeanData>>autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new Consumer<List<WendaArticleBeanData>>() {
                    @Override
                    public void accept(List<WendaArticleBeanData> wendaArticleBeanData) throws Exception {
                        mList.addAll(wendaArticleBeanData);
                        multiNewsAdapter.setData(mList);
                        refreshLayout.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        ToastUtil.toast(throwable.getMessage());
                    }
                });
    }


    public String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    @Override
    public void onRefresh() {
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition == 0) {
            mList.clear();
            refreshLayout.setRefreshing(true);
            getNewsData();
        }
    }
}
