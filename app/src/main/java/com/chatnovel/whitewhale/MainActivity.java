package com.chatnovel.whitewhale;

import android.os.Bundle;

import com.chatnovel.whitewhale.weex.qlxkit.QLXGlobal;
import com.chatnovel.whitewhale.weex.wxextend.activity.TFWXActivity;

import java.util.HashMap;

public class MainActivity extends TFWXActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HashMap param = new HashMap();
        param.put("url","home/WWHome.js");
        QLXGlobal.setActivityParam(MainActivity.class,param);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        findViewById(R.id.tvMyCenter).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, MyCenterActivity.class);
//                MainActivity.this.startActivity(intent);
//            }
//        });
    }
}
