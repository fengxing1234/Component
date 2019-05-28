package com.component.fx.image_viewer.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.component.fx.image_viewer.R;

//https://github.com/Todd-Davies/ProgressWheel
public class ProgressWheel extends View {



    // Sizes (with defaults)
    private int layoutHeight = 0;
    private int layoutWidth = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 20;
    private int rimWidth = 20;
    private int textSize = 20;
    private float contourSize = 0;

    // Padding (with defaults)
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;

    // Colors (with defaults)
    private int barColor = 0xAA000000;
    private int contourColor = 0xAA000000;
    private int circleColor = 0x00000000;
    private int rimColor = 0xAADDDDDD;
    private int textColor = 0xFF000000;

    // Paints
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint contourPaint = new Paint();

    // Rectangles
    private RectF innerCircleBounds = new RectF();
    private RectF circleBounds = new RectF();
    private RectF circleOuterContour = new RectF();
    private RectF circleInnerContour = new RectF();

    // Animation
    // The amount of pixels to move the bar by on each draw
    private float spinSpeed = 2f;
    // The number of milliseconds to wait in between each draw
    private int delayMillis = 10;
    private float progress = 0;
    boolean isSpinning = false;

    // Other
    private String text = "";
    private String[] splitText = {};

    public ProgressWheel(Context context) {
        super(context);
    }

    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressWheel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressWheel));
    }

    private void parseAttributes(TypedArray a) {
        barColor = a.getColor(R.styleable.ProgressWheel_pwBarColor, barColor);
        barWidth = (int) a.getDimension(R.styleable.ProgressWheel_pwBarWidth, barWidth);
        barLength = (int) a.getDimension(R.styleable.ProgressWheel_pwBarLength, barLength);
        rimColor = a.getColor(R.styleable.ProgressWheel_pwRimColor, rimColor);
        rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_pwRimWidth, rimWidth);
        contourColor = a.getColor(R.styleable.ProgressWheel_pwContourColor, contourColor);
        contourSize = a.getDimension(R.styleable.ProgressWheel_pwContourSize, contourSize);
        circleColor = a.getColor(R.styleable.ProgressWheel_pwCircleColor, circleColor);

        // Only set the text if it is explicitly defined
        if (a.hasValue(R.styleable.ProgressWheel_pwText)) {
            setText(a.getString(R.styleable.ProgressWheel_pwText));
        }
        textColor = a.getColor(R.styleable.ProgressWheel_pwTextColor, textColor);
        textSize = (int) a.getDimension(R.styleable.ProgressWheel_pwTextSize, textSize);

        spinSpeed = a.getFloat(R.styleable.ProgressWheel_pwSpinSpeed, spinSpeed);
        delayMillis = a.getInteger(R.styleable.ProgressWheel_pwDelayMillis, delayMillis);
        if (delayMillis < 0) {
            delayMillis = 10;
        }
        a.recycle();
    }

    public void setText(String text) {
        this.text = text;
        splitText = this.text.split("\n");
    }
}
