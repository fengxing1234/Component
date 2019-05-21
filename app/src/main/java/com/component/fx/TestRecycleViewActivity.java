package com.component.fx;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.component.fx.plugin_base.polling.PollingBroadcastService;
import com.component.fx.plugin_base.polling.PollingService;
import com.component.fx.plugin_base.base.BaseActivity;
import com.component.fx.plugin_base.base.recycle.BaseAdapter;
import com.component.fx.plugin_base.base.recycle.BaseHolder;
import com.component.fx.plugin_base.polling.RxjavaPolling;

import java.util.ArrayList;
import java.util.List;

public class TestRecycleViewActivity extends BaseActivity {

    private MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_recycle_animation);

        findViewById(R.id.test_btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PollingService.startAlarmPolling(TestRecycleViewActivity.this);
                RxjavaPolling.taskPolling();
                PollingBroadcastService.startService(TestRecycleViewActivity.this);
                adapter.addData(1, "1");
            }
        });


        findViewById(R.id.test_btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(1);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.test_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        //recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MyAdapter();

        recyclerView.setAdapter(adapter);
        adapter.setData(getData());
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
            list.add(i + "");
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
