package com.chatnovel.whitewhale.network;

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

    public static void weixinLogin(String code, final WWInterface.IString iString) {
        HashMap<String, String> param = new HashMap<>();
        param.put("code", code);
        HttpUtil.requestGet(Constant.BASE_URL+"/api/loginByWxCode", param, new HttpResponse() {
            @Override
            public void onResponse(JSONObject json, HttpError error) {
                if (json != null && json.getInteger("code")== 0) {
                    
                    iString.onResult("登录成功");
                }else{
                    iString.onResult("登录失败");
                }
            }
        });
    }
}
