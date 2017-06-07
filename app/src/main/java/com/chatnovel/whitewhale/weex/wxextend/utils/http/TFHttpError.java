package com.chatnovel.whitewhale.weex.wxextend.utils.http;

/**
 * Created by qlx on 2016/12/9.
 */

public class TFHttpError {

    public int code;  // 错误码
    public String domin;// 错误信息

    TFHttpError(String domin , int code){
        this.domin = domin;
        this.code = code;
    }
}
