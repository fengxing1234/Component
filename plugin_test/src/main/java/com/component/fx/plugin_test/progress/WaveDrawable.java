package com.component.fx.plugin_test.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Choreographer;
import android.view.animation.DecelerateInterpolator;

public class WaveDrawable extends Drawable implements Animatable, ValueAnimator.AnimatorUpdateListener {


    //波纹高度系数
    private static final float WAVE_HEIGHT_FACTOR = 0.2f;
    private static final float WAVE_SPEED_FACTOR = 0.02f;
    private static final int UNDEFINED_VALUE = Integer.MIN_VALUE;
    private static final String TAG = "WaveDrawable";


    //Matrix是一个矩阵，主要功能是坐标映射，数值转换。
    private Matrix mMatrix = new Matrix();
    private boolean mRunning = false;


    // 图片合成  dst 目标图片  in 交集
    private static final PorterDuffXfermode sXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    //色彩矩阵颜色过滤器
    private static ColorFilter sGrayFilter = new ColorMatrixColorFilter(new float[]{
            0.264F, 0.472F, 0.088F, 0, 0, //红
            0.264F, 0.472F, 0.088F, 0, 0, //绿
            0.264F, 0.472F, 0.088F, 0, 0, //蓝
            0, 0, 0, 1, 0                 //透明
    });

    private ColorFilter mCurFilter = null;

