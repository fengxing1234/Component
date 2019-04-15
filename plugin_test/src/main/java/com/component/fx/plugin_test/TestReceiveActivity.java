package com.component.fx.plugin_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class TestReceiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_receive_activity);

        Toast.makeText(this,getIntent().getAction(),Toast.LENGTH_SHORT).show();
    }
}