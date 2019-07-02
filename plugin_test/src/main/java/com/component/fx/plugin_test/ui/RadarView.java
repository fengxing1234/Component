package com.component.fx.plugin_test.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.component.fx.plugin_base.utils.UiSizeUtils;

public class RadarView extends View {

    private static final String TAG = RadarView.class.getSimpleName();

    private static final int DEF_COUNT = 6; //单位个数
    public static final int DEF_MARGIN = 4; //单位 sp
    private static final int DEF_CIRCLE_POINT = 2;//单位dp

    private int mTextMarginLeft;
    private int mSmallCirclePoint;
    //数据个数
    private int mCount = DEF_COUNT;

    //
    private double angle = (Math.PI * 2 / mCount);
    //网格最大半径
    private float mRadius;
    //中心X
    private int mCenterX;
    //中心Y
    private int mCenterY;

    private String[] mTitles = {"物理", "魔法", "物防", "法防", "移动", "控制"};

    //各维度分值
    private double[] mData = {100, 60, 60, 60, 100, 50, 10, 20};

    //数据最大值
    private float maxValue = 100;

    //雷达区画笔
    private Paint mRadarPaint;

    //数据区画笔
    private Paint mValuePaint;

    //小圆点 画笔
    private Paint mCirclePaint;

    //文本画笔
    private Paint mTextPaint;
    private float mTitleMaxLength;


    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.d(TAG, "init: ");
        mRadarPaint = new Paint();
        mRadarPaint.setColor(Color.BLACK);
        mRadarPaint.setStyle(Paint.Style.STROKE);
        mRadarPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mTextPaint = new Paint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(UiSizeUtils.sp2px(getContext(), 14));

        mValuePaint = new Paint();
        mValuePaint.setStyle(Paint.Style.STROKE);
        mValuePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setColor(Color.BLUE);

