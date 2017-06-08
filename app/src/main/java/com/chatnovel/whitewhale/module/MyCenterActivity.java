package com.chatnovel.whitewhale.module;
import android.os.Bundle;
import com.chatnovel.whitewhale.weex.qlxkit.QLXGlobal;
import com.chatnovel.whitewhale.weex.wxextend.activity.TFWXActivity;

import java.util.HashMap;

/**
 * Created by Wyatt on 2017/6/6/006.
 */

public class MyCenterActivity extends TFWXActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HashMap param = new HashMap();
        param.put("url","MyCenter/WWMyCenter.js");
        QLXGlobal.setActivityParam(MyCenterActivity.class,param);
        super.onCreate(savedInstanceState);
    }
}
