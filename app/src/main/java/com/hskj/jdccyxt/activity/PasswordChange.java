package com.hskj.jdccyxt.activity;

import java.util.ArrayList;
import java.util.List;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.StaticData;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @������PasswordChange
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ��������
 * @�������ڣ�2016.04.12
 */
public class PasswordChange extends Activity {
	private TextView txt_version, txt_viewer_name;
	private EditText original_password, new_password, confirm_password;
	private MyApplication app;
	private ProgressDialog progressDialog = null;
	private String ip, jgbh, jkxlh, userID, userName;
	private int PASSWORD_DIALOG_SHOW = StaticConst.PASSWORDCHANGE_PASSWORD_DIALOG_SHOW;
	private int PASSWORD_DIALOG_DISMISS = StaticConst.PASSWORDCHANGE_PASSWORD_DIALOG_DISMISS;
	private int PASSWORD_DIALOG_FAILURE = StaticConst.PASSWORDCHANGE_PASSWORD_DIALOG_FAILURE;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == PASSWORD_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("�����ϴ��У����Եȡ�����", PasswordChange.this);
				progressDialog.show();
			}
			if (msg.what == PASSWORD_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == PASSWORD_DIALOG_FAILURE) {
				DialogTool.getTextDialog(msg.obj.toString(), PasswordChange.this);
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.password_changetion);
		setInitialization();
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		userID = getIntent().getStringExtra("USERID");
		userName = getIntent().getStringExtra("USERNAME");
		// ���ñ���
		app = (MyApplication) getApplication();
		txt_version.setText(app.getVersion());
		txt_viewer_name.setText(app.getViewerName());
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setInitialization() {
		txt_version = (TextView) this.findViewById(R.id.txt_version);
		txt_viewer_name = (TextView) this.findViewById(R.id.txt_viewer_name);
		original_password = (EditText) this.findViewById(R.id.original_password);
		new_password = (EditText) this.findViewById(R.id.new_password);
		confirm_password = (EditText) this.findViewById(R.id.confirm_password);
	}

	/**
	 * ��ť����¼�
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		// ����
		case R.id.changetion_back:
			PasswordChange.this.finish();
			break;
		// ��������ύ
		case R.id.modification:
			final String original_password_data = original_password.getText().toString().trim();
			final String new_password_data = new_password.getText().toString().trim();
			final String confirm_password_data = confirm_password.getText().toString().trim();
			if (!original_password_data.equals("") && !new_password_data.equals("") && !confirm_password_data.equals("")) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							handler.sendEmptyMessage(PASSWORD_DIALOG_SHOW);
							// ��01JO1�ӿ�
							/*String data01J01 = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "01J01",
									get01J01Data(original_password_data, new_password_data, userName), StaticValues.writeResult,
									StaticValues.timeoutFive, StaticValues.numberFour);*/
							//����
							Thread.sleep(1000);
							String data01J01 = StaticData.Data01J01;
							Log.i("AAA", "data01J01:" + data01J01);
							List<CodeDomain> codelists01J01 = XMLParsingMethods.saxcode(data01J01);
							handler.sendEmptyMessage(PASSWORD_DIALOG_DISMISS);
							setmessage(codelists01J01.get(0).getMessage());
						} catch (Exception e) {
							handler.sendEmptyMessage(PASSWORD_DIALOG_DISMISS);
							e.printStackTrace();
						}
					}
				}).start();
			} else {
				Toast.makeText(PasswordChange.this, "ԭʼ����,�������뼰ȷ�����붼����Ϊ�գ�", Toast.LENGTH_LONG).show();
			}
			break;

		}
	}

	/**
	 * ��ȡ�����ύ01J01����
	 */
	private String get01J01Data(String passwordData, String newpasswordData, String userNameData) {
		String imei = ((TelephonyManager) PasswordChange.this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		imei = "E0E18651D1DC7C90";
		List<String> list = new ArrayList<String>();
		list.add(userID);
		list.add(passwordData);
		list.add(newpasswordData);
		list.add(userNameData);
		list.add(imei);
		String xmlData = CombinationXMLData.getWriteData(StaticValues.FieldName01J01, list);
		Log.i("AAA", "01J01xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * ��Ϣ����
	 */
	public void setmessage(String matter) {
		Message msg = new Message();
		msg.what = PASSWORD_DIALOG_FAILURE;
		msg.obj = matter;
		handler.sendMessage(msg);
	}
}
