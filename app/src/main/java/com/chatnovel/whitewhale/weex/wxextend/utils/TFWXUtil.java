package com.chatnovel.whitewhale.weex.wxextend.utils;

import android.content.Context;
import android.os.Environment;

import com.chatnovel.whitewhale.weex.qlxkit.QLXApplicationUtil;
import com.chatnovel.whitewhale.weex.qlxkit.QLXGlobal;
import com.chatnovel.whitewhale.weex.qlxkit.QLXStringUtil;
import com.taobao.weex.utils.WXFileUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by qlx on 2016/12/7.
 */

public class TFWXUtil {

//    public static String wxBaseUrl = "https://api.doufu.la/weex";
//   public static  String wxBaseUrl = "http://chatnovel.com/public/weex";//test
   public static  String wxBaseUrl = "http://10.0.0.8/diaobao_php/web/api.doufu.diaobao.la/weex";//test

    public static HashMap dayColorMap = null ;
    public static HashMap nightColorMap = null ;


    public static HashMap wxFileContentCache = new HashMap();

    public static String wxCacheDir = null;


    public static String wxBaseCacheDir(){
        if (wxCacheDir != null){
            return wxCacheDir;
        }
        Context context = QLXGlobal.getApplication();
            String cachePath = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalFilesDir(null).getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
            wxCacheDir = cachePath;
            return cachePath;

//        return  QLXGlobal.getApplication().getExternalFilesDir(null).getPath();

//        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile().getPath();
    }



    /**
     * 处理weex请求的url
     * @param url
     * @return
     */
    public static String handleWXUrl(String url){
        if (url instanceof String){
            // 先处理请求头的问题
            String newUrl = TFWXUtil.handleWXUrlBase(url);

            Boolean test = false;//测试开关

            if (QLXApplicationUtil.isApkInDebug() == false){
                test = false;
                wxBaseUrl =  "http://api.doufu.diaobao.la/weex";
            }else if(wxBaseUrl.indexOf("web/api.doufu.diaobao.la/weex") > 0){
                test = true  ;
            }

            if (!test){
                String realativeUrl = "";
                String fileName = "";
                String params = "";

                String sub = QLXStringUtil.q_subString(newUrl, "weex/[A-Za-z0-9/-]*.js");
                if (sub != null){
                    realativeUrl = sub.substring(4);
                }
                String[] splits = realativeUrl.split("/");
                fileName = splits[splits.length-1];

                int index = url.indexOf(".js");//打发
                params = url.substring(index + 3) ;




                 String diskPath = wxBaseCacheDir() + "/weex/" + realativeUrl;


                if (fileIsExists(diskPath)){

                    if (wxFileContentCache.containsKey(diskPath)){
                        newUrl = (String) wxFileContentCache.get(diskPath);
                    }else {
                        String path = WXFileUtils.loadFomDisk(diskPath,QLXGlobal.getApplication());//磁盘里面拿
                        if ((path instanceof String) && path.length() > 0){
                            wxFileContentCache.put(diskPath,path);
                            newUrl = path;
                        }
                    }


                }else {
                    String path = WXFileUtils.loadAsset(fileName,QLXGlobal.getApplication());//资源包里面拿
                    if ((path instanceof String) && path.length() > 0){
                        newUrl = path;
                    }

                }


            }
            return newUrl;
        }
        return "";
    }


    private static String handleWXUrlBase(String url){
        if (url instanceof String){
            if (!url.startsWith("http")){//no http start
                if (!url.startsWith("/")){
                    return wxBaseUrl + "/" + url;
                }
                return wxBaseUrl + url;
            }else {// http start
               int index = url.indexOf("/weex/");
                if (index != -1){
                    String retativeUrl = url.substring(index + 5);
                    if (retativeUrl instanceof String){
                        return wxBaseUrl + retativeUrl;
                    }
                }
            }
        }
        return "";
    }


//    public static WXUtils.WXUtilsDelegate getWXUtilsDelegate(){
//        return new WXUtils.WXUtilsDelegate() {
//            @Override
//            public Object getFloatObject(Object value) {
//                if (value instanceof String){
//                    if (((String) value).indexOf("|") > 0){
//                        String[] splits = ((String) value).split("\\|");
//                        if (splits == null){
//                            return value;
//                        }
//                        return splits[0];
//                    }
//                }
//                return value;
//            }
//        };
//    }
//
//    public static WXResourceUtils.WXResourceUtilsDelegate getWXResourceUtilsDelegate(){
//        return new WXResourceUtils.WXResourceUtilsDelegate() {
//            @Override
//            public String getStringColor(String color) {
//                if (color instanceof String){
//                    String key = color;
//                    if (((String) color).indexOf("|") > 0){
//                        String[] splits = ((String) color).split("\\|");
//                        if (splits == null){
//                            return color;
//                        }
//                        key = splits[0];
//                    }
//                    return getColorWithKey(key);//
//                }
//                return color;
//            }
//        };
//    }


    public static String getColorWithKey(String key){
        if ((key instanceof String) == false){
            return key;
        }
        String loKey = key.toLowerCase();
        if (dayColorMap == null){
            dayColorMap = new HashMap();
            dayColorMap.put("bg","#f4f4f4");
            dayColorMap.put("fg","#ffffff");
            dayColorMap.put("sep","#e8e8e8");
            dayColorMap.put("titletext","#101010");
            dayColorMap.put("text","#303030");
            dayColorMap.put("lighttext","#999999");
            dayColorMap.put("bar","#ffffff");
            dayColorMap.put("maincolor","#fe6e6e");

        }
        if (nightColorMap == null){
            nightColorMap = new HashMap();
            nightColorMap.put("bg","#1a1a1a");
            nightColorMap.put("fg","#212222");
            nightColorMap.put("sep","#2a2a2a");
            nightColorMap.put("titletext","#999999");
            nightColorMap.put("text","#898989");
            nightColorMap.put("lighttext","#383D40");
            nightColorMap.put("bar","#212222");
            nightColorMap.put("maincolor","#7f3037");
        }

//        if (WeMediaApplication.getInstance().isDayNightMode_Night){
            String value = (String) nightColorMap.get(loKey);
            if (value instanceof String){
                return value;
            }
            return key;
//        }else {
//            String value = (String) dayColorMap.get(loKey);
//            if (value instanceof String){
//                return value;
//            }
//            return key;
//        }


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





}
