package com.chatnovel.whitewhale.wxapi;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.chatnovel.whitewhale.R;
import com.chatnovel.whitewhale.base.BaseActivity;
import com.chatnovel.whitewhale.module.mycenter.NotifyUtil;
import com.chatnovel.whitewhale.utils.UIUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	
	private final String TAG = "WXEntryActivity";
	// wx
	private IWXAPI wxAPI;

	public final static String WX_APP_ID = "wxa3183597c2b41f49";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerShareServices();
	}
	
	private void registerShareServices() {
		
		// wx
		wxAPI = WXAPIFactory.createWXAPI(/*getThisActivity()*/this, WX_APP_ID, true);
		wxAPI.registerApp(WX_APP_ID);
		wxAPI.handleIntent(getIntent(), this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		wxAPI.handleIntent(intent, this);
	}


	@Override
	public void onReq(BaseReq baseReq) {

	}


	@Override
	public void onResp(BaseResp resp) {
		int result;
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登陆授权
			if (resp.errCode == BaseResp.ErrCode.ERR_OK){//用户同意
				String code = ((SendAuth.Resp) resp).code;
				WeixinLogin.getInstance(this).wxCodeLogin(code);
			}else {
				NotifyUtil.errorLogin("授权失败，请重新登录");
			}
		}else {
			//分享调用处理
			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					NotifyUtil.successShare();
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					result = R.string.errcode_cancel;
					NotifyUtil.errorShare(getString(result));
					break;
				case BaseResp.ErrCode.ERR_AUTH_DENIED:
					result = R.string.errcode_deny;
					NotifyUtil.errorShare(getString(result));
					break;
				default:
					result = R.string.errcode_unknown;
					NotifyUtil.errorShare(getString(result));
					break;
			}
		}
		this.finish();
	}

}
