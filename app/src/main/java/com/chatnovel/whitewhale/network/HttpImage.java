package com.chatnovel.whitewhale.network;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.chatnovel.whitewhale.common.Constant;
import com.chatnovel.whitewhale.common.WWInterface;
import com.chatnovel.whitewhale.network.okhttp.HttpError;
import com.chatnovel.whitewhale.network.okhttp.HttpResponse;
import com.chatnovel.whitewhale.network.okhttp.HttpUtil;

import java.util.HashMap;

/**
 * Created by Wyatt on 2017/6/14/014.
 */

public class HttpImage {
    public static void uploadImage(String base64image, final WWInterface.IString iString) {
        HashMap<String, String> param = new HashMap<>();
        param.put("base64image", base64image);
        HttpUtil.requestPost(Constant.BASE_URL+"uploadImage", param, new HttpResponse() {
            @Override
            public void onResponse(JSONObject json, HttpError error) {
                if (json != null && json.getJSONObject("data")!=null && json.getInteger("code")== 0) {
                    JSONObject obj = json.getJSONObject("data");
                    String domain = obj.getString("domain");
                    JSONObject result = obj.getJSONObject("result");
                    if (!TextUtils.isEmpty(domain) && result!=null) {
                        String key = result.getString("key");
                        if (!TextUtils.isEmpty(key)) {
                            iString.onResult(domain+key);
                        }else{
                            iString.onResult("");
                        }
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
