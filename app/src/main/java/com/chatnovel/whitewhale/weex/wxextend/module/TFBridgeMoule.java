package com.chatnovel.whitewhale.weex.wxextend.module;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chatnovel.whitewhale.base.BaseActivity;
import com.chatnovel.whitewhale.weex.qlxkit.QLXApplicationUtil;
import com.chatnovel.whitewhale.weex.qlxkit.QLXGlobal;
import com.chatnovel.whitewhale.weex.qlxkit.QLXStringUtil;
import com.chatnovel.whitewhale.weex.qlxkit.keyboard.QLXKeyBoard;
import com.chatnovel.whitewhale.weex.wxextend.activity.TFWXActivity;
import com.chatnovel.whitewhale.weex.wxextend.utils.statusbar.StatusBarCompatFlavorRom;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qlx on 2016/12/10.
 */




public class TFBridgeMoule extends WXModule {
    final static int TFAtResult = 12;
    public enum TFBridgeType{
        TFBridgeTypeDynamic(0) , // 根据param字典 动态打开
        TFBridgeTypeStatusBarLight(13),// 状态栏边成白色
        TFBridgeTypeStatusBarDark(14);// 状态栏边成黑色
        
        private int value = 0;

        TFBridgeType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }


    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void openPage(String jsonParam , final JSCallback callback){

        try {
            if (jsonParam instanceof String){
                JSONObject param = JSON.parseObject(jsonParam);
                TFBridgeType type = TFBridgeType.values()[param.getInteger("id")];
                JSONObject pageParam = null;
                if ((param.containsKey("param"))){
                   Object temp  =   param.get("param");
                    if (temp instanceof JSONObject){
                        pageParam = (JSONObject)temp;
                    }else if(temp instanceof String){
                        JSONObject jon = new JSONObject();
                        jon.put("url",temp);
                        pageParam = jon;
                    }

                }
                switch (type){
                    case TFBridgeTypeDynamic: {

                        break;
                    }

                    case TFBridgeTypeStatusBarLight:
                    {
                        try {
                                TFWXActivity activity = (TFWXActivity) mWXSDKInstance.getContext();
                                StatusBarCompatFlavorRom.setLightStatusBar(activity.getWindow(), false);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    }
                    case TFBridgeTypeStatusBarDark:
                    {
                        try {
                            TFWXActivity activity = (TFWXActivity ) mWXSDKInstance.getContext();
                            StatusBarCompatFlavorRom.setLightStatusBar(activity.getWindow(), true);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    }
                    default:
                    {
                        Toast.makeText(QLXGlobal.getApplication(), "请升级新版本体验该功能", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void closePage(String jsonParam , JSCallback callback){
        try {
            if (jsonParam instanceof String){
                JSONObject param = JSON.parseObject(jsonParam);

                String url = param.getString("url");
                if (url.indexOf("present") > 0){
                    Context context = mWXSDKInstance.getContext();
                    if (context instanceof BaseActivity){
                        BaseActivity activity = (BaseActivity)context;
                        activity.removeWeexView(mWXSDKInstance);
                    }
                }
                if (mWXSDKInstance.getContext() instanceof Activity){
                    ((Activity) mWXSDKInstance.getContext()).finish();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void uid(JSCallback callback){
        String uid = "";
        if (callback != null){
            HashMap param = new HashMap();
            param.put("uid", Integer.valueOf(uid));
            callback.invoke(param);
        }
    }


    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void openPageAlive(String jsonParam , final JSCallback callback){
        this.openPage(jsonParam, new JSCallback() {
            @Override
            public void invoke(Object data) {
                if (callback != null){
                    callback.invokeAndKeepAlive(data);
                }
            }
            @Override
            public void invokeAndKeepAlive(Object data) {
                if (callback != null){
                    callback.invokeAndKeepAlive(data);
                }
            }
        });
    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void openPageKeep(String jsonParam , final JSCallback callback){
        this.openPageAlive(jsonParam,callback);
    }
    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void statistics(String eventName){
        //统计
    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void openUrl(String url){
        try {
            if (url instanceof String){
                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put("url",url);

                Map dataParam = null;

                try {
                    JSONObject config = JSON.parseObject(url);
                    if(config != null && config.containsKey("url")){
                        String urlStr = config.getString("url");
                        url = urlStr;
                        JSONObject data = null;
                        if(config.containsKey("data")){
                            data = config.getJSONObject("data");
                        }else if(config.containsKey("param")){
                            data = config.getJSONObject("param");
                        }
                        dataParam = data;

                        param.put("url",urlStr);
                        param.put("data",data);
                    }
                }catch (Exception e){

                }
                if (url instanceof String){
                    if (url.indexOf("present") > 0){
                        if (mWXSDKInstance.getContext() instanceof TFWXActivity) {
                            TFWXActivity ac = (TFWXActivity) mWXSDKInstance.getContext();
                            ac.presentWeexView(url, dataParam);
                        }else if(mWXSDKInstance.getContext() instanceof BaseActivity){
                            BaseActivity activity = (BaseActivity)mWXSDKInstance.getContext();
                            activity.presentWeexView(url, dataParam);
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

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void openURL(String url){
         this.openUrl(url);


    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void addKeyboardShow(final JSCallback callback){

        QLXKeyBoard.addKeyboardListener((Activity) this.mWXSDKInstance.getContext(), new QLXKeyBoard.QLXKeyBoardListener() {
            @Override
            public void keyBoardShow(int height) {
                if (callback != null){
                    HashMap param = new HashMap();
                    String value = QLXStringUtil.pxToDp(height) + "dp";
                    param.put("height",value);
                    callback.invokeAndKeepAlive(param);
                }
            }
            @Override
            public void keyBoardHide(int height) {
            }
        });

    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void addKeyboardHide(final JSCallback callback){
        QLXKeyBoard.addKeyboardListener((Activity) this.mWXSDKInstance.getContext(), new QLXKeyBoard.QLXKeyBoardListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                if (callback != null){
                    HashMap param = new HashMap();
                    param.put("height", Integer.valueOf(height));
                    callback.invokeAndKeepAlive(param);
                }
            }
        });
    }

}
