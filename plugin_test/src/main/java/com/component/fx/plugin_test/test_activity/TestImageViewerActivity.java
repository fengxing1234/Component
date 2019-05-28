package com.component.fx.plugin_test.test_activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.component.fx.plugin_base.utils.GlideUtils;
import com.component.fx.plugin_test.R;
import com.component.fx.plugin_test.SourceUtils;

import java.util.ArrayList;
import java.util.List;

public class TestImageViewerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TestImageViewerAdapter adapter;

    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_image_viewer);
        initData();
        initView();
    }

    private void initData() {
        list = SourceUtils.getImageList();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new TestImageViewerAdapter(list);
        adapter.setOnItemClickCallback(new OnItemClickCallback() {
            @Override
            public void onItemClick(int position, ImageView view) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    private static class TestImageViewerAdapter extends RecyclerView.Adapter<TestImageViewerHolder> {

        private OnItemClickCallback mCallback;

        private List<String> list;

        public TestImageViewerAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public TestImageViewerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_item_image_viewer_layout, viewGroup, false);
            return new TestImageViewerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TestImageViewerHolder holder, int i) {
            ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.iv_src);
            GlideUtils.loadImage(imageView.getContext(), list.get(i), R.mipmap.ic_chading, imageView);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setOnItemClickCallback(OnItemClickCallback clickCallback) {
            this.mCallback = clickCallback;
        }

    }

    private static class TestImageViewerHolder extends RecyclerView.ViewHolder {


        public TestImageViewerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickCallback {
        void onItemClick(int position, ImageView view);
    }
}
