package com.chatnovel.whitewhale.weex.wxextend.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chatnovel.whitewhale.weex.wxextend.utils.http.TFHttpError;
import com.chatnovel.whitewhale.weex.wxextend.utils.http.TFHttpRequest;
import com.chatnovel.whitewhale.weex.wxextend.utils.http.TFHttpResponse;
import com.chatnovel.whitewhale.weex.wxextend.utils.http.TFHttpUtil;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by qlx on 2016/12/9.
 */

public class TFStreamModule extends WXModule {
    private HashMap<String,TFHttpRequest> requests = new HashMap<String,TFHttpRequest>();

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void requestGet(String url, String jsonParam, final JSCallback callback){
        String key = url;
        if (jsonParam instanceof String){
            key = url + jsonParam.trim();
        }
        if (key instanceof String){
            TFHttpRequest request = requests.get(key);
            if (request != null && request instanceof TFHttpRequest){
                request.cancel();
            }

            JSONObject param = null;
            if (jsonParam instanceof String){
                param  = JSON.parseObject(jsonParam);
            }
            final String _key = key;
            final WeakReference<TFStreamModule> weakSelf = new WeakReference<TFStreamModule>(this);
//            block(@{@"data":result,@"code":@(code),@"statusText":statusText,@"ok":@(ok)});
            request = TFHttpUtil.requestGet(url,  param, new TFHttpResponse() {
                @Override
                public void onResponse(JSONObject json, TFHttpError error) {
                    TFStreamModule self = weakSelf.get();
                    if (self == null){
                        return ;
                    }
                    self.requests.remove(_key);
                    HashMap res = new HashMap();
                    json = json == null ? new JSONObject():json;
                    res.put("data",json);
                    int code = (error != null) ? error.code : 0;
                    res.put("code",code);
                    String statusText = (error != null) ? error.domin : "";
                    res.put("statusText",statusText);
                    Boolean ok = (error == null);
                    res.put("ok",ok);
                    if (callback != null && res.size() > 3){
                        callback.invoke(res);
                    }
                }
            });
            requests.put(key,request);

        }

    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void requestPost(String url, String jsonParam, final JSCallback callback){
        String key = url;
        if (jsonParam instanceof String){
            key = url + jsonParam.trim();
        }
        if (key instanceof String){
            TFHttpRequest request = requests.get(key);
            if (request != null && request instanceof TFHttpRequest){
                request.cancel();
            }
            JSONObject param = null;
            if (jsonParam instanceof String){
                param  = JSON.parseObject(jsonParam);
            }
            final String _key = key;
            final WeakReference<TFStreamModule> weakSelf = new WeakReference<TFStreamModule>(this);
            request = TFHttpUtil.requestPost(url,  param, new TFHttpResponse() {
                @Override
                public void onResponse(JSONObject json, TFHttpError error) {
                    TFStreamModule self = weakSelf.get();
                    if (self == null){
                        return ;
                    }
                    self.requests.remove(_key);

                    HashMap res = new HashMap();
                    json = json == null ? new JSONObject():json;
                    res.put("data",json);
                    Integer code = error != null ? error.code : 0;
                    res.put("code",code);
                    String statusText = error != null ? error.domin : "";
                    res.put("statusText",statusText);
                    Boolean ok = error == null;
                    res.put("ok",ok);
                    if (callback != null){
                        callback.invoke(res);
                    }
                }
            });
            requests.put(key,request);
        }
    }

}
