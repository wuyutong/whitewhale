package com.chatnovel.whitewhale.weex.wxextend.module;

import android.app.Activity;

import com.chatnovel.whitewhale.base.BaseActivity;
import com.chatnovel.whitewhale.weex.qlxkit.QLXApplicationUtil;
import com.chatnovel.whitewhale.weex.wxextend.activity.TFWXActivity;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;

import java.util.HashMap;


/**
 * Created by qlx on 2016/12/8.
 */

public class TFEventModule extends WXModule {

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void openURL(String url) {
        try {
            if (url instanceof String){
                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put("url",url);




                if (url instanceof String){
                    if (url.indexOf("present") > 0){
                        if (mWXSDKInstance.getContext() instanceof TFWXActivity) {
                            TFWXActivity ac = (TFWXActivity) mWXSDKInstance.getContext();
                            ac.presentWeexView(url , null);
                        }else if(mWXSDKInstance.getContext() instanceof BaseActivity){
                            BaseActivity activity = (BaseActivity)mWXSDKInstance.getContext();
                            activity.presentWeexView(url , null);
                        }
                        return;
                    }
                }
                QLXApplicationUtil.pushActivity((Activity) mWXSDKInstance.getContext(), TFWXActivity.class,param);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
