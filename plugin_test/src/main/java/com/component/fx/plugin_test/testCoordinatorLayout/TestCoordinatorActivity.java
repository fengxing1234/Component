package com.component.fx.plugin_test.testCoordinatorLayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.component.fx.plugin_test.R;
import com.component.fx.plugin_test.ui.CheckBoxView;
import com.component.fx.plugin_test.ui.PieChartView;
import com.component.fx.plugin_test.ui.TestView;

import java.util.ArrayList;

public class TestCoordinatorActivity extends AppCompatActivity {

    private ArrayList<PieChartView.PieCharData> list;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //testPieChartView();
        //testCheckBoxView();
        testView();
    }

    private void testCheckBoxView() {
        setContentView(R.layout.test_check_box_view);
        final CheckBoxView checkBoxView = findViewById(R.id.check_box_view);
        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxView.check();
            }
        });

        findViewById(R.id.btn_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxView.unCheck();
            }
        });

    }

    private void testView() {
        setContentView(R.layout.test_view_layout);
        final TestView testView = findViewById(R.id.test_view);
        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.check();
            }
        });

        findViewById(R.id.btn_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.unCheck();
            }
        });
    }

    private void testPieChartView() {
        setContentView(R.layout.test_activity_coordinator_layout);
        final PieChartView pieChartView = (PieChartView) findViewById(R.id.pie_chart_view);
        pieChartView.setPieData(initData());

        findViewById(R.id.test_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChartView.setStartAngle(-90);
            }
        });

        findViewById(R.id.test_view2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(new PieChartView.PieCharData("ff", 55));
                pieChartView.setPieData(list);
            }
        });
    }

    private ArrayList<PieChartView.PieCharData> initData() {
        list = new ArrayList<>();
        list.add(new PieChartView.PieCharData("aa", 12));
        list.add(new PieChartView.PieCharData("ss", 23));
        list.add(new PieChartView.PieCharData("dd", 45));
        list.add(new PieChartView.PieCharData("ff", 78));
        list.add(new PieChartView.PieCharData("gg", 96));
        return list;
    }
}
