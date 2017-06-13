package com.chatnovel.whitewhale.weex.wxextend.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.chatnovel.whitewhale.base.BaseActivity;
import com.chatnovel.whitewhale.qqapi.QQService;
import com.chatnovel.whitewhale.weex.qlxkit.QLXGlobal;
import com.chatnovel.whitewhale.weex.wxextend.utils.TFWXUtil;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXResourceUtils;
import com.tencent.connect.common.Constants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qlx on 2016/12/7.
 */

public class TFWXActivity extends BaseActivity implements IWXRenderListener {

    WXSDKInstance mWXSDKInstance;

    ArrayList<WXSDKInstance> presentWXSDKInstances;


    private HashMap param;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View rootView = new View(this);
//        rootView.setBackgroundColor(WXResourceUtils.getColor("bg|bg"));
//        setContentView(rootView);

        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);
        /**
         * WXSample 可以替换成自定义的字符串，针对埋点有效。
         * template 是.we transform 后的 js文件。
         * option 可以为空，或者通过option传入 js需要的参数。例如bundle js的地址等。
         * jsonInitData 可以为空。
         * width 为-1 默认全屏，可以自己定制。
         * height =-1 默认全屏，可以自己定制。
         */
        try {
            this.param = (HashMap) QLXGlobal.getActivityParam(this);
            HashMap options = this.getOptions();

            String url = this.getUrl();
            if (url.startsWith("http") == true){
                mWXSDKInstance.renderByUrl("WW",url, options , null, WXRenderStrategy.APPEND_ASYNC);
            }else {
                mWXSDKInstance.render("WW",url, options , null, WXRenderStrategy.APPEND_ASYNC);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取传值额外参数 {data：map} 和bundleUrl同级
     */
    private HashMap getOptions(){
        Object param = this.param;
        HashMap<String,Object> options = new HashMap<String,Object>();
        String url = (String) ((Map) param).get("url");
        options.put("bundleUrl",url);
        if (param instanceof Map){
            param = ((Map) param).get("data");
            if (param instanceof Object){

               if (param instanceof HashMap){
                   options.put("data",param);
               }else {
                   options.put("data",JSON.parseObject(param.toString()));
               }


            }

        }
        return options;
    }
    /**
     * 获取Url
     */
    private String getUrl(){
        Object param = this.param;
        if (param instanceof Map){
            String url = (String) ((Map) param).get("url");
            return TFWXUtil.handleWXUrl(url);
        }
        return "";
    }
    private static View getRootView(Activity context)
    {
        return ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
    }
    // 移除
    public void removeWeexView(WXSDKInstance instance){
        if (presentWXSDKInstances != null){
            if (presentWXSDKInstances.contains(instance)){
//                mWXSDKInstance.getRootView().removeView(instance.getRootView());
                mWXSDKInstance.removeFixedView(instance.getRootView());
                instance.onActivityDestroy();
                presentWXSDKInstances.remove(instance);
            }
        }
    }

    // 在该页面弹出新的Weex页面
    public void presentWeexView(String url , Map data){
        try {
            HashMap option = new HashMap();
            option.put("bundleUrl",url);
            if(data instanceof Map){
                option.put("data",data);
            }
            String newUrl = TFWXUtil.handleWXUrl(url);

            final WeakReference<TFWXActivity> weakSelf = new WeakReference<TFWXActivity>(this);
            WXSDKInstance wXSDKInstance = new WXSDKInstance(this);
            wXSDKInstance.registerRenderListener(new IWXRenderListener() {
                @Override
                public void onViewCreated(WXSDKInstance instance, View view) {
                    TFWXActivity self = weakSelf.get();
                    if (self != null){
                        View rView = self.mWXSDKInstance.getRootView();
                        if (rView instanceof ViewGroup){
                          view.setBackgroundColor(Color.argb(0,0,0,0));
                            ((ViewGroup)rView).addView(view);
                        }
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
                wXSDKInstance.renderByUrl("TF",newUrl, option , null, -1, -1, WXRenderStrategy.APPEND_ASYNC);
            }else {
                wXSDKInstance.render("TF",newUrl, option , null, -1, -1, WXRenderStrategy.APPEND_ASYNC);
            }

            if (presentWXSDKInstances == null){
                presentWXSDKInstances = new ArrayList<>();
            }
            presentWXSDKInstances.add(wXSDKInstance);
        }catch (Exception e){

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            if (QQService.getmTencent() != null && QQService.getInstance() != null) {
                QQService.getmTencent().handleLoginData(data, QQService.getInstance().listener);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        setContentView(view);
        final WeakReference<TFWXActivity> weakSelf = new WeakReference<TFWXActivity>(this);
        if (view instanceof ViewGroup){
            ((ViewGroup)view).setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View child) {
                    TFWXActivity self = weakSelf.get();
                    if (self != null){
                        if (self.presentWXSDKInstances != null){
                            if (self.presentWXSDKInstances.size() > 0){
                                WXSDKInstance inatance = self.presentWXSDKInstances.get(self.presentWXSDKInstances.size() - 1);
                                if (inatance.getRootView() != null){
                                    inatance.getRootView().bringToFront();
                                }

                            }
                        }
                    }
                }

                @Override
                public void onChildViewRemoved(View parent, View child) {

                }
            });

        }

    }



    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityResume();
            if (presentWXSDKInstances != null){
                for (WXSDKInstance instance:presentWXSDKInstances){
                    instance.onActivityResume();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityPause();
            if (presentWXSDKInstances != null){
                for (WXSDKInstance instance:presentWXSDKInstances){
                    instance.onActivityPause();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityStop();
            if (presentWXSDKInstances != null){
                for (WXSDKInstance instance:presentWXSDKInstances){
                    instance.onActivityStop();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityDestroy();
            if (presentWXSDKInstances != null){
                for (WXSDKInstance instance:presentWXSDKInstances){
                    instance.onActivityDestroy();
                }
            }
        }
    }
}
