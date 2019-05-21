package com.component.fx.plugin_test.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class TestPathView extends View {


    private static final String TAG = TestPathView.class.getSimpleName();
    private Path path;
    private int mWidth;
    private int mHeight;
    private int mCenterY;
    private int mOffset;

    private Paint mPaint;

    private Path mPath;
    //一个波纹长度
    private int mWL = 1000;
    //波纹个数
    private int mWaveCount;
    //平移偏移量
    private int offset;


    //屏幕高度
    private int mScreenHeight;
    //屏幕宽度
    private int mScreenWidth;
    public TestPathView(Context context) {
        this(context, null);
    }

    public TestPathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
        initAnimator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: width = " + mWidth);
        Log.d(TAG, "onMeasure: height = " + mHeight);
        setMeasuredDimension(mWidth, mHeight);
    }

    private void initPaint() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#59c3e2"));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void initAnimator() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mWL);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
        mScreenWidth = w;
        mWaveCount = (int) Math.round(mScreenWidth / mWL + 1.5);
        mCenterY = mScreenHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(-mWL + offset, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            mPath.quadTo((-mWL * 3 / 4) + (i * mWL) + offset, mCenterY + 60, (-mWL / 2) + (i * mWL) + offset, mCenterY);
            mPath.quadTo((-mWL / 4) + (i * mWL) + offset, mCenterY - 60, i * mWL + offset, mCenterY);
        }
        mPath.lineTo(mScreenWidth, mScreenHeight);
        mPath.lineTo(0, mScreenHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    private void wavePath(Canvas canvas, int fudu) {
        //一段波纹的长度
        mWL = mWidth;
        mCenterY = mHeight / 2;

        Path path = new Path();
        mWaveCount = (int) Math.round(mWidth / mWL + 1.5); //这样就保证波纹能覆盖整个屏幕
        path.moveTo(-mWL + mOffset, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            path.quadTo((-mWL * 3 / 4) + (i * mWL) + mOffset, mCenterY + 60, (-mWL / 2) + (i * mWL) + mOffset, mCenterY);
            path.quadTo((-mWL / 4) + (i * mWL) + mOffset, mCenterY - 60, i * mWL + mOffset, mCenterY);
        }
//        path.quadTo(-mWL * 3 / 4, mCenterY + fudu, -mWL / 2, mCenterY);
//        path.quadTo(-mWL / 4, mCenterY - fudu, 0, mCenterY);
//
//        path.quadTo(mWL / 4, mCenterY + fudu, mWL / 2, mCenterY);
//        path.quadTo(mWL * 3 / 4, mCenterY - fudu, mWL, mCenterY);

        canvas.drawPath(path, mPaint);
    }

    private void defaultAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mWL);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //mOffset 的值的范围在[0,mWL]之间。
                mOffset = (Integer) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    private void addPath(Canvas canvas) {
        Path path = new Path();
        Path src = new Path();
        path.addRect(0, 0, 400, 400, Path.Direction.CW); //宽高为400的矩形
        src.addCircle(200, 200, 100, Path.Direction.CW); //圆心为(200,200)半径为100的正圆
        path.addPath(src);
        canvas.drawPath(path, mPaint);
    }
}
