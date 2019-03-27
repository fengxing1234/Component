package com.component.fx.plugin_toutiao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.base.BaseFragment;

public class TouTiaoHotFragment extends BaseFragment {

    public static TouTiaoHotFragment getInstance() {
        TouTiaoHotFragment fragment = new TouTiaoHotFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        ((TextView) view.findViewById(R.id.toutiao_name)).setText("头条号");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.toutiao_fragment_pic_layout;
    }
}
