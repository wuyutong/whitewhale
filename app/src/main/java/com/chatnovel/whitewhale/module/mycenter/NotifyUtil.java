package com.chatnovel.whitewhale.module.mycenter;

import com.chatnovel.whitewhale.weex.qlxkit.notification.QLXNotificationCenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wyatt on 2017/6/13/013.
 */

public class NotifyUtil {
    public static void successLogin() {
        QLXNotificationCenter.getInstance().postNotify("successLogin",null);
    }

    public static void errorLogin(String msg){
        Map<String, String> param = new HashMap<>();
        param.put("msg",msg);
        QLXNotificationCenter.getInstance().postNotify("errorLogin",param);
    }

    public static void successPay() {
        QLXNotificationCenter.getInstance().postNotify("successPay",null);
    }

    public static void errorPay(String msg){
        Map<String, String> param = new HashMap<>();
        param.put("msg",msg);
        QLXNotificationCenter.getInstance().postNotify("errorPay",param);
    }

    public static void notifyHeadUrl(String url){
        Map<String, String> param = new HashMap<>();
        param.put("url",url);
        QLXNotificationCenter.getInstance().postNotify("headUrl",param);
    }

    public static void successShare() {
        QLXNotificationCenter.getInstance().postNotify("successShare",null);
    }

    public static void errorShare(String msg) {
        Map<String, String> param = new HashMap<>();
        param.put("msg",msg);
        QLXNotificationCenter.getInstance().postNotify("errorShare",param);
    }


}
