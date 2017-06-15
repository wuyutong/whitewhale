package com.chatnovel.whitewhale.common;

/**
 * Created by Wyatt on 2017/6/6/006.
 */

public class Constant {
    public static final String HOST_NAME = "api.duihua.doufu.la";
    public static final String BASE_URL = "http://" + Constant.HOST_NAME+"/api/" ;

    public static class UploadpPortrait{
        public static final int TYPE_TACK_PHOTO = 1;
        public static final int TYPE_GET_PHOTO_FROM_PHONE = 2;
        public static final int TYPE_CLIP_PHOTO = 3;
    }

    public static class WXData
    {
        public final static int THUMB_WIDTH_SIZE = 120;
        public final static int THUMB_HEIGHT_SIZE = 120;
        public final static int PENG_YOU_QUAN_MIN_VERSION_CODE = 0x21020001;
    }

}
