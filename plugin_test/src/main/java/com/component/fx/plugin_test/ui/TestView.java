package com.component.fx.plugin_test.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
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


public class TestView extends View {

    private static final String TAG = "TestView";
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private RectF rectF = new RectF();

    private Picture mPicture = new Picture();

    private static final int ANIM_NULL = 0;         //动画状态-没有
    private static final int ANIM_CHECK = 1;        //动画状态-开启
    private static final int ANIM_UNCHECK = 2;      //动画状态-结束
    private Bitmap okBitmap;
    private static Handler mHandler;           // handler
    private int animCurrentPage = -1;       // 当前页码
    private int animMaxPage = 13;           // 总页数
    private int animDuration = 500;         // 动画时长
    private int animState = ANIM_NULL;      // 动画状态
    private boolean isCheck = false;        // 是否只选中状态


    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        recording();
        okBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.checkmark);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (animCurrentPage < animMaxPage && animCurrentPage >= 0) {
                    invalidate();
                    if (animState == ANIM_NULL)
                        return;
                    if (animState == ANIM_CHECK) {

                        animCurrentPage++;
                    } else if (animState == ANIM_UNCHECK) {
                        animCurrentPage--;
                    }

                    this.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
                    Log.e("AAA", "Count=" + animCurrentPage);
                } else {
                    if (isCheck) {
                        animCurrentPage = animMaxPage - 1;
                    } else {
                        animCurrentPage = -1;
                    }
                    invalidate();
                    animState = ANIM_NULL;
                }
            }
        };

    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void recording() {
        Canvas canvas = mPicture.beginRecording(500, 500);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);

        canvas.translate(250, 250);
        // 绘制一个圆
        canvas.drawCircle(0, 0, 200, paint);
        canvas.drawCircle(0, 0, 220, paint);
        testLine(canvas);
        mPicture.endRecording();
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
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth * 1.0f / 2, mHeight * 1.0f / 2);

        canvas.drawCircle(0, 0, 2, mPaint);

        //testLine(canvas);

        //testSkew(canvas);

        //testPicture(canvas);

        //testBitmap(canvas);

        //testBitmap2(canvas);

        testDrawText(canvas);
    }

    private void testDrawText(Canvas canvas) {
        String text = "ABCDEFGH";
        //canvas.drawText(text, 300, 300, mPaint);
        canvas.drawText(text, 1, 3, -300, -300, mPaint);
    }

    private void testBitmap2(Canvas canvas) {
        // 绘制背景圆形
        canvas.drawCircle(0, 0, 240, mPaint);

        // 得出图像边长
        int sideLength = okBitmap.getHeight();
        Log.d(TAG, "sideLength: " + sideLength);

        Log.d(TAG, "okBitmap.getWidth(): " + okBitmap.getWidth());
        // 得到图像选区 和 实际绘制位置
        Rect src = new Rect(sideLength * animCurrentPage, 0, sideLength * (animCurrentPage + 1), sideLength);
        Log.d(TAG, "src: " + src);
        Rect dst = new Rect(-200, -200, 200, 200);

        // 绘制
        canvas.drawBitmap(okBitmap, src, dst, null);
    }

    private void testBitmap(Canvas canvas) {
        Bitmap bitmap = BitmapUtils.getBitmapForResource(getContext(), R.drawable.test_bitmap2);
        canvas.translate(-mWidth * 1.0f / 2, -mHeight * 1.0f / 2); //回到0，0点
        //canvas.drawBitmap(bitmap, new Matrix(), new Paint());
        //canvas.drawBitmap(bitmap, 300, 400, new Paint());

        //从原图片中截取一个矩形
        Rect src = new Rect();
        src.set(300, 300, 900, 900);

        //把截取的矩形图片 放大或者缩小 展示在定义的矩形中
        RectF des = new RectF();
        des.set(0, 0, 200, 200);

        canvas.drawBitmap(bitmap, src, des, new Paint());
    }

    private void testPicture(Canvas canvas) {
        canvas.drawPicture(mPicture);
    }

    /**
     * 错切练习
     *
     * @param canvas
     */
    private void testSkew(Canvas canvas) {
        rectF.set(0, 0, 200, 200);

        canvas.drawRect(rectF, mPaint);

        canvas.skew(0.1f, 0.6f);

        mPaint.setColor(Color.YELLOW);

        canvas.drawRect(rectF, mPaint);
    }

    private void testLine(Canvas canvas) {
        for (int i = 0; i <= 360; i += 10) {               // 绘制圆形之间的连接线
            canvas.drawLine(0, 200, 0, 220, mPaint);
            canvas.rotate(10);
        }
    }

    /**
     * 选择
     */
    public void check() {
        if (animState != ANIM_NULL || isCheck)
            return;
        animState = ANIM_CHECK;
        animCurrentPage = 0;
        mHandler.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
        isCheck = true;
    }

    /**
     * 取消选择
     */
    public void unCheck() {
        if (animState != ANIM_NULL || (!isCheck))
            return;
        animState = ANIM_UNCHECK;
        animCurrentPage = animMaxPage - 1;
        mHandler.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
        isCheck = false;
    }

    /**
     * 设置动画时长
     *
     * @param animDuration
     */
    public void setAnimDuration(int animDuration) {
        if (animDuration <= 0)
            return;
        this.animDuration = animDuration;
    }

    /**
     * 设置背景圆形颜色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        mPaint.setColor(color);
    }
}
