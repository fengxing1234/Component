package com.component.fx.image_viewer.ui;

import android.view.View;
import android.view.ViewGroup;

public abstract class IndexUI {

    private View indexView;

    public abstract void handleItemChanged(int position, int length);

    public void setup(ViewGroup parent, int startPosition, int length) {

    }

    /**
     * 显示索引
     */
    public void show() {
        if (indexView != null) {
            indexView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏索引
     */
    public void hide() {
        if (indexView != null) {
            indexView.setVisibility(View.GONE);
        }
    }
}
