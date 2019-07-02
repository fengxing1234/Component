package com.component.fx.plugin_test.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.component.fx.plugin_base.utils.UiSizeUtils;
import com.component.fx.plugin_test.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LeafLoadingView extends View {

    private static final int LEFT_MARGIN = 9;

    private static final int RIGHT_MARGIN = 25;
    //总进度
    private static final int TOTAL_PROGRESS = 100;
    private static final String TAG = LeafLoadingView.class.getSimpleName();
    // 叶子飘动一个周期所花的时间
    private static final long LEAF_FLOAT_TIME = 3000;
    // 叶子旋转一周需要的时间
    private static final long LEAF_ROTATE_TIME = 2000;
    // 中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 13;
    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 5;

    // 淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    // 橙色
    private static final int ORANGE_COLOR = 0xffffa800;


    private int mProgress;
    private int mWidth;
    private int mHeight;
    //进度条的宽度
    private int mProgressWidth;
    //左上下 边距
    private int mLeftMargins;
    //右边距
    private int mRightMargins;
    //圆弧的半径
    private int mArcRadius;
    private Paint mWhitePaint;
    //左边扇形区域
    private RectF mArcRectF = new RectF();
    //白色矩形区域
    private RectF mWhiteRectF = new RectF();
    private int mCurrentProgressPosition;
    private int mArcRightLocation;
    private Paint mOrangePaint;
    private RectF mOrangeRectF;
    // 用于控制随机增加的时间不抱团
    private int mAddTime;
    // 中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    // 振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;
    private LeafFactory mLeafFactory;
    private List<Leaf> mLeafInfos;
    private int mLeafWidth, mLeafHeight;
    private Bitmap mLeafBitmap;
    private Bitmap mOuterBitmap;
    private int mOuterWidth;
    private int mOuterHeight;
    private Paint mBitmapPaint;
    private long mLeafFloatTime;
    private long mLeafRotateTime;


    public LeafLoadingView(Context context) {
        this(context, null);
    }

    public LeafLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeafLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLeftMargins = UiSizeUtils.dip2Px(getContext(), LEFT_MARGIN);
        mRightMargins = UiSizeUtils.dip2Px(getContext(), RIGHT_MARGIN);


        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);

        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setColor(WHITE_COLOR);

        mOrangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mOrangePaint.setColor(getResources().getColor(R.color.orange));
        mOrangePaint.setColor(ORANGE_COLOR);

        mLeafFactory = new LeafFactory();
        mLeafInfos = mLeafFactory.generateLeafs();
        //初始化Bitmap
        mLeafBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.leaf)).getBitmap();
        mLeafWidth = mLeafBitmap.getWidth();
        mLeafHeight = mLeafBitmap.getHeight();

        mOuterBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.leaf_kuang)).getBitmap();
        mOuterWidth = mOuterBitmap.getWidth();
        mOuterHeight = mOuterBitmap.getHeight();

    }

    /**
     * 设置中等振幅
     *
     * @param amplitude
     */
    public void setMiddleAmplitude(int amplitude) {
        this.mMiddleAmplitude = amplitude;
    }

    /**
     * 设置振幅差
     *
     * @param disparity
     */
    public void setMplitudeDisparity(int disparity) {
        this.mAmplitudeDisparity = disparity;
    }

    /**
     * 获取中等振幅
     */
    public int getMiddleAmplitude() {
        return mMiddleAmplitude;
    }

    /**
     * 获取振幅差
     */
    public int getMplitudeDisparity() {
        return mAmplitudeDisparity;
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    /**
     * 设置叶子飘完一个周期所花的时间
     *
     * @param time
     */
    public void setLeafFloatTime(long time) {
        this.mLeafFloatTime = time;
    }

    /**
     * 设置叶子旋转一周所花的时间
     *
     * @param time
     */
    public void setLeafRotateTime(long time) {
        this.mLeafRotateTime = time;
    }

    /**
     * 获取叶子飘完一个周期所花的时间
     */
    public long getLeafFloatTime() {
        mLeafFloatTime = mLeafFloatTime == 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        return mLeafFloatTime;
    }

    /**
     * 获取叶子旋转一周所花的时间
     */
    public long getLeafRotateTime() {
        mLeafRotateTime = mLeafRotateTime == 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;
        return mLeafRotateTime;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        Log.d(TAG, "mWidth : " + mWidth);
        Log.d(TAG, "mHeight: " + mHeight);

        mArcRadius = (mHeight - 2 * mLeftMargins) / 2;
        Log.d(TAG, "onSizeChanged: mArcRadius  = " + mArcRadius);

        mProgressWidth = mWidth - mLeftMargins - mRightMargins;

        Log.d(TAG, "mProgressWidth : " + mProgressWidth);
        Log.d(TAG, "mLeftMargins: " + mLeftMargins + "  mRightMargins : " + mRightMargins);

        mArcRectF.set(mLeftMargins, mLeftMargins, mLeftMargins + 2 * mArcRadius, mHeight - mLeftMargins);
        mOrangeRectF = new RectF(mLeftMargins + mArcRadius, mLeftMargins,
                mCurrentProgressPosition
                , mHeight - mLeftMargins);
        mWhiteRectF.set(mLeftMargins + mCurrentProgressPosition, mLeftMargins, mWidth - mRightMargins - mLeftMargins, mHeight - mLeftMargins);
        Log.d(TAG, "mWhiteRectF: " + mWhiteRectF);
        mArcRightLocation = mLeftMargins + mArcRadius;
        Log.d(TAG, "mArcRightLocation : " + mArcRightLocation);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "mArcRadius: " + mArcRadius);
        Log.d(TAG, "mProgress: " + mProgress);
        if (mProgress >= TOTAL_PROGRESS) {
            mProgress = 0;
        }

        //首先根据进度条的宽度和当前进度、总进度算出当前的位置：
        mCurrentProgressPosition = mProgressWidth * mProgress / TOTAL_PROGRESS;
        Log.d(TAG, "mCurrentProgressPosition: " + mCurrentProgressPosition);
        // 即当前位置在图中所示1范围内
        if (mCurrentProgressPosition < mArcRadius) {
            // 1.绘制白色ARC，绘制orange ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mWhitePaint);
            // 2.绘制白色矩形
            mWhiteRectF.left = mArcRightLocation;
            canvas.drawRect(mWhiteRectF, mWhitePaint);

            drawLeafs(canvas);

            // 3.绘制棕色 ARC

            float angle = (float) Math.toDegrees(Math.acos((mArcRadius - mCurrentProgressPosition) * 1.0 / mArcRadius));
            Log.d(TAG, "angle: " + angle);
            float startAngle = 180 - angle;
            float endAngle = 2 * angle;
            Log.d(TAG, "startAngle : " + startAngle + " endAngle : " + endAngle);
            canvas.drawArc(mArcRectF, startAngle, endAngle, false, mOrangePaint);
        } else {
            // 1.绘制white RECT
            mWhiteRectF.left = mCurrentProgressPosition;
            Log.d(TAG, "onDraw: " + mWhiteRectF);
            canvas.drawRect(mWhiteRectF, mWhitePaint);

            drawLeafs(canvas);

            // 2.绘制Orange ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mOrangePaint);

            // 3.绘制orange RECT
            mOrangeRectF.left = mArcRightLocation;
            mOrangeRectF.right = mCurrentProgressPosition;
            canvas.drawRect(mOrangeRectF, mOrangePaint);

        }
    }

    private void drawLeafs(Canvas canvas) {
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < mLeafInfos.size(); i++) {
            Leaf leaf = mLeafInfos.get(i);
            if (currentTime > leaf.startTime && leaf.startTime != 0) {
                // 绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
                getLeafLocation(leaf, currentTime);
                // 根据时间计算旋转角度
                canvas.save();
                // 通过Matrix控制叶子旋转
                Matrix matrix = new Matrix();
                float transX = mLeftMargins + leaf.x;
                float transY = mLeftMargins + leaf.y;
                Log.i(TAG, "left.x = " + leaf.x + "--leaf.y=" + leaf.y);
                matrix.postTranslate(transX, transY);

                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                float rotateFraction = ((currentTime - leaf.startTime) % LEAF_ROTATE_TIME) / (float) LEAF_ROTATE_TIME;
                int angle = (int) (rotateFraction * 360);

                // 根据叶子旋转方向确定叶子旋转角度
                int rotate = leaf.rotateDirection == 0 ? angle + leaf.rotateAngle : -angle + leaf.rotateAngle;

                matrix.postRotate(rotate, transX + mLeafWidth * 1.0f / 2, mLeafHeight * 1.0f / 2);

                canvas.drawBitmap(mLeafBitmap, matrix, mBitmapPaint);
                canvas.restore();
            } else {
                continue;
            }

        }
    }

    private void getLeafLocation(Leaf leaf, long currentTime) {
        long intervalTime = currentTime - leaf.startTime;
        mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        if (intervalTime < 0) {
            return;
        } else if (intervalTime > mLeafFloatTime) {
            leaf.startTime = System.currentTimeMillis()
                    + new Random().nextInt((int) mLeafFloatTime);
        }

        float fraction = (float) intervalTime / mLeafFloatTime;
        leaf.x = (int) (mProgressWidth - mProgressWidth * fraction);
        leaf.y = getLocationY(leaf);

    }

    //通过叶子信息获取当前叶子的Y值
    private int getLocationY(Leaf leaf) {
        // y = A(wx+Q)+h
        float w = (float) ((2 * Math.PI) / mProgressWidth);
        float a = mMiddleAmplitude;
        switch (leaf.type) {
            case LITTLE:
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
        }
        Log.d(TAG, "getLocationY: a = " + a);
        Log.d(TAG, "getLocationY: leaf.x = " + leaf.x);
        Log.d(TAG, "getLocationY: leaf.y = " + leaf.y);
        Log.d(TAG, "getLocationY: w = " + w);
        Log.d(TAG, "getLocationY: Math.sin(w * leaf.x) = " + Math.sin(w * leaf.x));
        return (int) (a * Math.sin(w * leaf.x)) + mArcRadius * 2 / 3;
    }

    private class LeafFactory {

        private static final int MAX_LEAFS = 8;

        Random random = new Random();

        public Leaf generateLeaf() {
            Leaf leaf = new Leaf();
            int randomType = random.nextInt(3);
            StartType type = StartType.MIDDLE;
            switch (randomType) {
                case 0:
                    break;
                case 1:
                    type = StartType.LITTLE;
                    break;
                case 2:
                    type = StartType.BIG;
                    break;
                default:
                    break;
            }
            leaf.type = type;
            leaf.rotateAngle = random.nextInt(360);
            leaf.rotateDirection = random.nextInt(2);
            // 为了产生交错的感觉，让开始的时间有一定的随机性
            mAddTime += random.nextInt((int) (LEAF_FLOAT_TIME * 1.5));
            leaf.startTime = System.currentTimeMillis() + mAddTime;
            return leaf;
        }

        public List<Leaf> generateLeafs(int leafSize) {
            LinkedList<Leaf> list = new LinkedList<>();
            for (int i = 0; i < leafSize; i++) {
                list.add(generateLeaf());
            }
            return list;
        }

        public List<Leaf> generateLeafs() {
            return generateLeafs(MAX_LEAFS);
        }

    }


    /**
     * 区分不同的振幅
     */
    private enum StartType {
        LITTLE, MIDDLE, BIG
    }

    /**
     * 叶子对象
     */
    private class Leaf {
        // 在绘制部分的位置
        float x, y;
        // 控制叶子飘动的幅度
        StartType type;
        // 旋转角度
        int rotateAngle;
        // 旋转方向--0代表顺时针，1代表逆时针
        int rotateDirection;
        // 起始时间(ms)
        long startTime;
    }
}
