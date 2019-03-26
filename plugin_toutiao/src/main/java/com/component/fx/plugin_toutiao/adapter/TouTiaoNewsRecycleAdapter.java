package com.component.fx.plugin_toutiao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class TouTiaoNewsRecycleAdapter extends RecyclerView.Adapter {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private static class TouTiaoNewsRecycleHodler extends RecyclerView.ViewHolder {

        public TouTiaoNewsRecycleHodler(@NonNull View itemView) {
            super(itemView);
        }
    }
}
