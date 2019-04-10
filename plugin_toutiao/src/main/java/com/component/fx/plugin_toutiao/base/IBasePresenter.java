package com.component.fx.plugin_toutiao.base;

public interface IBasePresenter extends BasePresenter {
    /**
     * 请求数据
     */
    void doLoadData(String... category);

    /**
     * 加载更多
     */
    void doLoadMoreData();
}
