package com.component.fx.plugin_base.base.recyclerview;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.component.fx.plugin_base.base.recyclerview.animator.AlphaInAnimation;
import com.component.fx.plugin_base.base.recyclerview.animator.BaseAnimation;
import com.component.fx.plugin_base.base.recyclerview.animator.ScaleInAnimation;
import com.component.fx.plugin_base.base.recyclerview.animator.SlideInBottomAnimation;
import com.component.fx.plugin_base.base.recyclerview.animator.SlideInLeftAnimation;
import com.component.fx.plugin_base.base.recyclerview.animator.SlideInRightAnimation;
import com.component.fx.plugin_base.base.recyclerview.provider.MultiTypeDelegate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

    // TODO: 2019-05-28 添加类型 别忘了其他地方做判断
    public static final int HEADER_VIEW_TYPE = 0x00001111;
    public static final int FOOTER_VIEW_TYPE = 0x00002222;

    private static final String TAG = "BaseAdapter";

    protected List<T> mList = new ArrayList<>();

    protected Context mContext;

    private LinearLayout headerLayout;
    private LinearLayout footerLayout;
    private boolean mEnableAnimation;


    ///////////////动画///////////////
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int ALPHAIN = 0x00000001;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 0x00000002;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 0x00000004;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 0x00000005;
    private Interpolator mInterpolator = new LinearInterpolator();
    //动画效果时长
    private long mDuration = 300;
    //上一次显示动画的位置
    private int mLastLayoutPosition;
    //每个条目 是否 只显示一次动画 默认 每一个item只显示一次动画
    private boolean mFirstOnlyEnable = true;
    //条目自定义动画
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();


    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemChildLongClickListener mOnItemChildLongClickListener;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {

    }

    protected BaseAdapter() {

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // TODO: 2019/3/31 需要处理GridLayoutManager
        Log.d(TAG, "onAttachedToRecyclerView: ");
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d(TAG, "onViewAttachedToWindow: ");
        addAnimation(holder);
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public void openLoadAnimation(BaseAnimation baseAnimation) {
        this.mCustomAnimation = baseAnimation;
        openLoadAnimation();
    }

    public void openLoadAnimation(int animationType) {
        mCustomAnimation = null;
        switch (animationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
        openLoadAnimation();
    }

    public void openLoadAnimation() {
        this.mEnableAnimation = true;
    }

    /**
     * 每个条目 是否 只显示一次动画
     *
     * @param firstOnlyEnable
     */
    public void isFirstOnly(boolean firstOnlyEnable) {
        this.mFirstOnlyEnable = firstOnlyEnable;
    }

    private void addAnimation(BaseHolder holder) {
        if (mEnableAnimation) {

            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastLayoutPosition) {

                BaseAnimation animation;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }

                for (Animator animator : animation.getAnimations(holder.itemView)) {
                    animator.setDuration(mDuration);
                    animator.start();
                    animator.setInterpolator(mInterpolator);
                }

                mLastLayoutPosition = holder.getLayoutPosition();
            }
        }
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
        if (multiTypeDelegate != null) {
            return multiTypeDelegate.getDefItemViewType(mList, position);
        }
        return super.getItemViewType(position);
    }

    public abstract int getLayoutRes();

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
        bindViewClickListener(holder);
        return holder;
    }

    /**
     * 绑定点击事件
     *
     * @param holder
     */
    private void bindViewClickListener(final BaseHolder holder) {
        if (holder == null) {
            return;
        }

        final View itemView = holder.getItemView();
        if (itemView == null) {
            return;
        }

        if (getOnItemClickListener() != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnItemClick(v, holder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }

        if (getOnItemLongClickListener() != null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return setOnItemLongClick(v, holder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }

    }

    public void setOnItemClick(View v, int position) {
        getOnItemClickListener().onItemClick(BaseAdapter.this, v, position);
    }

    public boolean setOnItemLongClick(View v, int position) {
        return getOnItemLongClickListener().onItemLongClick(BaseAdapter.this, v, position);
    }

    public BaseHolder getBaseHolder(View view) {
        return BaseHolder.get(view);
    }

    /**
     * 正常数据 包括多item数据
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    protected BaseHolder onCreateDefViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (multiTypeDelegate != null) {
            return BaseHolder.get(multiTypeDelegate.getLayoutId(viewType), viewGroup);
        }
        return BaseHolder.get(getLayoutRes(), viewGroup);
    }

    private MultiTypeDelegate<T> multiTypeDelegate;

    protected void setMultiTypeDelegate(MultiTypeDelegate<T> multiTypeDelegate) {
        this.multiTypeDelegate = multiTypeDelegate;
    }

    protected MultiTypeDelegate getMultiTypeDelegate() {
        return multiTypeDelegate;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int position) {
        switch (getItemViewType(position)) {
            case HEADER_VIEW_TYPE://不做处理
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

    public void remove(int index) {
        mList.remove(index);
        notifyItemRemoved(index);
    }

    public void remove(T data) {
        int i = mList.indexOf(data);
        mList.remove(data);
        notifyItemRemoved(i);
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
    public int getHeaderLayoutCount() {
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


    public interface OnItemChildClickListener {
        /**
         * callback method to be invoked when an itemchild in this view has been click
         *
         * @param adapter
         * @param view     The view whihin the ItemView that was clicked
         * @param position The position of the view int the adapter
         */
        void onItemChildClick(BaseAdapter adapter, View view, int position);
    }


    /**
     * Interface definition for a callback to be invoked when an childView in this
     * view has been clicked and held.
     */
    public interface OnItemChildLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param adapter  this BaseAdapter adapter
         * @param view     The childView whihin the itemView that was clicked and held.
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        boolean onItemChildLongClick(BaseAdapter adapter, View view, int position);
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * view has been clicked and held.
     */
    public interface OnItemLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param adapter  the adpater
         * @param view     The view whihin the RecyclerView that was clicked and held.
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        boolean onItemLongClick(BaseAdapter adapter, View view, int position);
    }


    /**
     * Interface definition for a callback to be invoked when an item in this
     * RecyclerView itemView has been clicked.
     */
    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this RecyclerView has
         * been clicked.
         *
         * @param adapter  the adpater
         * @param view     The itemView within the RecyclerView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        void onItemClick(BaseAdapter adapter, View view, int position);
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an itemchild in View has
     * been  clicked
     *
     * @param listener The callback that will run
     */
    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an itemchild  in this View has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        mOnItemChildLongClickListener = listener;
    }


    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been long clicked and held, or null id no callback as been set.
     */
    public final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been clicked and held, or null id no callback as been set.
     */
    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * @return The callback to be invoked with an itemchild in this RecyclerView has
     * been clicked, or null id no callback has been set.
     */
    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    /**
     * @return The callback to be invoked with an itemChild in this RecyclerView has
     * been long clicked, or null id no callback has been set.
     */
    @Nullable
    public final OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }
}

