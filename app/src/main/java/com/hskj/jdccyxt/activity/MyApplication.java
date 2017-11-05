package com.hskj.jdccyxt.activity;

import android.app.Application;

/**
 * @类名：MyApplication
 * @author:广东泓胜科技有限公司
 * @创建日期：2016.05.27
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
