package com.hskj.jdccyxt.loginunit;

import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.toolunit.StaticValues;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

/**
 * @类名：ParameterSetting
 * @author:广东泓胜科技有限公司
 * @实现功能：联网参数设置
 * @创建日期：2016.04.18
 */
public class ParameterSetting extends Activity {
	private EditText ed_ip, ed_jgbh, ed_wjyq_ip, ed_wjyq_dkh;
	private String info = StaticValues.ADDRESS_SETTING;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.parameter_setting);
		setInitialization();
		SharedPreferences get_preferences = getSharedPreferences(info, 0);
		ed_ip.setText(get_preferences.getString("IP", ""));
		ed_jgbh.setText(get_preferences.getString("JGBH", ""));
		ed_wjyq_ip.setText(get_preferences.getString("WJYQIP", ""));
		ed_wjyq_dkh.setText(get_preferences.getString("WJYQDK", ""));

	}

	/**
	 * 初始化控件
	 */
	private void setInitialization() {
		ed_ip = (EditText) this.findViewById(R.id.ed_ip);
		ed_jgbh = (EditText) this.findViewById(R.id.ed_jgbh);
		ed_wjyq_ip = (EditText) this.findViewById(R.id.ed_wjyq_ip);
		ed_wjyq_dkh = (EditText) this.findViewById(R.id.ed_wjyq_dkh);
	}

	/**
	 * 按钮点击事件
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.setting_back:
			ParameterSetting.this.finish();
			break;
		// 初始化设置
		case R.id.but_setting:
			String ip_data = ed_ip.getText().toString().trim();
			String jgbh_data = ed_jgbh.getText().toString().trim();
			String wjyq_ip_data = ed_wjyq_ip.getText().toString().trim();
			String wjyq_dkh_data = ed_wjyq_dkh.getText().toString().trim();
			SharedPreferences set_preferences = getSharedPreferences(info, 0);
			Editor editor = set_preferences.edit();
			editor.putString("IP", ip_data);
			editor.putString("JGBH", jgbh_data);
			editor.putString("JKXLH", "A");
			editor.putString("WJYQIP", wjyq_ip_data);
			editor.putString("WJYQDK", wjyq_dkh_data);
			editor.commit();
			ParameterSetting.this.finish();
			break;
		}
	}
}
