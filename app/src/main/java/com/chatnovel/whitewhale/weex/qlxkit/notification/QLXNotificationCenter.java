package com.chatnovel.whitewhale.weex.qlxkit.notification;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qlx on 2016/12/8.
 */



public class QLXNotificationCenter {

    private static QLXNotificationCenter instance = new QLXNotificationCenter();

    private HashMap<String,ArrayList<TargetInfo>> mGlobalEvents = new HashMap<String, ArrayList<TargetInfo>>();

    private class TargetInfo extends Object {
        public WeakReference<Object> weakTarget;//弱引用
        public QLXNotificationListener listener;

        TargetInfo(Object target , QLXNotificationListener listener){
            this.weakTarget = new WeakReference<Object>(target);
            this.listener = listener;
        }

        public Object getTarget(){
            if (this.weakTarget != null){
                return this.weakTarget.get();
            }else {
                return null;
            }
        }


        public  boolean isEqualTo(TargetInfo info){
            return this.getTarget() == info.getTarget() && this.listener == info.listener;
        }
    }

    /*
     * 单例
     */
    public static QLXNotificationCenter getInstance(){
        return instance;
    }
    public static QLXNotificationCenter defaultCenter(){
        return instance;
    }
    /*
     * 添加通知
     */
    public void  addNotify(String event, Object target , QLXNotificationListener listener){
        if (event instanceof String && target != null && listener != null){

            TargetInfo targetInfo = new TargetInfo(target,listener);
            ArrayList<TargetInfo> list = mGlobalEvents.get(event);
            if (list != null){
                for (TargetInfo info : list){
                    if (info.isEqualTo(targetInfo)){
                        return ;
                    }
                }
            }else {
                list = new ArrayList<TargetInfo>();
                mGlobalEvents.put(event, list);
            }
            list.add(targetInfo);
        }
    }

    /*
    * 发送通知
    */
    public void  postNotify(String event, Object data ){
        if (event instanceof String ){
            ArrayList<TargetInfo> targetInfos = mGlobalEvents.get(event);
            if (targetInfos != null){
                for (TargetInfo info : targetInfos){
                    if (info.getTarget() != null && info.listener != null){
                        info.listener.notify(event , data);
                    }
                }
            }
        }
    }


    /*
     * 删除关于该对象所有通知监听
     */
    public void remove(Object target){
        for (String key : mGlobalEvents.keySet()){
            ArrayList<TargetInfo> targetInfos = mGlobalEvents.get(key);

            for (int i = 0; i < targetInfos.size(); i++){
                TargetInfo info = targetInfos.get(i);
                if (info != null){
                    if (info.getTarget() == target){
                        targetInfos.remove(i);
                        i--;
                    }
                }
            }
        }
    }
    /*
     * 删除通知事件所有监听
     */
    public void removeEvent(String event){
        if (event instanceof String){
            mGlobalEvents.remove(event);
        }
    }
}
