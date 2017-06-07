package com.chatnovel.whitewhale.weex.wxextend.handler;
import android.widget.ImageView;

import com.chatnovel.whitewhale.weex.wxextend.utils.image.TFImageViewUtil;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;
import com.taobao.weex.utils.WXUtils;
/**
 * Created by qlx on 2016/12/6.
 */

public class WXImageAdapter implements IWXImgLoaderAdapter {
    @Override
    public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
        if (url instanceof String) {

            // new
            int exIndex = url.indexOf("|http");
            if (exIndex > 0){
                url = url.substring(0, exIndex);
            }

            //end

            int index = url.indexOf("radius=");
            if (index > 0){
                String value = url.substring(index + 7);
                if (value instanceof String){
                    float radiusRatio = WXUtils.getFloat(value);
                    TFImageViewUtil.setUrl(view,url.substring(0,index),radiusRatio);
                    return;
                }
            }
            TFImageViewUtil.setUrl(view,url);
        }
    }
}
