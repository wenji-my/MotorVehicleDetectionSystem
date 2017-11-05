package com.hskj.jdccyxt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.C07Domain;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.intentunit.socket.SocketClient;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.MessageTool;
import com.hskj.jdccyxt.toolunit.MutualConversionNameID;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.StaticData;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import com.zxing.scanner.android.CaptureActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @类名：InstrumentDetection
 * @author:广东泓胜科技有限公司
 * @实现功能：功能界面
 * @创建日期：2016.06.17
 */
public class InstrumentDetection extends Activity {
	final String[] arrayNum = new String[] {"1", "2", "3", "4", "5", "6", "7", "8"};
	private TextView querition_title, txt_version, txt_viewer_name;
	private EditText querition_lsh;
	private ListView vehicle_list;
	private Button wkcc_btn, zbzl_btn;
	private LinearLayout is_show_socket, is_show_btn;
	private MyApplication app;
	private ProgressDialog progressDialog = null;
	private List<C07Domain> wkzllist;
	private String ip, jgbh, jkxlh, wjyqip, wjyqdk;
	private String userID;
	private SocketClient socketClient;
	private int REQUEST_CODE_SCAN = StaticValues.STATIC_REQUEST_CODE_SCAN;
	private String DECODED_CONTENT_KEY = StaticValues.STATIC_DECODED_CONTENT_KEY;
	private int INSTRUMENT_DIALOG_SHOW = StaticConst.INSTRUMENTDETECTION_DIALOG_SHOW;
	private int INSTRUMENT_DIALOG_DISMISS = StaticConst.INSTRUMENTDETECTION_DIALOG_DISMISS;
	private int INSTRUMENT_DIALOG_FAILURE = StaticConst.INSTRUMENTDETECTION_DIALOG_FAILURE;
	private int INSTRUMENT_LIST_SHOW = StaticConst.INSTRUMENTDETECTION_LIST_SHOW;
	private int INSTRUMENT_BROKEN_NETWORK_SHOW = StaticConst.INSTRUMENTDETECTION_BROKEN_NETWORK_SHOW;
	private int INSTRUMENT_CONNET_NETWORK_DISMISS = StaticConst.INSTRUMENTDETECTION_CONNET_NETWORK_DISMISS;
	private int INSTRUMENT_SEND_MESSAGE = StaticConst.SOCKETCLIENT_SEND_MESSAGE;
	private int INSTRUMENT_TOAST_MESSAGE = StaticConst.SOCKETCLIENT_TOAST_MESSAGE;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == INSTRUMENT_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("正在请求中，请稍等。。。", InstrumentDetection.this);
				progressDialog.show();
			}
			if (msg.what == INSTRUMENT_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == INSTRUMENT_DIALOG_FAILURE) {
				DialogTool.getTextDialog(msg.obj.toString(), InstrumentDetection.this);
			}
			if (msg.what == INSTRUMENT_LIST_SHOW) {

				isShowButton(wkzllist);
				vehicle_list.setAdapter(getAdapter());
				vehicle_list.setVisibility(View.VISIBLE);

			}
			// socket断开
			if (msg.what == INSTRUMENT_BROKEN_NETWORK_SHOW) {
				is_show_socket.setVisibility(View.VISIBLE);
				wkcc_btn.setEnabled(false);
				zbzl_btn.setEnabled(false);
			}
			// socket连接
			if (msg.what == INSTRUMENT_CONNET_NETWORK_DISMISS) {
				is_show_socket.setVisibility(View.GONE);

//				wkcc_btn.setEnabled(true);
//				zbzl_btn.setEnabled(true);
//				isShowButton(wkzllist);


			}
			//开始读取数据
			if (msg.what == INSTRUMENT_TOAST_MESSAGE) {
//				DialogTool.setPositiveDialog("开始读取数据", InstrumentDetection.this);
				AlertDialog.Builder builder = new AlertDialog.Builder(InstrumentDetection.this);
				builder.setMessage("开始读取数据");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});
				builder.setCancelable(false);
				builder.show();

			}
		}
	};

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
		wjyqip = preferences.getString("WJYQIP", "");
		wjyqdk = preferences.getString("WJYQDK", "");
		userID = getIntent().getStringExtra("USERID");
		// 建立socket连接
		Log.i("AAA", startIP(wjyqip) + "------" + Integer.parseInt(startDK(wjyqdk)));
		socketClient = new SocketClient(handler, startIP(wjyqip), Integer.parseInt(startDK(wjyqdk)), StaticValues.timeoutThree);
		new Thread(socketClient).start();
		app = (MyApplication) getApplication();
		txt_version.setText(app.getVersion());
		txt_viewer_name.setText(app.getViewerName());
		querition_title.setText("仪 器 检 测");
		// vehicle_list.setOnItemClickListener(deteItemClick);
		vehicle_list.setEnabled(false);
		is_show_btn.setVisibility(View.VISIBLE);
	}

	/**
	 * 外检仪器IP，端口不为空
	 */
	public String startIP(String yqip) {
		String newyqip = "";
		if (yqip.equals("")) {
			newyqip = "192.168.1.1";
		} else {
			newyqip = yqip;
		}
		return newyqip;
	}

	public String startDK(String yqdk) {
		String newyqdk = "";
		if (yqdk.equals("")) {
			newyqdk = "1";
		} else {
			newyqdk = yqdk;
		}
		return newyqdk;
	}

	/**
	 * 列表点击事件
	 */
	OnItemClickListener deteItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		}
	};

	/**
	 * 初始化控件
	 */
	private void setInitialization() {
		txt_version = (TextView) this.findViewById(R.id.txt_version);
		txt_viewer_name = (TextView) this.findViewById(R.id.txt_viewer_name);
		querition_title = (TextView) this.findViewById(R.id.querition_title);
		querition_lsh = (EditText) this.findViewById(R.id.querition_lsh);
		vehicle_list = (ListView) this.findViewById(R.id.vehicle_list);
		is_show_socket = (LinearLayout) this.findViewById(R.id.is_show_socket);
		is_show_btn = (LinearLayout) this.findViewById(R.id.is_show_btn);
		wkcc_btn = (Button) this.findViewById(R.id.wkcc_btn);
		zbzl_btn = (Button) this.findViewById(R.id.zbzl_btn);
	}

	private void isShowButton(List<C07Domain> wkzllist3) {
		if (wkzllist3.get(0).getWkccsfjc().equals("1")) {
			wkcc_btn.setEnabled(true);
		} else {
			wkcc_btn.setEnabled(false);
		}
		if (wkzllist3.get(0).getZbzlsfjc().equals("1")) {
			zbzl_btn.setEnabled(true);
		} else {
			zbzl_btn.setEnabled(false);
		}
	}

	/**
	 * 按钮点击事件
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.querition_back:
			InstrumentDetection.this.finish();
			break;
		// 扫描
		case R.id.querition_scanning:
			Intent intent = new Intent(InstrumentDetection.this, CaptureActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SCAN);
			break;
		// 查询
		case R.id.querition_query:
			call01C07Port();
			break;
		// 外廓尺寸
		case R.id.wkcc_btn:
			try {
				MessageTool.setmessage(INSTRUMENT_SEND_MESSAGE,
						"<?xml version=\"1.0\" encoding=\"GBK\"?><root>\n" +
								"<vehispara>\n" +
								"<jyxm>" + "M1" + "</jyxm>\n" +
								"<jclsh>" + wkzllist.get(0).getJclsh() + "</jclsh>\n" +
								"</vehispara>\n" +
								"</root>",
						socketClient.revHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// 整备质量
		case R.id.zbzl_btn:
			try {
				//增加弹框
				Dialog alertDialog = new AlertDialog.Builder(InstrumentDetection.this)
                .setTitle("轴位：")
                .setItems(arrayNum, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        tvNum.setText(arrayNum[which]);
						MessageTool.setmessage(INSTRUMENT_SEND_MESSAGE,
								"<?xml version=\"1.0\" encoding=\"GBK\"?><root>\n" +
										"<vehispara>\n" +
										"<jyxm>" + "W1" + "</jyxm>\n" +
										"<zs>" + arrayNum[which] + "</zs>\n" +
										"<jclsh>" + wkzllist.get(0).getJclsh() + "</jclsh>\n" +
										"</vehispara>\n" +
										"</root>",
								socketClient.revHandler);
                    	
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
				

//				MessageTool.setmessage(INSTRUMENT_SEND_MESSAGE, "1," + wkzllist.get(0).getJclsh(), socketClient.revHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * listview界面显示
	 */
	public SimpleAdapter getAdapter() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		list = hashmapData(wkzllist);
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(InstrumentDetection.this, list,
				R.layout.vehicle_querition_list_item_two, new String[] { "hphm", "hpzl", "jclsh" }, new int[] {
						R.id.item_two_hphm, R.id.item_two_hpzl, R.id.item_two_lsh });
		return mSimpleAdapter;
	}

	/**
	 * listview获取ArrayList<HashMap<String, Object>>数据
	 * 
	 * @param wkzllist2
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> hashmapData(List<C07Domain> wkzllist2) {
		ArrayList<HashMap<String, Object>> listdata = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < wkzllist2.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("hphm", wkzllist2.get(i).getHphm());
			map.put("hpzl", MutualConversionNameID.getHpzlName(wkzllist2.get(i).getHpzl()));
			map.put("jclsh", wkzllist2.get(i).getJclsh());
			listdata.add(map);
		}
		return listdata;
	};

	/**
	 * 调01C07接口
	 */
	private void call01C07Port() {
		final String lsh_data = querition_lsh.getText().toString().trim();
		if (lsh_data.equals("")) {
//			Toast.makeText(InstrumentDetection.this, "流水号不能为空！", Toast.LENGTH_LONG).show();
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						handler.sendEmptyMessage(INSTRUMENT_DIALOG_SHOW);
						// 调01C07接口
						/*String data01C07 = ConnectMethods.connectWebService(ip, StaticValues.queryObject, jkxlh, "01C07",
								get01C07Data(jgbh, lsh_data, userID), StaticValues.queryResult, StaticValues.timeoutFive,
								StaticValues.numberFour);*/
						//离线
						Thread.sleep(500);
						String data01C07 = StaticData.Data01C07;
						Log.i("AAA", "data01C07:" + data01C07);
						List<CodeDomain> codelists01C07 = XMLParsingMethods.saxcode(data01C07);
						handler.sendEmptyMessage(INSTRUMENT_DIALOG_DISMISS);
						if (!codelists01C07.get(0).getCode().equals("1")) {
							MessageTool.setmessage(INSTRUMENT_DIALOG_FAILURE, codelists01C07.get(0).getMessage(), handler);
						} else {
							List<C07Domain> wkzllists = XMLParsingMethods.saxWkccAndZbzl(data01C07);
							wkzllist = wkzllists;
							Log.i("AAA", "wkzllists:" + wkzllists.get(0).getWkccsfjc());
							handler.sendEmptyMessage(INSTRUMENT_LIST_SHOW);

						}
					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(INSTRUMENT_DIALOG_DISMISS);
						MessageTool.setmessage(INSTRUMENT_DIALOG_FAILURE, "网络请求超时，请查看网络！", handler);
					}
				}
			}).start();
		}
	}

	/**
	 * 获取即将提交01C07数据
	 */
	private String get01C07Data(String jyjgbhData, String lshData, String userIDData) {
		List<String> list = new ArrayList<String>();
		list.add(jyjgbhData);
		list.add(lshData);
		list.add(userIDData);
		String xmlData = CombinationXMLData.getQueryData(StaticValues.FieldName01C07, list);
		Log.i("AAA", "01C07xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * 扫描结果处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
			if (data != null) {
				String content = data.getStringExtra(DECODED_CONTENT_KEY);
				querition_lsh.setText(content);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		call01C07Port();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SocketClient.setStart(false);
	}
}
