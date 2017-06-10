/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chatnovel.whitewhale.weiboapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * 该类定义了微博授权时�?��要的参数�? *
 * 
 * @author SINA
 * @since 2013-10-07
 */
public class AccessTokenKeep {
	private static final String PREFERENCES_NAME = "com_weibo_sdk_android";

	private static final String KEY_UID = "uid";
	private static final String KEY_ACCESS_TOKEN = "access_token";
	private static final String KEY_EXPIRES_IN = "expires_in";

	/**
	 * 保存accesstoken到SharedPreferences
	 * 
	 * @param context
	 *            Activity 上下文环�? * @param token Oauth2AccessToken
	 */
	public static void keepAccessToken(Context context, Oauth2AccessToken token, String uid) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(KEY_UID, uid);
		editor.putString(KEY_ACCESS_TOKEN, token.getToken());
		editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
		editor.commit();
	}

	/**
	 * 清空sharepreference
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 从SharedPreferences读取accessstoken
	 * 
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static Oauth2AccessToken readAccessToken(Context context) {
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
		token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
		return token;
	}
}
