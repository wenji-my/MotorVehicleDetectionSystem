package com.hskj.jdccyxt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.adapter.MyExpandableListViewAdapter;
import com.hskj.jdccyxt.domain.C02Domain;
import com.hskj.jdccyxt.domain.C06DomainCLLX;
import com.hskj.jdccyxt.domain.C09Domain;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.domain.MaulItemGroup;
import com.hskj.jdccyxt.domain.MaulListItem;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.DocumentTool;
import com.hskj.jdccyxt.toolunit.MessageTool;
import com.hskj.jdccyxt.toolunit.MutualConversionNameID;
import com.hskj.jdccyxt.toolunit.StaticConst;
import com.hskj.jdccyxt.toolunit.StaticData;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.sql.SQLFuntion;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;

/**
 * @类名：CosmeticInspect
 * @author:广东泓胜科技有限公司
 * @实现功能：车辆外观数据查验项
 * @创建日期：2016.04.19
 */
@SuppressLint("HandlerLeak")
public class DataInspection extends Activity implements ExpandableListView.OnGroupClickListener,
		ExpandableListView.OnChildClickListener {
	private TextView data_title;
	private Button submit_data;
	private ExpandableListView ex_list;
	private LinearLayout data_linear_states, lin_btcs;
	private EditText ed_hdzws, ed_remark;
	private Spinner sp_jcxdh, sp_cllx, sp_csys_one, sp_csys_two, sp_csys_three, sp_fzcyy;
	private MyExpandableListViewAdapter exlist_adapter;
	private ProgressDialog progressDialog = null;
	private String ip, jgbh, jkxlh;
	private boolean is_submit = true;// 是否判断照片上传状态在提交数据
	private boolean testDataValue;
	private C09Domain operators;// 传入的登录员信息
	private C02Domain vehiclelist;// 调01C02接口获得数据组
	private String userID;// 登录账号
	private List<C06DomainCLLX> cllxslists;
	private String cllxNameData;// 车辆类别名称
	private List<String> newTitleData, newChildData;// 父item及子item数据组
	private String[] itemName = null, inspectionItems = null, allNumbers = null;
	private String[] csysData = StaticValues.csysData;
	private String rootDirectory = StaticValues.rootDirectory;
	private int DATA_DIALOG_SHOW = StaticConst.DATAINSPECTION_DATA_DIALOG_SHOW;
	private int DATA_DIALOG_DISMISS = StaticConst.DATAINSPECTION_DATA_DIALOG_DISMISS;
	private int DATA_DIALOG_FAILURE = StaticConst.DATAINSPECTION_DATA_DIALOG_FAILURE;
	private int DATA_DIALOG_SUCCESSFUL = StaticConst.DATAINSPECTION_DATA_DIALOG_SUCCESSFUL;
	private int DATA_DIALOG_HIDDEN = StaticConst.DATAINSPECTION_DATA_DIALOG_HIDDEN;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == DATA_DIALOG_SHOW) {
				progressDialog = DialogTool.getProgressDialog("正在上传中，请稍等。。。", DataInspection.this);
				progressDialog.show();
			}
			if (msg.what == DATA_DIALOG_DISMISS) {
				progressDialog.dismiss();
			}
			if (msg.what == DATA_DIALOG_FAILURE) {
				DialogTool.getTextDialog(msg.obj.toString(), DataInspection.this);
			}
			if (msg.what == DATA_DIALOG_SUCCESSFUL) {
				// DialogTool.setSelectHandlerDialog(msg.obj.toString(),
				// DATA_DIALOG_HIDDEN, handler, DataInspection.this);
				DialogTool.setSelectDialog(msg.obj.toString(), DataInspection.this);
			}
			if (msg.what == DATA_DIALOG_HIDDEN) {
				data_linear_states.setVisibility(View.INVISIBLE);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.data_inspection);
		setInitialization();
		// 提取缓存中的数据
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		SharedPreferences share = getSharedPreferences(StaticValues.BASIC_SETTING, 1);
		testDataValue = share.getBoolean("TESTZT", true);
		// 上一级传入的数据
		Bundle bundle = this.getIntent().getExtras();
		operators = (C09Domain) bundle.getSerializable("operatorObj");
		vehiclelist = (C02Domain) bundle.getSerializable("vehicleQueryObj");
		userID = getIntent().getStringExtra("USERID");
		// 提取文本中01C06接口的数据
		String c06data = DocumentTool.readFileContent(rootDirectory + "/CllxEndZpsm/01C06.txt");
		cllxslists = XMLParsingMethods.saxVehicleType(c06data);
		data_title.setText(vehiclelist.getVin());
		sp_jcxdh.setAdapter(getSpinnerAdapter(StaticValues.jcxdhData));
		cllxNameData = MutualConversionNameID.getCllxName(cllxslists, vehiclelist.getCllx());
		// if (cllxNameData.indexOf("校车") != -1) {
		if (vehiclelist.getSfxc().equals("1")) {
			itemName = StaticValues.busName;
			inspectionItems = StaticValues.schoolBusInspectionItem;
			allNumbers = StaticValues.schoolBusAllNumbers;
		} else {
			itemName = StaticValues.regularName;
			inspectionItems = StaticValues.regularInspectionItems;
			allNumbers = StaticValues.regularAllNumbers;
		}
		// 构建ExpandableListView
		getChildData(allNumbers);
		exlist_adapter = new MyExpandableListViewAdapter(DataInspection.this, getEXlisviewData(), vehiclelist.getCyxm(),
				vehiclelist.getBhgx(), vehiclelist.getHgx(), vehiclelist.getBcyx(), inspectionItems, allNumbers);
		ex_list.setOnChildClickListener(DataInspection.this);
		ex_list.setAdapter(exlist_adapter);
		for (int i = 0; i < exlist_adapter.getGroupCount(); i++) {
			ex_list.expandGroup(i);
		}
		sp_cllx.setAdapter(getSpinnerAdapter(getCLLXData()));
		sp_csys_one.setAdapter(getViewSpinnerAdapter(csysData));
		sp_csys_two.setAdapter(getViewSpinnerAdapter(csysData));
		sp_csys_three.setAdapter(getViewSpinnerAdapter(csysData));
		String[] fzcyydata = null;
		if (!vehiclelist.getFzcyy().equals("")) {
			fzcyydata = ("," + vehiclelist.getFzcyy()).split(",");
		} else {
			fzcyydata = new String[1];
			fzcyydata[0] = "";
		}
		sp_fzcyy.setAdapter(getSpinnerAdapter(fzcyydata));
		setControlsAssignment();
	}

	/**
	 * 获取构建ExpandableListView的数据集
	 */
	private ArrayList<MaulItemGroup> getEXlisviewData() {
		ArrayList<MaulItemGroup> groups = new ArrayList<MaulItemGroup>();
		for (int i = 0; i < newTitleData.size(); i++) {
			List<MaulListItem> group_children = new ArrayList<MaulListItem>();
			for (int j = 0; j < newChildData.get(i).split(",").length; j++) {
				MaulListItem item = new MaulListItem(newChildData.get(i).split(",")[j], false);
				group_children.add(item);
			}
			MaulItemGroup phonegroup = new MaulItemGroup(newTitleData.get(i), group_children);
			groups.add(phonegroup);
		}
		return groups;
	}

	/**
	 * 获取子item的数据集
	 */
	private void getChildData(String[] cyxmData) {
		HashMap<String, String> childMap = new HashMap<String, String>();
		String[] cyxmlist = cyxmData;
		for (int i = 0; i < cyxmlist.length; i++) {
			for (int j = 0; j < inspectionItems.length; j++) {
				if (inspectionItems[j].split(",")[1].equals(cyxmlist[i])) {
					if (childMap.get(inspectionItems[j].split(",")[0]) == null) {
						childMap.put(inspectionItems[j].split(",")[0], inspectionItems[j].split(",")[2]);
					} else {
						childMap.put(inspectionItems[j].split(",")[0], childMap.get(inspectionItems[j].split(",")[0]) + ","
								+ inspectionItems[j].split(",")[2]);
					}
				}
			}
		}
		newTitleData = new ArrayList<String>();
		newChildData = new ArrayList<String>();
		if (cllxNameData.indexOf("校车") != -1) {
			// 校车类父标题只有三项
			if (childMap.get("1") != null) {
				newTitleData.add(itemName[0]);
				newChildData.add(childMap.get("1"));
			}
			if (childMap.get("2") != null) {
				newTitleData.add(itemName[1]);
				newChildData.add(childMap.get("2"));
			}
			if (childMap.get("3") != null) {
				newTitleData.add(itemName[2]);
				newChildData.add(childMap.get("3"));
			}
		} else {
			// 普通车类父标题有五项
			if (childMap.get("1") != null) {
				newTitleData.add(itemName[0]);
				newChildData.add(childMap.get("1"));
			}
			if (childMap.get("2") != null) {
				newTitleData.add(itemName[1]);
				newChildData.add(childMap.get("2"));
			}
			if (childMap.get("3") != null) {
				newTitleData.add(itemName[2]);
				newChildData.add(childMap.get("3"));
			}
			if (childMap.get("4") != null) {
				newTitleData.add(itemName[3]);
				newChildData.add(childMap.get("4"));
			}
			if (childMap.get("5") != null) {
				newTitleData.add(itemName[4]);
				newChildData.add(childMap.get("5"));
			}
		}
	}

	/**
	 * 初始化控件
	 */
	private void setInitialization() {
		data_linear_states = (LinearLayout) this.findViewById(R.id.data_linear_states);
		data_title = (TextView) this.findViewById(R.id.data_title);
		submit_data = (Button) this.findViewById(R.id.submit_data);
		ex_list = (ExpandableListView) this.findViewById(R.id.ex_list);
		lin_btcs = (LinearLayout) this.findViewById(R.id.lin_btcs);
		ed_hdzws = (EditText) this.findViewById(R.id.ed_hdzws);
		ed_remark = (EditText) this.findViewById(R.id.ed_remark);
		sp_jcxdh = (Spinner) this.findViewById(R.id.sp_jcxdh);
		sp_cllx = (Spinner) this.findViewById(R.id.sp_cllx);
		sp_csys_one = (Spinner) this.findViewById(R.id.sp_csys_one);
		sp_csys_two = (Spinner) this.findViewById(R.id.sp_csys_two);
		sp_csys_three = (Spinner) this.findViewById(R.id.sp_csys_three);
		sp_fzcyy = (Spinner) this.findViewById(R.id.sp_fzcyy);
	}

	/**
	 * 自定义布局
	 * 
	 * @return
	 */
	private ArrayAdapter<String> getSpinnerAdapter(String[] arrayData) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayData);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	private ArrayAdapter<String> getViewSpinnerAdapter(String[] arrayData) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_adapter, arrayData);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	/**
	 * 获取转换后的车辆类型数据组
	 */
	private String[] getCLLXData() {
		String[] cllxName = null;
		cllxName = new String[cllxslists.size() + 1];
		for (int i = 0; i < (cllxslists.size() + 1); i++) {
			if (i == 0) {
				cllxName[i] = "";
			} else {
				cllxName[i] = cllxslists.get(i - 1).getCllxmc();
			}
		}
		return cllxName;
	}

	/**
	 * 控件辅值
	 */
	private void setControlsAssignment() {
		ed_hdzws.setText(vehiclelist.getHdzk());
		sp_cllx.setSelection(MutualConversionNameID.getCllxIDLocation(cllxslists, vehiclelist.getCllx()) + 1);
		if (!vehiclelist.getCsys().equals("")) {
			if (vehiclelist.getCsys().length() == 1) {
				sp_csys_one.setSelection(MutualConversionNameID.getCsysIDLocation(vehiclelist.getCsys()));
			} else if (vehiclelist.getCsys().length() == 2) {
				sp_csys_one.setSelection(MutualConversionNameID.getCsysIDLocation(vehiclelist.getCsys().split("")[1]));
				sp_csys_two.setSelection(MutualConversionNameID.getCsysIDLocation(vehiclelist.getCsys().split("")[2]));
			} else if (vehiclelist.getCsys().length() == 3) {
				sp_csys_one.setSelection(MutualConversionNameID.getCsysIDLocation(vehiclelist.getCsys().split("")[1]));
				sp_csys_two.setSelection(MutualConversionNameID.getCsysIDLocation(vehiclelist.getCsys().split("")[2]));
				sp_csys_three.setSelection(MutualConversionNameID.getCsysIDLocation(vehiclelist.getCsys().split("")[3]));
			}
		}
		ed_remark.setText(vehiclelist.getBeizhu());
	}

	/**
	 * 点击事件
	 */
	public void doClick(View v) {
		switch (v.getId()) {
		// 退出
		case R.id.data_back:
			DataInspection.this.finish();
			break;
		// 检验开始
		case R.id.Inspection_start:
			submit_data.setEnabled(true);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						handler.sendEmptyMessage(DATA_DIALOG_SHOW);
						// 调01J11接口
						/*String data01J11 = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh, "01J11",
								get01J11end01J12Data(vehiclelist, jgbh, sp_jcxdh.getSelectedItem().toString().trim()),
								StaticValues.writeResult, StaticValues.timeoutThree, StaticValues.numberFive);*/
						//离线
						Thread.sleep(500);
						String data01J11 = StaticData.Data01J11;
						Log.i("AAA", "data01J11:" + data01J11);
						List<CodeDomain> codelists01J11 = XMLParsingMethods.saxcode(data01J11);
						handler.sendEmptyMessage(DATA_DIALOG_DISMISS);
						MessageTool.setmessage(DATA_DIALOG_FAILURE, codelists01J11.get(0).getMessage(), handler);
					} catch (Exception e) {
						handler.sendEmptyMessage(DATA_DIALOG_DISMISS);
						MessageTool.setmessage(DATA_DIALOG_FAILURE, "网络请求超时，请查看网络！", handler);
						e.printStackTrace();
					}
				}
			}).start();
			break;
		// 提交数据
		case R.id.submit_data:
			final String hdzwsData = getHdzwsResult(ed_hdzws.getText().toString().trim());// 核定座位数
			final String cllxData = MutualConversionNameID.getCllxID(cllxslists, sp_cllx.getSelectedItem().toString().trim());// 车辆类别
			final String csysData = getCsysData();// 车身颜色
			final String remarkData = ed_remark.getText().toString().trim();// 备注
			if (isEmptyBTCS(cllxData, csysData)) {
				is_submit = getSubmitValue(testDataValue, vehiclelist.getJclsh(), vehiclelist.getZpzl());
				if (is_submit) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								handler.sendEmptyMessage(DATA_DIALOG_SHOW);
								// 调01J02接口
								/*String data01J02 = ConnectMethods.connectWebService(ip,StaticValues.writeObject,jkxlh,"01J02",
										           get01J02Data(vehiclelist, operators, userID, jgbh, hdzwsData, cllxData, csysData,
												   remarkData), StaticValues.writeResult, StaticValues.timeoutFive, StaticValues.numberFour);*/
								//离线
								Thread.sleep(500);
								String data01J02 = StaticData.Data01J02;
								Log.i("AAA", "data01J02:" + data01J02);
								List<CodeDomain> codelists01J02 = XMLParsingMethods.saxcode(data01J02);
								handler.sendEmptyMessage(DATA_DIALOG_DISMISS);
								if (codelists01J02.get(0).getCode().equals("1")) {
									MessageTool.setmessage(DATA_DIALOG_SUCCESSFUL, codelists01J02.get(0).getMessage(), handler);
									handler.sendEmptyMessage(DATA_DIALOG_SHOW);
									// 调01J12接口
									/*String data01J12 = ConnectMethods.connectWebService(ip,StaticValues.writeObject,jkxlh,"01J12",
													   get01J11end01J12Data(vehiclelist, jgbh, sp_jcxdh.getSelectedItem().toString().trim()),
													   StaticValues.writeResult, StaticValues.timeoutThree,StaticValues.numberFive);*/
									//离线
									Thread.sleep(500);
									String data01J12 = StaticData.Data01J12;
									Log.i("AAA", "data01J12:" + data01J12);
									List<CodeDomain> codelists01J12 = XMLParsingMethods.saxcode(data01J12);
									handler.sendEmptyMessage(DATA_DIALOG_DISMISS);
									MessageTool.setmessage(DATA_DIALOG_FAILURE, codelists01J12.get(0).getMessage(), handler);
								} else {
									MessageTool.setmessage(DATA_DIALOG_FAILURE, codelists01J02.get(0).getMessage(), handler);
								}
							} catch (Exception e) {
								handler.sendEmptyMessage(DATA_DIALOG_DISMISS);
								MessageTool.setmessage(DATA_DIALOG_FAILURE, "网络请求超时，请查看网络！", handler);
								e.printStackTrace();
							}
						}
					}).start();
				} else {
					MessageTool.setmessage(DATA_DIALOG_FAILURE, "外检照片还未上传完毕，请查看是否还有未拍摄或未上传?", handler);
				}
			} else {
				ex_list.setVisibility(View.GONE);
				lin_btcs.setVisibility(View.VISIBLE);
				MessageTool.setmessage(DATA_DIALOG_FAILURE, "车辆类型及车身颜色不能为空！", handler);
			}
			break;
		// 可选参数
		case R.id.optional_parameters:
			lin_btcs.setVisibility(View.GONE);
			ex_list.setVisibility(View.VISIBLE);
			break;
		// 必填参数
		case R.id.required_parameters:
			ex_list.setVisibility(View.GONE);
			lin_btcs.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * 判断必填参数是否为空
	 */
	private Boolean isEmptyBTCS(String newCllxData, String newCsysData) {
		if (!newCllxData.equals("") && !newCsysData.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断核定座位数结果
	 */
	private String getHdzwsResult(String newHdzwsData) {
		if (newHdzwsData.equals("")) {
			newHdzwsData = "0";
		}
		return newHdzwsData;
	}

	/**
	 * 判断图片是否上传完才可提交外检数据
	 */
	private boolean getSubmitValue(boolean testValue, String cljylsh, String zpzlData) {
		if (testValue) {
			String[] zpzllist = zpzlData.split(",");
			for (int i = 0; i < zpzllist.length; i++) {
				ArrayList<HashMap<String, String>> sqldatas = SQLFuntion.query(DataInspection.this, cljylsh, zpzllist[i]);
				if (sqldatas.size() != 0) {
					if (!sqldatas.get(0).get("isupload").equals("03")) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 获取车身颜色
	 */
	private String getCsysData() {
		String newCsysData = "";
		String one = sp_csys_one.getSelectedItem().toString().trim().split(",")[0];
		String two = sp_csys_two.getSelectedItem().toString().trim().split(",")[0];
		String three = sp_csys_three.getSelectedItem().toString().trim().split(",")[0];
		newCsysData = one + two + three;
		return newCsysData;
	}

	/**
	 * 获取即将提交01J11end01J12数据
	 */
	private String get01J11end01J12Data(C02Domain lists, String jyjgbh, String jcxdh) {
		List<String> list = new ArrayList<String>();
		list.add(jyjgbh);
		list.add(lists.getJclsh());
		list.add(lists.getHphm());
		list.add(lists.getHpzl());
		list.add(lists.getVin());
		list.add("CY");
		list.add(jcxdh);
		list.add("1");
		String xmlData = CombinationXMLData.getWriteData(StaticValues.FieldName01J11end01J12, list);
		Log.i("AAA", "01J11end01J12xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * 获取即将提交01J02数据
	 */
	private String get01J02Data(C02Domain vehiclelists, C09Domain operatorlist, String loginUser, String jyjgbh, String hdzwData,
			String cllxData, String csysData, String remarkData) {
		String hgxData = "";
		String bhgxData = "";
		String bcyxData = "";
		HashMap<String, String> cyxmMap = exlist_adapter.returnCyxmInformation();
		String[] cyxmlistData = allNumbers;
		for (int i = 0; i < cyxmlistData.length; i++) {
			String cyxmName = MutualConversionNameID.getCyxmName(inspectionItems, cyxmlistData[i]);
			if (cyxmMap.get(cyxmName) == null || cyxmMap.get(cyxmName).equals("3")) {
				if (bcyxData.equals("")) {
					bcyxData = String.format("%03d", Integer.parseInt(cyxmlistData[i]));
				} else {
					bcyxData = bcyxData + " " + String.format("%03d", Integer.parseInt(cyxmlistData[i]));
				}
			} else if (cyxmMap.get(cyxmName).equals("2")) {
				if (bhgxData.equals("")) {
					bhgxData = String.format("%03d", Integer.parseInt(cyxmlistData[i]));
				} else {
					bhgxData = bhgxData + " " + String.format("%03d", Integer.parseInt(cyxmlistData[i]));
				}
			} else {
				if (hgxData.equals("")) {
					hgxData = String.format("%03d", Integer.parseInt(cyxmlistData[i]));
				} else {
					hgxData = hgxData + " " + String.format("%03d", Integer.parseInt(cyxmlistData[i]));
				}
			}
		}
		String cyzjlData = "1";
		if (!bhgxData.equals("")) {
			cyzjlData = "2";
		}
		List<String> list = new ArrayList<String>();
		list.add(loginUser);
		list.add(operatorlist.getIdenitycard());
		list.add(remarkData);
		list.add(getBiangengData(hdzwData, csysData, cllxData));
		list.add(operatorlist.getUsername());
		list.add(vehiclelists.getJclsh());
		list.add(jyjgbh);
		list.add(vehiclelists.getYwlx());
		list.add(hgxData);
		list.add(bhgxData);
		list.add(bcyxData);
		list.add(cyzjlData);
		String xmlData = CombinationXMLData.getWriteData(StaticValues.FieldName01J02, list);
		Log.i("AAA", "01J02xmlData:" + xmlData);
		return xmlData;
	}

	private String getBiangengData(String newhdzwData, String newcsysData, String newcllxData) {
		List<String> newlist = new ArrayList<String>();
		newlist.add(newcsysData);
		newlist.add(String.format("%03d", Integer.parseInt(newhdzwData)));
		newlist.add(newcllxData);
		String newxmlData = CombinationXMLData.getGatherData(StaticValues.FieldName01J02one, newlist);
		Log.i("AAA", "01J02onexmlData:" + newxmlData);
		return newxmlData;
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4) {
		return false;
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3) {
		return false;
	}

	/**
	 * 重写返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DialogTool.setSelectDialog("数据正在采集中，是否退出？", DataInspection.this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
