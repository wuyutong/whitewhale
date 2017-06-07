package com.chatnovel.whitewhale.weex.qlxkit;

import android.app.Activity;
import android.app.Application;
import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import java.util.HashMap;

/**
 * Created by qlx on 2016/12/3.
 */

public class QLXGlobal {
    private static Application mApplication = null;
    private static HashMap<String, Object> mActivitysParamCache = new HashMap<String, Object>();

    /*
     * 设置Activity参数缓存
     */

    public static void setActivityParam(Class<?> cls, Object param){
        String key = cls.toString();
        if (key instanceof String){
            mActivitysParamCache.put(key,param);
        }

    }

    /*
     * 获取Activity参数缓存
     */
    public static Object getActivityParam(Class<?> cls){
        String key = cls.toString();
        if (key instanceof String){
            Object object = mActivitysParamCache.get(key);
            mActivitysParamCache.remove(key);
           return object;
        }
        return null;
    }
    /*
    * 获取Activity参数缓存
    */
    public static Object getActivityParam(Activity activity){
        Class cls = activity.getClass();
        String key = cls.toString();
        if (key instanceof String){
            return mActivitysParamCache.get(key);
        }
        return null;
    }





    /*
     * 获取应用程序的Application
     */
    public static Application getApplication(){
          if (mApplication == null){
              saveGetApplication();
          }

        if (mApplication == null){
            mApplication = WhiteWhaleApplication.application;
        }

        return mApplication;
    }


    private synchronized static Application saveGetApplication () {
        if (mApplication == null) {
            try {
                Application application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
                mApplication = application;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mApplication;
    }


}
