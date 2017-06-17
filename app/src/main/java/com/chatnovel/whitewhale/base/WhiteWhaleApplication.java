package com.chatnovel.whitewhale.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.chatnovel.whitewhale.weiboapi.WeiboLogin;
import com.chatnovel.whitewhale.weex.manager.TFSDKManager;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

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
        //友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                System.out.println("deviceToken="+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        mPushAgent.setDebugMode(false);//关闭推送日志
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
