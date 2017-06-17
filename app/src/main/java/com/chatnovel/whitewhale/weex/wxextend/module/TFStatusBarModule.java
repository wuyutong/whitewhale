package com.chatnovel.whitewhale.weex.wxextend.module;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import com.chatnovel.whitewhale.weex.wxextend.utils.TFStatusBarUtil;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;

/**
 * Created by Wyatt on 2017/6/16/016.
 */

public class TFStatusBarModule extends WXModule {

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void hide(){
//        if (Build.VERSION.SDK_INT < 16) {
//            ((Activity)mWXSDKInstance.getContext()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        TFStatusBarUtil.transparencyBar((Activity) mWXSDKInstance.getContext());
    }

    public void show() {

    }
}
