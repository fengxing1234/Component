package com.component.fx.plugin_test.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.component.fx.plugin_test.R;

public class CircleProgress extends View {

    /**
     * 半径
     */
    private int mRadius;
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


    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Test_CircleProgress);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.Test_CircleProgress_test_cpRadius, defaultRadius());
        int margin = typedArray.getDimensionPixelSize(R.styleable.Test_CircleProgress_test_margin, defaultMargin());
        int color = typedArray.getColor(R.styleable.Test_CircleProgress_test_cpColor, 0x40ffffff);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(70);
        mPaint.setStyle(Paint.Style.FILL);

        mRadius = mRadius - margin;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = defaultRadius() * 2;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = defaultRadius() * 2;
        }

        setMeasuredDimension(mWidth, mHeight);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mProgress < 0 || mProgress > 100) {
            return;
        }

        RectF oval = new RectF(mWidth / 2 - mRadius, mHeight / 2 - mRadius, mWidth / 2 + mRadius, mHeight / 2 + mRadius);
        // 根据进度画圆弧 - 反向
        canvas.drawArc(oval, mProgress / 100 * 360 - 90, 360 - mProgress / 100 * 360, true, mPaint);

    }

    /**
     * 设置进度 最大值100
     */
    public void setProgress(float progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    private int defaultRadius() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28f, getResources().getDisplayMetrics());
    }

    private int defaultMargin() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4,getResources().getDisplayMetrics());
    }

}
