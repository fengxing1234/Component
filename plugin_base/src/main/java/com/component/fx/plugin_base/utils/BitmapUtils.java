package com.component.fx.plugin_base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {


    /**
     * 获取bitmap从资源文件中
     * 资源文件(drawable/mipmap/raw):
     *
     * @param context
     * @param idRes
     * @return
     */
    public static Bitmap getBitmapForResource(Context context, @DrawableRes int idRes) {
        return BitmapFactory.decodeResource(context.getResources(), idRes);
    }

    /**
     * 从资源文件中获取bitmap
     * 资源文件(assets)
     *
     * @param fileName
     * @return
     */
    public static Bitmap getBitmapForAssets(Context context, String fileName) {
        InputStream open = null;
        Bitmap bitmap = null;
        try {
            open = context.getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(open);
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 从本地文件中获取
     *
     * @param absolutePath
     * @return
     */
    public static Bitmap getBitmapForLocal(String absolutePath) {
        return BitmapFactory.decodeFile(absolutePath);
    }

    /**
     * 网络文件
     *
     * @param is
     * @return
     */
    public static Bitmap getBitmapForInput(InputStream is) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
