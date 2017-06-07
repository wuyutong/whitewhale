package com.chatnovel.whitewhale.weex.qlxkit;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qlx on 2016/12/7.
 */

public class QLXStringUtil {

    // 正则表达式匹配子串
    public static String q_subString(String from , String exp) {
        Pattern pattern = Pattern.compile(exp);
        Matcher matcher = pattern.matcher(from);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    public static int[] indexsOf(String string , String subString){
        if ((string instanceof String) && (subString instanceof String)){

            if(subString.length() == 0) return new int[0];


            int[] res = new int[ 2*string.length() ];
            int start = 0;
            int count = 0;
            while (start >= 0 && start < string.length()){
               int index = string.indexOf(subString, start);
                if (index >= 0){//fixed
                   res[count++] = index;
                   start = index + subString.length();
                }else {
                    break;
                }
            }
            if (count > 0){
                int[] result = new int[count];
                for (int i = 0 ; i < count ; ++i){
                    result[i] = res[i];
                }
                return result;
            }
        }
        return null;
    }

    public static  float pxToDp(float value){
        float scale = 2;
        try {
            scale = QLXGlobal.getApplication().getResources()//
                    .getDisplayMetrics().density;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value / scale;

    }




}
