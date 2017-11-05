package com.hskj.jdccyxt.activity;

import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.C02Domain;
import com.hskj.jdccyxt.domain.C09Domain;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

/**
 * @������CosmeticInspect
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ�����������ݲ��鼰��Ƭ����
 * @�������ڣ�2016.04.19
 */
@SuppressWarnings("deprecation")
public class CosmeticInspect extends TabActivity {
	private TextView txt_version, txt_viewer_name;
	private MyApplication app;
	private TabHost host;
	private int i = 0;
	private C09Domain operators;// ����ĵ�¼Ա��Ϣ
	private C02Domain vehiclelist;// ��01C02�ӿڻ��������
	private String userID;// ��¼�˺�
	private String data01C05;// 01C05�ӿ�����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cosmetic_inspect);
		// ��һ�����������
		Bundle bundle = this.getIntent().getExtras();
		operators = (C09Domain) bundle.getSerializable("operatorObj");
		vehiclelist = (C02Domain) bundle.getSerializable("vehicleQueryObj");
		userID = getIntent().getStringExtra("USERID");
		data01C05 = getIntent().getStringExtra("C05DATA");
		// ���ñ���
		txt_version = (TextView) this.findViewById(R.id.txt_version);
		txt_viewer_name = (TextView) this.findViewById(R.id.txt_viewer_name);
		app = (MyApplication) getApplication();
		txt_version.setText(app.getVersion());
		txt_viewer_name.setText(app.getViewerName());
		// ���tabhost��Ƭ����
		host = getTabHost();

		// ��ӵ�һ����Ƭ
		Intent intent1 = new Intent(CosmeticInspect.this, DataInspection.class);
		intent1.putExtra("USERID", userID);
		Bundle bundle1 = new Bundle();
		bundle1.putSerializable("operatorObj", operators);
		bundle1.putSerializable("vehicleQueryObj", vehiclelist);
		intent1.putExtras(bundle1);
		TabSpec tab1 = host.newTabSpec("tab1").setIndicator("�� �� ��", getResources().getDrawable(R.drawable.inspection))
				.setContent(intent1);
		host.addTab(tab1);
		// ��ӵڶ�����Ƭ
		Intent intent2 = new Intent(CosmeticInspect.this, PhotoShot.class);
		intent2.putExtra("STATE", "normal");
		Bundle bundle2 = new Bundle();
		bundle2.putSerializable("operatorObj", operators);
		bundle2.putSerializable("vehicleQueryObj", vehiclelist);
		intent2.putExtras(bundle2);
		TabSpec tab2 = host.newTabSpec("tab2").setIndicator("�� �� ��", getResources().getDrawable(R.drawable.shoot_photo))
				.setContent(intent2);
		host.addTab(tab2);
		// ��ӵ�������Ƭ
		Intent intent3 = new Intent(CosmeticInspect.this, VehicleInformation.class);
		intent3.putExtra("C05DATA", data01C05);
		Bundle bundle3 = new Bundle();
		bundle3.putSerializable("vehicleQueryObj", vehiclelist);
		intent3.putExtras(bundle3);
		TabSpec tab3 = host.newTabSpec("tab3").setIndicator("�� Ϣ ��", getResources().getDrawable(R.drawable.information))
				.setContent(intent3);
		host.addTab(tab3);
	}

	// ������ҳ����
	private GestureDetector detector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX, float velocityY) {
			if ((e2.getRawX() - e1.getRawX()) > 30) {
				showNext();
				return true;
			}
			if ((e1.getRawX() - e2.getRawX()) > 30) {
				showPre();
				return true;
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	});

	public boolean onTouchEvent(android.view.MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	// ��һҳ
	protected void showNext() {
		host.setCurrentTab(i = i == 2 ? i = 0 : ++i);
	}

	// ��һҳ
	protected void showPre() {
		host.setCurrentTab(i = i == 0 ? i = 2 : --i);
	}

}
