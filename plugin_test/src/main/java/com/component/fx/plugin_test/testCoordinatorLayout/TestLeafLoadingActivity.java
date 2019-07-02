package com.component.fx.plugin_test.testCoordinatorLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.component.fx.plugin_test.R;
import com.component.fx.plugin_test.ui.LeafLoadingView;

import java.util.Random;

public class TestLeafLoadingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {


    private static final String TAG = TestLeafLoadingActivity.class.getSimpleName();
    private TextView tvCurrentProgress;
    private SeekBar mSeekBarProgress;
    private LeafLoadingView mLeafLoadingView;
    private static final int REFRESH_PROGRESS = 0x10;
    private int mProgress = 0;


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_PROGRESS:
                    if (mProgress < 40) {
                        mProgress += 1;
                        // 随机800ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(800));
                        mLeafLoadingView.setProgress(mProgress);
                    } else {
                        mProgress += 1;
                        // 随机1200ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(1200));
                        mLeafLoadingView.setProgress(mProgress);

                    }
                    break;

                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testLeafLoading();
        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000);

    }

    private void testLeafLoading() {
        setContentView(R.layout.test_leaf_loading_layout);
        mLeafLoadingView = (LeafLoadingView) findViewById(R.id.leaf_loading_view);
        tvCurrentProgress = findViewById(R.id.tv_current_progress);
        mSeekBarProgress = (SeekBar) findViewById(R.id.seek_bar_progress);
        mSeekBarProgress.setOnSeekBarChangeListener(this);
        mSeekBarProgress.setProgress(0);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == mSeekBarProgress) {
            tvCurrentProgress.setText("进度 " + progress + "%");
            //mSeekBarProgress.setProgress(progress);
            mLeafLoadingView.setProgress(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStartTrackingTouch: ");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStopTrackingTouch: ");
    }
}
