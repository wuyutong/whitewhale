package com.chatnovel.whitewhale.weex.qlxkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by qlx on 2016/12/3.
 */

public class QLXApplicationUtil {
    /*
     * 获取应用版本名 如1.0.0
     */
    public static String getVersionName() {
        Context context = QLXGlobal.getApplication();
        if (context != null){
            try {
                PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return pi.versionName;
            }catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }
        }
        return  "";
    }
    /*
     * 获取应用包信息
     */
    public static PackageInfo getPackageInfo()  {
        Context context = QLXGlobal.getApplication();
        if (context != null){
            try {
                return  context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            }catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }
        }
        return  null;
    }
    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug() {
        try {
            ApplicationInfo info = QLXGlobal.getApplication().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
    /*
    * activity 跳转
    */
    public static Intent pushActivity(Activity aActivityInstance, Class bActivityClass, Object param){
        return pushActivity(aActivityInstance,bActivityClass,param,-1000);
    }

    /*
    * activity 跳转
    */
    public static Intent pushActivity(Activity aActivityInstance, Class bActivityClass, Object param , int flags){
        if (aActivityInstance instanceof Activity){
            Intent intent = new Intent(aActivityInstance, bActivityClass);
            if (param instanceof Object){
                QLXGlobal.setActivityParam(bActivityClass,param);
            }
            if (flags != -1000){
                intent.addFlags(flags);
            }
            aActivityInstance.startActivity(intent);
            return intent;
        }
        return null;
    }

}

