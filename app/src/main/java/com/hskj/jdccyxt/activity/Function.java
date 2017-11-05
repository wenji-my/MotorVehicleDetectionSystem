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
 * @������Function
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ����ܽ���
 * @�������ڣ�2016.04.12
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
	private List<C09Domain> operators;// ����ĵ�¼Ա��Ϣ
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
		// ��ȡ�����е�����
		SharedPreferences preferences = getSharedPreferences(StaticValues.ADDRESS_SETTING, 0);
		ip = preferences.getString("IP", "");
		jgbh = preferences.getString("JGBH", "");
		jkxlh = preferences.getString("JKXLH", "");
		// ��һ�����������
		Bundle bundle = this.getIntent().getExtras();
		operators = (List<C09Domain>) bundle.getSerializable("operatorObj");
		userID = getIntent().getStringExtra("USERID");
		isContinueUpdata = true;
		setLateralSpreadsMenu();
		// ����Ӧ�ü�ȫ�ֱ���
		app = (MyApplication) getApplication();
		app.setVersion("�汾�ţ�" + VersionTool.getAPPVersionCode(this));
		app.setViewerName("����Ա��" + operators.get(0).getUsername());
		txt_version.setText(app.getVersion());
		txt_viewer_name.setText(app.getViewerName());
		if (operators.get(0).getXszgy().equals("0")) {
			vehicle_license.setEnabled(true);
		}
		// �ϴ���Ƭ
		uploadThread = new Thread(downLoadBnsInfoRunable);
		uploadThread.start();
	}

	/**
	 * ��ʼ���ؼ�
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
	 * �����໬�˵�
	 */
	private void setLateralSpreadsMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		// ���ô�����Ļ��ģʽ
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// ������Ӱ�Ŀ��
		menu.setShadowWidthRes(R.dimen.shadow_width);
		// ������Ӱ����ɫ
		menu.setShadowDrawable(R.drawable.shadow);
		// ���û����˵���ͼ�Ŀ��
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// ���ý��뽥��Ч����ֵ
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
	 * ��ѡ��ؼ����и�ֵ.����
	 */
	private void assignment(View v) {
		RadioGroup photoType = (RadioGroup) v.findViewById(R.id.photoType);
		RadioGroup testdataType = (RadioGroup) v.findViewById(R.id.testdataType);
		// �Կؼ���ֵ
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
		// �Կؼ����н������
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
	 * ��ť����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ������ѯ����
		case R.id.vehicle_query:
			Intent intent1 = new Intent(Function.this, VehicleQuery.class);
			intent1.putExtra("CYLX", "cy");
			intent1.putExtra("USERID", userID);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("operatorObj", (Serializable) operators);
			intent1.putExtras(bundle1);
			startActivity(intent1);
			break;
		// ��Ƭ���Ľ���
		case R.id.photos_taken:
			Intent intent2 = new Intent(Function.this, VehicleQuery.class);
			intent2.putExtra("CYLX", "fcy");
			intent2.putExtra("USERID", userID);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("operatorObj", (Serializable) operators);
			intent2.putExtras(bundle2);
			startActivity(intent2);
			break;
		// �����޸Ľ���
		case R.id.password_change:
			Intent intent3 = new Intent(Function.this, PasswordChange.class);
			intent3.putExtra("USERID", userID);
			intent3.putExtra("USERNAME", operators.get(0).getUsername());
			startActivity(intent3);
			break;
		// �໬�˵�
		case R.id.but_menu:
			menu.showMenu();
			break;
		// ��ʻ֤��Ƭ����
		case R.id.vehicle_license:
			Intent intent4 = new Intent(Function.this, VehicleQuery.class);
			intent4.putExtra("CYLX", "xsz");
			intent4.putExtra("USERID", userID);
			Bundle bundle4 = new Bundle();
			bundle4.putSerializable("operatorObj", (Serializable) operators);
			intent4.putExtras(bundle4);
			startActivity(intent4);
			break;
		// �������
		case R.id.instrument_detection:
			Intent intent6 = new Intent(Function.this, InstrumentDetection.class);
			intent6.putExtra("USERID", userID);
			startActivity(intent6);
			break;
		// ����������������
		case R.id.but_arrow:
			if (czsz.getVisibility() == View.VISIBLE) {
				but_arrow.setBackgroundResource(R.drawable.down_arrow);
				czsz.setVisibility(View.GONE);
			} else {
				but_arrow.setBackgroundResource(R.drawable.up_arrow);
				czsz.setVisibility(View.VISIBLE);
			}
			break;
		// �鿴��Ƭ�ϴ�״̬
		case R.id.look_photo:
			Intent intent5 = new Intent(Function.this, PhotoState.class);
			startActivity(intent5);
			break;
		// �˳�����
		case R.id.logoff:
			DialogTool.setSelectDialog("�Ƿ��˳�����", Function.this);
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
									// ��01J63�ӿ�
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
	 * ��ȡ�����ύ01J63����
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
	 * ��д���ؼ�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DialogTool.setSelectDialog("�Ƿ��˳�����", Function.this);
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
