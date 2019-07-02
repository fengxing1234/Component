package com.component.fx.plugin_test.test_activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_test.R;
import com.component.fx.plugin_test.test_activity.activityLaunchMode.LaunchModeA;

import java.util.Arrays;
import java.util.List;

public class TestPluginMainActivity extends AppCompatActivity {

    private static final String[] TestName = new String[]{"蜘蛛网", "启动模式"};

    private static final Class[] classArray = new Class[]{RadarViewActivity.class, LaunchModeA.class};
    private RecyclerView recyclerView;
    private TestViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_plugin_main_activity);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new TestViewAdapter(this, createData(), createClass());
        recyclerView.setAdapter(adapter);
        dumpTaskAffinity();
    }

    protected void dumpTaskAffinity() {
        PackageManager packageManager = getPackageManager();
        try {
            ActivityInfo activityInfo = packageManager.getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            Log.d("LaunchModeBase", "TestPluginMainActivity = dumpTaskAffinity = " + activityInfo.taskAffinity + "  taskId = " + getTaskId());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        recyclerView = null;
    }

    private List<Class> createClass() {
        return Arrays.asList(classArray);
    }

    private List<String> createData() {
        return Arrays.asList(TestName);
    }

    private static class TestViewAdapter extends RecyclerView.Adapter<TestViewHolder> {

        private List<String> nameList;
        private List<Class> classList;
        private Context mContext;
        private final LayoutInflater inflater;

        public TestViewAdapter(Context context, List<String> list, List<Class> aClass) {
            this.mContext = context;
            this.nameList = list;
            this.classList = aClass;
            inflater = LayoutInflater.from(mContext);
            if (nameList.size() != classList.size()) {
                ToastUtil.toast("数量不对应");
            }
        }

        @NonNull
        @Override
        public TestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.test_recycle_item_layout, viewGroup, false);
            return new TestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TestViewHolder testViewHolder, final int i) {
            TextView testName = (TextView) testViewHolder.itemView.findViewById(R.id.test_name);
            testName.setText(nameList.get(i));
            testName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, classList.get(i));
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return nameList.size();
        }
    }

    private static class TestViewHolder extends RecyclerView.ViewHolder {

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
