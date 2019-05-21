package com.component.fx.plugin_test.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class SquareProgress extends View {

    private static final String TAG = "SquareProgress";

    /**
     * 宽
     */
    private int mWidth;
    /**
     * 高
     */
    private int mHeight;
    /**
     * 高
     */
    private Paint mPaint;
    /**
     * 进度（最大100）
     */
    private float mProgress = 0;


    public SquareProgress(Context context) {
        this(context, null);
    }

    public SquareProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(70);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else {
            mWidth = defaultLength();
        }
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else {
            mHeight = defaultLength();
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    private int defaultLength() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56f, getResources().getDisplayMetrics());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mProgress < 0 || mProgress > 100) {
            return;
        }

        // 偏移角度
        double x = mProgress / 100 * 2 * Math.PI;

        double ratio = mWidth * 1D / mHeight;

        Path path = new Path();

        path.moveTo(mWidth / 2, mHeight / 2);

        if (mProgress <= 25) {
            Log.e(TAG, "第一象限");
            if (Math.tan(x) <= ratio) {
                Log.e(TAG, "上横边");
                path.lineTo(mWidth / 2 + (float) Math.tan(x) * mHeight / 2, 0);
                path.lineTo(mWidth, 0);
                path.lineTo(mWidth, mHeight);
                path.lineTo(0, mHeight);
                path.lineTo(0, 0);
                path.lineTo(mWidth / 2, 0);
            } else {
                Log.e(TAG, "上竖边");
                path.lineTo(mWidth, mHeight / 2 - (float) Math.tan(Math.PI / 2 - x) * mWidth / 2);
                path.lineTo(mWidth, mHeight);
                path.lineTo(0, mHeight);
                path.lineTo(0, 0);
                path.lineTo(mWidth / 2, 0);
            }
        } else if (mProgress <= 50) {
            Log.e(TAG, "第二象限");
            if (Math.tan(x - Math.PI / 2) <= 1 / ratio) {
                Log.e(TAG, "下竖边");
                path.lineTo(mWidth, mHeight / 2 + (float) Math.tan(x - Math.PI / 2) * mWidth / 2);
                path.lineTo(mWidth, mHeight);
                path.lineTo(0, mHeight);
                path.lineTo(0, 0);
                path.lineTo(mWidth / 2, 0);
            } else {
                Log.e(TAG, "下横边");
                path.lineTo(mWidth / 2 + (float) Math.tan(Math.PI - x) * mHeight / 2, mHeight);
                path.lineTo(0, mHeight);
                path.lineTo(0, 0);
                path.lineTo(mWidth / 2, 0);
            }
        } else if (mProgress <= 75) {
            Log.e(TAG, "第三象限");
            if (Math.tan(x - Math.PI) <= ratio) {
                Log.e(TAG, "下横边");
                path.lineTo(mWidth / 2 - (float) Math.tan(x - Math.PI) * mHeight / 2, mHeight);
                path.lineTo(0, mHeight);
                path.lineTo(0, 0);
                path.lineTo(mWidth / 2, 0);
            } else {
                Log.e(TAG, "下竖边");
                path.lineTo(0, mHeight / 2 + (float) Math.tan(Math.PI * 3 / 2 - x) * mWidth / 2);
                path.lineTo(0, 0);
                path.lineTo(mWidth / 2, 0);
            }
        } else {
            Log.e(TAG, "第四象限");
            if (Math.tan(x - Math.PI * 3 / 2) <= 1 / ratio) {
                Log.e(TAG, "上竖边");
                path.lineTo(0, mHeight / 2 - (float) Math.tan(x - Math.PI * 3 / 2) * mWidth / 2);
                path.lineTo(0, 0);
                path.lineTo(mWidth / 2, 0);
            } else {
                Log.e(TAG, "上横边");
                path.lineTo(mWidth / 2 - (float) Math.tan(Math.PI * 2 - x) * mHeight / 2, 0);
                path.lineTo(mWidth / 2, 0);
            }
        }

        path.close();
        canvas.drawPath(path, mPaint);
    }

    /**
     * 设置进度 最大值100
     */
    public void setProgress(float progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    private int defaultMargin() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
    }

}
