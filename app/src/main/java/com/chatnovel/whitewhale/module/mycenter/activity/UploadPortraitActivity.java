package com.chatnovel.whitewhale.module.mycenter.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.chatnovel.whitewhale.base.BaseActivity;
import com.chatnovel.whitewhale.common.Constant;
import com.chatnovel.whitewhale.common.IntentKey;
import com.chatnovel.whitewhale.common.WWInterface;
import com.chatnovel.whitewhale.module.mycenter.NotifyUtil;
import com.chatnovel.whitewhale.network.HttpImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Wyatt on 2017/6/12/012.
 */

public class UploadPortraitActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra(IntentKey.UPLOAD_PORTRAIT, 0);
        if (type == Constant.UploadpPortrait.TYPE_GET_PHOTO_FROM_PHONE) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, Constant.UploadpPortrait.TYPE_GET_PHOTO_FROM_PHONE);
        } else if (type == Constant.UploadpPortrait.TYPE_TACK_PHOTO) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 下面这句指定调用相机拍照后的照片存储的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "uploadPortrait.jpg")));
            startActivityForResult(intent, Constant.UploadpPortrait.TYPE_TACK_PHOTO);
        } else {
            Toast.makeText(this, "操作失败，请重试", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case Constant.UploadpPortrait.TYPE_GET_PHOTO_FROM_PHONE:
                if (data != null)
                    startPhotoZoom(data.getData());
                break;
            // 如果是调用相机拍照时
            case Constant.UploadpPortrait.TYPE_TACK_PHOTO:
                File temp = new File(Environment.getExternalStorageDirectory() + "/uploadPortrait.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case Constant.UploadpPortrait.TYPE_CLIP_PHOTO:
                /**
                 */
                if (data != null) {
                    upload(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upload(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap userBitmap = extras.getParcelable("data");
            if (userBitmap == null)
                return;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            userBitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
            byte[] b = stream.toByteArray(); // 将图片流以字符串形式存储下来 tp = new
            String imageData = "data:image/png;base64,"+Base64.encodeToString(b, Base64.DEFAULT);
            HttpImage.uploadImage(imageData, new WWInterface.IString() {
                @Override
                public void onResult(String url) {
                    NotifyUtil.notifyHeadUrl(url);
                }
            });
            finish();
        }
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constant.UploadpPortrait.TYPE_CLIP_PHOTO);
    }
}
