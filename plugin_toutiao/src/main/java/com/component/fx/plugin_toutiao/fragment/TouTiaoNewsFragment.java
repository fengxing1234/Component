package com.component.fx.plugin_toutiao.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.component.fx.plugin_base.utils.LogUtils;
import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.adapter.TouTiaoNewsPagerAdapter;
import com.component.fx.plugin_toutiao.base.BaseFragment;
import com.component.fx.plugin_toutiao.bean.NewsChannelBean;
import com.component.fx.plugin_toutiao.db.dao.NewsChannelDao;
import com.component.fx.plugin_toutiao.db.tab.NewsChannelTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 四大主页 之 第一个主页 新闻
 */
public class TouTiaoNewsFragment extends BaseFragment {

    private static final String TAG = "TouTiaoNewsFragment";
    private static final String WENDA_TYPE = "question_and_answer";


    private TabLayout tabLayout;
    private ImageButton imageButton;
    private ViewPager viewPager;
    private TouTiaoNewsPagerAdapter pagerAdapter;
    private NewsChannelDao dao;
    private ArrayList<String> titles;
    private List<Fragment> fragments = new ArrayList<>();


    public static TouTiaoNewsFragment getInstance() {
        TouTiaoNewsFragment fragment = new TouTiaoNewsFragment();
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.toutiao_main_news_layout;
    }

    @Override
    protected void iniData() {
        if (dao == null) {
            dao = new NewsChannelDao();
        }

        List<NewsChannelBean> newsChannelList = dao.query(NewsChannelTable.NEWS_CHANNEL_ENABLE);
        if (newsChannelList == null || newsChannelList.size() == 0) {
            dao.initDao();
            newsChannelList = dao.query(NewsChannelTable.NEWS_CHANNEL_ENABLE);
        }

        titles = new ArrayList<>();

        for (NewsChannelBean newsChannel : newsChannelList) {
            titles.add(newsChannel.channelName);
            if (WENDA_TYPE.equals(newsChannel.channelId)) {
                fragments.add(TouTiaoWenDaFragment.getInstance());
            } else {
                fragments.add(TouTiaoNewsTabFragment.getInstance(newsChannel.channelId));
            }

        }
        LogUtils.d(TAG, "fragments size: " + fragments.size());
    }

    @Override
    protected void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.toutiao_news_tab_layout);
        imageButton = (ImageButton) view.findViewById(R.id.toutiao_add_channel);
        viewPager = (ViewPager) view.findViewById(R.id.toutiao_view_pager);
        pagerAdapter = new TouTiaoNewsPagerAdapter(getChildFragmentManager(), titles, fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(15);
        tabLayout.setupWithViewPager(viewPager);
    }

}
