package com.component.fx.plugin_toutiao.base;

import com.uber.autodispose.AutoDisposeConverter;

import java.util.List;

public interface IBaseView<T> extends BaseView<T> {

    /**
     * 显示加载动画
     */
    void onShowLoading();

    /**
     * 隐藏加载
     */
    void onHideLoading();

    /**
     * 显示网络错误
     */
    void onShowNetError();

    /**
     * 设置 presenter
     */
    void setPresenter(T presenter);

    /**
     * 设置适配器
     */
    void onSetAdapter(List<?> list);

    /**
     * 加载完毕
     */
    void onShowNoMore();


    /**
     * 绑定生命周期
     */
    <X> AutoDisposeConverter<X> bindAutoDispose();
}
