package com.component.fx.plugin_toutiao.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class LazyLoadFragment extends BaseFragment {

    /**
     * 页面是否可见
     */

    protected boolean isFragmentVisible;
    /**
     * 是否加载过数据
     */
    protected boolean isDataInitiated;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;
        tryLazyLoadData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //进入app 默认 显示第一个页面 如果不上这行代码 默认不会加载数据
        tryLazyLoadData();
    }

    private void tryLazyLoadData() {
        //页面可见 并且 view初始化完成
        if (isFragmentVisible && isViewCreate && !isDataInitiated) {
            lazyLoadData();
            isDataInitiated = true;
        }
    }

    //具体实现数据请求的逻辑
    protected abstract void lazyLoadData();
}
