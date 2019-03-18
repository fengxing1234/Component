package com.component.fx.plugin_news.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.component.fx.plugin_news.NewsFragment;
import com.component.fx.plugin_news.network.NewsEnum;

import java.util.ArrayList;
import java.util.List;

public class NewsFragmentPageAdapter extends FragmentPagerAdapter {

    private List<String> mTitles = new ArrayList();


    public NewsFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        mTitles.clear();
        for (NewsEnum newsEnum : NewsEnum.values()) {
            mTitles.add(newsEnum.getName());
        }
    }

    @Override
    public Fragment getItem(int i) {
        return NewsFragment.getInstance(mTitles.get(i));
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
