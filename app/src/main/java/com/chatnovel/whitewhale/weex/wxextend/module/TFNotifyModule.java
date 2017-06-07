package com.chatnovel.whitewhale.weex.wxextend.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chatnovel.whitewhale.weex.qlxkit.notification.QLXNotificationCenter;
import com.chatnovel.whitewhale.weex.qlxkit.notification.QLXNotificationListener;
import com.taobao.weex.IWXActivityStateListener;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qlx on 2016/12/8.
 */

public class TFNotifyModule extends WXModule {

    private HashMap<String,ArrayList<JSCallback>> mEventCallback = null;
    private QLXNotificationListener mListener = null;

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void addNotify(String event , JSCallback callback){
          this.initIfNeed();
        if (event instanceof String && callback != null){

            ArrayList<JSCallback> list = mEventCallback.get(event);
            if (list != null){
                list.add(callback);
            }else {
                list = new ArrayList<>();
                list.add(callback);
                mEventCallback.put(event,list);

                QLXNotificationCenter.getInstance().addNotify(event, this, this.mListener);
            }
        }
    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void postNotify(String event, String data){
        JSONObject jsonObject  = JSON.parseObject(data);
        QLXNotificationCenter.getInstance().postNotify(event,jsonObject);
    }
    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void removeNotify(String event){
        if (event instanceof String){
            mEventCallback.remove(event);
        }
    }



    private void  fireNotify(String event, Object data){
        if (event instanceof String){
            ArrayList<JSCallback> list = mEventCallback.get(event);
            if (list != null){
                for (JSCallback callback : list){
                    callback.invokeAndKeepAlive(data);
                }
            }
        }

    }

    private void initIfNeed(){
        if (mEventCallback == null){
            mEventCallback = new HashMap<String, ArrayList<JSCallback>>();


            final WeakReference weakSelf = new WeakReference<TFNotifyModule>(this);

            mListener = new QLXNotificationListener() {
                @Override
                public void notify(String event, Object data) {
                    TFNotifyModule self = (TFNotifyModule) weakSelf.get();
                    if (self == null) return;
                    self.fireNotify(event, data);
                }
            };

            mWXSDKInstance.registerActivityStateListener(new IWXActivityStateListener() {
                @Override
                public void onActivityCreate() {


                }

                @Override
                public void onActivityStart() {

                }

                @Override
                public void onActivityPause() {

                }

                @Override
                public void onActivityResume() {

                }

                @Override
                public void onActivityStop() {

                }

                @Override
                public void onActivityDestroy() {
                    TFNotifyModule self = (TFNotifyModule) weakSelf.get();
                    if (self == null) return;
                    QLXNotificationCenter.getInstance().remove(self);
                }

                @Override
                public boolean onActivityBack() {
                    return false;
                }
            });
        }
    }








}

