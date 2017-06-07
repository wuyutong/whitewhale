package com.chatnovel.whitewhale.weex.wxextend.utils;
import android.content.SharedPreferences;
import com.alibaba.fastjson.JSONObject;
import com.chatnovel.whitewhale.utils.FileUtil;
import com.chatnovel.whitewhale.weex.qlxkit.QLXGlobal;
import com.chatnovel.whitewhale.weex.wxextend.utils.http.TFHttpError;
import com.chatnovel.whitewhale.weex.wxextend.utils.http.TFHttpRequest;
import com.chatnovel.whitewhale.weex.wxextend.utils.http.TFHttpResponse;
import com.chatnovel.whitewhale.weex.wxextend.utils.http.TFHttpUtil;

import java.io.File;
import java.util.HashMap;

/**
 * Created by qlx on 2016/12/16.
 * 热更新工具 for weex
 */

public class TFHotUpdateUtil {

    private static boolean lastedVersion = false;// 是否为最新脚本包
    private static boolean isDownloading = false;// 是否在下载中
    private static TFHttpRequest request;

    public static void  updateIfNeed(){
        if (lastedVersion == false && isDownloading == false){
            final String url = "version/weex";
            String version = version();

            HashMap param = new HashMap();
            param.put("version", version);
            request = TFHttpUtil.requestGet(url, param, new TFHttpResponse() {
                @Override
                public void onResponse(JSONObject json, TFHttpError error) {
                    if ((error == null) && (json instanceof JSONObject)){
                        try {
                           if (json.getInteger("status") == 0){
                               boolean needUpdate = json.getJSONObject("data").getBoolean("latest");
                               if (needUpdate){
                                   String version = json.getJSONObject("data").getString("version");
                                   String downUrl = json.getJSONObject("data").getString("downloadUrl");
                                   downloadFile(downUrl, version);
                               }else {
                                   lastedVersion = true;
                               }

                           }
                        }catch (Exception e){

                        }
                    }
                }
            });
        }
    }


    private static String version(){
       SharedPreferences weexVersionSF = QLXGlobal.getApplication().getSharedPreferences("TFWeexVersion",0);
        String ver = weexVersionSF.getString("version","");
        String path = TFWXUtil.wxBaseCacheDir() + "/weex/TFNovel/TFNovelDetail.js";
        if(fileIsExists(path) == false){
           return "0";
        }else {
            return ver;
        }


    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile)
    {
        try
        {
            File f = new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    private static void saveVersion(String version){
        SharedPreferences weexVersionSF = QLXGlobal.getApplication().getSharedPreferences("TFWeexVersion",0);
        weexVersionSF.edit().putString("version",version).commit();

    }




    private static void  downloadFile(String url , final String version){
        isDownloading = true;

        final String downDir =  TFWXUtil.wxBaseCacheDir();
        String path = TFWXUtil.wxBaseCacheDir() + "/weex.zip";

        FileUtil.deleteAllWithPath(path);



        TFHttpUtil.downLoadFile(url, path, new TFHttpResponse() {
            @Override
            public void onResponse(JSONObject json, TFHttpError error) {

                isDownloading = false;
          try {
              String path = json.getString("destFilePath");

              String oldUnZipFilePath =  TFWXUtil.wxBaseCacheDir() + "/weex";
              FileUtil.deleteAllWithPath(oldUnZipFilePath);

              FileUtil.unzip(path,downDir);
              saveVersion(version);
              lastedVersion = true;
          }catch (Exception e){
               e.printStackTrace();
          }


            }
        });
    }


}
