package com.component.fx.plugin_base.base.recyclerview.drawswipe;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.component.fx.plugin_base.R;
import com.component.fx.plugin_base.base.recyclerview.BaseAdapter;

public class ItemDragAndSwipeCallback extends ItemTouchHelper.Callback {

    private static final String TAG = ItemDragAndSwipeCallback.class.getSimpleName();

    private BaseItemDraggableAdapter mAdapter;

    private float mMoveThreshold = 0.1f;
    private float mSwipeThreshold = 0.7f;


    private int mDragMoveFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    private int mSwipeMoveFlags = ItemTouchHelper.END;

    public ItemDragAndSwipeCallback(BaseItemDraggableAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 返回如果长按项目，ItemTouchHelper是否应该开始拖放操作
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        Log.d(TAG, "isLongPressDragEnabled: false");
        return false;
    }

    /**
     * 控制是否启用滑动删除
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        Log.d(TAG, "isItemViewSwipeEnabled: " + mAdapter.isItemSwipeEnable());
        return mAdapter.isItemSwipeEnable();
    }

    /**
     * 由ItemTouchHelper滑动或拖动ViewHolder时调用
     * 当item由静止状态变为滑动或拖动状态时调用此方法，可通过actionState判断Item在哪种状态下执行某些操作，
     * 重写该方法时必须调用其父类的该方法。
     * <p>
     * 松手的时返回0 手指抬起的那一刻
     *
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG
                && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemDragStart(viewHolder);
            viewHolder.itemView.setTag(R.id.BaseAdapter_dragging_support, true);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE
                && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwipeStart(viewHolder);
            viewHolder.itemView.setTag(R.id.BaseAdapter_swiping_support, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 当与用户交互结束或相关动画完成之后被 调用该方法被调用。
     * 当用户与元素的交互结束并且它也完成了动画时，由ItemTouchHelper调用
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (isViewCreateByAdapter(viewHolder)) {
            return;
        }

        if (viewHolder.itemView.getTag(R.id.BaseAdapter_dragging_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.BaseAdapter_dragging_support)) {
            mAdapter.onItemDragEnd(viewHolder);
            viewHolder.itemView.setTag(R.id.BaseAdapter_dragging_support, false);
        }
        if (viewHolder.itemView.getTag(R.id.BaseAdapter_swiping_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.BaseAdapter_swiping_support)) {
            mAdapter.onItemSwipeClear(viewHolder);
            viewHolder.itemView.setTag(R.id.BaseAdapter_swiping_support, false);
        }
    }

    /**
     * 控制滑动删除和拖放的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "getMovementFlags: mDragMoveFlags = " + mDragMoveFlags + " mSwipeMoveFlags = " + mSwipeMoveFlags);
        if (isViewCreateByAdapter(viewHolder)) {
            return makeMovementFlags(0, 0);
        }
        return makeMovementFlags(mDragMoveFlags, mSwipeMoveFlags);
    }

    /**
     * 拖放后回调 当旧item和新item交替时候
     *
     * @param recyclerView
     * @param viewHolder
     * @param viewHolder1
     * @return
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return viewHolder.getItemViewType() == viewHolder1.getItemViewType();
    }

    /**
     * 当onMove（RecyclerView，ViewHolder，ViewHolder）返回true时调用。
     * if (this.mCallback.onMove(this.mRecyclerView, viewHolder, target)) {
     * this.mCallback.onMoved(this.mRecyclerView, viewHolder, fromPosition, target, toPosition, x, y);
     * }
     *
     * @param recyclerView
     * @param viewHolder
     * @param fromPos
     * @param target
     * @param toPos
     * @param x
     * @param y
     */
    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        mAdapter.onItemDragMoving(viewHolder, target);
    }

    /**
     * 滑动完成后回调
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (!isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemSwiped(viewHolder);
        }
    }

    /**
     * 返回用户应移动View的部分，以便在拖动时考虑该部分。移动视图后，ItemTouchHelper开始检查其下方的视图是否可能丢弃。
     *
     * @param viewHolder
     * @return
     */
    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return mMoveThreshold;
    }

    /**
     * 返回用户应移动View以被视为滑动的分数。
     * 返回用户应移动View以被视为滑动的分数。该分数是根据RecyclerView的界限计算的。
     * 默认值为.5f，这意味着，要滑动视图，用户必须将视图移动至少一半RecyclerView的宽度或高度，具体取决于滑动方向
     *
     * @param viewHolder
     * @return
     */
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return mSwipeThreshold;
    }

    /**
     * 设置用户应将View移动的分数视为滑动。
     * 分数是根据RecyclerView的界限计算的。
     * <p>
     * 默认值为.5f，这意味着，要滑动视图，用户必须将视图移动至少一半RecyclerView的宽度或高度，具体取决于滑动方向。
     *
     * @param swipeThreshold 一个浮点值，表示视图大小的分数。 默认值为.7f。
     */
    public void setSwipeThreshold(float swipeThreshold) {
        mSwipeThreshold = swipeThreshold;
    }

    /**
     * 设置用户应移动视图的分数，以便在拖动视图时进行考虑。 移动视图后，ItemTouchHelper开始检查其下方的视图是否可能丢弃
     *
     * @param moveThreshold 一个浮点值，表示视图大小的分数。 默认值为.1f
     */
    public void setMoveThreshold(float moveThreshold) {
        mMoveThreshold = moveThreshold;
    }

    /**
     * 设置拖动移动方向。
     *
     * @param dragMoveFlags 拖动移动方向。 默认值为ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT。
     */
    public void setDragMoveFlags(int dragMoveFlags) {
        Log.d(TAG, "setDragMoveFlags: " + dragMoveFlags);
        mDragMoveFlags = dragMoveFlags;
    }

    /**
     * 设置滑动移动方向
     * 值应为ItemTouchHelper.START，ItemTouchHelper.END或它们的组合
     *
     * @param swipeMoveFlags 滑动移动方向。 默认值为ItemTouchHelper.END。
     */
    public void setSwipeMoveFlags(int swipeMoveFlags) {
        Log.d(TAG, "setSwipeMoveFlags: " + swipeMoveFlags);
        mSwipeMoveFlags = swipeMoveFlags;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && !isViewCreateByAdapter(viewHolder)) {
            final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }
    }


    /**
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX                X轴滑动的部分 像素
     * @param dY
     * @param actionState       View上的交互类型。是ACTION_STATE_DRAG还是ACTION_STATE_SWIPE。
     * @param isCurrentlyActive 如果此视图当前由用户控制，则为True，否则为false，它只是动画回原始状态
     */
    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && !isViewCreateByAdapter(viewHolder)) {
            View itemView = viewHolder.itemView;
            //将当前矩阵和剪辑保存到专用堆栈。
            //后续调用translate，scale，rotate，skew，concat或clipRect，clipPath将照常运行，但是当进行restore（）的平衡调用时，将忘记这些调用，以及保存之前存在的设置（ ）将恢复。
            c.save();

            if (dX > 0) {
                c.clipRect(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom());
                c.translate(itemView.getLeft(), itemView.getTop());
            } else {
                c.clipRect(itemView.getRight() + dX, itemView.getTop(),
                        itemView.getRight(), itemView.getBottom());
                c.translate(itemView.getRight() + dX, itemView.getTop());
            }
            mAdapter.onItemSwiping(c, viewHolder, dX, dY, isCurrentlyActive);
            c.restore();
        }
    }

    // TODO: 2019-05-28 添加类型 别忘了其他地方做判断
    public boolean isViewCreateByAdapter(@NonNull RecyclerView.ViewHolder viewHolder) {
        int type = viewHolder.getItemViewType();
        return type == BaseAdapter.HEADER_VIEW_TYPE || type == BaseAdapter.FOOTER_VIEW_TYPE;
    }
}
