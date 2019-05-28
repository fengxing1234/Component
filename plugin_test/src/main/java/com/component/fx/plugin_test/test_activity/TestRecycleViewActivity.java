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
import com.component.fx.plugin_base.base.recycle.BaseAdapter;
import com.component.fx.plugin_base.base.recycle.BaseHolder;
import com.component.fx.plugin_test.R;

import java.util.ArrayList;
import java.util.Collections;
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

        itemTouchHelper();
    }

    private void itemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = viewHolder1.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(data, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(data, i, i - 1);
                    }
                }

                adapter.notifyItemMoved(fromPosition, toPosition);
                //返回true表示执行拖动
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                data.remove(position);
                adapter.notifyItemRemoved(position);
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
            return R.layout.test_recycle_item;
        }

        @Override
        protected void convert(@NonNull BaseHolder baseHolder, String data, int position) {
            ((TextView) baseHolder.getView(R.id.tv_num)).setText(data);
        }
    }
}
