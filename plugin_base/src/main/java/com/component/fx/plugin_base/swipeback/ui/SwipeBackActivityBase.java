package com.component.fx.plugin_base.swipeback.ui;

public interface SwipeBackActivityBase {

    //得到SwipeBackLayout对象
    SwipeBackLayout getSwipeBackLayout();

    //设置是否可以滑动返回
    void setSwipeBackEnable(boolean enable);

    //自动滑动返回并关闭Activity
    void scrollToFinishActivity();
}
