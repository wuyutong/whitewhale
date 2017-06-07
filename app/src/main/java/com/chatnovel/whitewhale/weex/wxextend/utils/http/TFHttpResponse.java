package com.chatnovel.whitewhale.weex.wxextend.utils.http;


import com.alibaba.fastjson.JSONObject;

/**
 * Created by qlx on 2016/12/9.
 */

public interface TFHttpResponse {
    /**
     * 请求完成回调（主线程）
     */
    public void  onResponse(JSONObject json, TFHttpError error);

}
