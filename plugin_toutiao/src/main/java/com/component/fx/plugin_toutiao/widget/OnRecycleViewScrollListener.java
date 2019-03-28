package com.component.fx.plugin_toutiao.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.component.fx.plugin_base.utils.LogUtils;

public abstract class OnRecycleViewScrollListener extends RecyclerView.OnScrollListener {

    private int itemCount;
    private int lastItemCount;

    public abstract void onLoadMore();


    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            itemCount = layoutManager.getItemCount();
            LogUtils.d("fengxing", "onScrolled: "+itemCount);
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            if (lastItemCount != itemCount && lastVisibleItemPosition == itemCount - 1) {
                lastItemCount = itemCount;
                onLoadMore();
            }
        } else {
            LogUtils.e("OnLoadMoreListener", "The OnLoadMoreListener only support LinearLayoutManager");
        }
    }

}
