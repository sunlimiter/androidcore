package com.tywho.common.utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.tywho.common.R;
import com.tywho.common.utils.show.LogUtils;
import com.tywho.common.utils.transform.CircleTransform;
import com.tywho.common.utils.transform.RoundTransform;

import java.io.File;

/**
 * Created by limit on 2017/3/29.
 */

public class ImageLoads {

    /**
     * Glide 获取图片 有默认图片及错误图片
     *
     * @param context 上下文对象
     * @param url     图片地址
     * @param view    图片要显示的ImageView
     */
    public static void loadImage(Context context, String url, ImageView view, int placeId, int errorId) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(context)
                .load(StringUtils.isHttp(url) ? url : new File(url))
                .placeholder(placeId == 0 ? R.mipmap.img_normal : placeId)
                .error(errorId == 0 ? R.mipmap.img_normal : errorId)
                .into(view);
    }

    public static void loadImage(Context context, int resourceId, ImageView view, int placeId, int errorId) {
        if (resourceId <= 0) return;
        Glide.with(context)
                .load(resourceId)
                .placeholder(placeId == 0 ? R.mipmap.img_normal : placeId)
                .error(errorId == 0 ? R.mipmap.img_normal : errorId)
                .into(view);
    }

    @SuppressWarnings("unchecked")
    public static void loadImageBitmap(Context context, String url, Target target) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(context)
                .load(StringUtils.isHttp(url) ? url : new File(url))
                .asBitmap()
                .placeholder(R.mipmap.img_normal)
                .error(R.mipmap.img_normal)
                .into(target);
    }

    public static void loadImageView(String url, ImageView view) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(SysUtils.getActivityFromView(view))
                .load(url)
                .thumbnail(0.1f)
                .dontAnimate()
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(800, 800).into(view);
    }

    public static void loadImage(String url, ImageView view) {
        loadImage(SysUtils.getActivityFromView(view), url, view, 0, 0);
    }

    public static void loadImage(int resourceId, ImageView view) {
        loadImage(SysUtils.getActivityFromView(view), resourceId, view, 0, 0);
    }

    @BindingAdapter(value = {"imageSrc", "placeId", "errorId"}, requireAll = false)
    public static void loadImage(ImageView view, String url, int placeId, int errorId) {
        loadImage(SysUtils.getActivityFromView(view), url, view, placeId, errorId);
    }

    @BindingAdapter(value = {"imageSrc", "placeId", "errorId"}, requireAll = false)
    public static void loadImage(ImageView view, int resourceId, int placeId, int errorId) {
        loadImage(SysUtils.getActivityFromView(view), resourceId, view, placeId, errorId);
    }

    /**
     * Gilde 下载图片转换成圆形图片的方法
     *
     * @param context
     * @param url     下砸地址
     * @param view    显示的ImageView
     */
    public static void loadCircleImage(Context context, String url, ImageView view, int plcaeId, int errorId) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(context)
                .load(StringUtils.isHttp(url) ? url : new File(url))
                .placeholder(plcaeId == 0 ? R.mipmap.img_normal : plcaeId)
                .error(errorId == 0 ? R.mipmap.img_normal : errorId)
                .transform(new CircleTransform(context))
                .into(view);
    }

    @BindingAdapter(value = {"imageCircleUrl", "plcaeId", "errorId"}, requireAll = false)
    public static void loadCircleImage(ImageView view, String url, int plcaeId, int errorId) {
        loadCircleImage(SysUtils.getActivityFromView(view), url, view, plcaeId, errorId);
    }

    /**
     * Glide 下载图片并转换成默认圆角角度大小图片的方法 默认为4dp
     *
     * @param context
     * @param url         下载地址
     * @param view        显示的ImageView
     * @param roundRadius 自定义圆角的角度大小
     */
    @SuppressWarnings("unchecked")
    public static void loadRoundTransform(Context context, String url, ImageView view, int roundRadius, int plcaeId, int errorId) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(context)
                .load(StringUtils.isHttp(url) ? url : new File(url))
                .placeholder(plcaeId == 0 ? R.mipmap.img_normal : plcaeId)
                .error(errorId == 0 ? R.mipmap.img_normal : errorId)
                .bitmapTransform(roundRadius == 0 ? new RoundTransform(context, 16, 0) : new RoundTransform(context, roundRadius, 0))
                .into(view);
    }

    /**
     * Glide 下载图片并转换成自定义圆角角度大小图片的方法
     *
     * @param url         下载地址
     * @param view        显示的ImageView
     * @param roundRadius 自定义圆角的角度大小
     */
    @BindingAdapter(value = {"imageRoundUrl", "roundRadius", "plcaeId", "errorId"}, requireAll = false)
    public static void loadRoundTransform(ImageView view, String url, int roundRadius, int plcaeId, int errorId) {
        if (TextUtils.isEmpty(url)) return;
        loadRoundTransform(SysUtils.getActivityFromView(view), url, view, roundRadius, plcaeId, errorId);
    }


    @BindingAdapter("imageFileSrc")
    public static void loadRoundTransform(ImageView view, String imageRoundUrl) {
        LogUtils.d("imageRoundUrl"+imageRoundUrl);
        if (TextUtils.isEmpty(imageRoundUrl)) return;
        Glide.with(SysUtils.getActivityFromView(view))
                .load(new File(imageRoundUrl))
                .into(view);
//        loadRoundTransform(SysUtils.getActivityFromView(view), imageRoundUrl, view, 0, 0, 0);
    }
    @SuppressWarnings("unchecked")
    public static void loadBackground(Context context, int resId, Target target) {
        Glide.with(context)
                .load(resId)
                .into(target);
    }

    /**
     * 释放内存
     *
     * @param context 上下文
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }


    /**
     * 取消所有正在下载或等待下载的任务。
     *
     * @param context 上下文
     */
    public static void cancelAllTasks(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 恢复所有任务
     */
    public static void resumeAllTasks(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context 上下文
     */
    public static void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }


    /**
     * 清除所有缓存
     *
     * @param context 上下文
     */
    public static void cleanAll(Context context) {
        clearDiskCache(context);
        clearMemory(context);
    }
}
