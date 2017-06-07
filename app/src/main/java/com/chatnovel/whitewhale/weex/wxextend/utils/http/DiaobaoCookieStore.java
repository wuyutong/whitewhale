package com.chatnovel.whitewhale.weex.wxextend.utils.http;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import com.chatnovel.whitewhale.base.WhiteWhaleApplication;
import com.chatnovel.whitewhale.utils.DateUtil;
import com.chatnovel.whitewhale.utils.ScreenUtil;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import java.io.Serializable;

public class DiaobaoCookieStore extends BasicCookieStore implements
        Serializable {
	private static final long serialVersionUID = 1L;
	public static final String AID = "aid";
	public static final String AV = "av";
	public static final String CH = "ch";
	public static final String OS = "os";
	public static final String MD = "md";
	public static final String host = "diaobao.la";
	public static final String SCREEN_WIDTH_HEIGHT = "wh";

	public DiaobaoCookieStore() {
		Context context = WhiteWhaleApplication.applicationContext;
		
		PackageInfo packInfo;
		try {
			packInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			String version = packInfo.versionName;

			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			String channel = appInfo.metaData.getString("UMENG_CHANNEL");
			BasicClientCookie cookie  = new BasicClientCookie(AV, version);
			cookie.setDomain(host);
			cookie.setPath("/");
			cookie.setExpiryDate(DateUtil.strToDate("2034-12-31"));
			addCookie(cookie);
			cookie = new BasicClientCookie(CH, channel);
			cookie.setDomain(host);
			cookie.setPath("/");
			cookie.setExpiryDate(DateUtil.strToDate("2034-12-31"));
			addCookie(cookie);
			cookie = new BasicClientCookie(OS, "Android-"
					+ Build.VERSION.RELEASE);
			cookie.setDomain(host);
			cookie.setPath("/");
			cookie.setExpiryDate(DateUtil.strToDate("2034-12-31"));
			addCookie(cookie);
			cookie = new BasicClientCookie(MD, Build.MODEL);
			cookie.setDomain(host);
			cookie.setPath("/");
			cookie.setExpiryDate(DateUtil.strToDate("2034-12-31"));
			addCookie(cookie);
			cookie = new BasicClientCookie(SCREEN_WIDTH_HEIGHT , ScreenUtil.getScreenWidth(context) + "*" + ScreenUtil.getScreenHeight(context));
			cookie.setDomain(host);
			cookie.setPath("/");
			cookie.setExpiryDate(DateUtil.strToDate("2034-12-31"));
			addCookie(cookie);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

}