    /**
     * Choreographer主要作用是协调动画.
     * Choreographer这个类来控制同步处理输入(Input)、动画(Animation)、绘制(Draw)三个UI操作。
     * 它从显示子系统接收定时脉冲（例如垂直同步），
     * 然后安排渲染下一个frame的一部分工作。
     */
    private Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            invalidateSelf();
            if (mRunning) {
                Choreographer.getInstance().postFrameCallback(this);
            }
        }
    };


    private Drawable mDrawable;
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private int mWaveLength = UNDEFINED_VALUE;
    private int mWaveHeight = UNDEFINED_VALUE;
    //波纹速度
    private int mWaveStep = UNDEFINED_VALUE;
    private boolean mIndeterminate;
    private ValueAnimator mAnimator;
    private float mProgress;
    private int mWaveLevel;
    private int mWaveOffset;
    private Bitmap mMask;

    public WaveDrawable(Drawable drawable) {
        init(drawable);
    }

    public WaveDrawable(Context context, @DrawableRes int imgRes) {
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(imgRes);
        } else {
            drawable = context.getResources().getDrawable(imgRes);
        }
        init(drawable);
    }

    private void init(Drawable drawable) {
        mDrawable = drawable;

        mMatrix.reset();

        mPaint = new Paint();
        //防止边缘的锯齿
        //mPaint.setAntiAlias(true);
        //不对位图进行滤波处理
        mPaint.setFilterBitmap(false);
        mPaint.setColor(Color.BLACK);
        mPaint.setXfermode(sXfermode);

        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();

        if (mWidth > 0 && mHeight > 0) {
            mWaveLength = mWidth;
            mWaveHeight = Math.max(8, (int) (mHeight * WAVE_HEIGHT_FACTOR));
            mWaveStep = Math.max(1, (int) (mWidth * WAVE_SPEED_FACTOR));
            updateMask(mWidth, mWaveLength, mWaveHeight);
        }

        setProgress(0);
        start();

    }

    /**
     * 在动画的帧中设置波移动距离（以像素为单位）
     *
     * @param speed 以像素为单位
     */
    public void setWaveSpeed(int speed) {
        mWaveStep = Math.min(speed, mWidth / 2);
    }

    /**
     * 设置波幅（以像素为单位）
     * 波动幅度 越小越平稳 越大峰值越大 小点好看
     * <p>
     * 15以内
     *
     * @param amplitude
     */
    public void setWaveAmplitude(int amplitude) {
        int min = Math.min(amplitude, mHeight / 2);
        amplitude = Math.max(1, min);
        int height = amplitude * 2;
        if (mWaveHeight != height) {
            mWaveHeight = height;
            updateMask(mWidth, mWaveLength, mWaveHeight);
            invalidateSelf();
        }
    }


    /**
     * 设置波纹长度
     *
     * @param length
     */
    public void setWaveLength(int length) {
        length = Math.max(8, Math.min(mWidth * 2, length));

        if (length != mWaveLength) {
            mWaveLength = length;
            updateMask(mWidth, mWaveLength, mWaveHeight);
            invalidateSelf();
        }
    }

    /**
     * 设置波纹自动加载动画
     *
     * @param indeterminate
     */
    public void setIndeterminate(boolean indeterminate) {
        mIndeterminate = indeterminate;
        if (indeterminate) {
            if (mAnimator == null) {
                mAnimator = getDefaultAnimator();
            }
            mAnimator.addUpdateListener(this);
            mAnimator.start();
        } else {

            if (mAnimator != null) {
                mAnimator.removeAllUpdateListeners();
                mAnimator.cancel();
            }
            setLevel(calculateLevel());
        }
    }

    /**
     * 设置自定义加载动画
     *
     * @param animator
     */
    public void setIndeterminateAnimator(ValueAnimator animator) {
        if (mAnimator == animator) {
            return;
        }

        if (mAnimator != null) {
            mAnimator.removeUpdateListener(this);
            mAnimator.cancel();
        }

        mAnimator = animator;
        if (mAnimator != null) {
            mAnimator.addUpdateListener(this);
        }

    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mDrawable.setBounds(left, top, right, bottom);
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateBounds(bounds);
    }

    private void updateBounds(Rect bounds) {
        if (bounds.width() <= 0 || bounds.height() <= 0) {
            return;
        }

        if (mWidth < 0 || mHeight < 0) {
            mWidth = bounds.width();
            mHeight = bounds.height();
            if (mWaveHeight == UNDEFINED_VALUE) {
                mWaveHeight = Math.max(8, (int) (mHeight * WAVE_HEIGHT_FACTOR));
            }

            if (mWaveLength == UNDEFINED_VALUE) {
                mWaveLength = mWidth;
            }

            if (mWaveStep == UNDEFINED_VALUE) {
                mWaveStep = Math.max(1, (int) (mWidth * WAVE_SPEED_FACTOR));
            }

            updateMask(mWidth, mWaveLength, mWaveHeight);
        }
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    private int calculateLevel() {
        return (mHeight - mWaveLevel) * 10000 / (mHeight + mWaveHeight);
    }

    private ValueAnimator getDefaultAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(5000);
        return animator;
    }

    public void setProgress(float progress) {
        mProgress = progress;
        mWaveLevel = mHeight - (int) ((mHeight + mWaveHeight) * mProgress);
        invalidateSelf();
    }

    private void updateMask(int width, int length, int height) {
        if (width <= 0 || length <= 0 || height <= 0) {
            Log.w(TAG, "updateMask: size must > 0");
            mMask = null;
            return;
        }

        final int count = (int) Math.ceil((width + length) / (float) length);

        Bitmap bm = Bitmap.createBitmap(length * count, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        int amplitude = height / 2;
        Path path = new Path();
        path.moveTo(0, amplitude);

        final float stepX = length / 4f;
        float x = 0;
        float y = -amplitude;
        for (int i = 0; i < count * 2; i++) {
            x += stepX;
            path.quadTo(x, y, x + stepX, amplitude);
            x += stepX;
            y = bm.getHeight() - y;
        }
        path.lineTo(bm.getWidth(), height);
        path.lineTo(0, height);
        path.close();

        c.drawPath(path, p);

        mMask = bm;
    }


    /**
     * 绘图
     * 核心方法，最终绘制出图形都靠它
     *
     * @param canvas
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        mDrawable.setColorFilter(sGrayFilter);
        mDrawable.draw(canvas);
        mDrawable.setColorFilter(mCurFilter);

        if (mProgress <= 0.001f) {
            return;
        }


        int sc = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);

        if (mWaveLevel > 0) {
            canvas.clipRect(0, mWaveLevel, mWidth, mHeight);
        }
        mDrawable.draw(canvas);

        if (mProgress >= 0.999f) {
            return;
        }

        mWaveOffset += mWaveStep;
        if (mWaveOffset > mWaveLength) {
            mWaveOffset -= mWaveLength;
        }

        if (mMask != null) {
            mMatrix.setTranslate(-mWaveOffset, mWaveLevel);
            canvas.drawBitmap(mMask, mMatrix, mPaint);
        }

        canvas.restoreToCount(sc);

    }

    @Override
    protected boolean onLevelChange(int level) {
        setProgress(level / 10000f);
        return true;
    }

    /**
     * 设置透明度
     *
     * @param alpha
     */
    @Override
    public void setAlpha(int alpha) {
        mDrawable.setAlpha(alpha);
    }

    /**
     * 设置颜色过滤器
     *
     * @param colorFilter
     */
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mCurFilter = colorFilter;
        invalidateSelf();
    }

    /**
     * 设置颜色格式
     * 核心方法，最终绘制出图形都靠它
     *
     * @return
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    /**
     * 启动动画
     */
    @Override
    public void start() {
        mRunning = true;
        Choreographer.getInstance().postFrameCallback(mFrameCallback);
    }

    /**
     * 停止动画
     */
    @Override
    public void stop() {
        mRunning = false;
        Choreographer.getInstance().removeFrameCallback(mFrameCallback);
    }

    /**
     * 判断动画是否运行
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return mRunning;
    }

    /**
     * 动画更新中
     *
     * @param animation
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (mIndeterminate) {
            setProgress(animation.getAnimatedFraction());
            if (!mRunning) {
                invalidateSelf();
            }
        }
    }

    public boolean isIndeterminate() {
        return mIndeterminate;
    }
}
