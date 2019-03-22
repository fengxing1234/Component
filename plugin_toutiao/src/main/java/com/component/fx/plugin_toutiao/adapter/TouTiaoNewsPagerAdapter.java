package com.component.fx.plugin_toutiao.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.component.fx.plugin_toutiao.fragment.TouTiaoNewsTabFragment;

public class TouTiaoNewsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;

    public TouTiaoNewsPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        return TouTiaoNewsTabFragment.getInstance(titles[i]);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
