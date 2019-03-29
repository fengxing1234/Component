package com.component.fx.plugin_base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.component.fx.plugin_base.base.BaseActivity;

public class WebViewActivity extends BaseActivity {

    private WebView mWebView;

    private static final String WEB_VIEW_URL = "web_view_url";

    public static void startWebViewActivity(Context context, String url) {
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra(WEB_VIEW_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_web_view);

        initWebView();

        Intent intent = getIntent();
        String url = intent.getStringExtra(WEB_VIEW_URL);
        loadUrl(url);
    }

    private void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    private void initWebView() {
        FrameLayout container = findViewById(R.id.base_web_view_container);
        mWebView = new WebView(getApplicationContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        container.addView(mWebView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }
}
