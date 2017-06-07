package com.chatnovel.whitewhale.weex.wxextend.module;

import android.widget.Toast;

import com.chatnovel.whitewhale.weex.qlxkit.QLXGlobal;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;

/**
 * Created by qlx on 2016/12/10.
 */

public class TFHudModule extends WXModule {
    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void showMessage(String text){
        Toast.makeText(QLXGlobal.getApplication(), text, Toast.LENGTH_SHORT).show();
    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void showError(String text){
        Toast.makeText(QLXGlobal.getApplication(), text, Toast.LENGTH_SHORT).show();
    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void showSuccess(String text){
        Toast.makeText(QLXGlobal.getApplication(), text, Toast.LENGTH_SHORT).show();
    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void showLoading(String text){

        Toast.makeText(QLXGlobal.getApplication(), text, Toast.LENGTH_SHORT).show();//test
    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void dismiss(){
        //Toast.makeText(QLXGlobal.getApplication(), text, Toast.LENGTH_SHORT).show();//test
    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void  alert(String jsonParam , JSCallback callback){

    }

    @WXModuleAnno(moduleMethod = true,runOnUIThread = true)
    public void showGiftMessage(String text){
        Toast.makeText(QLXGlobal.getApplication(), text, Toast.LENGTH_SHORT).show();
    }











}
