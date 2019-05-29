package com.component.fx.plugin_test.test_activity;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.component.fx.plugin_base.base.BaseActivity;
import com.component.fx.plugin_base.base.recyclerview.BaseAdapter;
import com.component.fx.plugin_base.base.recyclerview.BaseHolder;
import com.component.fx.plugin_test.R;

import java.util.ArrayList;
import java.util.List;

public class TestRecycleViewActivity extends BaseActivity {

    private static final String TAG = "TestRecycleViewActivity";
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private List<String> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_recycle_animation);

        data = getData();
        findViewById(R.id.test_btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addData(1, "1");
            }
        });


        findViewById(R.id.test_btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(1);
            }
        });


        recyclerView = findViewById(R.id.test_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        //recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MyAdapter();

        recyclerView.setAdapter(adapter);
        adapter.setData(data);
        adapter.openLoadAnimation(BaseAdapter.SCALEIN);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        //RecylerView从空到渲染完内容时执行动画的最佳方式

//        //有效  进入动画
//        int resId = R.anim.layout_animation_from_bottom;
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
//        recyclerView.setLayoutAnimation(animation);


        /**
         *     java.lang.ClassCastException:
         *     android.view.animation.LayoutAnimationController$AnimationParameters
         *     cannot be cast to android.view.animation.GridLayoutAnimationController$AnimationParameters
         */

//        int resId = R.anim.grid_layout_animation_from_bottom;
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
//        recyclerView.setLayoutAnimation(animation);

        //runLayoutAnimation(recyclerView);

        //itemTouchHelper();
        //BaseItemDraggable();
    }

    private void BaseItemDraggable() {
        //recyclerView.getRecycledViewPool().setMaxRecycledViews(0,0);
//        final Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setTextSize(20);
//        paint.setColor(Color.BLACK);
//
//
//        ItemDragAndSwipeCallback callback = new ItemDragAndSwipeCallback(adapter);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//
//        callback.setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
//        callback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
//
//        adapter.enableSwipeItem();
//        adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
//            @Override
//            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "onItemSwipeStart: ");
//                BaseHolder holder = ((BaseHolder) viewHolder);
//                holder.setTextColor(R.id.tv_num, Color.WHITE);
//            }
//
//            @Override
//            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "clearView: ");
//                BaseHolder holder = ((BaseHolder) viewHolder);
//            }
//
//            @Override
//            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "onItemSwiped: ");
//            }
//
//            @Override
//            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
//                Log.d(TAG, "onItemSwipeMoving: ");
//                canvas.drawColor(ContextCompat.getColor(TestRecycleViewActivity.this, R.color.color_light_blue));
//                canvas.drawText("Just some text", 0, 40, paint);
//            }
//        });
//
//        adapter.enableDragItem(itemTouchHelper);
//        adapter.setOnItemDragListener(new OnItemDragListener() {
//            @Override
//            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "onItemDragStart: ");
//                BaseHolder holder = ((BaseHolder) viewHolder);
//                holder.setTextColor(R.id.tv_num, Color.BLUE);
//            }
//
//            @Override
//            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
//                Log.d(TAG, "onItemDragMoving: ");
//            }
//
//            @Override
//            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.d(TAG, "onItemDragEnd: ");
//                BaseHolder holder = ((BaseHolder) viewHolder);
//                holder.setTextColor(R.id.tv_num, Color.GREEN);
//            }
//        });


    }

    private void itemTouchHelper() {
        ItemTouchHelper.Callback simpleCallback = new ItemTouchHelper.Callback() {


            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }


            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.START);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                Log.d(TAG, "dx : " + dX + "  DY :" + dY);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    Log.d(TAG, "alpha : " + alpha);
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public List<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("Number : " + i);
        }
        return list;
    }

    private class MyAdapter extends BaseAdapter<String> {

        @Override
        public int getLayoutRes() {
            return R.layout.test_recycler_item_root;
        }

        @Override
        protected void convert(@NonNull BaseHolder baseHolder, String data, int position) {
            TextView view = (TextView) baseHolder.getView(R.id.tv_num);
            view.setText(data);
        }
    }
}
