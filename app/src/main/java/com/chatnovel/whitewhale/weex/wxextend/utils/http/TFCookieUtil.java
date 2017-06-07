package com.chatnovel.whitewhale.weex.wxextend.utils.http;

import android.content.Context;

import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import com.chatnovel.whitewhale.common.Constant;
import com.chatnovel.whitewhale.module.login.AccountService;
import com.chatnovel.whitewhale.utils.DateUtil;
import com.chatnovel.whitewhale.utils.PackageUtils;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by qlx on 2016/12/10.
 */

public class TFCookieUtil {
    private static final String COOKIE = "cookie";
    private static int lastUid = 0;
    private static DiaobaoCookieStore cookies = null;
    /*
     * 获得cookies
     */

    public static DiaobaoCookieStore  getCookies(){
        try {
            if (isNeedUpdateCookies()){
                cookies = null;
            }
            createCookiesIfNeed();
        }catch (Exception e){
            e.printStackTrace();
        }

        return cookies;
    }
    /*
     * 将coockies 转换为字符串
     */
    public static String cookieHeader(DiaobaoCookieStore cookieStore) {
        List<Cookie> cookies = cookieStore.getCookies();
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.getName()).append('=').append(cookie.getValue());
        }
        return cookieHeader.toString();
    }


   /*
    * 获得cookies
    * @param extraCoockie 添加额外的coockie
    */
    public static DiaobaoCookieStore  getCookies(HashMap<String,String> extraCoockie){
        if (isNeedUpdateCookies()){
            cookies = null;
        }
        createCookiesIfNeed();
        for (Map.Entry<String, String> entry : extraCoockie.entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(entry.getKey(), (String) entry.getValue());
            cookies.addCookie(cookie);
        }
        DiaobaoCookieStore res = cookies;
        setNeedUpdate();
        return res;
    }




    // 设置后coockies会重新生成
    public static void setNeedUpdate(){
        cookies = null;
    }

    // private

    private static boolean isNeedUpdateCookies(){
        int uid = 0;
        if (AccountService.getInstance().isLogin() == true){
            uid = AccountService.getInstance().getUid();
        }
        return  uid != lastUid;
    }



    private static void  createCookiesIfNeed(){
        if (cookies == null){
            cookies = new DiaobaoCookieStore();
            if (AccountService.getInstance().isLogin()){
                try {
                    String json = WhiteWhaleApplication.applicationContext.getSharedPreferences(COOKIE, Context.MODE_PRIVATE).getString(String.valueOf(AccountService.getInstance().getUid()), null);
                    JSONObject obj = new JSONObject(json);
                    DiaobaoCookieStore store = new DiaobaoCookieStore();
                    Iterator<String> it = obj.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        if (!key.equals(Constant.DOMAIN) && !key.equals(Constant.DATE))
                        {
                            BasicClientCookie cookie = new BasicClientCookie(key, (String) obj.get(key));
                            Date date = DateUtil.strToDate(obj.getString(Constant.DATE));
                            cookie.setExpiryDate(date);
                            cookie.setDomain((String) obj.get(Constant.DOMAIN));
                            store.addCookie(cookie);
                        }

                    }

                    BasicClientCookie cookie = new BasicClientCookie(DiaobaoCookieStore.AV, PackageUtils.getVersionName(WhiteWhaleApplication.applicationContext));
                    cookie.setDomain(DiaobaoCookieStore.host);
                    cookie.setPath("/");
                    cookie.setExpiryDate(DateUtil.strToDate("2034-12-31"));
                    store.addCookie(cookie);
                    cookies = store;
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (AccountService.getInstance().isLogin()){
                lastUid = AccountService.getInstance().getUid();
            }else {
                lastUid = 0;
            }
        }
    }



}
