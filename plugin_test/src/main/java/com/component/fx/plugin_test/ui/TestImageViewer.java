package com.component.fx.plugin_test.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class TestImageViewer extends FrameLayout {

    private ViewPager viewPager;

    public TestImageViewer(Context context) {
        super(context);
        init();
    }

    public TestImageViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestImageViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        viewPager = new ViewPager(getContext());
        viewPager.setOffscreenPageLimit(1);
        addView(viewPager, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setData(){

    }

    public void watch(int position) {

    }

    private static final class TestViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return false;
        }
    }
}
