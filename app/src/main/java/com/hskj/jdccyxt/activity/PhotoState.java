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
 * @������PhotoState
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ���Ƭ�ϴ�����鿴
 * @�������ڣ�2016.05.05
 */
@SuppressLint("HandlerLeak")
public class PhotoState extends Activity {
	private EditText ed_lsh;
	private RadioGroup uploadStatus;
	private ListView photo_list;
	private ProgressDialog progressDialog = null;
	private boolean statusValue = false;// ��ѡ��״̬
	private int STATE_DIALOG_SHOW = StaticConst.PHOTOSTATE_STATE_DIALOG_SHOW;
	private int STATE_DIALOG_DISMISS = StaticConst.PHOTOSTATE_STATE_DIALOG_DISMISS;
	private int STATE_DIALOG_FAILURE = StaticConst.PHOTOSTATE_STATE_DIALOG_FAILURE;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == STATE_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("���ڼ����У����Եȡ�����", PhotoState.this);
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
	 * ����listview������
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
	 * ��ȡ����listview������
	 * 
	 * @param condition
	 *            :��ѯ����
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
		// ���ݹ���
		ArrayList<HashMap<String, String>> newlist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			if (newlist.size() == 0) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("jylsh", list.get(i).get("jylsh"));
				map.put("zpmc", list.get(i).get("zpmc"));
				map.put("pssj", list.get(i).get("pssj"));
				newlist.add(map);
			} else {
				boolean ifxd = false;// ����ˮ���Ƿ����newlist��
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
	 * ��ʼ���ؼ�
	 */
	private void setInitialization() {
		ed_lsh = (EditText) this.findViewById(R.id.ed_lsh);
		uploadStatus = (RadioGroup) this.findViewById(R.id.uploadStatus);
		photo_list = (ListView) this.findViewById(R.id.photo_list);

	}

	public void doClick(View v) {
		switch (v.getId()) {
		// ����
		case R.id.state_back:
			PhotoState.this.finish();
			break;
		// ˢ��
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
