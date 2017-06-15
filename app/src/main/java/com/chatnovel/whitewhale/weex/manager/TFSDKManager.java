package com.chatnovel.whitewhale.weex.manager;
import android.app.Application;
import android.widget.Toast;
import com.chatnovel.whitewhale.weex.qlxkit.QLXApplicationUtil;
import com.chatnovel.whitewhale.weex.qlxkit.QLXGlobal;
import com.chatnovel.whitewhale.weex.wxextend.handler.WXImageAdapter;
import com.chatnovel.whitewhale.weex.wxextend.module.TFBridgeMoule;
import com.chatnovel.whitewhale.weex.wxextend.module.TFCacheModule;
import com.chatnovel.whitewhale.weex.wxextend.module.TFEventModule;
import com.chatnovel.whitewhale.weex.wxextend.module.TFHudModule;
import com.chatnovel.whitewhale.weex.wxextend.module.TFNotifyModule;
import com.chatnovel.whitewhale.weex.wxextend.module.TFStreamModule;
import com.chatnovel.whitewhale.weex.wxextend.utils.TFHotUpdateUtil;
import com.taobao.hotfix.HotFixManager;
import com.taobao.hotfix.PatchLoadStatusListener;
import com.taobao.hotfix.util.PatchStatusCode;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.ui.component.Textarea;
/**
 * Created by qlx on 2016/12/3.
 */

public class TFSDKManager {
    private static TFSDKManager instance = new TFSDKManager();

    /*
     * 单例
     */
    public static TFSDKManager getInstance(){
        return instance;
    }

    public void initSDK(){
        this.initHotFix();
        this.initWeexSDK();
    }
    /*
     * 设置HotFix SDK
     */
    private void initHotFix() {
        Application context = QLXGlobal.getApplication();

        String version = QLXApplicationUtil.getVersionName();
        String appId = "80486-1";
        HotFixManager.getInstance().initialize(context, version, appId, true, new PatchLoadStatusListener() {
            @Override
            public void onload(int mode, int code, String info, int handlePatchVersion) {
                // 补丁加载回调通知
                if (code == PatchStatusCode.CODE_SUCCESS_LOAD) {
                    // TODO: 10/24/16 表明补丁加载成功
                } else if (code == PatchStatusCode.CODE_ERROR_NEEDRESTART) {
                    Toast.makeText(QLXGlobal.getApplication(), "请重启应用更新补丁", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: 10/25/16 其它信息
                }
            }
        });
        HotFixManager.getInstance().queryNewHotPatch();
    }

    /*
    * 设置Weex SDK
    */
    private void initWeexSDK(){
        InitConfig config=new InitConfig.Builder().setImgAdapter(new WXImageAdapter()).build();
        WXSDKEngine.initialize(QLXGlobal.getApplication(),config);
        try {
            WXSDKEngine.registerComponent("qtextarea", Textarea.class);
            WXSDKEngine.registerModule("notify", TFNotifyModule.class);
            WXSDKEngine.registerModule("bridge", TFBridgeMoule.class);
            WXSDKEngine.registerModule("event", TFEventModule.class);
            WXSDKEngine.registerModule("http", TFStreamModule.class);
            WXSDKEngine.registerModule("hud", TFHudModule.class);
            WXSDKEngine.registerModule("cache", TFCacheModule.class);
            TFHotUpdateUtil.updateIfNeed();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
