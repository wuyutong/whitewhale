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

package com.chatnovel.whitewhale.qqapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONObject;

/**
 * 该类定义了微博授权时所需要的参数。
 * 
 * @author SINA
 * @since 2013-10-07
 */
public class QQTokenKeeper {
	private static final String PREFERENCES_NAME = "com_qq_sdk_android";

	public static final String OPENID = "openid";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String EXPIRES_IN = "expires_in";

	/**
	 * 保存 Token 对象到 SharedPreferences。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 * @param token
	 *            Token 对象
	 */
	public static void writeAccessToken(Context context, JSONObject json) {
		try {
			SharedPreferences pref = context.getSharedPreferences(
					PREFERENCES_NAME, Context.MODE_APPEND);
			Editor editor = pref.edit();
			editor.putString(EXPIRES_IN, json.getString(EXPIRES_IN));
			editor.putString(ACCESS_TOKEN, json.getString(ACCESS_TOKEN));
			editor.putString(OPENID, json.getString(OPENID));
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从 SharedPreferences 读取 Token 信息。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 * 
	 * @return 返回 Token 对象
	 */
	public static SharedPreferences readAccessToken(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		return pref;
	}

	/**
	 * 清空 SharedPreferences 中 Token信息。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 */
	public static void clear(Context context) {
		if (null == context) {
			return;
		}

		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
}
