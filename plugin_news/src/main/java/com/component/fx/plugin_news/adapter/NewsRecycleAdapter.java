package com.component.fx.plugin_news.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.component.fx.plugin_news.R;
import com.component.fx.plugin_news.network.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

public class NewsRecycleAdapter extends RecyclerView.Adapter {

    private Fragment mFragment;
    private List<NewsModel.DataBean> mNewsList;

    public NewsRecycleAdapter(Fragment context) {
        this.mFragment = context;
        if (mNewsList == null) {
            mNewsList = new ArrayList<>();
        }
    }

    public void setNewsModel(NewsModel newsModel) {
        mNewsList.addAll(newsModel.getData());
    }

    public void addNewList(NewsModel newsModel) {

    }

    public List<NewsModel.DataBean> getNewsModel() {
        return mNewsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_layout_news_item, viewGroup, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        NewsModel.DataBean dataBean = mNewsList.get(i);
        NewsViewHolder holder = (NewsViewHolder) viewHolder;
        TextView tvTitle = (TextView) holder.getView(R.id.news_tv_title);
        ImageView tvThumbnail1 = (ImageView) holder.getView(R.id.news_iv_thumbnail_1);
        ImageView tvThumbnail2 = (ImageView) holder.getView(R.id.news_iv_thumbnail_2);
        ImageView tvThumbnail3 = (ImageView) holder.getView(R.id.news_iv_thumbnail_3);
        TextView tvAuthorName = (TextView) holder.getView(R.id.news_tv_author_name);
        TextView tvNewsDate = (TextView) holder.getView(R.id.news_tv_news_date);
        Glide.with(mFragment).load(dataBean.getThumbnail_pic_s()).placeholder(R.drawable.place).into(tvThumbnail1);
        Glide.with(mFragment).load(dataBean.getThumbnail_pic_s02()).placeholder(R.drawable.place).into(tvThumbnail2);
        Glide.with(mFragment).load(dataBean.getThumbnail_pic_s03()).placeholder(R.drawable.place).into(tvThumbnail3);
        tvTitle.setText(dataBean.getTitle());
        tvAuthorName.setText(dataBean.getAuthor_name());
        tvNewsDate.setText(dataBean.getDate());

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        private SparseArray<View> mViews;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mItemView = itemView;
            mViews = new SparseArray<>();
        }

        public View getView(@IdRes int id) {
            View view = mViews.get(id);
            if (view == null) {
                view = mItemView.findViewById(id);
                mViews.put(id, view);
            }
            return view;
        }
    }
}
