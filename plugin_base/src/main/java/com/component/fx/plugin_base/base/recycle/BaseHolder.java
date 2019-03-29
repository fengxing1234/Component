package com.component.fx.plugin_base.base.recycle;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private SparseArray<View> mViews;

    private BaseHolder(@NonNull View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<>();
    }

    public Context getContext() {
        return mItemView.getContext();
    }

    public static BaseHolder get(int layoutId, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new BaseHolder(view);
    }

    public <T extends View> T getView(@IdRes int resId) {

        View view = mViews.get(resId);
        if (view == null) {
            view = mItemView.findViewById(resId);
            mViews.put(resId, view);
        }
        return (T) view;

    }


    /**
     * 下面定义辅助方法
     */


    public BaseHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public BaseHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public BaseHolder setOnClickListener(int viewId,
                                         View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
