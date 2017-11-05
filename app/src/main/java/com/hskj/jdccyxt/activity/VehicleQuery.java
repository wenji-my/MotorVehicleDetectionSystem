package com.hskj.jdccyxt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.C02Domain;
import com.hskj.jdccyxt.domain.C09Domain;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.MessageTool;
import com.hskj.jdccyxt.toolunit.MutualConversionNameID;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.StaticData;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import com.zxing.scanner.android.CaptureActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @������VehicleQuery
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ�������ѯ
 * @�������ڣ�2016.04.12
 */
@SuppressLint("HandlerLeak")
public class VehicleQuery extends Activity {
	private TextView querition_title, txt_version, txt_viewer_name;
	private EditText querition_lsh;
	private ListView vehicle_list;
	private MyApplication app;
	private ProgressDialog progressDialog = null;
	private String ip, jgbh, jkxlh;
	private String cylxData;
	private String userID;
	private List<C09Domain> operators;// ����ĵ�¼Ա��Ϣ
	private List<C02Domain> vehiclelist;// ��01C02�ӿڻ��������
	private String[] titleName = StaticValues.titleName;
	private int REQUEST_CODE_SCAN = StaticValues.STATIC_REQUEST_CODE_SCAN;
	private String DECODED_CONTENT_KEY = StaticValues.STATIC_DECODED_CONTENT_KEY;
	private int QUERY_DIALOG_SHOW = StaticConst.VEHICLEQUERY_QUERY_DIALOG_SHOW;
	private int QUERY_DIALOG_DISMISS = StaticConst.VEHICLEQUERY_QUERY_DIALOG_DISMISS;
	private int QUERY_DIALOG_FAILURE = StaticConst.VEHICLEQUERY_QUERY_DIALOG_FAILURE;
	private int QUERY_LIST_SHOW = StaticConst.VEHICLEQUERY_QUERY_LIST_SHOW;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == QUERY_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("���������У����Եȡ�����", VehicleQuery.this);
				progressDialog.show();
			}
			if (msg.what == QUERY_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == QUERY_DIALOG_FAILURE) {
				DialogTool.getTextDialog(msg.obj.toString(), VehicleQuery.this);
			}
			if (msg.what == QUERY_LIST_SHOW) {
				vehicle_list.setAdapter(getAdapter());
				vehicle_list.setVisibility(View.VISIBLE);
			}

		};
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vehicle_querition);
		setInitialization();
		// ��ȡ�����е�����
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		// ��һ�����������
		Bundle bundle = this.getIntent().getExtras();
		operators = (List<C09Domain>) bundle.getSerializable("operatorObj");
		cylxData = getIntent().getStringExtra("CYLX");
		userID = getIntent().getStringExtra("USERID");
		// ��������
		app = (MyApplication) getApplication();
		txt_version.setText(app.getVersion());
		txt_viewer_name.setText(app.getViewerName());
		setTitleName(cylxData);
		vehicle_list.setOnItemClickListener(queryItemClick);
	}

	/**
	 * �б����¼�
	 */
	OnItemClickListener queryItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
			if (vehiclelist.get(arg2).getTs().equals("")) {
				CheckJump(arg2);
			} else {
				setDialog(arg2);
			}

		}
	};

	/**
	 * �Ի���
	 */
	private void setDialog(final int arg2) {
		AlertDialog.Builder tcbuilder = new AlertDialog.Builder(VehicleQuery.this);
		tcbuilder.setTitle("��ʾ��");
		tcbuilder.setMessage(vehiclelist.get(arg2).getTs() + "��ȷ���������в�����");
		tcbuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				CheckJump(arg2);
			}
		});
		tcbuilder.setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				VehicleQuery.this.finish();
			}
		});
		tcbuilder.setCancelable(false);
		tcbuilder.show();
	}

	/**
	 * item����¼���ת����������
	 */
	private void CheckJump(final int arg2) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Intent intent2 = new Intent();
					if (cylxData.equals("cy")) {// ����
						String data01C05 = get01C05InformationData(vehiclelist.get(arg2));
						intent2.setClass(VehicleQuery.this, CosmeticInspect.class);
						intent2.putExtra("USERID", userID);
						intent2.putExtra("C05DATA", data01C05);
					} else if (cylxData.equals("fcy")) {// ����
						intent2.setClass(VehicleQuery.this, PhotoShot.class);
						intent2.putExtra("STATE", "normaltwo");
					} else {// ��ʻ֤����
						intent2.setClass(VehicleQuery.this, PhotoShot.class);
						intent2.putExtra("STATE", "unnormal");
					}
					Bundle bundle2 = new Bundle();
					bundle2.putSerializable("operatorObj", operators.get(0));
					bundle2.putSerializable("vehicleQueryObj", vehiclelist.get(arg2));
					intent2.putExtras(bundle2);
					startActivity(intent2);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(QUERY_DIALOG_DISMISS);
					MessageTool.setmessage(QUERY_DIALOG_FAILURE, "��������ʱ����鿴���磡", handler);
				}
			}
		}).start();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setInitialization() {
		txt_version = (TextView) this.findViewById(R.id.txt_version);
		txt_viewer_name = (TextView) this.findViewById(R.id.txt_viewer_name);
		querition_title = (TextView) this.findViewById(R.id.querition_title);
		querition_lsh = (EditText) this.findViewById(R.id.querition_lsh);
		vehicle_list = (ListView) this.findViewById(R.id.vehicle_list);
	}

	/**
	 * ��������
	 */
	private void setTitleName(String newCylxData) {
		for (int i = 0; i < titleName.length; i++) {
			if (titleName[i].split(",")[0].equals(newCylxData)) {
				querition_title.setText(titleName[i].split(",")[1]);
			}
		}
	}

	/**
	 * ��ť����¼�
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.querition_back:
			VehicleQuery.this.finish();
			break;
		// ɨ��
		case R.id.querition_scanning:
			Intent intent1 = new Intent(VehicleQuery.this, CaptureActivity.class);
			startActivityForResult(intent1, REQUEST_CODE_SCAN);
			break;
		// ��ѯ
		case R.id.querition_query:
			call01C02Port();
			break;
		}
	}

	/**
	 * ��01C02�ӿ�
	 */
	public void call01C02Port() {
		final String lsh_data = querition_lsh.getText().toString().trim();
		if (lsh_data.equals("")) {
//			Toast.makeText(VehicleQuery.this, "��ˮ�Ų���Ϊ�գ�", Toast.LENGTH_LONG).show();
		} else {
			vehicle_list.setVisibility(View.GONE);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						handler.sendEmptyMessage(QUERY_DIALOG_SHOW);
						// ��01C02�ӿ�
//						String data01C02 = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh, "01C02",
//								get01C02Data(jgbh, lsh_data, cylxData, userID), StaticValues.queryResult,
//								StaticValues.timeoutFive, StaticValues.numberFour);
						//����
						Thread.sleep(500);
						String data01C02 =StaticData.Data01C02;
						Log.i("AAA", "data01C02:" + data01C02);
						List<CodeDomain> codelists01C02 = XMLParsingMethods.saxcode(data01C02);
						handler.sendEmptyMessage(QUERY_DIALOG_DISMISS);
						if (!codelists01C02.get(0).getCode().equals("1")) {
							MessageTool.setmessage(QUERY_DIALOG_FAILURE, codelists01C02.get(0).getMessage(), handler);
						} else {
							List<C02Domain> vehiclelists = XMLParsingMethods.saxVehicleQuerition(data01C02);
							vehiclelist = vehiclelists;
							handler.sendEmptyMessage(QUERY_LIST_SHOW);
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(QUERY_DIALOG_DISMISS);
						MessageTool.setmessage(QUERY_DIALOG_FAILURE, "��������ʱ����鿴���磡", handler);
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/**
	 * listview������ʾ
	 */
	public SimpleAdapter getAdapter() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		list = hashmapData(vehiclelist);
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(VehicleQuery.this, list, R.layout.vehicle_querition_list_item,
				new String[] { "hphm", "hpzl", "ywlx", "vin", "jclsh", "ts" }, new int[] { R.id.item_hphm, R.id.item_hpzl,
						R.id.item_ywlx, R.id.item_cjh, R.id.item_lsh, R.id.item_ts });
		return mSimpleAdapter;
	}

	/**
	 * listview��ȡArrayList<HashMap<String, Object>>����
	 * 
	 * @param vehicleDatatwo
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> hashmapData(List<C02Domain> vehicleDatatwo) {
		ArrayList<HashMap<String, Object>> listdata = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < vehicleDatatwo.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("hphm", vehicleDatatwo.get(i).getHphm());
			map.put("hpzl", MutualConversionNameID.getHpzlName(vehicleDatatwo.get(i).getHpzl()));
			map.put("ywlx", MutualConversionNameID.getYwlxName(vehicleDatatwo.get(i).getYwlx()));
			map.put("vin", vehicleDatatwo.get(i).getVin());
			map.put("jclsh", vehicleDatatwo.get(i).getJclsh());
			map.put("ts", vehicleDatatwo.get(i).getTs());
			listdata.add(map);
		}
		return listdata;
	}

	/**
	 * ��ȡ�����ύ01C02����
	 */
	private String get01C02Data(String jyjgbhData, String lshData, String cyxlData, String userIDData) {
		List<String> list = new ArrayList<String>();
		list.add(jyjgbhData);
		list.add(lshData);
		list.add(cyxlData);
		list.add(userIDData);
		String xmlData = CombinationXMLData.getQueryData(StaticValues.FieldName01C02, list);
		Log.i("AAA", "01C02xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * ��ȡ01C05δ��������
	 */
	private String get01C05InformationData(C02Domain vehicleData) {
		handler.sendEmptyMessage(QUERY_DIALOG_SHOW);
		// ��01C05�ӿ�
		/*String data01C05 = ConnectMethods
				.connectWebService(ip, StaticValues.queryObject, jkxlh, "01C05", get01C05Data(vehicleData.getJclsh()),
						StaticValues.queryResult, StaticValues.timeoutFive, StaticValues.numberFour);*/
		//����
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String data01C05 = StaticData.Data01C05;
		Log.i("AAA", "data01C05:" + data01C05);
		handler.sendEmptyMessage(QUERY_DIALOG_DISMISS);
		return data01C05;
	}

	/**
	 * ��ȡ�����ύ01C05����
	 */
	private String get01C05Data(String lshData) {
		List<String> list = new ArrayList<String>();
		list.add(lshData);
		String xmlData = CombinationXMLData.getQueryData(StaticValues.FieldName01C05, list);
		Log.i("AAA", "01C05xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * ɨ��������
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
			Log.i("BBB", "onActivityResult");
			if (data != null) {
				String content = data.getStringExtra(DECODED_CONTENT_KEY);
				querition_lsh.setText(content);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("BBB", "onResume");
		call01C02Port();
	}
}
