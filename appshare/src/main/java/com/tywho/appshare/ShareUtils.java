package com.tywho.appshare;

import android.app.Activity;
import android.content.Intent;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

/**
 * Created by limit on 2017/5/8/0008.
 *
 */

public class ShareUtils {


    private SHARE_MEDIA[] displaylist;
    private ShareCallBackListener shareCallBackListener;
    private String text;
    private Activity activity;
    private UMImage umImage;
    private UMWeb umWeb;

    public ShareUtils(Builder builder) {
        this.activity = builder.activity;
        this.displaylist = builder.displaylist;
        this.shareCallBackListener = builder.shareCallBackListener;
        this.text = builder.text;
        this.umImage = builder.umImage;
        this.umWeb = builder.umWeb;
    }

    public void share() {
        ShareBoardConfig config = new ShareBoardConfig();
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
        config.setCancelButtonVisibility(false);
        config.setIndicatorVisibility(false);
        new ShareAction(activity)
                .withText(text)
                .withMedia(umImage)
                .setDisplayList(displaylist)
                .withMedia(umWeb)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        if (shareCallBackListener != null)
                            shareCallBackListener.onStart(share_media);
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        if (shareCallBackListener != null)
                            shareCallBackListener.onResult(share_media);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        if (shareCallBackListener != null)
                            shareCallBackListener.onError(share_media, throwable);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        if (shareCallBackListener != null)
                            shareCallBackListener.onCancel(share_media);
                    }
                }).open(config);
    }

    public void shareSingle() {
        new ShareAction(activity)
                .withText(text)
                .withMedia(umImage)
                .setPlatform(displaylist[0])
                .withMedia(umWeb)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        if (shareCallBackListener != null)
                            shareCallBackListener.onStart(share_media);
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        if (shareCallBackListener != null)
                            shareCallBackListener.onResult(share_media);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        if (shareCallBackListener != null)
                            shareCallBackListener.onError(share_media, throwable);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        if (shareCallBackListener != null)
                            shareCallBackListener.onCancel(share_media);
                    }
                }).share();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }

    public void release() {
        UMShareAPI.get(activity).release();
    }

    public interface ShareCallBackListener {
        public void onStart(SHARE_MEDIA share_media);

        public void onResult(SHARE_MEDIA share_media);

        public void onError(SHARE_MEDIA share_media, Throwable throwable);

        public void onCancel(SHARE_MEDIA share_media);
    }

    public static class Builder {
        private Activity activity;
        private SHARE_MEDIA[] displaylist = {SHARE_MEDIA.QQ,SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SMS, SHARE_MEDIA.MORE};
        private String text;
        private ShareCallBackListener shareCallBackListener;
        private UMImage umImage;//图片
        private UMWeb umWeb;//链接

        public Builder() {
        }

        public ShareUtils build() {
            return new ShareUtils(this);
        }

        public Builder activity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder displayList(SHARE_MEDIA... list) {
            this.displaylist = list;
            return this;
        }

        public Builder shareCallBackListener(ShareCallBackListener shareCallBackListener) {
            this.shareCallBackListener = shareCallBackListener;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder umImage(UMImage umImage) {
            this.umImage = umImage;
            return this;
        }

        public Builder umWeb(UMWeb umWeb) {
            this.umWeb = umWeb;
            return this;
        }
    }
}