        mCirclePaint = new Paint();
        mCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.BLUE);

        mTextMarginLeft = UiSizeUtils.dp2px(getContext(), DEF_MARGIN);
        mSmallCirclePoint = UiSizeUtils.dip2Px(getContext(), DEF_CIRCLE_POINT);


        for (int i = 0; i < mTitles.length; i++) {
            String title = mTitles[i];
            float textLength = mTextPaint.measureText(title);
            if (textLength > mTitleMaxLength) {
                mTitleMaxLength = textLength;
            }
        }
    }


    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    public String[] getTitles() {
        return mTitles;
    }

    public void setTitles(String[] mTitles) {
        this.mTitles = mTitles;
    }

    public double[] getData() {
        return mData;
    }

    public void setData(double[] mData) {
        this.mData = mData;
        postInvalidate();
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public Paint getRadarPaint() {
        return mRadarPaint;
    }

    public void setRadarPaint(Paint mRadarPaint) {
        this.mRadarPaint = mRadarPaint;
    }

    public Paint getValuePaint() {
        return mValuePaint;
    }

    public void setValuePaint(Paint mValuePaint) {
        this.mValuePaint = mValuePaint;
    }

    public Paint getCirclePaint() {
        return mCirclePaint;
    }

    public void setCirclePaint(Paint mCirclePaint) {
        this.mCirclePaint = mCirclePaint;
    }

    public Paint getTextPaint() {
        return mTextPaint;
    }

    public void setTextPaint(Paint mTextPaint) {
        this.mTextPaint = mTextPaint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRadius = (Math.min(w, h) * 1.0f / 2 * 0.9f) - mTitleMaxLength;
        mCenterX = w / 2;
        mCenterY = h / 2;
        Log.d(TAG, "onSizeChanged : " + "  mRadius = " + mRadius);
        Log.d(TAG, "onSizeChanged : " + "  mCenterX = " + mCenterX + "  mCenterY = " + mCenterY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);
        drawLine(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    /**
     * 绘制正多边形
     * <p>
     * 已知圆心，半径，角度，求圆上的点坐标。
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float distance = mRadius / (mCount - 1);
        //中心点不用绘制
        for (int i = 1; i < mCount; i++) {
            //当前半径
            float currentR = distance * i;
            Log.d(TAG, "i =  " + i + " distance = " + distance);
            for (int j = 0; j < mCount; j++) {
                Log.d(TAG, " j  = " + j);
                if (j == 0) {
                    path.moveTo(mCenterX + currentR, mCenterY);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x = (float) (mCenterX + currentR * Math.cos(angle * j));
                    float y = (float) (mCenterY + currentR * Math.sin(angle * j));
                    Log.d(TAG, "angle: " + angle + " X = " + x + "  Y = " + y);
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mRadarPaint);
        }
    }

    /**
     * 绘制从中心到末端的直线
     * <p>
     * 已知圆心，半径，角度，求圆上的点坐标。
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < mCount; i++) {
            path.moveTo(mCenterX, mCenterY);
            float x = mCenterX + mRadius * (float) Math.cos(angle * i);
            float y = mCenterY + mRadius * (float) Math.sin(angle * i);
            path.lineTo(x, y);
            canvas.drawPath(path, mRadarPaint);
        }
    }

    /**
     * 画文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < mCount; i++) {
            float x = mCenterX + (mRadius) * (float) (Math.cos(angle * i));
            float y = mCenterY + (mRadius + textHeight / 2) * (float) (Math.sin(angle * i));
            float textWidth = mTextPaint.measureText(mTitles[i]);
            if (angle * i > 0 && angle * i < Math.PI / 2) {//第四象限
                Log.d(TAG, "第四象限 : " + mTitles[i]);
                canvas.drawText(mTitles[i], x - textWidth / 2, y + textHeight / 2, mTextPaint);
            } else if (angle * i >= Math.PI / 2 && angle * i < Math.PI) {//第三象限
                Log.d(TAG, "第3象限 : " + mTitles[i]);
                canvas.drawText(mTitles[i], x - textWidth / 2, y + textHeight / 2, mTextPaint);
            } else if (angle * i > Math.PI && angle * i < Math.PI + Math.PI / 2) {//第二象限
                Log.d(TAG, "第2象限 : " + mTitles[i] + "  弧度 = " + angle * i + "  PI = " + Math.PI);
                canvas.drawText(mTitles[i], x - textWidth / 2, y, mTextPaint);
            } else if (angle * i >= 3 * Math.PI / 2 && angle * i < 2 * Math.PI) {//第一象限
                Log.d(TAG, "第1象限 : " + mTitles[i]);
                canvas.drawText(mTitles[i], x - textWidth / 2, y, mTextPaint);
            } else {//i = 0 时 也是 起始点 不需要偏移
                Log.d(TAG, "其它 : " + mTitles[i]);
                if (i == 0) {
                    canvas.drawText(mTitles[i], x + mTextMarginLeft, y + textHeight / 4, mTextPaint);
                } else {
                    canvas.drawText(mTitles[i], x - textWidth - mTextMarginLeft, y + textHeight / 4, mTextPaint);
                }
            }
        }

    }

    /**
     * 绘制区域
     *
     * @param canvas
     */
    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < mCount; i++) {
            double percent = mData[i] / maxValue;

            float x = mCenterX + (float) ((mRadius) * Math.cos(angle * i) * percent);
            float y = mCenterY + (float) ((mRadius) * Math.sin(angle * i) * percent);

            if (i == 0) {
                path.moveTo(x, mCenterY);
            } else {
                path.lineTo(x, y);
            }

            canvas.drawCircle(x, y, mSmallCirclePoint, mCirclePaint);
        }
        mValuePaint.setStyle(Paint.Style.STROKE);
        path.close();
        canvas.drawPath(path, mValuePaint);
        mValuePaint.setAlpha(127);
        //绘制填充区域
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, mValuePaint);

    }


}
