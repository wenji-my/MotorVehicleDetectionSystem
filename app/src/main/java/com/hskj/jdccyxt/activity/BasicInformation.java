package com.hskj.jdccyxt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.C02Domain;
import com.hskj.jdccyxt.domain.C05DomainBaseInfo;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.MessageTool;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @类名：BasicInformation
 * @author:广东泓胜科技有限公司
 * @实现功能：车辆基本信息
 * @创建日期：2016.05.27
 */
public class BasicInformation extends Activity {
	private ListView list_information;
	private TextView text_information;
	private String data01C05;
	private List<C05DomainBaseInfo> baseInfolists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.information);
		data01C05 = getIntent().getStringExtra("C05DATA");
		list_information = (ListView) this.findViewById(R.id.list_information);
		text_information = (TextView) this.findViewById(R.id.text_information);
		List<CodeDomain> codelists01C05 = XMLParsingMethods.saxcode(data01C05);
		if (codelists01C05.get(0).getCode().equals("1")) {
			baseInfolists = XMLParsingMethods.saxBasicInformation(data01C05);
			list_information.setAdapter(getAdapter());
			list_information.setVisibility(View.VISIBLE);
		} else {
			text_information.setText(codelists01C05.get(0).getMessage());
			text_information.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * listview界面显示
	 */
	private SimpleAdapter getAdapter() {
		ArrayList<HashMap<String, Object>> list = hashmapData(baseInfolists);
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(BasicInformation.this, list, R.layout.information_item, new String[] {
				"name", "matter" }, new int[] { R.id.info_name, R.id.info_matter });
		return mSimpleAdapter;
	}

	/**
	 * listview获取ArrayList<HashMap<String, Object>>数据
	 * 
	 * @param vehicleDatatwo
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> hashmapData(List<C05DomainBaseInfo> baseInfoData) {
		ArrayList<HashMap<String, Object>> listdata = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 57; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", baseInfoData.get(i).getInfonMame());
			map.put("matter", baseInfoData.get(i).getInfoMatter());
			listdata.add(map);
		}
		return listdata;
	};
}
