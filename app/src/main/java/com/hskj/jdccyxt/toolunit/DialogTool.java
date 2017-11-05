package com.hskj.jdccyxt.toolunit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

/**
 * @������DialogTool
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ��Ի���
 * @�������ڣ�2016.04.19
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
		progressDialog.setTitle("��ʾ");
		progressDialog.setCancelable(false);
		progressDialog.setMessage(matter);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		return progressDialog;
	}

	public static void getTextDialog(String matter, Context context) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setTitle("��ʾ��");
		builder1.setMessage(matter);
		builder1.setPositiveButton("ȷ��", null);
		builder1.setCancelable(false);
		builder1.show();
	}

	public static void setSelectDialog(String matter, final Context context) {
		AlertDialog.Builder tcbuilder = new AlertDialog.Builder(context);
		tcbuilder.setTitle("��ʾ��");
		tcbuilder.setMessage(matter);
		tcbuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				((Activity) context).finish();
			}
		});
		tcbuilder.setNegativeButton("ȡ��", null);
		tcbuilder.setCancelable(false);
		tcbuilder.show();
	}

	public static void setPositiveDialog(String matter, final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("��ʾ��");
		builder.setMessage(matter);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
		builder.setTitle("��ʾ��");
		builder.setMessage(matter);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				handler.sendEmptyMessage(constant);
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.setCancelable(false);
		builder.show();
	}
}
