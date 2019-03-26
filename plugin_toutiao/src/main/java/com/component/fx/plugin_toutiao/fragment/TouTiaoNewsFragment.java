package com.component.fx.plugin_toutiao.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.adapter.TouTiaoNewsPagerAdapter;

public class TouTiaoNewsFragment extends Fragment {

    private TabLayout tabLayout;
    private ImageButton imageButton;
    private ViewPager viewPager;
    private TouTiaoNewsPagerAdapter pagerAdapter;
    private String[] titles;


    public static TouTiaoNewsFragment getInstance() {
        TouTiaoNewsFragment fragment = new TouTiaoNewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles = getResources().getStringArray(R.array.toutiao_news_name);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.toutiao_main_news_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.toutiao_news_tab_layout);
        imageButton = (ImageButton) view.findViewById(R.id.toutiao_add_channel);
        viewPager = (ViewPager) view.findViewById(R.id.toutiao_view_pager);
        pagerAdapter = new TouTiaoNewsPagerAdapter(getChildFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(15);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
