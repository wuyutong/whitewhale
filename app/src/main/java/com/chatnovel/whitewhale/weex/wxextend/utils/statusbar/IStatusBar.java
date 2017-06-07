package com.chatnovel.whitewhale.weex.wxextend.utils.statusbar;

import android.view.Window;

/**
 * 状态栏接口
 */

interface IStatusBar {
    void setStatusBarColor(Window window, int color, boolean lightStatusBar);
}
