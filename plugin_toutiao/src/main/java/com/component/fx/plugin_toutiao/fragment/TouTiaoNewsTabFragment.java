package com.component.fx.plugin_toutiao.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.component.fx.plugin_toutiao.R;

public class TouTiaoNewsTabFragment extends Fragment {

    private static final String TAG = "TouTiaoNewsTabFragment";

    public static final String NEWS_CATEGORY_KEY = "news_category_key";
    private String type;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    public static TouTiaoNewsTabFragment getInstance(String type) {
        TouTiaoNewsTabFragment fragment = new TouTiaoNewsTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_CATEGORY_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getString(NEWS_CATEGORY_KEY);
        }
        Log.d(TAG, "setUserVisibleHint: " + isVisibleToUser + "   新闻类型 : " + type);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public boolean getUserVisibleHint() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getString(NEWS_CATEGORY_KEY);
        }
        Log.d(TAG, "getUserVisibleHint: " + "   新闻类型 : " + type);
        return super.getUserVisibleHint();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getString(NEWS_CATEGORY_KEY);
        }
        Log.d(TAG, "onAttach: " + "   新闻类型 : " + type);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getString(NEWS_CATEGORY_KEY);
        }
        Log.d(TAG, "onCreate: " + "   新闻类型 : " + type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + "   新闻类型 : " + type);
        View view = inflater.inflate(R.layout.toutiao_fragment_item_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.toutiao_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.toutiao_recycle_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: " + "   新闻类型 : " + type);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: " + "   新闻类型 : " + type);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + "   新闻类型 : " + type);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + "   新闻类型 : " + type);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + "   新闻类型 : " + type);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + "   新闻类型 : " + type);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: " + "   新闻类型 : " + type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + "   新闻类型 : " + type);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG, "onAttachFragment: " + "   新闻类型 : " + type);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: " + "   新闻类型 : " + type);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged: " + hidden + "   新闻类型 : " + type);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory: " + "   新闻类型 : " + type);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored: " + "   新闻类型 : " + type);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: " + "   新闻类型 : " + type);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: " + "   新闻类型 : " + type);
    }
}
