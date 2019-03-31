package com.component.fx.plugin_base.base.recycle.provider;

import android.util.SparseIntArray;

import java.util.List;

public abstract class MultiTypeDelegate<T> {

    private SparseIntArray layouts;
    private static final int DEFAULT_VIEW_TYPE = -0xff;


    public int getDefItemViewType(List<T> list, int position) {
        T t = list.get(position);
        return t != null ? getViewType(t) : DEFAULT_VIEW_TYPE;
    }


    public int getLayoutId(int viewType) {
        return layouts.get(viewType);
    }

    //传入数据 由用户根据数据做判断 应该返回那种类型
    public abstract int getViewType(T data);

    public MultiTypeDelegate registerItemType(int key, int layout) {
        addItemType(key, layout);
        return this;
    }

    private void addItemType(int key, int layout) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }

        layouts.put(key, layout);

    }
}
