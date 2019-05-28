package com.component.fx.image_viewer.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.component.fx.image_viewer.ImageDrawee;

public class ImagePagerAdapter extends PagerAdapter {

    private int mItemCount;


    public ImagePagerAdapter(int size) {
        this.mItemCount = size;
    }

    @Override
    public int getCount() {
        return mItemCount;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ImageDrawee) object).recycle();
        // 移除页面
        container.removeView((View) object);
    }
}
