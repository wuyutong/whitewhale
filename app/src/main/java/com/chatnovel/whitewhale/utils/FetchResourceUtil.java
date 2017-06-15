package com.chatnovel.whitewhale.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public class FetchResourceUtil {

	public static int fetchResource(Context context, String resType, String resName, int index) {
		Resources resources = context.getResources();
		return resources.getIdentifier(resName + index, resType, context.getPackageName());
	}
	
	/**
	 * getIdentifier("90", "drawable", context.getPackageName());
	 * 
	 * */
	
	public static Drawable fetchDrawable(Context context, String resName, int dayOfWeek) {
		Resources resources = context.getResources();
		return resources.getDrawable(resources.getIdentifier(resName + "_" + dayOfWeek, "drawable", context.getPackageName()));
	}
	
	public static Bitmap fetchBitmap(Resources resources, int resId) {
		return BitmapFactory.decodeResource(resources, resId);
	}

}
