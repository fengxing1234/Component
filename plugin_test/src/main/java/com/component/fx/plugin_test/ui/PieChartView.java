package com.component.fx.plugin_test.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.component.fx.plugin_base.utils.ColorTools;

import java.util.List;

/**
 * 饼状图
 */
public class PieChartView extends View {

    private static final String TAG = "PieChartView";
    private Context mContext;
    private Paint paint;
    private int mWidth;
    private int mHeight;
    private RectF rectF;
    private List<PieCharData> mPieDataList;
    private float mStartAngle;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        this.mContext = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
    }

    public void setStartAngle(int startAngle) {
        this.mStartAngle = startAngle;
        invalidate();
    }

    public void setPieData(List<PieCharData> pieDataList) {
        this.mPieDataList = pieDataList;
        handlerPieData(mPieDataList);
        invalidate();
    }

    private void handlerPieData(List<PieCharData> list) {
        if (list == null || list.size() == 0) {
            Log.e(TAG, "handlerPieData: 数据源不能是null" + list);
            return;
        }

        int sumValue = 0;
        for (int i = 0; i < list.size(); i++) {
            PieCharData data = list.get(i);
            sumValue += data.getValue();  //计算数值和
        }
        Log.d(TAG, "sumValue: " + sumValue);
        for (int i = 0; i < list.size(); i++) {
            PieCharData data = list.get(i);
            data.percentage = data.getValue() / sumValue;
            Log.d(TAG, "percentage: " + data.percentage);
            data.angle = 360 * data.percentage;
            Log.d(TAG, "angle: " + data.angle);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        Log.d(TAG, "mWidth: " + mWidth + "  mHeight : " + mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float dx = mWidth * 1.0f / 2;
        float dy = mHeight * 1.0f / 2;
        canvas.translate(dx, dy);//设置原点位置

        float radius = Math.min(mWidth, mHeight) * 1.0f / 2 * 0.8f; //计算半径

        rectF.set(-radius, -radius, radius, radius); //弧度的范围

//        paint.setColor(Color.parseColor("#" + mPieDataList.get(0).color));
//        canvas.drawArc(rectF, 0, 90, true, paint);
        for (int i = 0; i < mPieDataList.size(); i++) {
            PieCharData pieCharData = mPieDataList.get(i);
            int color = Color.parseColor(pieCharData.color);
            paint.setColor(color);
            float angle = pieCharData.angle;
            canvas.drawArc(rectF, mStartAngle, angle, true, paint);
            mStartAngle += angle;
        }

    }


    public static class PieCharData {

        //用户关心的数据

        private String name;        // 名字
        private float value;        // 数值
        public float percentage;   // 百分比

        // 非用户关心数据
        public String color;      // 颜色
        private float angle = 0;    // 角度

        public PieCharData(String name, float value) {
            this.name = name;
            this.value = value;
            initDefColor();
        }

        private void initDefColor() {
            color = ColorTools.getRandColorCode();//设置随机颜色
        }

        public String getName() {
            return name;
        }

        public float getValue() {
            return value;
        }
    }

}
