package com.component.fx.plugin_test.test_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.component.fx.plugin_test.R;
import com.component.fx.plugin_test.ui.RadarView;

public class RadarViewActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private RadarView radarView;

    private double[] data = new double[]{20, 20, 20, 20, 20, 20};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_radar_activity);
        radarView = (RadarView) findViewById(R.id.radar_view);

        SeekBar seekBar1 = (SeekBar) findViewById(R.id.seek_bar_1);
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seek_bar_2);
        SeekBar seekBar3 = (SeekBar) findViewById(R.id.seek_bar_3);
        SeekBar seekBar4 = (SeekBar) findViewById(R.id.seek_bar_4);
        SeekBar seekBar5 = (SeekBar) findViewById(R.id.seek_bar_5);
        SeekBar seekBar6 = (SeekBar) findViewById(R.id.seek_bar_6);
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);
        seekBar4.setOnSeekBarChangeListener(this);
        seekBar5.setOnSeekBarChangeListener(this);
        seekBar6.setOnSeekBarChangeListener(this);
        radarView.setData(data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        radarView = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        switch (id) {
            case R.id.seek_bar_1:
                data[0] = progress;
                break;
            case R.id.seek_bar_2:
                data[1] = progress;
                break;
            case R.id.seek_bar_3:
                data[2] = progress;
                break;
            case R.id.seek_bar_4:
                data[3] = progress;
                break;
            case R.id.seek_bar_5:
                data[4] = progress;
                break;
            case R.id.seek_bar_6:
                data[5] = progress;
                break;
        }
        radarView.setData(data);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
