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
 * @类名：VehicleQuery
 * @author:广东泓胜科技有限公司
 * @实现功能：车辆查询
 * @创建日期：2016.04.12
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
	private List<C09Domain> operators;// 传入的登录员信息
	private List<C02Domain> vehiclelist;// 调01C02接口获得数据组
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
				progressDialog = DialogTool.getProgressDialog("正在请求中，请稍等。。。", VehicleQuery.this);
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
		// 提取缓存中的数据
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		// 上一级传入的数据
		Bundle bundle = this.getIntent().getExtras();
		operators = (List<C09Domain>) bundle.getSerializable("operatorObj");
		cylxData = getIntent().getStringExtra("CYLX");
		userID = getIntent().getStringExtra("USERID");
		// 标题命名
		app = (MyApplication) getApplication();
		txt_version.setText(app.getVersion());
		txt_viewer_name.setText(app.getViewerName());
		setTitleName(cylxData);
		vehicle_list.setOnItemClickListener(queryItemClick);
	}

	/**
	 * 列表点击事件
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
	 * 对话框
	 */
	private void setDialog(final int arg2) {
		AlertDialog.Builder tcbuilder = new AlertDialog.Builder(VehicleQuery.this);
		tcbuilder.setTitle("提示框：");
		tcbuilder.setMessage(vehiclelist.get(arg2).getTs() + "，确定继续进行查验吗？");
		tcbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				CheckJump(arg2);
			}
		});
		tcbuilder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				VehicleQuery.this.finish();
			}
		});
		tcbuilder.setCancelable(false);
		tcbuilder.show();
	}

	/**
	 * item点击事件跳转查验界面后续
	 */
	private void CheckJump(final int arg2) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Intent intent2 = new Intent();
					if (cylxData.equals("cy")) {// 查验
						String data01C05 = get01C05InformationData(vehiclelist.get(arg2));
						intent2.setClass(VehicleQuery.this, CosmeticInspect.class);
						intent2.putExtra("USERID", userID);
						intent2.putExtra("C05DATA", data01C05);
					} else if (cylxData.equals("fcy")) {// 补拍
						intent2.setClass(VehicleQuery.this, PhotoShot.class);
						intent2.putExtra("STATE", "normaltwo");
					} else {// 行驶证拍摄
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
					MessageTool.setmessage(QUERY_DIALOG_FAILURE, "网络请求超时，请查看网络！", handler);
				}
			}
		}).start();
	}

	/**
	 * 初始化控件
	 */
	private void setInitialization() {
		txt_version = (TextView) this.findViewById(R.id.txt_version);
		txt_viewer_name = (TextView) this.findViewById(R.id.txt_viewer_name);
		querition_title = (TextView) this.findViewById(R.id.querition_title);
		querition_lsh = (EditText) this.findViewById(R.id.querition_lsh);
		vehicle_list = (ListView) this.findViewById(R.id.vehicle_list);
	}

	/**
	 * 标题命名
	 */
	private void setTitleName(String newCylxData) {
		for (int i = 0; i < titleName.length; i++) {
			if (titleName[i].split(",")[0].equals(newCylxData)) {
				querition_title.setText(titleName[i].split(",")[1]);
			}
		}
	}

	/**
	 * 按钮点击事件
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.querition_back:
			VehicleQuery.this.finish();
			break;
		// 扫描
		case R.id.querition_scanning:
			Intent intent1 = new Intent(VehicleQuery.this, CaptureActivity.class);
			startActivityForResult(intent1, REQUEST_CODE_SCAN);
			break;
		// 查询
		case R.id.querition_query:
			call01C02Port();
			break;
		}
	}

	/**
	 * 调01C02接口
	 */
	public void call01C02Port() {
		final String lsh_data = querition_lsh.getText().toString().trim();
		if (lsh_data.equals("")) {
//			Toast.makeText(VehicleQuery.this, "流水号不能为空！", Toast.LENGTH_LONG).show();
		} else {
			vehicle_list.setVisibility(View.GONE);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						handler.sendEmptyMessage(QUERY_DIALOG_SHOW);
						// 调01C02接口
//						String data01C02 = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh, "01C02",
//								get01C02Data(jgbh, lsh_data, cylxData, userID), StaticValues.queryResult,
//								StaticValues.timeoutFive, StaticValues.numberFour);
						//离线
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
						MessageTool.setmessage(QUERY_DIALOG_FAILURE, "网络请求超时，请查看网络！", handler);
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/**
	 * listview界面显示
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
	 * listview获取ArrayList<HashMap<String, Object>>数据
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
	 * 获取即将提交01C02数据
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
	 * 获取01C05未解析数据
	 */
	private String get01C05InformationData(C02Domain vehicleData) {
		handler.sendEmptyMessage(QUERY_DIALOG_SHOW);
		// 调01C05接口
		/*String data01C05 = ConnectMethods
				.connectWebService(ip, StaticValues.queryObject, jkxlh, "01C05", get01C05Data(vehicleData.getJclsh()),
						StaticValues.queryResult, StaticValues.timeoutFive, StaticValues.numberFour);*/
		//离线
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
	 * 获取即将提交01C05数据
	 */
	private String get01C05Data(String lshData) {
		List<String> list = new ArrayList<String>();
		list.add(lshData);
		String xmlData = CombinationXMLData.getQueryData(StaticValues.FieldName01C05, list);
		Log.i("AAA", "01C05xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * 扫描结果处理
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
