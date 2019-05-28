package com.component.fx.image_viewer.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 预览器 ViewPager
 */
public class ImageViewPager extends ViewPager {

    // 是否可滑动
    private boolean isScrollable = true;

    public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (isScrollable) {
            super.scrollTo(x, y);
        }
    }

    /**
     * 设置是否可滑动
     *
     * @param isScrollable
     */
    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    // TODO: 2019-05-22 未写相关代码

    /**
     * 重写 onInterceptTouchEvent 和 onTouchEvent 方法是为了解决
     * PhotoView + Viewpager 双指缩放的时候出现 pointerIndex out of range 问题
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
