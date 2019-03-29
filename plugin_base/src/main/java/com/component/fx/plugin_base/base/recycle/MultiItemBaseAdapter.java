package com.component.fx.plugin_base.base.recycle;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.ViewGroup;

public abstract class MultiItemBaseAdapter<T extends MultiItemEntity> extends BaseAdapter<T> {

    public static final int TYPE_NOT_FOUND = -404;


    private SparseIntArray layouts;

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getItemType();
    }

    @Override
    protected BaseHolder onCreateDefViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return BaseHolder.get(getItemTypeLayoutRes(viewType), viewGroup);
    }

    public void addItemType(int type, @LayoutRes int layoutRes) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutRes);
    }

    private int getItemTypeLayoutRes(int type) {
        if (layouts != null) {
            return layouts.get(type, TYPE_NOT_FOUND);
        }
        return TYPE_NOT_FOUND;
    }
}
