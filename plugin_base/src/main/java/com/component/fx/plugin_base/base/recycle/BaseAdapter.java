package com.component.fx.plugin_base.base.recycle;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

    protected List<T> mList = new ArrayList<>();

    protected Context mContext;

    protected BaseAdapter() {

    }

    /**
     * 重新设置数据 清除以前的旧数据
     *
     * @param list
     */
    public void setData(List<T> list) {
        if (list == null || list.size() == 0)
            return;

        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(int index, T data) {
        if (data == null) return;
        mList.add(index, data);
        notifyItemInserted(index);
    }

    public void addData(int index, List<T> list) {
        if (list == null || list.size() == 0) return;
        mList.addAll(index, list);
        notifyItemInserted(index);
    }

    public void addData(List<T> list) {
        if (list == null || list.size() == 0) return;
        mList.addAll(list);
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }


    public abstract @LayoutRes
    int getLayoutRes();

    protected abstract void convert(@NonNull BaseHolder baseHolder, T data, int position);


    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        BaseHolder defViewHolder = onCreateDefViewHolder(viewGroup, viewType);
        return defViewHolder;
    }

    protected BaseHolder onCreateDefViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return BaseHolder.get(getLayoutRes(), viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int position) {
        convert(baseHolder, mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
