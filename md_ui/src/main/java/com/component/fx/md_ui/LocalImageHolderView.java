package com.component.fx.md_ui;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

public class LocalImageHolderView extends Holder<Integer> {

    private ImageView ivBannerImg;

    public LocalImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        ivBannerImg = (ImageView) itemView.findViewById(R.id.iv_banner_img);
    }

    @Override
    public void updateUI(Integer data) {
        ivBannerImg.setImageResource(data);
    }
}
