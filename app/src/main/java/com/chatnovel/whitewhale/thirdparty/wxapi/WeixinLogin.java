package com.chatnovel.whitewhale.thirdparty.wxapi;
import android.content.Context;
import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import com.chatnovel.whitewhale.utils.UIUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
    public void wxcodelogin(String code) {

    }
}
