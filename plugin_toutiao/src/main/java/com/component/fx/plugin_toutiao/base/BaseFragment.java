package com.component.fx.plugin_toutiao.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.component.fx.plugin_base.utils.LogUtils;

public abstract class BaseFragment extends Fragment {

    protected final String TAG = this.getClass().getSimpleName();
    // 界面是否已创建完成
    protected boolean isViewCreate;

    @NonNull
    protected Context context;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        LogUtils.d(TAG, "setUserVisibleHint: " + isVisibleToUser);
    }

    @Override
    public boolean getUserVisibleHint() {
        LogUtils.d(TAG, "getUserVisibleHint: ");
        return super.getUserVisibleHint();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        LogUtils.d(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        initData();
        initView(view);
        return view;

    }

    protected abstract int getLayoutRes();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreate = true;
    }

    protected void initData() {

    }

    protected void initView(View view) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume: ");
    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy: ");
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        LogUtils.d(TAG, "onAttachFragment: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d(TAG, "onConfigurationChanged: ");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.d(TAG, "onHiddenChanged: " + hidden);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.d(TAG, "onLowMemory: ");
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        LogUtils.d(TAG, "onViewStateRestored: ");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        LogUtils.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d(TAG, "onDetach: ");
    }

    @Override
    public void onDestroyView() {
        isViewCreate = false;
        super.onDestroyView();
        LogUtils.d(TAG, "onDestroyView: ");
    }
}
