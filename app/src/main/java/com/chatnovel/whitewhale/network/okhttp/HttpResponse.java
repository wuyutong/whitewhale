package com.chatnovel.whitewhale.network.okhttp;


import com.alibaba.fastjson.JSONObject;

/**
 * Created by Wyatt on 2017/6/9/009.
 */
public interface HttpResponse {
    /**
     * 请求完成回调（主线程）
     */
    public void  onResponse(JSONObject json, HttpError error);

}
