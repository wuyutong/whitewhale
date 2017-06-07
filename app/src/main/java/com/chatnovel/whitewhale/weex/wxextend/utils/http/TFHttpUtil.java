package com.chatnovel.whitewhale.weex.wxextend.utils.http;

import android.os.Handler;
import android.os.Looper;
import com.alibaba.fastjson.JSONObject;
import com.chatnovel.whitewhale.common.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by qlx on 2016/12/9.
 */

public class TFHttpUtil {

    private static String baseUrl = Constant.BASE_URL;//外网
    //private static String baseUrl = "http://187.api.doufu.diaobao.la/index.php";//内网

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Handler handler = new Handler(Looper.getMainLooper());


    public static TFHttpRequest requestGet(String url, Map params, TFHttpResponse response) {
        Request request = buildRequest(url, Method.GET, params);
        return request(request, response);
    }


    public static TFHttpRequest requestPost(String url, Map params, TFHttpResponse response) {
        Request request = buildRequest(url, Method.POST, params);
        return request(request, response);
    }



    /**
     * 构建请求对象
     *
     * @param url    请求url
     * @param method 请求方式
     * @param params 传入一个map集合请求参数 否则传入空
     * @return 请求对象
     */
    private static Request buildRequest(String url, Method method, Map<String, String> params) {
        url = getAbsoluteUrl(url);
        Request request = null;

        if (method == Method.GET) {
            Request.Builder reqBuild = new Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            if (params instanceof Map){
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    urlBuilder.addQueryParameter(entry.getKey(), toStringValue(entry.getValue()));
                }
            }
            DiaobaoCookieStore cookies = TFCookieUtil.getCookies();
            reqBuild.header("Cookie", TFCookieUtil.cookieHeader(cookies));
            request = reqBuild.url(urlBuilder.build()).build();


        } else if (method == Method.POST) {
            Request.Builder builder = new Request.Builder().url(url);
            //构建post请求的request
            FormBody.Builder formBody = new FormBody.Builder();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBody.add(entry.getKey(), toStringValue(entry.getValue()));
            }
            DiaobaoCookieStore cookies = TFCookieUtil.getCookies();
            builder.header("Cookie", TFCookieUtil.cookieHeader(cookies));
            request = builder.post(formBody.build()).build();
        }


        return request;
    }


    private static TFHttpRequest request(Request request, final TFHttpResponse callBack) {

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call != null && call.isCanceled() == false){
                    TFHttpUtil.handleResponseOnMainThreadExcuted(callBack, null);
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call != null && call.isCanceled() == false){
                    TFHttpUtil.handleResponseOnMainThreadExcuted(callBack, response);
                }

            }
        });
        return new TFHttpRequest(call);
    }

    private static void handleResponseOnMainThreadExcuted(final TFHttpResponse callBack , final Response response){
        if (callBack == null){
            return ;
        }
        JSONObject json = null;
        TFHttpError error = null;
        if (response == null){
            error = new TFHttpError("网络异常，请检查网络后再试", -1);
        }else {
            if (response.isSuccessful()){
                try {
                    String result = response.body().string();
                    if (result instanceof String){
                        JSONObject tempJson =  (JSONObject) JSONObject.parse(result);
                        if (tempJson instanceof JSONObject){
                            json = tempJson;
                        }
                    }
                } catch (Exception e) {//hot fixed
                    error = new TFHttpError("数据解析异常",-100);//changed
                    e.printStackTrace();
                }
            }else {
                error = new TFHttpError(response.message(),response.code());
            }
        }
        final JSONObject resJson = json;
        final TFHttpError resError = error;
        if (handler != null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onResponse(resJson,resError);
                }
            });
        }
    }

    private static String toStringValue(Object value){
        if (value instanceof String){
            return (String)value;
        }else {
            return value.toString();
        }
    }

    // 获取完整的url

    private static String getAbsoluteUrl(String url){
        if ((url instanceof String) == false ){
            return "";
        }

        if (url.startsWith("https")){
            return url;
        }

        if (url.startsWith("http")){
            int index = url.indexOf("index.php");
            if (index >= 0){
                String retitaveUrl = url.substring(index + 9);
                if (retitaveUrl instanceof String){
                    return baseUrl + retitaveUrl;
                }
            }
            return url;
        }else {

            if (url.startsWith("/")){
                return baseUrl + url.substring(1);
            }else {
                return baseUrl + url;
            }


        }
    }

    /**
     * 下载文件
     * @param fileUrl 文件url
     * @param destFileFullPath 存储目标目录
     */
    public  static void downLoadFile(String fileUrl, final String destFileFullPath, final TFHttpResponse callback) {


        final File file = new File(destFileFullPath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }else {

        }


        final Request request = new Request.Builder().url(fileUrl).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (callback != null){
                    TFHttpError error = new TFHttpError("网络不好", -1);
                    callback.onResponse(null,error);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {

                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file,false);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    JSONObject json = new JSONObject();
                    json.put("destFilePath",destFileFullPath);
                    callback.onResponse(json,null);
                } catch (IOException e) {
                    callback.onResponse(null,new TFHttpError("下载失败", -2));
                }

            }
        });
    }


    enum Method {
        GET, POST
    }
}
