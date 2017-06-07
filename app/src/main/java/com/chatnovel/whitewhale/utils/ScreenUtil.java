package com.chatnovel.whitewhale.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class ScreenUtil
{

    /**
     * 获取手机屏幕宽度
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context)
    {
        //屏幕宽度
        int screenWidth = 0;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();

        //获取手机系统版本信息
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {//  sdk version>=API3
            Point outSize = new Point();
            defaultDisplay.getSize(outSize);
            screenWidth = outSize.x;
        } else
        {//  <api13
            screenWidth = defaultDisplay.getWidth();
        }
        return screenWidth;
    }

    /**
     * 获取手机屏幕高度
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int getScreenHeight(Context context)
    {
        //屏幕高度
        int screenHeight = 0;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();

        //获取手机系统版本信息
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {//  sdk version>=API3
            Point outSize = new Point();
            defaultDisplay.getSize(outSize);
            screenHeight = outSize.y;
        } else
        {//  <api13
            screenHeight = defaultDisplay.getHeight();
        }
        return screenHeight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int sp2Heightpx(Context context , int spValue){
        TextView textView = new TextView(context);
        textView.setText("1");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, spValue);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ScreenUtil.getScreenWidth(context), View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

}
