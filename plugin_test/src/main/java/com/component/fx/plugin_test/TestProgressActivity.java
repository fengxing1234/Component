package com.component.fx.plugin_test;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.component.fx.plugin_test.progress.CircleProgress;
import com.component.fx.plugin_test.progress.SquareProgress;
import com.component.fx.plugin_test.progress.WaveDrawable;

public class TestProgressActivity extends AppCompatActivity {

    private static final String TAG = TestProgressActivity.class.getSimpleName();
    private ImageView mImageView;
    private WaveDrawable mWaveDrawable;
    private SeekBar mLevelSeekBar;
    private SeekBar mAmplitudeSeekBar;
    private SeekBar mSpeedSeekBar;
    private SeekBar mLengthSeekBar;
    private RadioGroup mRadioGroup;


    private CircleProgress circleProgress;

    private int current;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

//            circleProgress.setProgress(msg.what);
//            squareProgress.setProgress(msg.what);
            chromeWave.setLevel(msg.what * 100);

        }
    };
    private SquareProgress squareProgress;
    private ImageView src3;
    private WaveDrawable chromeWave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_progress_activity2);

//        ImageView imageView = (ImageView) findViewById(R.id.iv_src1);
//
//        circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
//
//        squareProgress = (SquareProgress) findViewById(R.id.square_progress);
//
//        src3 = (ImageView) findViewById(R.id.iv_src3);
//        WaveDrawable mWaveDrawable = new WaveDrawable(this, R.mipmap.ic_chading);
//        src3.setImageDrawable(mWaveDrawable);


        testWave();
        setProgress();
    }

    private void testWave() {
        mImageView = (ImageView) findViewById(R.id.image);
        mWaveDrawable = new WaveDrawable(this, R.mipmap.ic_chading);
        mImageView.setImageDrawable(mWaveDrawable);


        mLevelSeekBar = (SeekBar) findViewById(R.id.level_seek);
        mLevelSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "setLevel: " + progress);
                mWaveDrawable.setLevel(progress);
            }
        });

        mAmplitudeSeekBar = (SeekBar) findViewById(R.id.amplitude_seek);
        mAmplitudeSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "setWaveAmplitude: " + progress);
                mWaveDrawable.setWaveAmplitude(progress);
            }
        });

        mLengthSeekBar = (SeekBar) findViewById(R.id.length_seek);
        mLengthSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "setWaveLength: " + progress);
                mWaveDrawable.setWaveLength(progress);
            }
        });

        mSpeedSeekBar = (SeekBar) findViewById(R.id.speed_seek);
        mSpeedSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "setWaveSpeed: " + progress);
                mWaveDrawable.setWaveSpeed(progress);
            }
        });

        mRadioGroup = (RadioGroup) findViewById(R.id.modes);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final boolean indeterminate = checkedId == R.id.rb_yes;
                setIndeterminateMode(indeterminate);
            }
        });
        setIndeterminateMode(mRadioGroup.getCheckedRadioButtonId() == R.id.rb_yes);

        ImageView imageView2 = (ImageView) findViewById(R.id.image2);
        imageView2.setImageDrawable(getDrawable(R.mipmap.ic_weixiuchang));
        Drawable drawable = imageView2.getDrawable();
        chromeWave = new WaveDrawable(drawable);
        imageView2.setImageDrawable(chromeWave);

        // Set customised animator here
//        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
//        animator.setRepeatMode(ValueAnimator.REVERSE);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setDuration(4000);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        chromeWave.setIndeterminateAnimator(animator);
        chromeWave.setIndeterminate(false);

//        View view = findViewById(R.id.view);
//        int color = getResources().getColor(R.color.test_colorAccent);
//        WaveDrawable colorWave = new WaveDrawable(new ColorDrawable(color));
//        view.setBackground(colorWave);
//        colorWave.setIndeterminate(true);
    }

    private void setProgress() {
        new Thread() {
            @Override
            public void run() {

                for (; ; ) {
                    try {
                        Thread.sleep(2000);
                        if (current == 100) {
                            current = 0;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = Message.obtain();
                    current += 10;
                    message.what = current;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private void setIndeterminateMode(boolean indeterminate) {
        mWaveDrawable.setIndeterminate(indeterminate);
        mLevelSeekBar.setEnabled(!indeterminate);

        if (!indeterminate) {
            mWaveDrawable.setLevel(mLevelSeekBar.getProgress());
        }
        mWaveDrawable.setWaveAmplitude(mAmplitudeSeekBar.getProgress());
        mWaveDrawable.setWaveLength(mLengthSeekBar.getProgress());
        mWaveDrawable.setWaveSpeed(mSpeedSeekBar.getProgress());
    }

    private static class SimpleOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Nothing
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Nothing
        }
    }
}
