package com.component.fx.plugin_test.test_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.component.fx.plugin_test.R;

public class TestPluginMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_plugin_main_activity);
        findViewById(R.id.test_btn_notify).setOnClickListener(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            Toast.makeText(this, action, Toast.LENGTH_LONG).show();
        }
        startActivity(new Intent(this, TestRecycleViewActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_btn_notify:
                startActivity(new Intent(this, TestProgressActivity.class));
                break;
        }
    }
}
