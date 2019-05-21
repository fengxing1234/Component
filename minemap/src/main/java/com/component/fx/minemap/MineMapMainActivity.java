package com.component.fx.minemap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.minedata.minemap.MinemapAccountManager;
import com.minedata.minemap.map.MapView;
import com.minedata.minemap.map.MineMap;

public class MineMapMainActivity extends AppCompatActivity {

    private MapView mapView;
    private static MineMap mineMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MinemapAccountManager.getInstance(getApplicationContext(),
                "",//访问token
                null,
                "5696");//方案id

        setContentView(R.layout.activity_mine_map_main);
//        mapView = (MapView) findViewById(R.id.mapView);
//        mapView.setStyleUrl("http://minedata.cn/service/solu/style/id/5696");
//        mapView.addMapRenderCallback(new MapView.OnMapReadyListener() {
//            @Override
//            public void onMapReady(MineMap mineMap) {
//                MainActivity.mineMap = mineMap;
//                mineMap.setCameraPosition(
//                        new CameraPosition.Builder()
//                                .target(new LatLng(39.897424, 116.356508))//设置相机指向的位置
//                                .zoom(13)//设置相机缩放等级
//                                .tilt(0)//设置相机的俯视角度
//                                .bearing(0) //摄像机指向的方向,从北部顺时针方向设置
//                                .build());
//                //默认显示路况
//                mineMap.setTrafficShow(true);
//                //60秒刷新一次
//                mineMap.setTrafficRote(60);
//            }
//        });
    }
}
