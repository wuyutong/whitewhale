package com.chatnovel.whitewhale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chatnovel.whitewhale.base.BaseActivity;
import com.chatnovel.whitewhale.module.MyCenterActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tvMyCenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MyCenterActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
