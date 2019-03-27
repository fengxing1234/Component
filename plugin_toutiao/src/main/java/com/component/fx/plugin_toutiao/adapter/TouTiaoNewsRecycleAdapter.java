package com.component.fx.plugin_toutiao.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.component.fx.plugin_base.utils.TimeUtils;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.bean.MultiNewsArticleBeanData;

import java.util.ArrayList;
import java.util.List;

public class TouTiaoNewsRecycleAdapter extends RecyclerView.Adapter {

    private List<MultiNewsArticleBeanData> mList = new ArrayList<>();


    public void setData(List<MultiNewsArticleBeanData> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<MultiNewsArticleBeanData> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.toutiao_fragment_new_text_content, viewGroup, false);
        TouTiaoNewsRecycleHolder hodler = new TouTiaoNewsRecycleHolder(view);
        return hodler;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        if (viewHolder instanceof TouTiaoNewsRecycleHolder) {
            final TouTiaoNewsRecycleHolder holder = (TouTiaoNewsRecycleHolder) viewHolder;

            //头像
            ImageView ivHeader = (ImageView) holder.getViewId(R.id.toutiao_iv_header);
            //来源 评论 时间
            TextView tvExtra = (TextView) holder.getViewId(R.id.toutiao_tv_extra);
            //更多
            final ImageView ivMore = (ImageView) holder.getViewId(R.id.toutiao_iv_more);
            //标题
            TextView tvTitle = (TextView) holder.getViewId(R.id.toutiao_tv_title);
            //内容
            TextView tvContent = (TextView) holder.getViewId(R.id.toutiao_tv_content);

            MultiNewsArticleBeanData data = mList.get(i);

            //设置头像
            MultiNewsArticleBeanData.UserInfoBean user_info = data.getUser_info();
            if (user_info != null && !TextUtils.isEmpty(user_info.getAvatar_url())) {
                Glide.with(holder.getContext())
                        .load(user_info.getAvatar_url())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(ivHeader);
            }

            //设置 来源 评论 时间
            String source = data.getSource();
            String comment = data.getComment_count() + "条评论";
            String stampAgo = TimeUtils.getTimeStampAgo(data.getBehot_time() + "");

            tvExtra.setText(source + "  " + comment + "  " + stampAgo);

            //标题
            String title = data.getTitle();
            tvTitle.setText(title);

            //内容 摘要
            final String abstractX = data.getAbstractX();
            tvContent.setText(abstractX);

            ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(holder.mItemView.getContext(), ivMore, Gravity.END);
                    popupMenu.inflate(R.menu.toutiao_menu_share);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.menu.toutiao_menu_share) {
                                ToastUtil.toast("点击了分享" + i);
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static class TouTiaoNewsRecycleHolder extends RecyclerView.ViewHolder {

        private View mItemView;

        public TouTiaoNewsRecycleHolder(@NonNull View itemView) {
            super(itemView);
            this.mItemView = itemView;
        }

        public Context getContext() {
            return mItemView.getContext();
        }

        public View getViewId(@IdRes int resId) {
            if (mItemView == null) return null;

            return mItemView.findViewById(resId);
        }
    }
}
