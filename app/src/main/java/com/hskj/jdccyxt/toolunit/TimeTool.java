package com.hskj.jdccyxt.toolunit;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

/**
 * @类名：TimeTool
 * @author:广东泓胜科技有限公司
 * @实现功能：时间获取方法
 * @创建日期：2016.04.19
 */
@SuppressLint("SimpleDateFormat")
public class TimeTool {

	public static String getTiemData() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sDateFormat.format(new java.util.Date());
		return time;
	}

	public static String getTwoTiem() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String time = sDateFormat.format(new java.util.Date());
		return time;
	}
}
