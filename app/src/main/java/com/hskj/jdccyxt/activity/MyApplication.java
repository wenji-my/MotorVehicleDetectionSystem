package com.hskj.jdccyxt.activity;

import android.app.Application;

/**
 * @������MyApplication
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @�������ڣ�2016.05.27
 */
public class MyApplication extends Application {
	private String version = "";
	private String viewerName = "";

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getViewerName() {
		return viewerName;
	}

	public void setViewerName(String viewerName) {
		this.viewerName = viewerName;
	}

}
