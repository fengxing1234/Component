package com.component.fx.plugin_toutiao.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.component.fx.plugin_base.base.recycle.BaseHolder;
import com.component.fx.plugin_base.base.recycle.MultiItemBaseAdapter;
import com.component.fx.plugin_base.utils.GlideUtils;
import com.component.fx.plugin_base.utils.TimeUtils;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.bean.WendaArticleBeanData;

public class TouTiaoMultiNewsAdapter extends MultiItemBaseAdapter<WendaArticleBeanData> {


    @Override
    protected void convert(@NonNull BaseHolder baseHolder, WendaArticleBeanData data, final int position) {
        try {
            int itemViewType = baseHolder.getItemViewType();

            TextView tvTitle = (TextView) baseHolder.getView(R.id.toutiao_tv_title);
            ImageView ivDots = (ImageView) baseHolder.getView(R.id.toutiao_iv_dots);
            TextView tvAnswerCount = (TextView) baseHolder.getView(R.id.toutiao_tv_answer_count);
            TextView tvTime = (TextView) baseHolder.getView(R.id.toutiao_tv_time);

            tvTitle.setText(data.getQuestionBean().getTitle());
            tvAnswerCount.setText((data.getQuestionBean().getNormal_ans_count() + data.getQuestionBean().getNice_ans_count()) + "回答");
            String tv_datetime = data.getQuestionBean().getCreate_time() + "";
            if (!TextUtils.isEmpty(tv_datetime)) {
                tv_datetime = TimeUtils.getTimeStampAgo(tv_datetime);
            }
            tvTime.setText(tv_datetime);
            ivDots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.toast("点击了"+position);
                }
            });


            switch (itemViewType) {
                case WendaArticleBeanData.WENDA_ONE_IMG_TYPE:
                    ImageView ivImageBig = (ImageView) baseHolder.getView(R.id.toutiao_iv_image_big);
                    String url = data.getExtraBean().getWenda_image().getLarge_image_list().get(0).getUrl();
                    GlideUtils.loadImage(mContext, url, R.color.toutiao_viewBackground, ivImageBig);
                    break;
                case WendaArticleBeanData.WENDA_THREE_IMG_TYPE:
                    ImageView iv0 = baseHolder.getView(R.id.toutiao_iv_0);
                    ImageView iv1 = baseHolder.getView(R.id.toutiao_iv_1);
                    ImageView iv2 = baseHolder.getView(R.id.toutiao_iv_2);

                    int size = data.getExtraBean().getWenda_image().getThree_image_list().size();
                    String[] ivs = new String[size];
                    for (int i = 0; i < size; i++) {
                        ivs[i] = data.getExtraBean().getWenda_image().getThree_image_list().get(i).getUrl();
                    }
                    switch (ivs.length) {
                        case 1:
                            GlideUtils.loadImage(mContext, ivs[0], R.color.toutiao_viewBackground, iv0);
                            break;
                        case 2:
                            GlideUtils.loadImage(mContext, ivs[0], R.color.toutiao_viewBackground, iv0);
                            GlideUtils.loadImage(mContext, ivs[1], R.color.toutiao_viewBackground, iv1);
                            break;
                        case 3:
                            GlideUtils.loadImage(mContext, ivs[0], R.color.toutiao_viewBackground, iv0);
                            GlideUtils.loadImage(mContext, ivs[1], R.color.toutiao_viewBackground, iv1);
                            GlideUtils.loadImage(mContext, ivs[2], R.color.toutiao_viewBackground, iv2);
                            break;
                    }

                    break;
                case WendaArticleBeanData.WENDA_TEXT_TYPE:
                    TextView tvContent = (TextView) baseHolder.getView(R.id.toutiao_tv_content);
                    tvContent.setText(data.getAnswerBean().getAbstractX());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
