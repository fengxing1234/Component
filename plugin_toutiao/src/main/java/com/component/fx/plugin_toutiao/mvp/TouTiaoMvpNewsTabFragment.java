package com.component.fx.plugin_toutiao.mvp;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.adapter.TouTiaoMultiNewRvAdapter;
import com.component.fx.plugin_toutiao.bean.MultiNewsArticleBeanData;

import java.util.List;

public class TouTiaoMvpNewsTabFragment extends TouTiaoMvpRecycleFragment<INewsTabContract.Presenter> implements INewsTabContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static final String NEWS_CATEGORY_KEY = "news_category_key";
    private String type;
    private TouTiaoMultiNewRvAdapter adapter;


    public static TouTiaoMvpNewsTabFragment getInstance(String type) {
        TouTiaoMvpNewsTabFragment fragment = new TouTiaoMvpNewsTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_CATEGORY_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getString(NEWS_CATEGORY_KEY);
        } else {
            type = "news_hot";
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        adapter = new TouTiaoMultiNewRvAdapter();
        adapter.addFooter(getLayoutInflater().inflate(R.layout.toutiao_fragment_news_foot_view, (ViewGroup) recyclerView.getParent(), false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public INewsTabContract.Presenter getPresenter() {
        return new INewsTabPresenter(this);
    }

    @Override
    protected void lazyLoadData() {
        presenter.doLoadData(type);
    }


    @Override
    public void onSetAdapter(List<?> list) {
        adapter.setData((List<MultiNewsArticleBeanData>) list);
    }


    @Override
    public void onRefresh() {
        presenter.doLoadData(type);
    }
}
