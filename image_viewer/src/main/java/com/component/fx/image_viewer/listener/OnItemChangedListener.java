package com.component.fx.image_viewer.listener;


import com.component.fx.image_viewer.ImageDrawee;

/**
 * item 的切换事件
 */
public interface OnItemChangedListener {

    void onItemChanged(int position, ImageDrawee drawee);
}
