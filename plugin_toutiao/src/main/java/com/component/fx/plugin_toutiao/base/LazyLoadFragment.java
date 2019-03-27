package com.component.fx.plugin_toutiao.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class LazyLoadFragment extends BaseFragment {

    private boolean isFragmentVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;
        tryLazyLoadData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tryLazyLoadData();
    }

    private void tryLazyLoadData() {
        //页面可见 并且 view初始化完成
        if (isFragmentVisible && isViewCreate) {
            lazyLoadData();
        }
    }

    //具体实现数据请求的逻辑
    protected abstract void lazyLoadData();
}
