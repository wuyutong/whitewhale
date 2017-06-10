package com.chatnovel.whitewhale.network.okhttp;

/**
 * Created by Wyatt on 2017/6/9/009.
 */
public class HttpError {

    public int code;  // 错误码
    public String domin;// 错误信息

    HttpError(String domin , int code){
        this.domin = domin;
        this.code = code;
    }
}
