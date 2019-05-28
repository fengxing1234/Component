package com.component.fx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.component.fx.plugin_base.utils.ToastUtil;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(this,TestRecycleViewActivity.class));
        finish();
        ImageView ivCircle = findViewById(R.id.iv_glide_circle);
        Glide.with(this)
                .load(getResources().getDrawable(R.drawable.aaaaaaa))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivCircle);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                Log.d(TAG, "onDrawerSlide: " + v);
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                Log.d(TAG, "onDrawerOpened: ");
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                Log.d(TAG, "onDrawerClosed: ");
            }

            @Override
            public void onDrawerStateChanged(int i) {
                Log.d(TAG, "onDrawerStateChanged: " + i);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        setupDrawerContent(navigationView);
        toggle.syncState();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.project_home:
                        ToastUtil.toast("点击了 主页");
                        //startActivity(new Intent(MainActivity.this, TestCoordinatorActivity.class));
                        break;
                    case R.id.project_id:
                        ToastUtil.toast("点击了 主页id");
                        break;
                    case R.id.project_shared:
                        ToastUtil.toast("点击了 主页分享");
                        break;
                }
                //drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    private void jump() {
        ARouter.getInstance().build("/plugin_news/news").navigation();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("相应的提示内容");
        //searchView.setIconifiedByDefault(true);
        //searchView.onActionViewExpanded();
        //searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ToastUtil.toast(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ToastUtil.toast(newText);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast("开始搜索");
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ToastUtil.toast("关闭");
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

}
