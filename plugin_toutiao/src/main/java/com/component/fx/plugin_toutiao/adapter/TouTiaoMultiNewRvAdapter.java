package com.component.fx.plugin_toutiao.adapter;

import com.component.fx.plugin_base.base.recyclerview.MultipleItemRvAdapter;
import com.component.fx.plugin_base.base.recyclerview.provider.ProviderDelegate;
import com.component.fx.plugin_toutiao.bean.MultiNewsArticleBeanData;

public class TouTiaoMultiNewRvAdapter extends MultipleItemRvAdapter<MultiNewsArticleBeanData> {

    public static final int TEXT_ITEM_PROVIDER = 101;
    public static final int IMAGE_ITEM_PROVIDER = 102;
    public static final int VIDEO_ITEM_PROVIDER = 103;

    public TouTiaoMultiNewRvAdapter(){
        finishInitialize();
    }


    @Override
    public int getItemViewType(MultiNewsArticleBeanData data) {
        boolean hasVideo = data.isHas_video();
        if (hasVideo) {
            return VIDEO_ITEM_PROVIDER;
        } else if (data.getImage_list() != null && data.getImage_list().size() > 0) {
            return IMAGE_ITEM_PROVIDER;

        }
        return TEXT_ITEM_PROVIDER;
    }

    @Override
    public void registerItemProvider(ProviderDelegate providerDelegate) {
        providerDelegate.registerProvider(new TouTiaoNewsTextProvider());
        providerDelegate.registerProvider(new TouTiaoNewsImageProvider());
        providerDelegate.registerProvider(new TouTiaoNewsVideoProvider());
    }


}
