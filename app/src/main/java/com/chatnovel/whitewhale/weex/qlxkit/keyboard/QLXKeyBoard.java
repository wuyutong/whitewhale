package com.chatnovel.whitewhale.weex.qlxkit.keyboard;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * Created by qlx on 2017/3/13.
 */



public class QLXKeyBoard {

    // 接口
    public interface QLXKeyBoardListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }

    public static class HeightObject{
        public int height = -1;
    }



    /**
     * 添加键盘事件监听
     * @param activity
     * @param listener
     */
    static public void addKeyboardListener(Activity activity , QLXKeyBoardListener listener){
         if (activity instanceof Activity){
             View rootView = activity.getWindow().getDecorView();
             _addKeyboardListener(rootView,listener);
         }
    }


    static private void _addKeyboardListener(View rootView , final QLXKeyBoardListener listener){
        final WeakReference<View> weakSelf = new WeakReference<View>(rootView);



        final  HeightObject lastHeight = new HeightObject();
        final  HeightObject fullHeight = new HeightObject();

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (weakSelf == null) return ;
                View rootView = weakSelf.get();
                if (rootView == null) return ;
                if (listener == null) return;

                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.height();

                if (fullHeight.height == -1 && visibleHeight > 0){
                    fullHeight.height = visibleHeight;// 原来总可见高度
                }
                int originHeight = fullHeight.height;

                int keyboardHeight = 200;  // 键盘最低高度 作为判断标准

                if (originHeight - visibleHeight > keyboardHeight){
                    int height = originHeight - visibleHeight;
                    if (height != lastHeight.height){
                        lastHeight.height = height;
                        listener.keyBoardShow(height);
                    }

                }else {
                    int height = 0;
                    if (height != lastHeight.height){
                        lastHeight.height = height;
                        listener.keyBoardHide(height);
                    }

                }
            }
        });

    }
}
