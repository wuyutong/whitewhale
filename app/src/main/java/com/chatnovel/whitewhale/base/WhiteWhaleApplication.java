package com.chatnovel.whitewhale.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.chatnovel.whitewhale.weiboapi.WeiboLogin;
import com.chatnovel.whitewhale.weex.manager.TFSDKManager;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

/**
 * Created by Wyatt on 2017/6/5/005.
 */

public class WhiteWhaleApplication extends Application{
    public static WhiteWhaleApplication application;
    public static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        applicationContext = this;
        //微博注册
        WbSdk.install(this,new AuthInfo(this,WeiboLogin.APP_KEY,WeiboLogin.REDIRECT_URL,WeiboLogin.SCOPE));
        // 初始化SDK
        TFSDKManager.getInstance().initSDK();
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
