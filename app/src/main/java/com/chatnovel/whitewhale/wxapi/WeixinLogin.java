package com.chatnovel.whitewhale.wxapi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chatnovel.whitewhale.R;
import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import com.chatnovel.whitewhale.common.Constant;
import com.chatnovel.whitewhale.common.WWInterface;
import com.chatnovel.whitewhale.model.ShareInfo;
import com.chatnovel.whitewhale.module.mycenter.NotifyUtil;
import com.chatnovel.whitewhale.network.HttpLogin;
import com.chatnovel.whitewhale.sp.SharePreferenceKey;
import com.chatnovel.whitewhale.sp.WWSharePreference;
import com.chatnovel.whitewhale.utils.FetchResourceUtil;
import com.chatnovel.whitewhale.utils.ImageUtil;
import com.chatnovel.whitewhale.utils.UIUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Wyatt on 2017/6/6/006.
 */

public class WeixinLogin {

    private volatile static WeixinLogin singleton;
    private Context mContext;
    private IWXAPI api;
    public static WeixinLogin getInstance(Context context) {
        if (singleton == null) {
            synchronized (WeixinLogin.class) {
                if (singleton == null) {
                    singleton = new WeixinLogin();
                }
            }
        }
        if (!(context instanceof WXEntryActivity)) {
            singleton.mContext = context;
        }
        return singleton;
    }

    public WeixinLogin() {
        api = WXAPIFactory.createWXAPI(WhiteWhaleApplication.applicationContext, WXEntryActivity.WX_APP_ID, true);
        api.registerApp(WXEntryActivity.WX_APP_ID);
    }

    /**
     * 发送微信登陆请求
     */
    public void sendWeChatLogin() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(WhiteWhaleApplication.applicationContext, WXEntryActivity.WX_APP_ID, true);
            api.registerApp(WXEntryActivity.WX_APP_ID);
        }
        if (! api.isWXAppInstalled()) {
            UIUtil.toastMessage(mContext, "请先安装微信");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "carjob_wx_login";
        api.sendReq(req);
    }


    // 分享 微信
    public void shareToWeixin(final ShareInfo shareInfo) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(WhiteWhaleApplication.applicationContext, WXEntryActivity.WX_APP_ID, true);
            api.registerApp(WXEntryActivity.WX_APP_ID);
        }
        if (! api.isWXAppInstalled()) {
            UIUtil.toastMessage(mContext, "请先安装微信");
            return;
        }
        Glide.with(WhiteWhaleApplication.applicationContext).load(shareInfo.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap logoBitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                if (logoBitmap == null) {
                    logoBitmap = FetchResourceUtil.fetchBitmap(mContext.getResources(),
                            R.mipmap.app_icon);
                }
                uploadFriend(shareInfo, logoBitmap);

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Bitmap logoBitmap = FetchResourceUtil.fetchBitmap(mContext.getResources(),
                        R.mipmap.app_icon);
                uploadFriend(shareInfo,logoBitmap);
            }
        });
    }

    private void uploadFriend(ShareInfo shareInfo, Bitmap logoBitmap) {
        WXWebpageObject novelpageObj = new WXWebpageObject();
        WXMediaMessage msg = new WXMediaMessage(novelpageObj);
        novelpageObj.webpageUrl = shareInfo.getShareUrl();
        msg.title = shareInfo.getTitle();
        if (!TextUtils.isEmpty(shareInfo.getShareText())) {
            msg.description = shareInfo.getShareText();
        }else{
            msg.description = "我最近在读的小说，推荐给你";
        }
        if (logoBitmap != null) {
            Bitmap thumbBmp = ImageUtil.centerSquareScaleBitmap(logoBitmap, Constant.WXData.THUMB_WIDTH_SIZE);
            msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene = SendMessageToWX.Req.WXSceneSession; // for wechat friends
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        api.sendReq(req);
    }

    // 朋友圈
    public void shareToPengyouquan(final ShareInfo shareInfo) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(WhiteWhaleApplication.applicationContext, WXEntryActivity.WX_APP_ID, true);
            api.registerApp(WXEntryActivity.WX_APP_ID);
        }
        if (! api.isWXAppInstalled()) {
            UIUtil.toastMessage(mContext, "请先安装微信");
            return;
        }
        if (api.getWXAppSupportAPI() < Constant.WXData.PENG_YOU_QUAN_MIN_VERSION_CODE) {
            Toast.makeText(mContext,
                    "微信版本过低，请更新至4.2或更高版本！",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Glide.with(WhiteWhaleApplication.applicationContext).load(shareInfo.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap logoBitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                if (logoBitmap == null) {
                    logoBitmap = FetchResourceUtil.fetchBitmap(mContext.getResources(),
                            R.mipmap.app_icon);
                }
                uploadPengyouquan(shareInfo,logoBitmap);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Bitmap logoBitmap = FetchResourceUtil.fetchBitmap(mContext.getResources(),
                        R.mipmap.app_icon);
                uploadPengyouquan(shareInfo,logoBitmap);
            }
        });
    }

    private void uploadPengyouquan(ShareInfo shareInfo,Bitmap logoBitmap) {
        WXWebpageObject novelpageObj = new WXWebpageObject();
        novelpageObj.webpageUrl = shareInfo.getShareUrl();
        WXMediaMessage msg = new WXMediaMessage(novelpageObj);
        StringBuilder showTitle = new StringBuilder();
        showTitle.append(shareInfo.getTitle());
        if (!TextUtils.isEmpty(shareInfo.getShareText())) {
            showTitle.append(":");
            showTitle.append(shareInfo.getShareText());
        }
        msg.title = showTitle.toString();
        msg.description = "这篇小说太赞了！";
        if (logoBitmap != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(logoBitmap, Constant.WXData.THUMB_WIDTH_SIZE, Constant.WXData.THUMB_HEIGHT_SIZE, true);
            msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene = SendMessageToWX.Req.WXSceneTimeline; // for pengyou quan
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        api.sendReq(req);
    }



    /**
     * 微信登录，只需要传入code,剩余的工作服务端完成，
     * @param code
     */
    public void wxCodeLogin(String code) {
        HttpLogin.weixinLogin(code, new WWInterface.IString() {
            @Override
            public void onResult(String token) {
                WWSharePreference.setSharedPreferencesValueToString(SharePreferenceKey.SP_KEY_TOKEN,token,mContext);
                if (!TextUtils.isEmpty(token)) {
                    NotifyUtil.successLogin();
                } else {
                    NotifyUtil.errorLogin("登录失败");
                }
            }
        });
    }
}
