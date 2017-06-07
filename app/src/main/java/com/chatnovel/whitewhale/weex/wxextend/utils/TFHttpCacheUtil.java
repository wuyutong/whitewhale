package com.chatnovel.whitewhale.weex.wxextend.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by qlx on 2016/12/10.
 */

public class TFHttpCacheUtil {

    private static HashMap cacheNovels = new HashMap();

    public static void  cacheInNeed(JSONObject json){
        if (json instanceof JSONObject){
            cacheNovelFromJson(json);
        }else if(json instanceof Map){
           // cacheNovelIfNeed(json);
        }
    }

    public static JSONObject getCacheNovelWithID(String id){
        Object value = cacheNovels.get(id);
        if (value instanceof WeakReference){
            WeakReference<JSONObject> object = (WeakReference<JSONObject>)value;
            return  object.get();
        }
        return null;
    }


    private static void cacheNovelFromJson(JSONObject json){
        if ((json instanceof JSONObject) || (json instanceof Map)){
            try {
                Object data =  json.get("data");
                if (data instanceof JSONArray){
                    cacheNovelFromJsonArray( data);
                }else if(data instanceof JSONObject){
                    JSONObject jsonObject = (JSONObject)data;

                    String key = getArrayKeyFromJson(jsonObject);
                    if (key instanceof String){
                        JSONArray list = (JSONArray) jsonObject.get(key);
                        cacheNovelFromJsonArray(list);
                        int size = list.length();
                        for (int i = 0; i < size; i++){
                            JSONObject ob = (JSONObject) list.get(i);
                            String arrayKey = getArrayKeyFromJson(ob);
                            if (arrayKey instanceof String){
                                cacheNovelFromJsonArray(ob.get(arrayKey));
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private static String getArrayKeyFromJson(JSONObject json){
         if(json instanceof JSONObject){
             if (json.has("object_type")){
                 return null;
             }
             try {
                 Iterator<String> keys = json.keys();
                 while (keys.hasNext()) {
                     String key = (String) keys.next();
                     Object value = json.get(key);
                     if (value instanceof JSONArray){
                         return key;
                     }
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }
        }
        return null;
    }

    private static void cacheNovelFromJsonArray(Object jsonArray){
        try {
            if (jsonArray instanceof JSONArray){
                JSONArray list = (JSONArray)jsonArray;
                int size = list.length();
                for (int i = 0 ; i < size ; i++){
                    Object value = list.get(i);
                    if (isNovel((JSONObject) value)){
                        String key = ((JSONObject) value).getString("id");
                        if (key instanceof String){
                            if (cacheNovels.containsKey(key) == false){
                                cacheNovels.put(key,new WeakReference<JSONObject>((JSONObject) value));
                            }else {
                                WeakReference<JSONObject> weakValue = (WeakReference<JSONObject>)cacheNovels.get(key);
                                if ((weakValue == null) || (weakValue.get() == null) ){
                                    cacheNovels.put(key,new WeakReference<JSONObject>((JSONObject) value));
                                }
                            }

                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();;
        }

    }



    private static Boolean isNovel(JSONObject json){
        if ((json instanceof JSONObject) || (json instanceof Map)){
            try {
                int objcet_type = json.getInt("object_type");
                if (objcet_type == 5){
                    int topic_type = json.getInt("topic_type");
                    if (topic_type == 18){
                        return true;
                    }
                }
            }catch (Exception e){

            }
        }

        return false;
    }


}
