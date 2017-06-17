package com.chatnovel.whitewhale.weiboapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chatnovel.whitewhale.R;
import com.chatnovel.whitewhale.base.BaseActivity;
import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import com.chatnovel.whitewhale.common.IntentKey;
import com.chatnovel.whitewhale.model.ShareInfo;
import com.chatnovel.whitewhale.module.mycenter.NotifyUtil;
import com.chatnovel.whitewhale.utils.FetchResourceUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;


public class SinaWeiboActivity extends BaseActivity implements WbShareCallback {
    private String shareWeiboText;
    private Bitmap bmp;
    /**
     * 微博微博分享接口实例
     */
    private WbShareHandler shareHandler ;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        share();
    }

    public void share() {
        ShareInfo beansInfo = (ShareInfo) getIntent().getSerializableExtra(IntentKey.SHARE_INFO);
        if (beansInfo == null) {
            NotifyUtil.errorShare("分享失败");
            return;
        }
        shareWeiboText = beansInfo.getShareText();
        Glide.with(WhiteWhaleApplication.applicationContext).load(beansInfo.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap logoBitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                if (logoBitmap == null) {
                    logoBitmap = FetchResourceUtil.fetchBitmap(getResources(),
                            R.mipmap.app_icon);
                }
                bmp=logoBitmap;
                // 创建微博分享接口实例
                shareHandler = new WbShareHandler(SinaWeiboActivity.this);
                shareHandler.registerApp();
                // 如果未安装微博客户端，设置下载微博对应的回调
                sendMessage();
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                bmp = FetchResourceUtil.fetchBitmap(getResources(),
                        R.mipmap.app_icon);
                // 创建微博分享接口实例
                shareHandler = new WbShareHandler(SinaWeiboActivity.this);
                shareHandler.registerApp();
                // 如果未安装微博客户端，设置下载微博对应的回调
                sendMessage();
            }
        });
    }

    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        shareHandler.doResultIntent(intent, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link } < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     */
    private void sendMessage() {
        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = getTextObj();
        weiboMessage.mediaObject = getImageObj();
        // 3. 发送请求消息到微博，唤起微博分享界面
        shareHandler.shareMessage(weiboMessage,false);
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = shareWeiboText;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        if (bmp != null) {
            imageObject.setImageObject(bmp);
        }
        return imageObject;
    }


    @Override
    public void onWbShareSuccess() {
        NotifyUtil.successShare();
        finish();
    }

    @Override
    public void onWbShareCancel() {
        NotifyUtil.errorShare("取消分享");
        finish();
    }

    @Override
    public void onWbShareFail() {
        NotifyUtil.errorShare("分享失败");
        finish();
    }
}