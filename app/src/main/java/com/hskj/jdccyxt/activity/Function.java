package com.hskj.jdccyxt.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.C09Domain;
import com.hskj.jdccyxt.domain.CodeDomain;
import com.hskj.jdccyxt.intentunit.ksoap2.ConnectMethods;
import com.hskj.jdccyxt.toolunit.CombinationXMLData;
import com.hskj.jdccyxt.toolunit.DialogTool;
import com.hskj.jdccyxt.toolunit.PhotoTool;
import com.hskj.jdccyxt.toolunit.StaticValues;
import com.hskj.jdccyxt.toolunit.sql.SQLFuntion;
import com.hskj.jdccyxt.toolunit.xmlparsing.VersionTool;
import com.hskj.jdccyxt.toolunit.xmlparsing.XMLParsingMethods;
import com.slidingmenu.lib.SlidingMenu;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @类名：Function
 * @author:广东泓胜科技有限公司
 * @实现功能：功能界面
 * @创建日期：2016.04.12
 */
public class Function extends Activity implements OnClickListener {
	private SlidingMenu menu;
	private TextView txt_version, txt_viewer_name;
	private Button vehicle_query, photos_taken, vehicle_license, instrument_detection, password_change, but_menu, look_photo,
			logoff;
	private ImageView but_arrow;
	private LinearLayout czsz;
	private MyApplication app;
	private String ip, jgbh, jkxlh;
	private String userID;
	private List<C09Domain> operators;// 传入的登录员信息
	private String photoSubcatalog = StaticValues.photoSubcatalog;
	private Thread uploadThread = null;
	private boolean isContinueUpdata = false;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.function);
		setInitialization();
		// 提取缓存中的数据
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		// 上一级传入的数据
		Bundle bundle = this.getIntent().getExtras();
		operators = (List<C09Domain>) bundle.getSerializable("operatorObj");
		userID = getIntent().getStringExtra("USERID");
		isContinueUpdata = true;
		setLateralSpreadsMenu();
		// 设置应用级全局变量
		app = (MyApplication) getApplication();
		app.setVersion("版本号：" + VersionTool.getAPPVersionCode(this));
		app.setViewerName("查验员：" + operators.get(0).getUsername());
		txt_version.setText(app.getVersion());
		txt_viewer_name.setText(app.getViewerName());
		if (operators.get(0).getXszgy().equals("0")) {
			vehicle_license.setEnabled(true);
		}
		// 上传照片
		uploadThread = new Thread(downLoadBnsInfoRunable);
		uploadThread.start();
	}

	/**
	 * 初始化控件
	 */
	private void setInitialization() {
		txt_version = (TextView) this.findViewById(R.id.txt_version);
		txt_viewer_name = (TextView) this.findViewById(R.id.txt_viewer_name);
		vehicle_query = (Button) this.findViewById(R.id.vehicle_query);
		vehicle_query.setOnClickListener(this);
		photos_taken = (Button) this.findViewById(R.id.photos_taken);
		photos_taken.setOnClickListener(this);
		vehicle_license = (Button) this.findViewById(R.id.vehicle_license);
		vehicle_license.setOnClickListener(this);
		instrument_detection = (Button) this.findViewById(R.id.instrument_detection);
		instrument_detection.setOnClickListener(this);
		password_change = (Button) this.findViewById(R.id.password_change);
		password_change.setOnClickListener(this);
		but_menu = (Button) this.findViewById(R.id.but_menu);
		but_menu.setOnClickListener(this);
	}

	/**
	 * 构建侧滑菜单
	 */
	private void setLateralSpreadsMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置阴影的宽度
		menu.setShadowWidthRes(R.dimen.shadow_width);
		// 设置阴影的颜色
		menu.setShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单视图的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.leftmenu);
		View view = menu.getMenu();
		but_arrow = (ImageView) view.findViewById(R.id.but_arrow);
		czsz = (LinearLayout) view.findViewById(R.id.czsz);
		but_arrow.setOnClickListener(this);
		assignment(view);
		look_photo = (Button) view.findViewById(R.id.look_photo);
		look_photo.setOnClickListener(this);
		logoff = (Button) view.findViewById(R.id.logoff);
		logoff.setOnClickListener(this);
	}

	/**
	 * 对选择控件进行赋值.操作
	 */
	private void assignment(View v) {
		RadioGroup photoType = (RadioGroup) v.findViewById(R.id.photoType);
		RadioGroup testdataType = (RadioGroup) v.findViewById(R.id.testdataType);
		// 对控件辅值
		final SharedPreferences share = getSharedPreferences(StaticValues.BASIC_SETTING, 1);
		boolean photoValue = share.getBoolean("PHOTOZ", true);
		boolean testDataValue = share.getBoolean("TESTZT", true);
		RadioButton rb = null;
		if (photoValue) {
			rb = (RadioButton) v.findViewById(R.id.photoS);
		} else {
			rb = (RadioButton) v.findViewById(R.id.photoF);
		}
		rb.setChecked(true);
		if (testDataValue) {
			rb = (RadioButton) v.findViewById(R.id.testdataS);
		} else {
			rb = (RadioButton) v.findViewById(R.id.testdataF);
		}
		rb.setChecked(true);
		// 对控件进行结果保存
		photoType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				int radioButtonId = arg0.getCheckedRadioButtonId();
				Editor editor = share.edit();
				if (radioButtonId == R.id.photoS) {
					editor.putBoolean("PHOTOZ", true);
				} else {
					editor.putBoolean("PHOTOZ", false);
				}
				editor.commit();
			}
		});
		testdataType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				int radioButtonId = arg0.getCheckedRadioButtonId();
				Editor editor = share.edit();
				if (radioButtonId == R.id.testdataS) {
					editor.putBoolean("TESTZT", true);
				} else {
					editor.putBoolean("TESTZT", false);
				}
				editor.commit();
			}
		});
	}

	/**
	 * 按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 车辆查询界面
		case R.id.vehicle_query:
			Intent intent1 = new Intent(Function.this, VehicleQuery.class);
			intent1.putExtra("CYLX", "cy");
			intent1.putExtra("USERID", userID);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("operatorObj", (Serializable) operators);
			intent1.putExtras(bundle1);
			startActivity(intent1);
			break;
		// 照片补拍界面
		case R.id.photos_taken:
			Intent intent2 = new Intent(Function.this, VehicleQuery.class);
			intent2.putExtra("CYLX", "fcy");
			intent2.putExtra("USERID", userID);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("operatorObj", (Serializable) operators);
			intent2.putExtras(bundle2);
			startActivity(intent2);
			break;
		// 密码修改界面
		case R.id.password_change:
			Intent intent3 = new Intent(Function.this, PasswordChange.class);
			intent3.putExtra("USERID", userID);
			intent3.putExtra("USERNAME", operators.get(0).getUsername());
			startActivity(intent3);
			break;
		// 侧滑菜单
		case R.id.but_menu:
			menu.showMenu();
			break;
		// 行驶证照片拍摄
		case R.id.vehicle_license:
			Intent intent4 = new Intent(Function.this, VehicleQuery.class);
			intent4.putExtra("CYLX", "xsz");
			intent4.putExtra("USERID", userID);
			Bundle bundle4 = new Bundle();
			bundle4.putSerializable("operatorObj", (Serializable) operators);
			intent4.putExtras(bundle4);
			startActivity(intent4);
			break;
		// 仪器检测
		case R.id.instrument_detection:
			Intent intent6 = new Intent(Function.this, InstrumentDetection.class);
			intent6.putExtra("USERID", userID);
			startActivity(intent6);
			break;
		// 操作设置显隐控制
		case R.id.but_arrow:
			if (czsz.getVisibility() == View.VISIBLE) {
				but_arrow.setBackgroundResource(R.drawable.down_arrow);
				czsz.setVisibility(View.GONE);
			} else {
				but_arrow.setBackgroundResource(R.drawable.up_arrow);
				czsz.setVisibility(View.VISIBLE);
			}
			break;
		// 查看照片上传状态
		case R.id.look_photo:
			Intent intent5 = new Intent(Function.this, PhotoState.class);
			startActivity(intent5);
			break;
		// 退出程序
		case R.id.logoff:
			DialogTool.setSelectDialog("是否退出程序？", Function.this);
			break;
		}
	}

	Runnable downLoadBnsInfoRunable = new Runnable() {
		@Override
		public void run() {
			while (isContinueUpdata) {
				try {
					Log.i("BBB", "---------11---------");
					ArrayList<HashMap<String, String>> list = SQLFuntion.query(Function.this, null, null);
					for (int i = 0; i < list.size(); i++) {
						try {
							if (list.get(i).get("isupload").equals("02")) {
								String photoPath = photoSubcatalog + list.get(i).get("jylsh") + "/" + list.get(i).get("zpmc")
										+ ".jpg";
								File file = new File(photoPath);
								if (file.exists()) {
									// 调01J63接口
									String data01J63 = ConnectMethods.connectWebService(ip, StaticValues.writeObject, jkxlh,
											"01J63", get01J63Data(list.get(i), photoPath), StaticValues.writeResult,
											StaticValues.timeoutFifteen, StaticValues.numberThree);
									Log.i("BBB", "data01J63:" + data01J63);
									List<CodeDomain> codelists01J63 = XMLParsingMethods.saxcode(data01J63);
									if (codelists01J63.get(0).getCode().equals("1")) {
										Object[] data = { "03", list.get(i).get("jylsh"), list.get(i).get("zpzl") };
										SQLFuntion.update(Function.this, data);
									}
									Thread.sleep(1000);
								}
							}
						} catch (Exception e) {
							Thread.sleep(1000);
							e.printStackTrace();
						}
					}
					Thread.sleep(15000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	/**
	 * 获取即将提交01J63数据
	 */
	private String get01J63Data(HashMap<String, String> hashMap, String photoPath) {
		List<String> datalist = new ArrayList<String>();
		datalist.add(jgbh);
		datalist.add(hashMap.get("jylsh"));
		datalist.add(hashMap.get("pssj"));
		datalist.add(hashMap.get("wjybh"));
		datalist.add(hashMap.get("zpzl"));
		datalist.add("1");
		String xmlData = CombinationXMLData.getPhotoWriteData(StaticValues.FieldName01J63, datalist,
				PhotoTool.getPhotoData(photoPath));
		Log.i("BBB", "01J63xmlData:" + xmlData);
		return xmlData;
	}

	/**
	 * 重写返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DialogTool.setSelectDialog("是否退出程序？", Function.this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isContinueUpdata = false;
	}
}
