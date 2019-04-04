package com.component.fx.plugin_test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class TestWindowService extends Service {


    private static final String TAG = "TestWindowService";
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private View view;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
        initWindow();
        addWindowView2Window();
        initClick();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void initWindow() {
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams();


        // 更多type：https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#TYPE_PHONE
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }


        params.format = PixelFormat.TRANSPARENT;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                |WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                        |WindowManager.LayoutParams.FLAG_FULLSCREEN
        ;

        // 设置通知栏的长和宽
//        params.width = windowManager.getDefaultDisplay().getWidth();
//        params.height = 200;

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = (int) (200 * getResources().getDisplayMetrics().density + 0.5);
        params.gravity = Gravity.LEFT | Gravity.TOP;

    }

    private void createFloatView() {
        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.test_custom_remote_view_dj, null);
        int height = view.getHeight();
    }

    private void addWindowView2Window() {
        windowManager.addView(view, params);
    }

    private void initClick() {
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        Log.d(TAG, "getRawX: " + startX + "  getX() =  " + event.getX());
                        Log.d(TAG, "getRawY: " + startY + "  getY() =  " + event.getY());
                        //getRawX是触摸位置相对于屏幕的坐标，getX是相对于父控件的坐标
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch: move");
                        endX = (int) event.getRawX();
                        endY = (int) event.getRawY();

                        params.x = (int) (event.getRawX() - view.getMeasuredWidth() / 2);
                        params.y = (int) (event.getRawY() - view.getMeasuredHeight() / 2);

                        windowManager.updateViewLayout(view, params);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (needIntercept()) {
                            return true;
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
            }
        });
    }

    private boolean needIntercept() {
        if (Math.abs(startX - endX) > 30 || Math.abs(startY - endY) > 30) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null) {
            windowManager.removeView(view);
        }
        view = null;
    }
}
