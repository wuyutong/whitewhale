package com.chatnovel.whitewhale.utils;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chatnovel.whitewhale.base.WhiteWhaleApplication;

public class UIUtil {

	private static final String TAG = "SDK_Sample.Util";

	private static Toast mToast;
	public static final void showResultDialog(Context context, String msg,
                                              String title) {
		if (msg == null)
			return;
		String rmsg = msg.replace(",", "\n");
		Log.d("Util", rmsg);
		new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
				.setNegativeButton("确定", null).create().show();

	}


	private static PopupWindow window;
	public static final void dismissDialog() {
		try {
			if (window != null && window.isShowing()) {
				window.dismiss();
			}
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IllegalStateException e){}

	}

	/**
	 * 打印消息并且用Toast显示消息
	 *
	 * @param activity
	 * @param message
	 * @param logLevel
	 *            填d, w, e分别代表debug, warn, error; 默认是debug
	 */
	public static final void toastMessage(Context activity,
                                          final String message, String logLevel) {
		if ("w".equals(logLevel)) {
			Log.w("sdkDemo", ""+message);
		} else if ("e".equals(logLevel)) {
			Log.e("sdkDemo", ""+message);
		} else {
			Log.d("sdkDemo", ""+message);
		}
		// // TODO Auto-generated method stub
		if (mToast != null) {
			mToast.cancel();
			mToast = null;
		}
		if(activity==null)
			activity = WhiteWhaleApplication.application;
		mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	/**
	 * 打印消息并且用Toast显示消息
	 *
	 * @param activity
	 * @param message
	 *            填d, w, e分别代表debug, warn, error; 默认是debug
	 */
	public static final void toastMessage(final Context activity,
			final String message) {
		toastMessage(activity, message, null);
	}
}
