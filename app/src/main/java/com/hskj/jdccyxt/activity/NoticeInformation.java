package com.hskj.jdccyxt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.C05DomainNoticeInfo;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @类名：NoticeInformation
 * @author:广东泓胜科技有限公司
 * @实现功能：车辆公告信息
 * @创建日期：2016.05.27
 */
public class NoticeInformation extends Activity {
	private ListView list_information;
	private TextView text_information;
	private String data01C05;
	private List<C05DomainNoticeInfo> noticeInfolists;

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
			noticeInfolists = XMLParsingMethods.saxNoticeInformation(data01C05);
			list_information.setAdapter(getAdapter());
			list_information.setVisibility(View.VISIBLE);
		} else {
			text_information.setText(codelists01C05.get(0).getMessage());
			text_information.setVisibility(View.VISIBLE);
		}

		//添加OBD数据对比验证
		list_information.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				new AlertDialog.Builder(getParent())
						.setTitle("提示框：")
						.setMessage("确定要进行车辆识别代号验证吗？")
						.setNegativeButton("取消", null)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(getParent(), OBDActivity.class);
								startActivity(intent);
							}
						})
						.show();

			}
		});
	}

	/**
	 * listview界面显示
	 */
	private SimpleAdapter getAdapter() {
		ArrayList<HashMap<String, Object>> list = hashmapData(noticeInfolists);
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(NoticeInformation.this, list, R.layout.information_item, new String[] {
				"name", "matter" }, new int[] { R.id.info_name, R.id.info_matter });
		return mSimpleAdapter;
	}

	/**
	 * listview获取ArrayList<HashMap<String, Object>>数据
	 * 
	 * @param vehicleDatatwo
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> hashmapData(List<C05DomainNoticeInfo> noticeInfolist) {
		ArrayList<HashMap<String, Object>> listdata = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < noticeInfolist.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", noticeInfolist.get(i).getInfonMame());
			map.put("matter", noticeInfolist.get(i).getInfoMatter());
			listdata.add(map);
		}
		return listdata;
	}
}
