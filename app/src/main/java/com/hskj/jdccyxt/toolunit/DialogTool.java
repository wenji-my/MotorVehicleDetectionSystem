package com.hskj.jdccyxt.toolunit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

/**
 * @类名：DialogTool
 * @author:广东泓胜科技有限公司
 * @实现功能：对话框
 * @创建日期：2016.04.19
 */
public class DialogTool {

	public static ProgressDialog getProgressDialog(String matter, Context context) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(matter);
		progressDialog.setCancelable(false);
		return progressDialog;
	}

	public static ProgressDialog getDownloadProgressBarDialog(String matter, Context context) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("提示");
		progressDialog.setCancelable(false);
		progressDialog.setMessage(matter);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		return progressDialog;
	}

	public static void getTextDialog(String matter, Context context) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setTitle("提示框");
		builder1.setMessage(matter);
		builder1.setPositiveButton("确定", null);
		builder1.setCancelable(false);
		builder1.show();
	}

	public static void setSelectDialog(String matter, final Context context) {
		AlertDialog.Builder tcbuilder = new AlertDialog.Builder(context);
		tcbuilder.setTitle("提示框：");
		tcbuilder.setMessage(matter);
		tcbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				((Activity) context).finish();
			}
		});
		tcbuilder.setNegativeButton("取消", null);
		tcbuilder.setCancelable(false);
		tcbuilder.show();
	}

	public static void setPositiveDialog(String matter, final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示框：");
		builder.setMessage(matter);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				((Activity) context).finish();
			}
		});
		builder.setCancelable(false);
		builder.show();
	}

	public static void setSelectHandlerDialog(String matter, final int constant, final Handler handler, Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示框：");
		builder.setMessage(matter);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				handler.sendEmptyMessage(constant);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.setCancelable(false);
		builder.show();
	}
}
