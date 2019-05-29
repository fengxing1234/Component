package com.component.fx.plugin_base.base.recyclerview;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import com.component.fx.plugin_base.base.recyclerview.provider.BaseItemProvider;
import com.component.fx.plugin_base.base.recyclerview.provider.MultiTypeDelegate;
import com.component.fx.plugin_base.base.recyclerview.provider.ProviderDelegate;

public abstract class MultipleItemRvAdapter<T> extends BaseAdapter<T> {

    public ProviderDelegate providerDelegate;
    private SparseArray<BaseItemProvider> itemProviders;

    protected MultipleItemRvAdapter() {

    }

    /**
     * 用于adapter构造函数完成参数的赋值后调用
     * Called after the assignment of the argument to the adapter constructor
     */
    public void finishInitialize() {
        providerDelegate = new ProviderDelegate();

        setMultiTypeDelegate(new MultiTypeDelegate<T>() {
            @Override
            public int getViewType(T data) {
                return getItemViewType(data);
            }
        });

        registerItemProvider(providerDelegate);

        itemProviders = providerDelegate.getItemProviders();

        for (int i = 0; i < itemProviders.size(); i++) {
            int key = itemProviders.keyAt(i);
            BaseItemProvider itemProvider = itemProviders.get(key);
            getMultiTypeDelegate().registerItemType(key, itemProvider.getLayout());
        }
    }

    public abstract int getItemViewType(T data);

    public abstract void registerItemProvider(ProviderDelegate providerDelegate);

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    protected void convert(@NonNull BaseHolder baseHolder, T data, int position) {
        int itemViewType = baseHolder.getItemViewType();

        BaseItemProvider itemProvider = itemProviders.get(itemViewType);

        itemProvider.convert(baseHolder, data, position);

        bindClick(baseHolder, data, position, itemProvider);
    }

    private void bindClick(final BaseHolder baseHolder, final T data, final int position, final BaseItemProvider itemProvider) {
        baseHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemProvider.onItemClick(baseHolder, data, position);
            }
        });

        baseHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return itemProvider.onItemLongClick(baseHolder, data, position);
            }
        });
    }
}
