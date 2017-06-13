package com.chatnovel.whitewhale.wxapi;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import com.chatnovel.whitewhale.common.WWInterface;
import com.chatnovel.whitewhale.module.mycenter.LoginUtil;
import com.chatnovel.whitewhale.network.HttpLogin;
import com.chatnovel.whitewhale.sp.SharePreferenceKey;
import com.chatnovel.whitewhale.sp.WWSharePreference;
import com.chatnovel.whitewhale.utils.UIUtil;
import com.chatnovel.whitewhale.weex.qlxkit.notification.QLXNotificationCenter;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wyatt on 2017/6/6/006.
 */

public class WeixinLogin {

    private volatile static WeixinLogin singleton;
    private Context mContext;

    public static WeixinLogin getInstance(Context context) {
        if (singleton == null) {
            synchronized (WeixinLogin.class) {
                if (singleton == null) {
                    singleton = new WeixinLogin();
                }
            }
        }
        if (!(context instanceof WXEntryActivity)) {
            singleton.mContext = context;
        }
        return singleton;
    }

    /**
     * 发送微信登陆请求
     */
    public void sendWeChatLogin() {
        IWXAPI api = WXAPIFactory.createWXAPI(WhiteWhaleApplication.applicationContext, WXEntryActivity.WX_APP_ID, true);
        api.registerApp(WXEntryActivity.WX_APP_ID);
        if (! api.isWXAppInstalled()) {
            UIUtil.toastMessage(mContext, "请先安装微信");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "carjob_wx_login";
        api.sendReq(req);
    }



    /**
     * 微信登录，只需要传入code,剩余的工作服务端完成，
     * @param code
     */
    public void wxCodeLogin(String code) {
        HttpLogin.weixinLogin(code, new WWInterface.IString() {
            @Override
            public void onResult(String token) {
                WWSharePreference.setSharedPreferencesValueToString(SharePreferenceKey.SP_KEY_TOKEN,token,mContext);
                if (!TextUtils.isEmpty(token)) {
                    LoginUtil.successLogin();
                } else {
                    LoginUtil.errorLogin("登录失败");
                }
            }
        });
    }
}
