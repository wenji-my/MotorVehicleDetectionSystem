package com.hskj.jdccyxt.toolunit.xmlparsing;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @������VersionTool
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ���ȡ��ǰ�汾
 */
public class VersionTool {
	public static String getAPPVersionCode(Context context) {
		String appVersionName = "";
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			appVersionName = info.versionName; // �汾��
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appVersionName;
	}
}
