package com.component.fx.plugin_toutiao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.fragment.TouTiaoHotFragment;
import com.component.fx.plugin_toutiao.fragment.TouTiaoNewsFragment;
import com.component.fx.plugin_toutiao.fragment.TouTiaoPicFragment;
import com.component.fx.plugin_toutiao.fragment.TouTiaoVideoFragment;

public class PluginTouTiaoActivity extends AppCompatActivity {

    private static final String TAG = PluginTouTiaoActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavigationView;
    private TouTiaoNewsFragment newsFragment;

    public static final int NEWS_FRAGMENT_FLAG = 0x001;
    public static final int VIDEO_FRAGMENT_FLAG = 0x002;
    public static final int PIC_FRAGMENT_FLAG = 0x003;
    public static final int HOT_FRAGMENT_FLAG = 0x004;
    private TouTiaoVideoFragment videoFragment;
    private TouTiaoPicFragment picFragment;
    private TouTiaoHotFragment hotFragment;
    private Fragment mShowFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toutiao_activity_plugin_tou_tiao);


        mNavigationView = (NavigationView) findViewById(R.id.toutiao_draw_nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.toutiao_draw_layout);
        mToolbar = (Toolbar) findViewById(R.id.toutiao_tool_bar);

        initDrawLayout();
        initToolBar();
        initNavigationView();
        initBottomNavigationView();

        mBottomNavigationView.setSelectedItemId(R.id.toutiao_menu_action_news);
    }

    private void initBottomNavigationView() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.toutiao_bottom_nav_view);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.toutiao_menu_action_news:
                        changeFragment(NEWS_FRAGMENT_FLAG);
                        break;
                    case R.id.toutiao_menu_action_video:
                        changeFragment(VIDEO_FRAGMENT_FLAG);
                        break;
                    case R.id.toutiao_menu_action_pic:
                        changeFragment(PIC_FRAGMENT_FLAG);
                        break;
                    case R.id.toutiao_menu_action_toutiao:
                        changeFragment(HOT_FRAGMENT_FLAG);
                        break;
                }
                return true;
            }
        });
    }

    private void changeFragment(int flag) {

        //FragmentManager不会重复创建实例
        FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction 会重新创建实例
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Log.d(TAG, "会不会重复创建实例 transaction : " + transaction);
        if (mShowFragment != null) {
            transaction.hide(mShowFragment);
        }

        switch (flag) {
            case NEWS_FRAGMENT_FLAG:
                if (newsFragment == null) {
                    newsFragment = TouTiaoNewsFragment.getInstance();
                    transaction.add(R.id.toutiao_main_container, newsFragment, TouTiaoNewsFragment.class.getName());
                } else {
                    transaction.show(newsFragment);
                }
                mShowFragment = newsFragment;
                break;
            case VIDEO_FRAGMENT_FLAG:
                if (videoFragment == null) {
                    videoFragment = TouTiaoVideoFragment.getInstance();
                    transaction.add(R.id.toutiao_main_container, videoFragment, TouTiaoVideoFragment.class.getName());
                } else {
                    transaction.show(videoFragment);
                }
                mShowFragment = videoFragment;
                break;
            case PIC_FRAGMENT_FLAG:
                if (picFragment == null) {
                    picFragment = TouTiaoPicFragment.getInstance();
                    transaction.add(R.id.toutiao_main_container, picFragment, TouTiaoPicFragment.class.getName());
                } else {
                    transaction.show(picFragment);
                }
                mShowFragment = picFragment;
                break;
            case HOT_FRAGMENT_FLAG:
                if (hotFragment == null) {
                    hotFragment = TouTiaoHotFragment.getInstance();
                    transaction.add(R.id.toutiao_main_container, hotFragment, TouTiaoHotFragment.class.getName());
                } else {
                    transaction.show(hotFragment);
                }
                mShowFragment = hotFragment;
                break;
        }
        transaction.commit();
    }


    private void initDrawLayout() {

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.toutiao_close, R.string.toutiao_open);
        toggle.syncState();
    }

    private void initNavigationView() {
        View headerView = mNavigationView.getHeaderView(0);
        View headerRoot = headerView.findViewById(R.id.toutiao_iv_draw_header_root);
        headerRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast("图片被点击了");
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.toutiao_menu_switch_theme:
                        ToastUtil.toast("切换主题");
                        break;
                    case R.id.toutiao_menu_nav_setting:
                        ToastUtil.toast("设置");
                        mDrawerLayout.closeDrawer(Gravity.START, true);
                        break;
                    case R.id.toutiao_menu_nav_shared:
                        ToastUtil.toast("分享");
                        mDrawerLayout.closeDrawer(Gravity.START, true);
                        break;
                }

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toutiao_main_activity_menu, menu);
        initSearchView(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.toutiao_activity_menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ToastUtil.toast(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ToastUtil.toast(s);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast("SearchView Click");
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ToastUtil.toast("SearchView Close");
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
