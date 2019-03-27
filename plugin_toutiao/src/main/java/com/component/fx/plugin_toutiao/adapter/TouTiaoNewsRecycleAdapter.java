package com.component.fx.plugin_toutiao.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.component.fx.plugin_base.utils.GlideUtils;
import com.component.fx.plugin_base.utils.TimeUtils;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.bean.MultiNewsArticleBeanData;

import java.util.ArrayList;
import java.util.List;

public class TouTiaoNewsRecycleAdapter extends RecyclerView.Adapter {

    private static final int VIDEO_VIEW_TYPE = 0x001;
    private static final int IMAGE_VIEW_TYPE = 0x002;
    private static final int TEXT_VIEW_TYPE = 0x003;
    private static final String TAG = "TouTiaoNewsRecycleAdapter";

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
        View view = null;
        switch (viewType) {
            case TEXT_VIEW_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.toutiao_fragment_news_text_content, viewGroup, false);
                break;
            case IMAGE_VIEW_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.toutiao_fragment_news_image_right_content, viewGroup, false);
                break;

            case VIDEO_VIEW_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.toutiao_fragment_news_video_content, viewGroup, false);
                break;
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.toutiao_fragment_news_text_content, viewGroup, false);
                break;
        }

        TouTiaoNewsRecycleHolder hodler = new TouTiaoNewsRecycleHolder(view);
        return hodler;
    }

    @Override
    public int getItemViewType(int position) {
        final MultiNewsArticleBeanData data = mList.get(position);
        boolean hasVideo = data.isHas_video();
        Log.d(TAG, "getItemViewType: "+hasVideo + "  position = "+position);
        if (hasVideo) {
            return VIDEO_VIEW_TYPE;
        } else if (data.getImage_list() != null && data.getImage_list().size() > 0) {
            return IMAGE_VIEW_TYPE;

        }
        return TEXT_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

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

            MultiNewsArticleBeanData data = mList.get(position);

            //设置头像
            MultiNewsArticleBeanData.UserInfoBean user_info = data.getUser_info();
            if (user_info != null && !TextUtils.isEmpty(user_info.getAvatar_url())) {
                GlideUtils.loadCircleImage(holder.getContext(), user_info.getAvatar_url(), R.color.toutiao_viewBackground, ivHeader);
            }

            //设置 来源 评论 时间
            String source = data.getSource();
            String comment = data.getComment_count() + "条评论";
            String stampAgo = TimeUtils.getTimeStampAgo(data.getBehot_time() + "");

            tvExtra.setText(source + "  " + comment + "  " + stampAgo);

            //标题
            String title = data.getTitle();
            tvTitle.setText(title);

            ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(holder.getContext(), ivMore, Gravity.END);
                    popupMenu.inflate(R.menu.toutiao_menu_share);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.menu.toutiao_menu_share) {
                                ToastUtil.toast("点击了分享" + position);
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });

            int itemViewType = getItemViewType(position);

            if (itemViewType == TEXT_VIEW_TYPE) {
                //内容 摘要
                String abstractX = data.getAbstractX();
                tvContent.setText(abstractX);
            }

            if (itemViewType == IMAGE_VIEW_TYPE) {
                ImageView ivThumbnail = (ImageView) holder.getViewId(R.id.toutiao_iv_thumbnail);
                GlideUtils.loadImage(holder.getContext(), data.getImage_list().get(0).getUrlX(), R.color.toutiao_viewBackground, ivThumbnail);
            }

            if (itemViewType == VIDEO_VIEW_TYPE) {

                ImageView ivVideo = (ImageView) holder.getViewId(R.id.toutiao_iv_video);
                TextView tvVideoTime = (TextView) holder.getViewId(R.id.toutiao_tv_video_time);

                GlideUtils.loadImageError(holder.getContext(), data.getVideo_detail_info().getDetail_video_large_image().getUrlX(), R.color.toutiao_viewBackground, ivVideo, R.drawable.toutiao_error_image);
                int video_duration = data.getVideo_duration();
                String min = String.valueOf(video_duration / 60);
                String second = String.valueOf(video_duration % 10);
                if (Integer.parseInt(second) < 10) {
                    second = "0" + second;
                }
                String tv_video_time = min + ":" + second;
                tvVideoTime.setText(tv_video_time);
            }

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
