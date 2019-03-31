package com.component.fx.plugin_toutiao.adapter;

import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.component.fx.plugin_base.base.recycle.BaseHolder;
import com.component.fx.plugin_base.base.recycle.provider.BaseItemProvider;
import com.component.fx.plugin_base.utils.GlideUtils;
import com.component.fx.plugin_base.utils.TimeUtils;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.bean.MultiNewsArticleBeanData;

public class TouTiaoNewsTextProvider extends BaseItemProvider<MultiNewsArticleBeanData> {
    @Override
    public int viewType() {
        return TouTiaoMultiNewRvAdapter.TEXT_ITEM_PROVIDER;
    }

    @Override
    public int getLayout() {
        return R.layout.toutiao_fragment_news_text_content;
    }

    @Override
    public void convert(final BaseHolder holder, MultiNewsArticleBeanData data, final int position) {
        //头像
        ImageView ivHeader = (ImageView) holder.getView(R.id.toutiao_iv_header);
        //来源 评论 时间
        TextView tvExtra = (TextView) holder.getView(R.id.toutiao_tv_extra);
        //更多
        final ImageView ivMore = (ImageView) holder.getView(R.id.toutiao_iv_more);
        //标题
        TextView tvTitle = (TextView) holder.getView(R.id.toutiao_tv_title);
        //内容
        TextView tvContent = (TextView) holder.getView(R.id.toutiao_tv_content);


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

        //内容 摘要
        String abstractX = data.getAbstractX();
        tvContent.setText(abstractX);

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


    }
}
