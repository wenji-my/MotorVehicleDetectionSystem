package com.hskj.jdccyxt.toolunit;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

/**
 * @������TimeTool
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ�ʱ���ȡ����
 * @�������ڣ�2016.04.19
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
