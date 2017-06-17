package com.chatnovel.whitewhale.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.chatnovel.whitewhale.weex.wxextend.utils.TFWXUtil;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wyatt on 2017/6/5/005.
 */

public class BaseActivity extends FragmentActivity{

    public ArrayList<Object> mPresentWXSDKInstances ;

    // 在该页面弹出新的Weex页面
    public void presentWeexView(String url){
        this.presentWeexView(url,null);
    }

    public void presentWeexView(String url , Map data){

        try {

            HashMap option = new HashMap();
            option.put("bundleUrl",url);

            if(data instanceof Map){
                option.put("data", data);
            }
            String  newUrl = TFWXUtil.handleWXUrl(url);

            final WeakReference<BaseActivity> weakSelf = new WeakReference<>(this);
            WXSDKInstance wXSDKInstance = new WXSDKInstance(this);
            wXSDKInstance.registerRenderListener(new IWXRenderListener() {
                @Override
                public void onViewCreated(WXSDKInstance instance, View view) {
                    BaseActivity self = weakSelf.get();
                    if (self != null){
                        view.setBackgroundColor(Color.argb(0,0,0,0));
                        self.addContentView(view,  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));


                    }
                }

                @Override
                public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
                    instance.getRootView().bringToFront();
                }

                @Override
                public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

                }

                @Override
                public void onException(WXSDKInstance instance, String errCode, String msg) {

                }
            });
            if (newUrl.startsWith("http") == true){
                wXSDKInstance.renderByUrl("WW",newUrl, option , null, WXRenderStrategy.APPEND_ASYNC);
            }else {
                wXSDKInstance.render("WW",newUrl, option , null, WXRenderStrategy.APPEND_ASYNC);
            }

            if (mPresentWXSDKInstances == null){
                mPresentWXSDKInstances = new ArrayList<Object>();
            }
            mPresentWXSDKInstances.add(wXSDKInstance);


        }catch (Exception e){

        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    // 移除
    public void removeWeexView(WXSDKInstance instance){
        try {
            if (mPresentWXSDKInstances != null){
                if (mPresentWXSDKInstances.contains(instance)){

                    ViewGroup parentView =(ViewGroup)instance.getRootView().getParent();
                    parentView.removeView(instance.getRootView());
                    instance.onActivityDestroy();
                    mPresentWXSDKInstances.remove(instance);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
