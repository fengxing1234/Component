package com.component.fx.plugin_toutiao.mvp;

import android.view.View;
import android.view.ViewGroup;

import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.adapter.TouTiaoMultiWendaAdapter;
import com.component.fx.plugin_toutiao.bean.WendaArticleBeanData;

import java.util.List;

public class TouTiaoMvpWenDaFragment extends TouTiaoMvpRecycleFragment<IWenDaContract.Presenter> implements IWenDaContract.View {


    private TouTiaoMultiWendaAdapter multiNewsAdapter;

    public static TouTiaoMvpWenDaFragment getInstance() {

        return new TouTiaoMvpWenDaFragment();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        multiNewsAdapter = new TouTiaoMultiWendaAdapter();
        multiNewsAdapter.addItemType(WendaArticleBeanData.WENDA_TEXT_TYPE, R.layout.toutiao_fragment_wenda_text_content);
        multiNewsAdapter.addItemType(WendaArticleBeanData.WENDA_THREE_IMG_TYPE, R.layout.toutiao_fragment_wenda_three_image_content);
        multiNewsAdapter.addItemType(WendaArticleBeanData.WENDA_ONE_IMG_TYPE, R.layout.toutiao_fragment_wenda_one_image_content);
        multiNewsAdapter.addFooter(getLayoutInflater().inflate(R.layout.toutiao_fragment_news_foot_view, (ViewGroup) recyclerView.getParent(), false));
        recyclerView.setAdapter(multiNewsAdapter);
    }

    @Override
    public IWenDaContract.Presenter getPresenter() {
        return new IWenDaPresenter(this);
    }

    @Override
    public void onSetAdapter(List<?> list) {
        multiNewsAdapter.setData((List<WendaArticleBeanData>) list);
    }
}
