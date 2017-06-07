package com.chatnovel.whitewhale.weex.wxextend.utils.statusbar;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.taobao.weex.utils.WXResourceUtils;

/**
 * 兼容LOLLIPOP版本
 *
 */

class StatusBarLollipopImpl implements IStatusBar {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(Window window, int color, boolean lightStatusBar) {
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (color == -1 && lightStatusBar == true){
            color = WXResourceUtils.getColor("#999999");
        }


        window.setStatusBarColor(color);



        StatusBarCompatFlavorRom.setLightStatusBar(window, lightStatusBar);
    }

}
