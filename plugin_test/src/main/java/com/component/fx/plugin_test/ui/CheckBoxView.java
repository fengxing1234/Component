package com.component.fx.plugin_test.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.component.fx.plugin_base.utils.BitmapUtils;
import com.component.fx.plugin_test.R;


public class CheckBoxView extends View {

    private static final String TAG = "CheckBoxView";
    private Bitmap bitmap;
    private Rect src = new Rect();
    private RectF dst = new RectF();
    private static final int mTotalPage = 13;
    private int mCurrentPage = -1;
    private int mWidth;
    private int mHeight;
    private long duration = 500;

    private CheckStatus mStatus;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mCurrentPage < mTotalPage - 1 && mCurrentPage >= 0) {
                Log.d(TAG, "handleMessage: " + mCurrentPage);
                invalidate();
                if (mStatus == CheckStatus.CHECK) {
                    mCurrentPage++;
                } else {
                    mCurrentPage--;
                }

                this.sendEmptyMessageDelayed(0, duration / mTotalPage);
            }
        }
    };
    private Paint mPaint;

    public CheckBoxView(Context context) {
        super(context);
    }

    public CheckBoxView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapUtils.getBitmapForResource(context, R.drawable.checkmark);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth * 1.0f / 2, mHeight * 1.0f / 2);

        float r = Math.min(mWidth * 1.0f / 2, mHeight * 1.0f / 2);
        canvas.drawCircle(0, 0, r, mPaint);

        int childWidth = bitmap.getWidth() / mTotalPage;
        int childHeight = bitmap.getHeight();
        src.set(childWidth * mCurrentPage, 0, childWidth * (mCurrentPage + 1), childHeight);
        Log.d(TAG, "onDraw: " + src);
        r = r * 0.8f;
        dst.set(-r, -r, r, r);
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public void check() {
        if (mStatus == CheckStatus.CHECK) {
            Log.d(TAG, "status = " + mStatus);
            return;
        }
        mStatus = CheckStatus.CHECK;
        mCurrentPage = 0;
        mHandler.sendEmptyMessageDelayed(0, 0);
    }

    public void unCheck() {
        if (mStatus == CheckStatus.UNCHECK) {
            Log.d(TAG, "status  = " + mStatus);
            return;
        }
        mStatus = CheckStatus.UNCHECK;
        mCurrentPage = mTotalPage-2;
        Log.d(TAG, "unCheck: " + mCurrentPage);
        mHandler.sendEmptyMessageDelayed(0, 0);
    }

    public enum CheckStatus {
        CHECK,
        UNCHECK,
    }
}
