package com.hskj.jdccyxt.loginunit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.activity.Function;
import com.hskj.jdccyxt.domain.C09Domain;
import com.hskj.jdccyxt.domain.C50Domain;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.DocumentTool;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.StaticData;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.TimeTool;
import com.hskj.jdccyxt.toolunit.sql.SQLFuntion;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @类名：UserLogin
 * @author:广东泓胜科技有限公司
 * @实现功能：用户登录
 * @创建日期：2016.04.12
 */
@SuppressLint({ "SimpleDateFormat", "ShowToast" })
public class UserLogin extends Activity {
	private TextView version_number;
	private EditText user_name, password;
	private ProgressDialog progressDialog = null;
	private ProgressDialog downloadProgressDialog = null;
	private String ip, jgbh, jkxlh;
	private String APKDownloadPath = "";
	private boolean isRequest = true;
	private String rootDirectory = StaticValues.rootDirectory;
	private String photoSubcatalog = StaticValues.photoSubcatalog;
	private String UpdataSubcatalog = StaticValues.UpdataSubcatalog;
	private int TIME_DIALOG_SHOW = StaticConst.USERLOGIN_TIME_DIALOG_SHOW;
	private int TIME_DIALOG_DISMISS = StaticConst.USERLOGIN_TIME_DIALOG_DISMISS;
	private int LOGIN_DIALOG_FAILURE = StaticConst.USERLOGIN_LOGIN_DIALOG_FAILURE;
	private int LOGIN_REFRESH_UI = StaticConst.USERLOGIN_LOGIN_REFRESH_UI;
	private int TIME_DOWNLOAD_DIALOG_SHOW = StaticConst.USERLOGIN_TIME_DOWNLOAD_DIALOG_SHOW;
	private int TIME_DOWNLOAD_DIALOG_DISMISS = StaticConst.USERLOGIN_TIME_DOWNLOAD_DIALOG_DISMISS;
	private int TIME_IS_DOWNLOAD = StaticConst.USERLOGIN_TIME_IS_DOWNLOAD;
	private int TIME_DOWNLOAD_APK = StaticConst.USERLOGIN_TIME_DOWNLOAD_APK;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == TIME_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("正在登陆中，请稍等。。。", UserLogin.this);
				progressDialog.show();
			}
			if (msg.what == TIME_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == TIME_DOWNLOAD_DIALOG_SHOW) {
				downloadProgressDialog = DialogTool.getDownloadProgressBarDialog("正在下载更新包,请耐心等收......", UserLogin.this);
				downloadProgressDialog.show();
			}
			if (msg.what == TIME_DOWNLOAD_DIALOG_DISMISS) {
				downloadProgressDialog.dismiss();
			}
			if (msg.what == LOGIN_DIALOG_FAILURE) {
				DialogTool.getTextDialog(msg.obj.toString(), UserLogin.this);
			}
			if (msg.what == LOGIN_REFRESH_UI) {
				setToastShow(msg.obj.toString());
			}
			if (msg.what == TIME_IS_DOWNLOAD) {
				DialogTool.setSelectHandlerDialog("该版本目前不是最新的，是否更新？", TIME_DOWNLOAD_APK, handler, UserLogin.this);
			}
			if (msg.what == TIME_DOWNLOAD_APK) {
				APKDownloadPath = "http://" + ip + ":8011/机动车查验系统.apk";
				new MyAPKDownloadTask().execute(APKDownloadPath);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_login);
		setInitialization();
		// 数据库
		SQLFuntion.initTable(UserLogin.this);
		// deleteSOL();
		DeleteSQLData();
		deleteFile(new File(photoSubcatalog));
		version_number.setText(getAPPVersionCodeFromAPP(UserLogin.this));
	}

	/**
	 * 将数据库数据全部删除
	 */
	public void deleteSOL() {
		ArrayList<HashMap<String, String>> list = SQLFuntion.query(UserLogin.this, null, null);
		for (int i = 0; i < list.size(); i++) {
			Object[] data = { list.get(i).get("jylsh") };
			SQLFuntion.delete(UserLogin.this, data);
		}
	}

	/**
	 * 删除照片
	 */
	private void deleteFile(final File file) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File[] oldfiles = file.listFiles();
					if (oldfiles != null) {
						for (int i = 0; i < oldfiles.length; i++) {
							File fileTemp = oldfiles[i];
							long timeone = fileTemp.lastModified();
							SimpleDateFormat dftwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							long timetwo = dftwo.parse(TimeTool.getTwoTiem() + " 00:00:00").getTime();
							if (timetwo > timeone) {
								DocumentTool.deleteFiles(fileTemp);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 删除数据库数据
	 */
	private void DeleteSQLData() {
		ArrayList<HashMap<String, String>> sqldatas = SQLFuntion.query(UserLogin.this, null, null);
		for (int i = 0; i < sqldatas.size(); i++) {
			try {
				// 计算数据库时间的时间差
				String sqltime1 = TimeTool.getTiemData();
				String sqltime2 = sqldatas.get(i).get("pssj");
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d1 = df.parse(sqltime1);
				Date d2 = df.parse(sqltime2);
				long sqlsjc = (d1.getTime() - d2.getTime()) / 1000;
				if (sqlsjc >= 604800) {
					Object[] data = { sqldatas.get(i).get("jylsh") };
					SQLFuntion.delete(UserLogin.this, data);
					Log.i("AAA", sqldatas.get(i).get("jylsh") + "的流水号数据已删除");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化控件
	 */
	private void setInitialization() {
		version_number = (TextView) this.findViewById(R.id.version_number);
		user_name = (EditText) this.findViewById(R.id.user_name);
		password = (EditText) this.findViewById(R.id.password);
	}

	/**
	 * 按钮点击事件
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		// 参数设置
		case R.id.setting:
			Intent intent1 = new Intent(UserLogin.this, ParameterSetting.class);
			startActivity(intent1);
			break;

		// 登录
		case R.id.login:
			final String user_name_data = user_name.getText().toString().trim();
			final String password_data = password.getText().toString().trim();

			if (!ip.equals("") && !jgbh.equals("")) {
				if (user_name_data.equals("") || password_data.equals("")) {
					Toast.makeText(this, "请确认用户名及密码是否为空！", Toast.LENGTH_SHORT).show();
				} else {
					if (is01C06FileExist()) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									handler.sendEmptyMessage(TIME_DIALOG_SHOW);
									// 调01C50接口
									/* String data01C50 = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh,
											"01C50", get01C50Data(), StaticValues.queryResult, StaticValues.timeoutThree,
											StaticValues.numberFive); */
									//离线
									Thread.sleep(250);
									String data01C50 = StaticData.Data01C50;
									Log.i("AAA", "data01C50:" + data01C50);
									List<CodeDomain> codelists01c50 = XMLParsingMethods.saxcode(data01C50);
									if (!codelists01c50.get(0).getCode().equals("1")) {
										handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
										setmessage(codelists01c50.get(0).getMessage());
									} else {
										List<C50Domain> timelists = XMLParsingMethods.saxtime(data01C50);
										int timeDiff = getTimeDifference(timelists.get(0).getSj());
										if (0 <= timeDiff || timeDiff <= 60) {
											// 调01C09接口
											/*String data01C09 = ConnectMethods.connectWebService(ip, StaticValues.queryObject,
													jkxlh, "01C09", get01C09Data(user_name_data, password_data, jgbh),
													StaticValues.queryResult, StaticValues.timeoutFive, StaticValues.numberThree);*/
											//离线
											Thread.sleep(250);
											String data01C09 = StaticData.Data01C09;
											Log.i("AAA", "data01C09:" + data01C09);
											List<CodeDomain> codelists01C09 = XMLParsingMethods.saxcode(data01C09);
											handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
											if (!codelists01C09.get(0).getCode().equals("1")) {
												if (codelists01C09.get(0).getMessage().indexOf("请使用PDA版本为") != -1) {
													handler.sendEmptyMessage(TIME_IS_DOWNLOAD);
												} else {
													setmessage(codelists01C09.get(0).getMessage());
												}
											} else {
												List<C09Domain> loginlists = XMLParsingMethods.saxuserlogin(data01C09);
												Intent intent2 = new Intent(UserLogin.this, Function.class);
												intent2.putExtra("USERID", user_name_data);
												Bundle bundle2 = new Bundle();
												bundle2.putSerializable("operatorObj", (Serializable) loginlists);
												intent2.putExtras(bundle2);
												startActivity(intent2);
												UserLogin.this.finish();
											}
										} else {
											handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
											String data = "服务器时间为：" + timelists.get(0).getSj() + "，请重新设置系统时间，相差不要超过60秒！";
											setmessage(data);
										}
									}

								} catch (Exception e) {
									handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
									setmessage("网络请求超时，请查看网络！");
									e.printStackTrace();
								}
							}
						}).start();
					} else {
						DialogTool.getTextDialog("配置文件出错，请手动添加！", UserLogin.this);
					}
				}
			} else {
				Toast.makeText(this, "请确认IP地址及机构编号是否为空！", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	/**
	 * 更新安装包
	 * 
	 * @author lenovo
	 * 
	 */
	public class MyAPKDownloadTask extends AsyncTask<String, Integer, byte[]> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			handler.sendEmptyMessage(TIME_DOWNLOAD_DIALOG_SHOW);
		}

		@Override
		protected byte[] doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			byte[] result = null;
			InputStream inputStream = null;
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				HttpResponse response = httpClient.execute(httpGet);
				long file_length = response.getEntity().getContentLength();// 文件总长度
				int total_length = 0;
				byte[] data = new byte[1024];
				int len = 0;
				if (response.getStatusLine().getStatusCode() == 200) {
					inputStream = response.getEntity().getContent();
					while ((len = inputStream.read(data)) != -1) {
						total_length += len;
						int progress_value = (int) ((total_length / (float) file_length) * 100);
						publishProgress(progress_value);// 发布刻度单位
						outputStream.write(data, 0, len);
					}
				}
				result = outputStream.toByteArray();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			downloadProgressDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(byte[] result) {
			super.onPostExecute(result);
			try {
				File file = new File(UpdataSubcatalog);
				if (!file.exists())
					file.mkdirs();
				if (result != null) {
					FileOutputStream fos = new FileOutputStream(UpdataSubcatalog + "机动车查验系统.apk");
					fos.write(result, 0, result.length);
					fos.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessage(TIME_DOWNLOAD_DIALOG_DISMISS);
			installApp();
		}
	}

	/**
	 * 配置文件是否为空
	 * 
	 * @return
	 */
	public boolean is01C06FileExist() {
		try {
			String c06path = Environment.getExternalStorageDirectory().getCanonicalPath() + rootDirectory
					+ "/CllxEndZpsm/01C06.txt";
			File file = new File(c06path);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取当前手机版本号
	 */
	public String getAPPVersionCodeFromAPP(Context ctx) {
		// int currentVersionCode = 0;
		String appVersionName = "";
		PackageManager manager = ctx.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
			appVersionName = info.versionName; // 版本名
			// currentVersionCode = info.versionCode; // 版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appVersionName;
	}

	/**
	 * 获取即将提交01C06数据
	 */
	private String get01C06Data() {
		String imei = ((TelephonyManager) UserLogin.this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		imei = "E0E18651D1DC7C90";
		List<String> list = new ArrayList<String>();
		list.add(imei);
		String xmlData = CombinationXMLData.getQueryData(StaticValues.FieldName01C06, list);
		Log.i("AAA", "01C06xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * 获取即将提交01C50数据
	 */
	private String get01C50Data() {
		String imei = ((TelephonyManager) UserLogin.this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		imei = "E0E18651D1DC7C90";
		List<String> list = new ArrayList<String>();
		list.add(imei);
		String xmlData = CombinationXMLData.getQueryData(StaticValues.FieldName01C50, list);
		Log.i("AAA", "01C50xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * 获取即将提交01C09数据
	 */
	private String get01C09Data(String userid, String password, String jyjgbh) {
		String imei = ((TelephonyManager) UserLogin.this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		imei = "E0E18651D1DC7C90";
		List<String> list = new ArrayList<String>();
		list.add(userid);
		list.add(password);
		list.add(jyjgbh);
		list.add(imei);
		list.add(getAPPVersionCodeFromAPP(UserLogin.this) + "");
		String xmlData = CombinationXMLData.getQueryData(StaticValues.FieldName01C09, list);
		Log.i("AAA", "01C09xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * 消息发送
	 */
	public void setmessage(String matter) {
		Message msg = new Message();
		msg.what = LOGIN_DIALOG_FAILURE;
		msg.obj = matter;
		handler.sendMessage(msg);
	}

	/**
	 * 显示Toast
	 */
	public void setToastShow(String matter) {
		Toast.makeText(UserLogin.this, matter, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获取时间差
	 */
	public int getTimeDifference(String timeOne) throws java.text.ParseException {
		long timeDif = 0;
		String timeTwo = TimeTool.getTiemData();
		try {
			// 计算时间差
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = df.parse(timeOne);
			Date d2 = df.parse(timeTwo);
			timeDif = (d1.getTime() - d2.getTime()) / 1000;
			Log.i("AAA", "timeDif:" + timeDif);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int) timeDif;
	}

	/**
	 * 安装新版本应用
	 */
	private void installApp() {
		try {
			File appFile = new File(UpdataSubcatalog + "机动车查验系统.apk");
			if (!appFile.exists()) {
				return;
			}
			// 跳转到新版本应用安装页面
			Intent apkintent = new Intent(Intent.ACTION_VIEW);
			apkintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			apkintent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
			startActivity(apkintent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 提取缓存中的数据
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		if (isRequest) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						handler.sendEmptyMessage(TIME_DIALOG_SHOW);
						//离线
						Thread.sleep(1000);
						// 调01C06接口
						/*String data01C06 = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh, "01C06",
								get01C06Data(), StaticValues.queryResult, StaticValues.timeoutFive, StaticValues.numberThree);
						Log.i("AAA", "data01C06:" + data01C06);
						List<CodeDomain> codelists01C06 = XMLParsingMethods.saxcode(data01C06);*/
						handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
						/*if (!codelists01C06.get(0).getCode().equals("1")) {
							setmessage(codelists01C06.get(0).getMessage());
						} else {
							// 将原始数据写入文档保存
							DocumentTool.addFolder(rootDirectory + "/CllxEndZpsm");
							DocumentTool.addFiles(rootDirectory + "/CllxEndZpsm/01C06.txt");
							DocumentTool.writeData(rootDirectory + "/CllxEndZpsm/01C06.txt", data01C06);
							setmessage("配置文件加载完毕！");
						}*/
					} catch (Exception e) {
						handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
						setmessage("网络请求超时，请查看网络！");
						e.printStackTrace();
					}
				}
			}).start();
			isRequest = false;
		}
	}
}
