package com.chatnovel.whitewhale.network.okhttp;

import okhttp3.Call;

/**
 * Created by Wyatt on 2017/6/9/009.
 */
public class HttpRequest {
    private Call mCall = null;
    HttpRequest(Call call){
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
