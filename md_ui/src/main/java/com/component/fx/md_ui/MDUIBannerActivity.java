package com.component.fx.md_ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.component.fx.plugin_base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class MDUIBannerActivity extends AppCompatActivity {

    private static final String TAG = "MDUIBannerActivity";
    private ArrayList<String> list;
    List<Integer> images = new ArrayList<>();
    private ConvenientBanner banner;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_banner);

        StatusBarUtil.imageFullScreen(this);
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT, 0);

        final View rootView = findViewById(R.id.cl_root);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float progress = (float) (((int) (Math.abs(verticalOffset) / (appBarLayout.getTotalScrollRange() * 0.01))) * 0.01);

                // 展开状态 progress为0，展开状态的toolbar可见
                if (verticalOffset == 0) {
                    Log.d(TAG, "展开状态");
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    //折叠状态 progress为1，折叠状态的toolbar可见
                    Log.d(TAG, "折叠状态");
                } else {
                    //中间状态，两个toolbar是重叠在一起的，根据是从折叠到展开，还是从展开到折叠，同时加上进度progress确定两个toolbar的透明比
                    Log.d(TAG, "onOffsetChanged: " + progress);
                }
            }
        });


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData();
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                TextView textView = new TextView(viewGroup.getContext());
                textView.setTextSize(14);
                textView.setPadding(10, 10, 10, 10);
                return new BannerViewHolder(textView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ((TextView) viewHolder.itemView).setText(list.get(i));
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        });

        images.add(R.drawable.m_img1);
        images.add(R.drawable.m_img2);
        images.add(R.drawable.mdui_error_image);
        banner = (ConvenientBanner) findViewById(R.id.banner);
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                return new LocalImageHolderView(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.banner_image_layout;
            }
        }, images)
                //.setPageIndicator(new int[]{R.drawable.selector_banner_indicator_circle})
                //.setPageIndicator(new int[]{R.drawable.banner_indicator_gray_circle,R.drawable.banner_indicator_white_circle})
                //.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setCanLoop(true)
                .startTurning(3000).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Snackbar.make(banner, "点击了" + position, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add((i + 1) + "");
        }
    }

    private class BannerViewHolder extends RecyclerView.ViewHolder {

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
