package com.chatnovel.whitewhale.weex.wxextend.utils.http;

import okhttp3.Call;

/**
 * Created by qlx on 2016/12/9.
 */

public class TFHttpRequest {
    private Call mCall = null;
    TFHttpRequest(Call call){
        mCall = call;
    }

    public void  cancel(){
        if (mCall != null){
            mCall.cancel();
        }
    }

    public boolean isExecuted(){
        if (mCall != null){
            return mCall.isExecuted();
        }
        return false;
    }
    public boolean isCanceled(){
        if (mCall != null){
            return mCall.isCanceled();
        }
        return false;
    }




}
