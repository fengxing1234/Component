package com.component.fx.image_viewer;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * 图片进退场动画处理类
 */
public class ImageTransfer {

    // 没有需要执行的动画
    public static final int ACTION_IDEL = -1;
    // 执行进场动画
    public static final int ACTION_ENTER = 0;
    // 执行退场动画
    public static final int ACTION_EXIT = 1;
    // 拖拽后，执行复原动画
    public static final int ACTION_DRAG_RESTORE = 2;
    // 在灵巧模式下，拖拽后，执行退场动画
    public static final int ACTION_DRAG_EXIT_AGILE = 3;
    // 在简单模式下，拖拽后，执行退场动画
    public static final int ACTION_DRAG_EXIT_SIMPLE = 4;


    private int interfaceHeight;
    private int interfaceWidth;

    public ImageTransfer(int interfaceWidth, int interfaceHeight) {
        this.interfaceWidth = interfaceWidth;
        this.interfaceHeight = interfaceHeight;
    }

    public ImageTransfer with(ImageView imageView) {
        return this;
    }

    public ImageTransfer loadEnterData(ViewData viewData) {
        return this;
    }

    public ImageTransfer background(Drawable background) {
        return this;
    }

    public ImageTransfer duration(long duration) {
        return this;
    }

    public ImageTransfer callback(OnTransCallback callback) {
        return this;
    }

    public ImageTransfer play() {
        return this;
    }

    public ImageTransfer loadExitData(ViewData viewData) {
        return this;
    }

    public interface OnTransCallback {
        void onStart();

        void onRunning(float progress);

        void onEnd();
    }
}
