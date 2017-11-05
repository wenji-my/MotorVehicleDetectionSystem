package com.hskj.jdccyxt.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.sql.SQLFuntion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @类名：PhotoState
 * @author:广东泓胜科技有限公司
 * @实现功能：照片上传情况查看
 * @创建日期：2016.05.05
 */
@SuppressLint("HandlerLeak")
public class PhotoState extends Activity {
	private EditText ed_lsh;
	private RadioGroup uploadStatus;
	private ListView photo_list;
	private ProgressDialog progressDialog = null;
	private boolean statusValue = false;// 单选框状态
	private int STATE_DIALOG_SHOW = StaticConst.PHOTOSTATE_STATE_DIALOG_SHOW;
	private int STATE_DIALOG_DISMISS = StaticConst.PHOTOSTATE_STATE_DIALOG_DISMISS;
	private int STATE_DIALOG_FAILURE = StaticConst.PHOTOSTATE_STATE_DIALOG_FAILURE;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == STATE_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("正在加载中，请稍等。。。", PhotoState.this);
				progressDialog.show();
			}
			if (msg.what == STATE_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == STATE_DIALOG_FAILURE) {
				DialogTool.getTextDialog(msg.obj.toString(), PhotoState.this);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_state);
		setInitialization();
		uploadStatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				int radioButtonId = arg0.getCheckedRadioButtonId();
				if (radioButtonId == R.id.statusS) {
					statusValue = true;
				} else {
					statusValue = false;
				}
			}
		});
		photo_list.setAdapter(getAdapter(null));
	}

	/**
	 * 构建listview适配器
	 * 
	 * @param object
	 * @return
	 */
	private ListAdapter getAdapter(String condition) {
		ArrayList<HashMap<String, String>> listData = getSQLData(condition);
		SimpleAdapter mSimpleAdapter = null;
		if (statusValue) {
			Collections.reverse(listData);
			mSimpleAdapter = new SimpleAdapter(PhotoState.this, listData, R.layout.photo_state_item_one, new String[] { "jylsh",
					"pssj", "zpmc" }, new int[] { R.id.item_lsh, R.id.item_time, R.id.item_zpmc });
		} else {
			mSimpleAdapter = new SimpleAdapter(PhotoState.this, listData, R.layout.photo_state_item_two, new String[] { "jylsh",
					"pssj", "zpmc" }, new int[] { R.id.item_lsh, R.id.item_time, R.id.item_zpmc });
		}
		return mSimpleAdapter;
	}

	/**
	 * 获取构建listview的数据
	 * 
	 * @param condition
	 *            :查询条件
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getSQLData(String condition) {
		String value = null;
		if (statusValue) {
			value = "03";
		} else {
			value = "02";
		}
		ArrayList<HashMap<String, String>> list = SQLFuntion.querytwo(PhotoState.this, condition, value);
		// 数据归类
		ArrayList<HashMap<String, String>> newlist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			if (newlist.size() == 0) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("jylsh", list.get(i).get("jylsh"));
				map.put("zpmc", list.get(i).get("zpmc"));
				map.put("pssj", list.get(i).get("pssj"));
				newlist.add(map);
			} else {
				boolean ifxd = false;// 该流水号是否存在newlist中
				for (int j = 0; j < newlist.size(); j++) {
					if (newlist.get(j).get("jylsh").equals(list.get(i).get("jylsh"))) {
						String mc = newlist.get(j).get("zpmc");
						newlist.get(j).put("zpmc", mc + "\n" + list.get(i).get("zpmc"));
						ifxd = true;
					}
				}
				if (!ifxd) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("jylsh", list.get(i).get("jylsh"));
					map.put("zpmc", list.get(i).get("zpmc"));
					map.put("pssj", list.get(i).get("pssj"));
					newlist.add(map);
				}
			}
		}
		return newlist;
	}

	/**
	 * 初始化控件
	 */
	private void setInitialization() {
		ed_lsh = (EditText) this.findViewById(R.id.ed_lsh);
		uploadStatus = (RadioGroup) this.findViewById(R.id.uploadStatus);
		photo_list = (ListView) this.findViewById(R.id.photo_list);

	}

	public void doClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.state_back:
			PhotoState.this.finish();
			break;
		// 刷新
		case R.id.but_refresh:
			handler.sendEmptyMessage(STATE_DIALOG_SHOW);
			String lshData = ed_lsh.getText().toString().trim();
			if (lshData.equals("")) {
				lshData = null;
			}
			photo_list.setAdapter(getAdapter(lshData));
			handler.sendEmptyMessage(STATE_DIALOG_DISMISS);
			break;

		}
	}
}
