package com.chatnovel.whitewhale.network;

import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import com.chatnovel.whitewhale.common.Constant;
import com.chatnovel.whitewhale.common.WWInterface;
import com.chatnovel.whitewhale.network.okhttp.HttpError;
import com.chatnovel.whitewhale.network.okhttp.HttpResponse;
import com.chatnovel.whitewhale.network.okhttp.HttpUtil;

import java.util.HashMap;

/**
 * Created by Wyatt on 2017/6/9/009.
 */

public class HttpLogin {


    public static void weiboLogin(String access_token,String refresh_token, final WWInterface.IString iString) {
        HashMap<String, String> param = new HashMap<>();
        param.put("access_token", access_token);
        param.put("refresh_token", refresh_token);
        HttpUtil.requestPost(Constant.BASE_URL+"loginByWbToken", param, new HttpResponse() {
            @Override
            public void onResponse(JSONObject json, HttpError error) {
                if (json != null && json.getJSONObject("data")!=null && json.getInteger("code")== 0) {
                    JSONObject obj = json.getJSONObject("data");
                    String token = obj.getString("token");
                    if (!TextUtils.isEmpty(token)) {
                        iString.onResult(token);
                    }else{
                        iString.onResult("");
                    }

                }else{
                    iString.onResult("");
                }
            }
        });
    }

    public static void weixinLogin(String code, final WWInterface.IString iString) {
        HashMap<String, String> param = new HashMap<>();
        param.put("code", code);
        HttpUtil.requestPost(Constant.BASE_URL+"loginByWxCode", param, new HttpResponse() {
            @Override
            public void onResponse(JSONObject json, HttpError error) {
                if (json != null && json.getJSONObject("data")!=null && json.getInteger("code")== 0) {
                    JSONObject obj = json.getJSONObject("data");
                    String token = obj.getString("token");
                    if (!TextUtils.isEmpty(token)) {
                        iString.onResult(token);
                    }else{
                        iString.onResult("");
                    }

                }else{
                    iString.onResult("");
                }
            }
        });
    }

    public static void qqLogin(String access_token, final WWInterface.IString iString) {
        HashMap<String, String> param = new HashMap<>();
        param.put("access_token", access_token);
        HttpUtil.requestPost(Constant.BASE_URL+"loginByQQToken", param, new HttpResponse() {
            @Override
            public void onResponse(JSONObject json, HttpError error) {
                if (json != null && json.getJSONObject("data")!=null && json.getInteger("code")== 0) {
                    JSONObject obj = json.getJSONObject("data");
                    String token = obj.getString("token");
                    if (!TextUtils.isEmpty(token)) {
                        iString.onResult(token);
                    }else{
                        iString.onResult("");
                    }

                }else{
                    iString.onResult("");
                }
            }
        });
    }
}
