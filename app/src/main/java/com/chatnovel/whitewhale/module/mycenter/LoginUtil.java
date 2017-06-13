package com.chatnovel.whitewhale.module.mycenter;

import com.chatnovel.whitewhale.weex.qlxkit.notification.QLXNotificationCenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wyatt on 2017/6/13/013.
 */

public class LoginUtil {
    public static void successLogin() {
        QLXNotificationCenter.getInstance().postNotify("successLogin",null);
    }

    public static void errorLogin(String msg){
        Map<String, String> param = new HashMap<>();
        param.put("msg",msg);
        QLXNotificationCenter.getInstance().postNotify("errorLogin",param);
    }
}
