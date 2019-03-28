package com.component.fx.plugin_toutiao.fragment;

import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.base.LazyLoadFragment;

public class TouTiaoWenDaFragment extends LazyLoadFragment {

    public static TouTiaoWenDaFragment getInstance() {

        return new TouTiaoWenDaFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.toutiao_fragment_item_layout;
    }

    @Override
    protected void lazyLoadData() {

    }
}
