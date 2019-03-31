package com.component.fx.plugin_base.base.recycle;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

    private static final int HEADER_VIEW_TYPE = 0x00001111;
    private static final int FOOTER_VIEW_TYPE = 0x00002222;

    protected List<T> mList = new ArrayList<>();

    protected Context mContext;

    private LinearLayout headerLayout;
    private LinearLayout footerLayout;

    protected BaseAdapter() {

    }

    @Override
    public int getItemViewType(int position) {
        int headerLayoutCount = getHeaderLayoutCount();

        if (position < headerLayoutCount) {//头布局 有就一个 没有就0个 只有position ==0时 header == 1时 返回头布局
            return HEADER_VIEW_TYPE;
        } else {// 默认的布局 和 尾布局

            int exceptHeaderSize = position - headerLayoutCount;//除了头布局 剩下的就是尾布局 和 正常的布局
            int adapterSize = mList.size();//正常布局的数量

            if (exceptHeaderSize < adapterSize) {
                return getDefItemViewType(exceptHeaderSize);//返回默认的数据布局 默认布局 0
            } else {
                return FOOTER_VIEW_TYPE;//返回尾布局
            }
        }

    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public abstract @LayoutRes
    int getLayoutRes();

    protected abstract void convert(@NonNull BaseHolder baseHolder, T data, int position);


    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        BaseHolder holder;
        switch (viewType) {
            case HEADER_VIEW_TYPE:
                holder = getBaseHolder(headerLayout);
                break;
            case FOOTER_VIEW_TYPE:
                holder = getBaseHolder(footerLayout);
                break;
            default:
                holder = onCreateDefViewHolder(viewGroup, viewType);
                break;
        }
        return holder;
    }

    public BaseHolder getBaseHolder(View view) {
        return BaseHolder.get(view);
    }

    protected BaseHolder onCreateDefViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return BaseHolder.get(getLayoutRes(), viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int position) {
        switch (getItemViewType(position)) {
            case HEADER_VIEW_TYPE:
                break;
            case FOOTER_VIEW_TYPE:
                break;
            default:
                convert(baseHolder, getItem(position - getHeaderLayoutCount()), position);
                break;
        }

    }

    private T getItem(int position) {
        if (position >= 0 && position < mList.size()) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + getHeaderLayoutCount() + getFooterLayoutCount();
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


    /**
     * 添加头布局
     *
     * @param header
     */

    public void addHeader(View header) {
        addHeader(header, -1);
    }

    public void addHeader(View header, int index) {
        addHeader(header, index, LinearLayout.VERTICAL);
    }

    public void addHeader(View header, int index, int orientation) {
        if (headerLayout == null) {
            headerLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                headerLayout.setOrientation(LinearLayout.VERTICAL);
                headerLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                headerLayout.setOrientation(LinearLayout.HORIZONTAL);
                headerLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }

        int childCount = headerLayout.getChildCount();
        int position = index;
        if (index < 0 || index > childCount) {//边界
            position = childCount;
        }

        headerLayout.addView(header, position);
    }

    /**
     * 添加了头布局 返回 1  没有添加返回0
     *
     * @return
     */
    private int getHeaderLayoutCount() {
        if (headerLayout == null || headerLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 添加尾布局
     *
     * @param footer
     */

    public void addFooter(View footer) {
        addFooter(footer, -1);
    }

    public void addFooter(View footer, int index) {
        addFooter(footer, index, LinearLayout.VERTICAL);
    }

    public void addFooter(View footer, int index, int orientation) {
        if (footerLayout == null) {
            footerLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                footerLayout.setOrientation(LinearLayout.VERTICAL);
                footerLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                footerLayout.setOrientation(LinearLayout.HORIZONTAL);
                footerLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }

        int childCount = footerLayout.getChildCount();
        int position = index;
        if (index < 0 || index > childCount) {//边界
            position = childCount;
        }

        footerLayout.addView(footer, position);
    }

    private int getFooterLayoutCount() {
        if (footerLayout == null || footerLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }
}

