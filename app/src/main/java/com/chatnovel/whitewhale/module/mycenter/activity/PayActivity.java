package com.chatnovel.whitewhale.module.mycenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chatnovel.whitewhale.base.BaseActivity;
import com.chatnovel.whitewhale.module.mycenter.NotifyUtil;
import com.pingplusplus.android.Pingpp;

/**
 * Created by Wyatt on 2017/6/10/010.
 */

public class PayActivity extends BaseActivity {
    public static final String INTENT_KEY_PAY_DATA = "pay_data";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data = getIntent().getStringExtra(INTENT_KEY_PAY_DATA);
        if (!TextUtils.isEmpty(data)) {
            //调用支付接口
            Pingpp.createPayment(PayActivity.this, data,"qwallet1106205324");
        } else {
            //失败
            NotifyUtil.errorPay("支付失败");
            finish();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - 支付成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
                 */
                if (!TextUtils.isEmpty(result)) {
                    if ("success".equals(result)) {
                        NotifyUtil.successPay();
                    } else {
                        NotifyUtil.errorPay(result);
                    }
                } else {
                    NotifyUtil.errorPay("支付失败");
                }
                finish();
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            }
        }
    }
}
