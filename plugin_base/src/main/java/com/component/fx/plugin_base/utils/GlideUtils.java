package com.component.fx.plugin_base.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtils {

    public static void loadImageError(Context context, String url, int resId, ImageView imageView, @DrawableRes int drawableId) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().centerCrop())
                .error(drawableId)
                .placeholder(resId)
                .into(imageView);
    }

    public static void loadImage(Context context, String url, int resId, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().centerCrop())
                .placeholder(resId)
                .into(imageView);
    }

    public static void loadCircleImage(Context context, String url, int resId, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(resId)
                .into(imageView);
    }
}
