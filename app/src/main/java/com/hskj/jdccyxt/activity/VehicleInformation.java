package com.hskj.jdccyxt.activity;

import java.util.ArrayList;
import java.util.List;

import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.C02Domain;
import com.hskj.jdccyxt.domain.C09Domain;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @������VehicleInformation
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ�����������Ϣ
 * @�������ڣ�2016.05.27
 */
@SuppressWarnings("deprecation")
public class VehicleInformation extends ActivityGroup {
	private TextView id_notice_information, id_basic_information, id_notice_photo;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private String data01C05;
	private List<TextView> textlist = new ArrayList<TextView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vehicle_information);
		setInitialization();
		// ��һ�����������
		Bundle bundle = this.getIntent().getExtras();
		data01C05 = getIntent().getStringExtra("C05DATA");
		initView();
		// mViewPager��ʼ��
		viewPager.setAdapter(new PagerAdapter() {
			public int getCount() {
				return pageViews.size();
			}

			public boolean isViewFromObject(View view, Object objcet) {
				return view == objcet;
			}

			// ��������Ҫ����ˮƽ�л���ҳ������˼��غͳ�ʼ�� android:tileMode="repeat"
			public Object instantiateItem(View view, int id) {
				((ViewPager) view).addView(pageViews.get(id));
				return pageViews.get(id);
			}

			public void destroyItem(View view, int id, Object arg2) {
				((ViewPager) view).removeView(pageViews.get(id));
			}
		});
		viewPager.setCurrentItem(1);// Ĭ����ʾҳ��
		// ViewPagerҳ�滬���¼�
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				resetOtherTabs();
				textlist.get(position).setTextColor(Color.BLACK);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		initTabIndicator();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setInitialization() {
		id_notice_information = (TextView) this.findViewById(R.id.id_notice_information);
		id_basic_information = (TextView) this.findViewById(R.id.id_basic_information);
		id_notice_photo = (TextView) this.findViewById(R.id.id_notice_photo);
		viewPager = (ViewPager) this.findViewById(R.id.id_viewpager);
	}

	public void initView() {
		pageViews = new ArrayList<View>();
		Intent intent1 = new Intent(VehicleInformation.this, NoticeInformation.class);
		intent1.putExtra("C05DATA", data01C05);
		View view1 = getLocalActivityManager().startActivity("������Ϣ", intent1).getDecorView();
		Intent intent2 = new Intent(VehicleInformation.this, BasicInformation.class);
		intent2.putExtra("C05DATA", data01C05);
		View view2 = getLocalActivityManager().startActivity("������Ϣ", intent2).getDecorView();
		Intent intent3 = new Intent(VehicleInformation.this, NoticePhoto.class);
		intent3.putExtra("C05DATA", data01C05);
		View view3 = getLocalActivityManager().startActivity("������Ƭ", intent3).getDecorView();
		pageViews.add(0, view1);
		pageViews.add(1, view2);
		pageViews.add(2, view3);
	}

	private void initTabIndicator() {
		textlist.add(id_notice_information);
		textlist.add(id_basic_information);
		textlist.add(id_notice_photo);
		id_basic_information.setTextColor(Color.BLACK);
	}

	/*
	 * ��ť����¼�
	 */
	@SuppressLint("ShowToast")
	public void doClick(View v) {
		resetOtherTabs();
		switch (v.getId()) {
		case R.id.id_notice_information:
			textlist.get(0).setTextColor(Color.WHITE);
			viewPager.setCurrentItem(0);
			break;
		case R.id.id_basic_information:
			textlist.get(1).setTextColor(Color.WHITE);
			viewPager.setCurrentItem(1);
			break;
		case R.id.id_notice_photo:
			textlist.get(2).setTextColor(Color.WHITE);
			viewPager.setCurrentItem(2);
			break;
		}
	}

	/**
	 * ����������Button
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < textlist.size(); i++) {
			textlist.get(i).setTextColor(Color.WHITE);
		}
	}
}
