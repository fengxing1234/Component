package com.component.fx.plugin_toutiao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.component.fx.plugin_base.utils.ToastUtil;

public class PluginTouTiaoActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavigationView;


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
        initBottomNavigationVoew();

    }

    private void initBottomNavigationVoew() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.toutiao_bottom_nav_view);
        //mBottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
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
