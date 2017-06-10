package com.chatnovel.whitewhale.weiboapi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;
import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import com.chatnovel.whitewhale.utils.UIUtil;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

public class WeiboLogin {
    public static final String SINA_WEIBO_USER_INFO_URL = "https://api.weibo.com/2/users/show.json";

    private static final String REFRESH_URL = "https://api.weibo.com/oauth2/access_token";
    /**
     * 新浪微博 第三方应用 APP_KEY
     */
    public static final String APP_KEY = "2476545949";
    public static final String REDIRECT_URL = "http://www.sharereader.cn";
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    public static Oauth2AccessToken mAccessToken;
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    public SsoHandler mSsoHandler;

    private static WeiboLogin instance = new WeiboLogin();

    public static final String SCOPE = "email,direct_messages_read,direct_messages_write," + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

    private Activity activity;

    private boolean isLogin;

    private WeiboLogin()
    {

    }

    public static WeiboLogin getInstance()
    {
        if (mAccessToken == null)
        {
            mAccessToken = AccessTokenKeeper.readAccessToken(WhiteWhaleApplication.applicationContext);
        }
        return instance;
    }

    public void ssoAuthorize(Activity activity, final boolean isFlag)
    {
        this.activity = activity;
        mSsoHandler = new SsoHandler(activity);
        if (isLogin == true)
        {
            UIUtil.toastMessage(activity, "别点了，微博正在启动...");
            return;
        }
        isLogin = true;
        mSsoHandler.authorize(new WbAuthListener()
        {

            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                isLogin = false;
                mAccessToken = oauth2AccessToken;
                Toast toast;
                if (mAccessToken.isSessionValid())
                {
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(WhiteWhaleApplication.applicationContext, mAccessToken);
                    if (isFlag) {
                        return;

                    }
                    UIUtil.toastMessage(WeiboLogin.this.activity, "微博授权成功，正在获取用户信息");
                    loadWeiboUserInfo();
                } else
                {
                    toast = Toast.makeText(WeiboLogin.this.activity, "授权失败，请重新登录", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    UIUtil.dismissDialog();
                }
            }

            @Override
            public void cancel() {
                isLogin = false;
                UIUtil.toastMessage(WeiboLogin.this.activity, "授权失败，请重新登录");
                UIUtil.dismissDialog();
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                isLogin = false;
                UIUtil.toastMessage(null, "授权失败:" + wbConnectErrorMessage.getErrorMessage());
                UIUtil.dismissDialog();
            }
        });
    }


    public void sinaAuthorizeCallBack(int requestCode, int resultCode, Intent data){
        if (mSsoHandler != null){
            mSsoHandler.authorizeCallBack(requestCode , resultCode , data);
        }
    }

    public void refreshTokenRequest(Context context)
    {    // https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=refresh_token&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&refresh_token=…
        final Oauth2AccessToken token = WeiboLogin.getInstance().mAccessToken;
        WeiboParameters params = new WeiboParameters(APP_KEY);
//        params.add("client_id", Constant.APP_KEY);
        params.add("client_secret", "1977434707b47437b33b24c887f7fafb");
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", token.getRefreshToken());
        try {
            params.add("redirect_uri", URLEncoder.encode(REDIRECT_URL , "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new AsyncWeiboRunner(activity).requestAsync(REFRESH_URL, params, "POST", new RequestListener() {
            @Override
            public void onComplete(String arg0) {
                try {
                    JSONObject jsonObject = new JSONObject(arg0);
                    if (WeiboLogin.getInstance().mAccessToken != null && jsonObject != null){

                        WeiboLogin.getInstance().mAccessToken.setUid(jsonObject.getString("uid"));
                        WeiboLogin.getInstance().mAccessToken.setExpiresIn(jsonObject.getString("expires_in"));
                        WeiboLogin.getInstance().mAccessToken.setRefreshToken(jsonObject.getString("refresh_token"));
                        //WeiboService.getInstance().mAccessToken.set(jsonObject.getString("remind_in"));
                        WeiboLogin.getInstance().mAccessToken.setToken(jsonObject.getString("access_token"));
                        AccessTokenKeeper.writeAccessToken(WhiteWhaleApplication.applicationContext, mAccessToken);
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onWeiboException(WeiboException e) {
                e.printStackTrace();
            }


        });
    }



    private void loadWeiboUserInfo()
    {
        WeiboParameters params = new WeiboParameters(APP_KEY);
        params.add("source", APP_KEY);
        params.add("uid", mAccessToken.getUid());
        params.add("access_token", mAccessToken.getToken());
        //params.add("uid", mAccessToken.getRefreshToken());
        new AsyncWeiboRunner(activity).requestAsync(SINA_WEIBO_USER_INFO_URL, params, "GET", new RequestListener()
        {


            @Override
            public void onComplete(String arg0)
            {
                try
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            UIUtil.toastMessage(activity, "获取微博用户信息成功，正在登录...");
                            UIUtil.dismissDialog();
                        }
                    });
                    JSONObject obj = new JSONObject(arg0);
                    final String nickname = obj.getString("screen_name");
                    final String id = mAccessToken.getUid();
//                    final String expires_in = data.getString("expires_in");
                    final String access_token = mAccessToken.getToken();

                    List<NameValuePair> lsit = new LinkedList<NameValuePair>();
                    NameValuePair tokenValue = new BasicNameValuePair("auth_token", id);
//                    NameValuePair expiresValue = new BasicNameValuePair("expires_in", expires_in);
                    NameValuePair nickValue = new BasicNameValuePair("nickname", nickname);
//                    NameValuePair avatarValue = new BasicNameValuePair("head_img", DiaobaoUtil.getStringFromJSONObject(obj, "profile_image_url"));

                    NameValuePair typeValue = new BasicNameValuePair("social_type", "1");

                    String uid = "0";
                    NameValuePair uidValue = new BasicNameValuePair("uid", uid);
                    NameValuePair accessValue = new BasicNameValuePair("access_token", access_token);
                    lsit.add(uidValue);
                    lsit.add(typeValue);
                    lsit.add(tokenValue);
//                    lsit.add(expiresValue);
//                    lsit.add(avatarValue);
                    lsit.add(nickValue);
                    lsit.add(accessValue);

//                    HttpRequestObject request = new HttpRequestObject();
//                    request.setHandleType(HANDLE_WEIBO_BIND_OR_REGIST);
//                    request.setUrl(DEFAULT_URL);
//                    request.setData(lsit);
//                    request.setListener((HttpResponseListener) activity);
//                    HttpUtil.sendLockedPostRequest(request, activity);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        UIUtil.toastMessage(activity, "获取微博用户信息失败");
                        UIUtil.dismissDialog();
                    }
                });
            }
        });
    }
}
