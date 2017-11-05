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
 * @������UserLogin
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ��û���¼
 * @�������ڣ�2016.04.12
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
				progressDialog = DialogTool.getProgressDialog("���ڵ�½�У����Եȡ�����", UserLogin.this);
				progressDialog.show();
			}
			if (msg.what == TIME_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == TIME_DOWNLOAD_DIALOG_SHOW) {
				downloadProgressDialog = DialogTool.getDownloadProgressBarDialog("�������ظ��°�,�����ĵ���......", UserLogin.this);
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
				DialogTool.setSelectHandlerDialog("�ð汾Ŀǰ�������µģ��Ƿ���£�", TIME_DOWNLOAD_APK, handler, UserLogin.this);
			}
			if (msg.what == TIME_DOWNLOAD_APK) {
				APKDownloadPath = "http://" + ip + ":8011/����������ϵͳ.apk";
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
		// ���ݿ�
		SQLFuntion.initTable(UserLogin.this);
		// deleteSOL();
		DeleteSQLData();
		deleteFile(new File(photoSubcatalog));
		version_number.setText(getAPPVersionCodeFromAPP(UserLogin.this));
	}

	/**
	 * �����ݿ�����ȫ��ɾ��
	 */
	public void deleteSOL() {
		ArrayList<HashMap<String, String>> list = SQLFuntion.query(UserLogin.this, null, null);
		for (int i = 0; i < list.size(); i++) {
			Object[] data = { list.get(i).get("jylsh") };
			SQLFuntion.delete(UserLogin.this, data);
		}
	}

	/**
	 * ɾ����Ƭ
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
	 * ɾ�����ݿ�����
	 */
	private void DeleteSQLData() {
		ArrayList<HashMap<String, String>> sqldatas = SQLFuntion.query(UserLogin.this, null, null);
		for (int i = 0; i < sqldatas.size(); i++) {
			try {
				// �������ݿ�ʱ���ʱ���
				String sqltime1 = TimeTool.getTiemData();
				String sqltime2 = sqldatas.get(i).get("pssj");
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d1 = df.parse(sqltime1);
				Date d2 = df.parse(sqltime2);
				long sqlsjc = (d1.getTime() - d2.getTime()) / 1000;
				if (sqlsjc >= 604800) {
					Object[] data = { sqldatas.get(i).get("jylsh") };
					SQLFuntion.delete(UserLogin.this, data);
					Log.i("AAA", sqldatas.get(i).get("jylsh") + "����ˮ��������ɾ��");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setInitialization() {
		version_number = (TextView) this.findViewById(R.id.version_number);
		user_name = (EditText) this.findViewById(R.id.user_name);
		password = (EditText) this.findViewById(R.id.password);
	}

	/**
	 * ��ť����¼�
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		// ��������
		case R.id.setting:
			Intent intent1 = new Intent(UserLogin.this, ParameterSetting.class);
			startActivity(intent1);
			break;

		// ��¼
		case R.id.login:
			final String user_name_data = user_name.getText().toString().trim();
			final String password_data = password.getText().toString().trim();

			if (!ip.equals("") && !jgbh.equals("")) {
				if (user_name_data.equals("") || password_data.equals("")) {
					Toast.makeText(this, "��ȷ���û����������Ƿ�Ϊ�գ�", Toast.LENGTH_SHORT).show();
				} else {
					if (is01C06FileExist()) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									handler.sendEmptyMessage(TIME_DIALOG_SHOW);
									// ��01C50�ӿ�
									/* String data01C50 = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh,
											"01C50", get01C50Data(), StaticValues.queryResult, StaticValues.timeoutThree,
											StaticValues.numberFive); */
									//����
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
											// ��01C09�ӿ�
											/*String data01C09 = ConnectMethods.connectWebService(ip, StaticValues.queryObject,
													jkxlh, "01C09", get01C09Data(user_name_data, password_data, jgbh),
													StaticValues.queryResult, StaticValues.timeoutFive, StaticValues.numberThree);*/
											//����
											Thread.sleep(250);
											String data01C09 = StaticData.Data01C09;
											Log.i("AAA", "data01C09:" + data01C09);
											List<CodeDomain> codelists01C09 = XMLParsingMethods.saxcode(data01C09);
											handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
											if (!codelists01C09.get(0).getCode().equals("1")) {
												if (codelists01C09.get(0).getMessage().indexOf("��ʹ��PDA�汾Ϊ") != -1) {
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
											String data = "������ʱ��Ϊ��" + timelists.get(0).getSj() + "������������ϵͳʱ�䣬��Ҫ����60�룡";
											setmessage(data);
										}
									}

								} catch (Exception e) {
									handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
									setmessage("��������ʱ����鿴���磡");
									e.printStackTrace();
								}
							}
						}).start();
					} else {
						DialogTool.getTextDialog("�����ļ��������ֶ���ӣ�", UserLogin.this);
					}
				}
			} else {
				Toast.makeText(this, "��ȷ��IP��ַ����������Ƿ�Ϊ�գ�", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	/**
	 * ���°�װ��
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
				long file_length = response.getEntity().getContentLength();// �ļ��ܳ���
				int total_length = 0;
				byte[] data = new byte[1024];
				int len = 0;
				if (response.getStatusLine().getStatusCode() == 200) {
					inputStream = response.getEntity().getContent();
					while ((len = inputStream.read(data)) != -1) {
						total_length += len;
						int progress_value = (int) ((total_length / (float) file_length) * 100);
						publishProgress(progress_value);// �����̶ȵ�λ
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
					FileOutputStream fos = new FileOutputStream(UpdataSubcatalog + "����������ϵͳ.apk");
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
	 * �����ļ��Ƿ�Ϊ��
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
	 * ��ȡ��ǰ�ֻ��汾��
	 */
	public String getAPPVersionCodeFromAPP(Context ctx) {
		// int currentVersionCode = 0;
		String appVersionName = "";
		PackageManager manager = ctx.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
			appVersionName = info.versionName; // �汾��
			// currentVersionCode = info.versionCode; // �汾��
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appVersionName;
	}

	/**
	 * ��ȡ�����ύ01C06����
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
	 * ��ȡ�����ύ01C50����
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
	 * ��ȡ�����ύ01C09����
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
	 * ��Ϣ����
	 */
	public void setmessage(String matter) {
		Message msg = new Message();
		msg.what = LOGIN_DIALOG_FAILURE;
		msg.obj = matter;
		handler.sendMessage(msg);
	}

	/**
	 * ��ʾToast
	 */
	public void setToastShow(String matter) {
		Toast.makeText(UserLogin.this, matter, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ��ȡʱ���
	 */
	public int getTimeDifference(String timeOne) throws java.text.ParseException {
		long timeDif = 0;
		String timeTwo = TimeTool.getTiemData();
		try {
			// ����ʱ���
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
	 * ��װ�°汾Ӧ��
	 */
	private void installApp() {
		try {
			File appFile = new File(UpdataSubcatalog + "����������ϵͳ.apk");
			if (!appFile.exists()) {
				return;
			}
			// ��ת���°汾Ӧ�ð�װҳ��
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
		// ��ȡ�����е�����
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
						//����
						Thread.sleep(1000);
						// ��01C06�ӿ�
						/*String data01C06 = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh, "01C06",
								get01C06Data(), StaticValues.queryResult, StaticValues.timeoutFive, StaticValues.numberThree);
						Log.i("AAA", "data01C06:" + data01C06);
						List<CodeDomain> codelists01C06 = XMLParsingMethods.saxcode(data01C06);*/
						handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
						/*if (!codelists01C06.get(0).getCode().equals("1")) {
							setmessage(codelists01C06.get(0).getMessage());
						} else {
							// ��ԭʼ����д���ĵ�����
							DocumentTool.addFolder(rootDirectory + "/CllxEndZpsm");
							DocumentTool.addFiles(rootDirectory + "/CllxEndZpsm/01C06.txt");
							DocumentTool.writeData(rootDirectory + "/CllxEndZpsm/01C06.txt", data01C06);
							setmessage("�����ļ�������ϣ�");
						}*/
					} catch (Exception e) {
						handler.sendEmptyMessage(TIME_DIALOG_DISMISS);
						setmessage("��������ʱ����鿴���磡");
						e.printStackTrace();
					}
				}
			}).start();
			isRequest = false;
		}
	}
}
