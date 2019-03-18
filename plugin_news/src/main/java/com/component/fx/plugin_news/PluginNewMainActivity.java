package com.component.fx.plugin_news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.component.fx.plugin_base.BaseActivity;
import com.component.fx.plugin_news.adapter.NewsFragmentPageAdapter;
import com.component.fx.plugin_news.network.NewsEnum;

import java.util.ArrayList;
import java.util.List;

public class PluginNewMainActivity extends BaseActivity {

    private static final String TAG = PluginNewMainActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> mTitles;
    private NewsFragmentPageAdapter newsFragmentPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity_main);
        setSwipeBackEnable(false);
        initData();
        initView();
    }

    private void initData() {
        if (mTitles == null) {
            mTitles = new ArrayList<>();
        }
        mTitles.clear();
        for (NewsEnum newsEnum : NewsEnum.values()) {
            mTitles.add(newsEnum.getName());
        }

    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.news_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.news_view_pager);
        newsFragmentPageAdapter = new NewsFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(newsFragmentPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
}
