package com.hskj.jdccyxt.toolunit.xmlparsing;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @类名：VersionTool
 * @author:广东泓胜科技有限公司
 * @实现功能：获取当前版本
 */
public class VersionTool {
	public static String getAPPVersionCode(Context context) {
		String appVersionName = "";
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			appVersionName = info.versionName; // 版本名
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appVersionName;
	}
}
