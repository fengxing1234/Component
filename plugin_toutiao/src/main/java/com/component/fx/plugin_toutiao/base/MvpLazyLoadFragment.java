package com.component.fx.plugin_toutiao.base;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

public abstract class MvpLazyLoadFragment<T extends IBasePresenter> extends LazyLoadFragment implements IBaseView<T> {

    protected T presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
    }

    @Override
    public void setPresenter(T presenter) {
        if (presenter == null) {
            return;
        }
        this.presenter = presenter;
    }

    @Override
    public <X> AutoDisposeConverter<X> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));

    }
}
