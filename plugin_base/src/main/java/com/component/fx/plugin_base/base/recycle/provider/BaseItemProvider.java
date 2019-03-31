package com.component.fx.plugin_base.base.recycle.provider;

import com.component.fx.plugin_base.base.recycle.BaseHolder;

public abstract class BaseItemProvider<T> {

    public abstract int viewType();

    public abstract int getLayout();

    public abstract void convert(BaseHolder holder, T data, int position);

    //子类若想实现条目点击事件则重写该方法
    //Subclasses override this method if you want to implement an item click event
    public void onItemClick(BaseHolder holder, T data, int position) {

    }


    //子类若想实现条目长按事件则重写该方法
    //Subclasses override this method if you want to implement an item long press event
    public boolean onItemLongClick(BaseHolder holder, T data, int position) {
        return false;
    }


}
