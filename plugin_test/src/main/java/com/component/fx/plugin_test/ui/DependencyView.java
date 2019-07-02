package com.component.fx.plugin_test.ui;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class DependencyView extends AppCompatTextView {

    private static final String TAG = "DependencyView";

    private float startX;
    private float startY;
    private ViewConfiguration configuration;

    public DependencyView(Context context) {
        this(context, null);
    }

    public DependencyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DependencyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configuration = ViewConfiguration.get(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                Log.d(TAG, "down : " + startX);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "move : " + event.getX());
                int offsetX = (int) (event.getX() - startX);
                int offsetY = (int) (event.getY() - startY);
                Log.d(TAG, "offsetX: " + offsetX);
                Log.d(TAG, "offsetY: " + offsetY);
                if (Math.abs(offsetX) > ViewConfiguration.getTouchSlop() || Math.abs(offsetY) > ViewConfiguration.getTouchSlop()) {
                    ViewCompat.offsetTopAndBottom(this, offsetY);
                    ViewCompat.offsetLeftAndRight(this, offsetX);
                    //layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
                startX = event.getX();
                startY = event.getY();

                break;
        }
        return true;
    }


//    private int mLastX;
//    private int mLastY;
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int x = (int) event.getRawX();
//        int y = (int) event.getRawY();
//
//        Log.d(TAG, "getRawX : " + event.getRawX() + "  getRawY : " + event.getRawY());
//        Log.d(TAG, "getX : " + event.getX() + "  getY : " + event.getY());
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastX = x;
//                mLastY = y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int offsetX = x - mLastX;
//                int offsetY = y - mLastY;
//                ViewCompat.offsetTopAndBottom(this, offsetY);
//                ViewCompat.offsetLeftAndRight(this, offsetX);
//                //重新设置初始坐标
//                mLastX = x;
//                mLastY = y;
//                break;
//        }
//        return true;
//    }
}
