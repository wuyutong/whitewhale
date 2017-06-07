package com.chatnovel.whitewhale.weex.wxextend.utils.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by qlx on 2016/12/12.
 */

public class TFImageViewUtil {

    public static void setUrl(ImageView view , String url , float radiusRatio){
        if (url instanceof String){
            if (radiusRatio > 0){
                Glide.with(view.getContext()).load(url).transform(new GlideRoundTransform(view.getContext(),radiusRatio)).dontAnimate().into(view);
            }else {
                Glide.with(view.getContext()).load(url).dontAnimate().into(view);
            }
        }

    }

    public static void setUrl(ImageView view , String url) {

        Glide.with(view.getContext()).load(url).dontAnimate().into(view);
    }
}
