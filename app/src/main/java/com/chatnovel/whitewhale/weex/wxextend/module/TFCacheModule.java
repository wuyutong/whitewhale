package com.chatnovel.whitewhale.weex.wxextend.module;

import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.chatnovel.whitewhale.weex.qlxkit.QLXGlobal;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;
import java.util.HashMap;

/**
 * Created by qlx on 2016/12/16.
 */

public class TFCacheModule extends WXModule {

    private static HashMap cacheCenter = new HashMap();
    private static SharedPreferences weexCachePF = null;

    @WXModuleAnno(moduleMethod = true)
    public void set(String key , String jsonValue){
        if ((key instanceof String) ){
            Object value  = null;
            if (jsonValue instanceof String){
                try {
                    value = JSON.parseObject(jsonValue);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            try {
                cacheCenter.put(key,value);
                SharedPreferences.Editor editor = getWeexCachePF().edit();
                editor.putString(key,jsonValue);
                editor.commit();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @WXModuleAnno(moduleMethod = true)
    public void get(String key , final JSCallback callback){
       Object value = this.getValue(key);
        if (callback != null){
            callback.invoke(value);
        }
    }

    @WXModuleAnno(moduleMethod = true)
    public void setIfNeed(String key , String jsonValue){
         Object value = this.getValue(key);
        if (value == null){
            this.set(key , jsonValue);
        }
    }

    private Object getValue(String key){
        if ((key instanceof String)){
            Object value = cacheCenter.get(key);// 先从内存获取

            if (value == null){// 接着从磁盘读取
                String jsonValue = getWeexCachePF().getString(key,null);
                if (jsonValue instanceof String) {
                    value = JSON.parseObject(jsonValue);
                    if (value != null) {
                        cacheCenter.put(key, value);//缓存到内存中
                    }
                }
            }
            return value;
        }
        return null;
    }

    private static SharedPreferences getWeexCachePF(){
        if (weexCachePF == null){
            weexCachePF = QLXGlobal.getApplication().getSharedPreferences("TFWeexCache",0);
        }
        return weexCachePF;
    }



}
