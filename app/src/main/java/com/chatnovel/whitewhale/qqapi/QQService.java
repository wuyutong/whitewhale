package com.chatnovel.whitewhale.qqapi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import com.chatnovel.whitewhale.common.WWInterface;
import com.chatnovel.whitewhale.model.ShareInfo;
import com.chatnovel.whitewhale.module.mycenter.NotifyUtil;
import com.chatnovel.whitewhale.network.HttpLogin;
import com.chatnovel.whitewhale.sp.SharePreferenceKey;
import com.chatnovel.whitewhale.sp.WWSharePreference;
import com.chatnovel.whitewhale.utils.UIUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

public class QQService {
	private static String APP_ID = "1106205324";
	private static String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends," + "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";
	public static Tencent getmTencent() {
		return mTencent;
	}
	private static Tencent mTencent;
	private boolean isLogin;
	private static QQService instance = new QQService();
	private QQService() {
		init();
		SharedPreferences pres = QQTokenKeeper
				.readAccessToken(WhiteWhaleApplication.applicationContext);
		if (pres != null) {
			String token = pres.getString(QQTokenKeeper.ACCESS_TOKEN, null);
			String openId = pres.getString(QQTokenKeeper.OPENID, null);
			if (token != null && openId != null) {
				initToken(token, openId);
			}
		}
	}

	public static QQService getInstance() {
		return instance;
	}

	public void initToken(String token, String openId) {
		mTencent.setAccessToken(token, System.currentTimeMillis() + "");
		mTencent.setOpenId(openId);
	}

	private void init() {
		if (mTencent == null || !mTencent.isSessionValid()) {
			mTencent = Tencent.createInstance(APP_ID,
					WhiteWhaleApplication.applicationContext);
		}
	}

	public void login(final Activity activity) {
		if (isLogin == true) {
			UIUtil.toastMessage(activity, "别点了，QQ正在启动...");
			return;
		}
		isLogin = true;
		if (mTencent.isSessionValid()) {
			mTencent.logout(activity);
		}
		mTencent.login(activity, SCOPE, listener);

	}

	public IUiListener listener = new IUiListener() {
		@Override
		public void onComplete(Object o) {
			isLogin = false;
			login(o);
		}

		@Override
		public void onError(UiError uiError) {
			isLogin = false;
			NotifyUtil.errorLogin("登录失败");
		}

		@Override
		public void onCancel() {
			isLogin = false;
			NotifyUtil.errorLogin("取消登录");
		}
	};

	private void login(Object o) {
		try {
			JSONObject jsonObject = JSON.parseObject(o.toString());
			String access_token = jsonObject.getString("access_token");
			if (!TextUtils.isEmpty(access_token)) {
				HttpLogin.qqLogin(access_token, new WWInterface.IString() {
					@Override
					public void onResult(String token) {
						WWSharePreference.setSharedPreferencesValueToString(SharePreferenceKey.SP_KEY_TOKEN,token,WhiteWhaleApplication.applicationContext);
						if (!TextUtils.isEmpty(token)) {
							NotifyUtil.successLogin();
						} else {
							NotifyUtil.errorLogin("登录失败");
						}
					}
				});
			}else{
				NotifyUtil.errorLogin("登录失败");
			}
		} catch (Exception e) {
			NotifyUtil.errorLogin("登录失败");
		}

	}


	public void shareToQQ(Activity activity,ShareInfo shareInfo) {
		Bundle params = new Bundle();
		String title,summary,extStr;
		String shareUrl ;
		String imgUrl ;
		title = shareInfo.getTitle();
		summary = shareInfo.getShareText();
		extStr = shareInfo.getShareText();
		shareUrl = shareInfo.getShareUrl();
		imgUrl = shareInfo.getIcon();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
		params.putString(QQShare.SHARE_TO_QQ_EXT_STR, extStr);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "白鲸");
		mTencent.shareToQQ(activity, params,shareListener );
	}

	public IUiListener shareListener  = new IUiListener() {
		@Override
		public void onError(UiError arg0) {
			NotifyUtil.errorShare("分享失败");
		}

		@Override
		public void onComplete(Object arg0) {
			NotifyUtil.successShare();
		}

		@Override
		public void onCancel() {
			NotifyUtil.errorShare("取消分享");
		}
	};

	public void shareToQQSpace(Activity activity,ShareInfo shareInfo) {

		Bundle params = new Bundle();
		String title, summary ;
		String shareUrl ;
		String imgUrl ;
		title = shareInfo.getTitle();
		summary = shareInfo.getShareText();
		shareUrl = shareInfo.getShareUrl();
		imgUrl = shareInfo.getIcon();
		ArrayList<String> imgs = new ArrayList<String>();
		imgs.add(imgUrl);
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_NO_TYPE);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);// 必填
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);// 选填
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);// 必填
		params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, imgs);
		mTencent.shareToQzone(activity, params, shareListener);

	}
}
