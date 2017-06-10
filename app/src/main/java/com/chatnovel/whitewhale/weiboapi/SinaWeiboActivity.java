//package com.chatnovel.whitewhale.weiboapi;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.chatnovel.whitewhale.base.BaseActivity;
//import com.sina.weibo.sdk.WbSdk;
//import com.sina.weibo.sdk.WeiboAppManager;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.auth.AuthInfo;
//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//import com.sina.weibo.sdk.net.RequestListener;
//import com.sina.weibo.sdk.share.WbShareCallback;
//import com.sina.weibo.sdk.share.WbShareHandler;
//import com.taptech.common.util.CacheUtil;
//import com.taptech.common.util.FetchResourceUtil;
//import com.taptech.common.util.HttpRequestUtil;
//import com.taptech.common.util.TTLog;
//import com.taptech.doufu.R;
//import com.taptech.doufu.base.DiaobaoBaseActivity;
//import com.taptech.doufu.base.WeMediaApplication;
//import com.taptech.doufu.comment.beans.CommentDataBean;
//import com.taptech.doufu.data.Constant;
//import com.taptech.doufu.data.MyAccountOperation;
//import com.taptech.doufu.info.ShareBeansInfo;
//import com.taptech.doufu.listener.HttpResponseListener;
//import com.taptech.doufu.personalCenter.services.WeiboService;
//import com.taptech.doufu.services.HomeMainServices;
//import com.taptech.doufu.ugc.services.UGCMainService;
//import com.taptech.doufu.util.DiaobaoUtil;
//import com.taptech.doufu.util.ImageManager;
//import com.taptech.doufu.util.UIUtil;
//import com.taptech.doufu.util.httputils.HttpResponseObject;
//import com.taptech.doufu.view.WaitDialog;
//import com.taptech.personal.util.WMUrlRequest_1_1;
//import com.umeng.analytics.MobclickAgent;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//
//public class SinaWeiboActivity extends BaseActivity implements OnClickListener, WbShareCallback {
//
//    //	private final String CONSUMER_KEY = "260183038";// 替换为开发者的appkey，例如"1646212860";
//    private final String REDIRECT_URL = "http://www.sharereader.cn";
//    private Oauth2AccessToken accessToken;
//    private RequestListener weiboListener;
//
//    private String shareWeiboText = "";
//    private String imgPath = "";
//    private String mPaperId = "0";
//    private int flag;
//    private String title = "";
//    private String des = "";
//
//    private ImageView mClose;
//    private TextView mSent;
//    private EditText etMessage;
//    private RelativeLayout shareLayout;
//    private ImageView showShareImg;
//
//    private Boolean isImgPaht = false;
//    private WaitDialog dialog;
//    Bitmap bmp = null;
//
//    /**
//     * 微博微博分享接口实例
//     */
//    private WbShareHandler mWeiboShareAPI = null;
//    private ShareBeansInfo beansInfo;
//
//    @SuppressLint("NewApi")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_share_weibo);
//        creatUi();
//    }
//
//    public void creatUi() {
//        try {
//            mClose = (ImageView) findViewById(R.id.iv_weibo_close);
//            mClose.setOnClickListener(this);
//            mSent = (TextView) findViewById(R.id.iv_weibo_send);
//            mSent.setOnClickListener(this);
//            etMessage = (EditText) findViewById(R.id.et_weibo_msg);
//            showShareImg = (ImageView) findViewById(R.id.iv_weibo_sharephoto);
//            shareLayout = (RelativeLayout) findViewById(R.id.rl_weibo_share_layout);
//
//            Intent intent = getIntent();
//            if (intent != null) {
//
//                beansInfo = (ShareBeansInfo) intent
//                        .getSerializableExtra("shareInfo");
//                switch (flag) {
//                    case ShareBeansInfo.AETICLE_SHARE_FLAG:
//                        mPaperId = beansInfo.getId();
//                        shareWeiboText = beansInfo.getShartText();
//                        imgPath = ImageManager.getCacheFilePath(beansInfo
//                                .getImagUrl());
//                        isImgPaht = true;
//                        break;
//                    case ShareBeansInfo.OWN_SHARE_FLAG:
//                        shareWeiboText = beansInfo.getShartText();
//                        break;
//                    case ShareBeansInfo.PHOTO_SHARE_FLAG:
//                        shareWeiboText = beansInfo.getShartText();
//                        imgPath = ImageManager.getCacheFilePath(beansInfo
//                                .getImagUrl());
//                        isImgPaht = true;
//                        break;
//                    case 3:
//                        imgPath = intent.getStringExtra("imgPath");
//                        shareWeiboText = "我在#豆腐#里面发现这张图屌炸天了！更多宅腐内容尽在豆腐，你们也快来下载吧！http://www.diaobao.la/down.php?ref=wb";
//                        isImgPaht = true;
//                        break;
//                    case 4:
//                        imgPath = intent.getStringExtra("imgPath");
//                        shareWeiboText = "#豆腐撸妹子#我觉得这个妹子不错，大家觉得这个妹子值多少分？分享自@豆腐客户端 http://www.diaobao.la/down.php?ref=wb";
//                        isImgPaht = true;
//                        break;
//                    case 5:
//                        shareWeiboText = "#豆腐撸历#大家看看我今天刚算的撸历！太有意思了！你们撸之前也下载#豆腐#算一下吧！正所谓“撸前算宜忌，番号你懂的。 ”@豆腐客户端 下载地址：http://www.diaobao.la/down.php?ref=wb";
//                        imgPath = Constant.AppDir.DIR_CACHE_IMAGE
//                                + "/dateshare.png";
//                        shareLayout.setVisibility(View.VISIBLE);
//                        bmp = CacheUtil.readBitmapFromCache(new File(imgPath));
//                        showShareImg.setImageBitmap(bmp);
//                        isImgPaht = true;
//                        break;
//
//                    case ShareBeansInfo.WOED_SHARE_FlAG:
//                        shareWeiboText = beansInfo.getShartText();
//                        break;
//                    case ShareBeansInfo.VIDEO_SHARE_FLAG:
//                        if (beansInfo.getTitle() != null) {
//                            title = beansInfo.getTitle();
//                        }
//                        if (beansInfo.getDescription() != null) {
//                            des = beansInfo.getDescription();
//                        }
//                        shareWeiboText = beansInfo.getShartText();
//                        if (beansInfo.getShareData() != null) {
//                            imgPath = beansInfo.getShareData().getShareUrl();
//                        } else {
//                            imgPath = beansInfo.getShareUrl();
//                        }
//                        if (imgPath == null) {
//                            imgPath = Constant.DOWN_URL_WB;
//                        }
//                        break;
//                    case ShareBeansInfo.TOPIC_SHARE_FLAG:
//                        shareWeiboText = beansInfo.getShartText();
//                        TTLog.s("beansInfo.getImagUrl()==" + beansInfo.getImagUrl());
//                        isImgPaht = beansInfo.getImagUrl() != null;
//                        if (isImgPaht) {
//                            imgPath = ImageManager.getCacheFilePath(beansInfo
//                                    .getImagUrl());
//                        }
//
//                        break;
//                    case ShareBeansInfo.NOVEL_SHARE_FLAG:
//                        shareWeiboText = beansInfo.getShartText();
//                        isImgPaht = beansInfo.getImagUrl() != null;
//                        if (isImgPaht) {
//                            String url = Constant.LOGO_URL;
//                            if (!TextUtils.isEmpty(beansInfo.getImagUrl())) {
//                                url = beansInfo.getImagUrl();
//                            }
//                            Glide.with(WeMediaApplication.applicationContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
//                                @Override
//                                public void onResourceReady(Bitmap logoBitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                                    imgPath = ImageManager.getCacheFilePath(beansInfo
//                                            .getImagUrl());
//                                    shareLayout.setVisibility(View.VISIBLE);
//                                    if (logoBitmap == null) {
//                                        logoBitmap = FetchResourceUtil.fetchBitmap(getResources(),
//                                                R.drawable.new_icon);
//                                    }
//                                    showShareImg.setImageBitmap(logoBitmap);
//                                    etMessage.setText(shareWeiboText);
//                                    // 创建微博分享接口实例
//                                    mWeiboShareAPI = new WbShareHandler(SinaWeiboActivity.this);
//                                    mWeiboShareAPI.registerApp();
//                                    // 如果未安装微博客户端，设置下载微博对应的回调
//
////                                    if (!mWeiboShareAPI.isWeiboAppInstalled()) {
////                                        mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
////                                            @Override
////                                            public void onCancel() {
////                                                Toast.makeText(SinaWeiboActivity.this,
////                                                        R.string.weibosdk_demo_cancel_download_weibo,
////                                                        Toast.LENGTH_SHORT).show();
////                                            }
////                                        });
////                                    }
//                                    try {
//                                        WbSdk.checkInit();
//                                        sendMessage(isImgPaht, false);
//                                    } catch (Exception e) {
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(SinaWeiboActivity.this);
//                                        builder.setIcon(R.drawable.ic_launcher);
//                                        builder.setTitle("下载提示");
//                                        builder.setMessage("您还没有安装微博，是否要下载安装？");
//
//                                        /**左边按钮*/
//                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                WbSdk.install(SinaWeiboActivity.this,new AuthInfo(SinaWeiboActivity.this,Constant.APP_KEY, Constant.REDIRECT_URL,
//                                                        WeiboService.SCOPE));
//                                            }
//                                        });
//                                        /**右边按钮*/
//                                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            // TODO Auto-generated method stub
//                                                Toast.makeText(SinaWeiboActivity.this,
//                                                        R.string.weibosdk_demo_cancel_download_weibo,
//                                                        Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//
//                                    }
////                                    // 检查微博客户端环境是否正常，如果未安装微博，弹出对话框询问用户下载微博客户端
////                                    if (!Constant.EXIST_SINA_WEIBO) {
////                                        try {
////                                            // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
////                                            // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
////                                            // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
////                                            mWeiboShareAPI.registerApp();
////                                            existSinaWeibo = mWeiboShareAPI.checkEnvironment(true);
////                                            if (!existSinaWeibo) {
////                                                Constant.EXIST_SINA_WEIBO = true;//不重复询问
////                                            }
////                                        } catch (WeiboShareException e) {
////                                            e.printStackTrace();
////                                            Toast.makeText(SinaWeiboActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
////                                        }
////                                    }
////                                    if (existSinaWeibo) {//本地有新浪微博客户端
////                                        sendMessage(isImgPaht, false);
////                                    }
//                                }
//                            });
//                            return;
//                        }
//                        break;
//                    case ShareBeansInfo.SWEEP_SHARE_FLAG:
//                        shareWeiboText = beansInfo.getShartText();
//                        break;
//                    case ShareBeansInfo.RANK_SORCE_SHARE_FLAG:
//                        shareWeiboText = beansInfo.getShartText();
//                        break;
//                    case ShareBeansInfo.NOTE_SHARE_FLAG:
//                        shareWeiboText = beansInfo.getShartText();
//                        TTLog.d("tag", "beanInfo share image is==============" + beansInfo.getImagUrl());
//                        isImgPaht = beansInfo.getImagUrl() != null;
//                        if (isImgPaht) {
//                            imgPath = ImageManager.getCacheFilePath(beansInfo
//                                    .getImagUrl());
//                        }
//                        break;
//                    default:
//                        shareWeiboText = beansInfo.getShartText();
//                        break;
//                }
//
//                if (isImgPaht && flag != 5) {
//                    shareLayout.setVisibility(View.VISIBLE);
//                    bmp = ImageManager.getCacheBitMap(beansInfo.getImagUrl());
//                    if (bmp == null) {
//                        bmp = FetchResourceUtil.fetchBitmap(getResources(),
//                                R.drawable.new_icon);
//                    }
//                    showShareImg.setImageBitmap(bmp);
//
//                }
//                etMessage.setText(shareWeiboText);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 创建微博分享接口实例
//        mWeiboShareAPI = new WbShareHandler(this);
//        mWeiboShareAPI.registerApp();
//        // 如果未安装微博客户端，设置下载微博对应的回调
////        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
////            mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
////                @Override
////                public void onCancel() {
////                    Toast.makeText(SinaWeiboActivity.this,
////                            R.string.weibosdk_demo_cancel_download_weibo,
////                            Toast.LENGTH_SHORT).show();
////                }
////            });
////        }
////        // 检查微博客户端环境是否正常，如果未安装微博，弹出对话框询问用户下载微博客户端
////        if (!Constant.EXIST_SINA_WEIBO) {
////            try {
////                // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
////                // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
////                // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
////                mWeiboShareAPI.registerApp();
////
////                existSinaWeibo = mWeiboShareAPI.checkEnvironment(true);
////                if (!existSinaWeibo) {
////                    Constant.EXIST_SINA_WEIBO = true;//不重复询问
////                }
////            } catch (WeiboShareException e) {
////                e.printStackTrace();
////                Toast.makeText(SinaWeiboActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
////            }
////        }
//        if (existSinaWeibo) {//本地有新浪微博客户端
//            sendMessage(isImgPaht, false);
//        }
//
//    }
//
//    /**
//     * @see {@link Activity#onNewIntent}
//     */
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
//        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
//        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
//        mWeiboShareAPI.doResultIntent(intent, this);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//        switch (v.getId()) {
//            case R.id.iv_weibo_close:
//                finish();
//                break;
//            case R.id.iv_weibo_send:
//                ShareSinaWeb();
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    private void ShareSinaWeb() {
//        //提示下载微博
//
////        weiboListener = new RequestListener() {
////
////            @Override
////            public void onComplete(String response) {
////                // TODO Auto-generated method stub
////                TTLog.s("发送微博成功");
////                mHandler.sendEmptyMessage(1);
////            }
////
////            @Override
////            public void onWeiboException(WeiboException e) {
////                TTLog.s("发送微博失败" + e.getMessage());
////                mHandler.sendEmptyMessage(0);
////            }
////        };
////        accessToken = AccessTokenKeep.readAccessToken(this);
////        TTLog.s("accessToken.isSessionValid()" + accessToken.isSessionValid());
////
////        if (!accessToken.isSessionValid()) {
////            try {
////                WeiboService.getInstance().ssoAuthorize(SinaWeiboActivity.this, false);
////            } catch (Exception e) {
////
////            }
////        } else {
////            dialog = new WaitDialog(SinaWeiboActivity.this,
////                    R.style.updateDialog, "正在发送");
////            dialog.show();
////            com.sina.weibo.sdk.web.client.ShareWebViewClient api = new StatusesAPI(accessToken);
////            if (isImgPaht) {
////                api.upload(shareWeiboText, imgPath, "0.00", "0.00",
////                        weiboListener);
////            } else {
////                api.update(shareWeiboText, "0.00", "0.00", weiboListener);
////            }
////        }
//    }
//
//    @Override
//    public void handleResponse(int handleType, HttpResponseObject response) {
//        if (handleType == UGCMainService.HANDLE_TYPE_LOAD_SHARE_SCORE) {
//            try {
//                if (response.getStatus() == 0) {
//                    UIUtil.dismissDialog();
//                    CommentDataBean scrore = new CommentDataBean();
//                    scrore.setJson(((JSONObject) response.getData()).getJSONObject("score_result"));
//                    int commentScore = Integer.valueOf(scrore.getScore());
//                    if (commentScore != 0) {
//                        UIUtil.toastMessage(SinaWeiboActivity.this, "分享成“攻” +5豆子");
//                    } else {
//                        UIUtil.toastMessage(SinaWeiboActivity.this, "分享成“攻”" + Constant.REPLY_TODAY_FULL);
//                    }
//                    //回调，给后台数据
//                    setResult(666, getIntent());
//                } else {
//                    UIUtil.toastMessage(SinaWeiboActivity.this, DiaobaoUtil.getErrorTips(response));
//                }
//                finish();
//            } catch (JSONException e) {
//                e.printStackTrace();
//                finish();
//            }
//
//        } else {
//            if (response.getStatus() == 0) {
//                UIUtil.toastMessage(this, "授权成功");
//                mHandler.sendEmptyMessage(NEW_WEIBO_SHARE);
//            } else {
//                UIUtil.toastMessage(this, DiaobaoUtil.getErrorTips(response));
//            }
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (WeiboLogin.getInstance().mSsoHandler != null) {
//            WeiboLogin.getInstance().mSsoHandler.authorizeCallBack(
//                    requestCode, resultCode, data);
//        }
//    }
//
//    private Handler mHandler = new Handler() {
//
//        public void handleMessage(android.os.Message msg) {
//
//            switch (msg.what) {
//                case 0:
//                    UIUtil.toastMessage(getApplicationContext(), "发送微博失败");
//                    if (dialog != null) {
//                        dialog.dismiss();
//                    }
//                    break;
//                case 1:
//                    if (flag == 1) {
//                    }
//                    if (dialog != null) {
//                        dialog.dismiss();
//                    }
//                    subShare(1);
//                    break;
//                case NEW_WEIBO_SHARE:
//                    ShareSinaWeb();
//                    break;
//                default:
//                    break;
//            }
//
//        }
//
//        ;
//    };
//
//    private final int NEW_WEIBO_SHARE = 999;
//
//    private void subShare(final int type) {
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                int uid = MyAccountOperation.getId();
//                if (uid == -1) {
//                    uid = 0;
//                }
//                String link = WMUrlRequest_1_1.getShareCountByPaperId(mPaperId,
//                        uid, type);
//                JSONObject json = HttpRequestUtil.getJsonObjectFromServer(link);
//               // TTLog.e("share", json.toString());
//            }
//        }).start();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }
//
//    @Override
//    public void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
////		if (bmp != null) {
////			bmp.recycle();
////			bmp = null;
////		}
//    }
//
//    /**
//     * 第三方应用发送请求消息到微博，唤起微博分享界面。
//     *
//     * @see {@link #sendMultiMessage} 或者 {@link #sendSingleMessage}
//     */
//    private void sendMessage(boolean hasImage, boolean hasVideo) {
//
//        int supportApi = WeiboAppManager.getInstance(this).getWbAppInfo().getSupportVersion();
//        if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
//            sendMultiMessage(hasImage, hasVideo);
//        } else {
//            sendSingleMessage(hasImage, hasVideo);
//        }
//    }
//
//    /**
//     * 第三方应用发送请求消息到微博，唤起微博分享界面。
//     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
//     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
//     *
//     * @param hasImage   分享的内容是否有图片
//     * @param hasVideo   分享的内容是否有视频
//     */
//    private void sendMultiMessage(boolean hasImage, boolean hasVideo) {
//
//        // 1. 初始化微博的分享消息
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        weiboMessage.textObject = getTextObj();
//        if (hasImage) {
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) showShareImg.getDrawable();
//            if (bitmapDrawable != null) {
//                weiboMessage.imageObject = getImageObj();
//            }
//        }
//        // 3. 发送请求消息到微博，唤起微博分享界面
//        mWeiboShareAPI.shareMessage(weiboMessage,false);
//    }
//
//    /**
//     * 第三方应用发送请求消息到微博，唤起微博分享界面。
//     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
//     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
//     *
//     * @param hasImage   分享的内容是否有图片
//     * @param hasVideo   分享的内容是否有视频
//     */
//    private void sendSingleMessage(boolean hasImage, boolean hasVideo) {
//        // 1. 初始化微博的分享消息
//        // 用户可以分享文本、图片、网页、音乐、视频中的一种
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        weiboMessage.mediaObject = getTextObj();
//        if (hasImage) {
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) showShareImg.getDrawable();
//            if (bitmapDrawable != null) {
//                weiboMessage.mediaObject = getImageObj();
//            }
//        }
//        if (hasVideo) {
////        	weiboMessage.mediaObject = getVideoObj();
//        }
//        // 3. 发送请求消息到微博，唤起微博分享界面
//        mWeiboShareAPI.shareMessage(weiboMessage,false);
//    }
//
//    /**
//     * 创建文本消息对象。
//     *
//     * @return 文本消息对象。
//     */
//    private TextObject getTextObj() {
//        TextObject textObject = new TextObject();
//        textObject.text = shareWeiboText;
//        return textObject;
//    }
//
//    /**
//     * 创建图片消息对象。
//     *
//     * @return 图片消息对象。
//     */
//    private ImageObject getImageObj() {
//        ImageObject imageObject = new ImageObject();
//        Bitmap bmp = ((BitmapDrawable) showShareImg.getDrawable()).getBitmap();
//        imageObject.setImageObject(bmp);
//        return imageObject;
//    }
//
//
//    @Override
//    public void onWbShareSuccess() {
//        //分享统计
//        if (beansInfo.getId() != null && beansInfo.getShareData() != null) {
//            HomeMainServices.getInstance().getShareUrlRequest(beansInfo.getId(), beansInfo.getShareData().getObject_type(),null,beansInfo.shareUrl);
//            UGCMainService.getInstance().uploadShareScore(beansInfo.getId(),
//                    String.valueOf(beansInfo.getSocial_type()), beansInfo.getShareData().getObject_type(), SinaWeiboActivity.this);
//        } else {
//            finish();
//        }
//    }
//
//    @Override
//    public void onWbShareCancel() {
//        Toast.makeText(SinaWeiboActivity.this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
//        finish();
//    }
//
//    @Override
//    public void onWbShareFail() {
//        Toast.makeText(SinaWeiboActivity.this,
//                getString(R.string.weibosdk_demo_toast_share_failed) ,
//                Toast.LENGTH_LONG).show();
//        finish();
//    }
//}