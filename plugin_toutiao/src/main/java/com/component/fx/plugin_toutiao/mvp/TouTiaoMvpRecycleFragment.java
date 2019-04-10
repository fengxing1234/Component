package com.component.fx.plugin_toutiao.mvp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.component.fx.plugin_base.utils.LogUtils;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.base.IBasePresenter;
import com.component.fx.plugin_toutiao.base.IBaseView;
import com.component.fx.plugin_toutiao.base.MvpLazyLoadFragment;
import com.component.fx.plugin_toutiao.widget.OnRecycleViewScrollListener;

public abstract class TouTiaoMvpRecycleFragment<T extends IBasePresenter> extends MvpLazyLoadFragment<T> implements IBaseView<T>, SwipeRefreshLayout.OnRefreshListener {


    protected SwipeRefreshLayout refreshLayout;
    protected RecyclerView recyclerView;
    protected LinearLayoutManager layoutManager;


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
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.toutiao_colorPrimary));
        recyclerView.addOnScrollListener(new OnRecycleViewScrollListener(2) {
            @Override
            public void onLoadMore() {
                LogUtils.d(TAG, "onLoadMore: ");
                presenter.doLoadMoreData();
            }
        });
    }

    @Override
    public void onRefresh() {
        presenter.doLoadData();
    }

    @Override
    protected void lazyLoadData() {
        presenter.doLoadData();
    }

    @Override
    public void onShowLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onShowNetError() {
        ToastUtil.toast("onShowNetError");
    }


    @Override
    public void onShowNoMore() {
        ToastUtil.toast("onShowNoMore");
    }

}
