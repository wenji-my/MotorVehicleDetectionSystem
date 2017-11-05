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
 * @类名：VehicleInformation
 * @author:广东泓胜科技有限公司
 * @实现功能：车辆基本信息
 * @创建日期：2016.05.27
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
		// 上一级传入的数据
		Bundle bundle = this.getIntent().getExtras();
		data01C05 = getIntent().getStringExtra("C05DATA");
		initView();
		// mViewPager初始化
		viewPager.setAdapter(new PagerAdapter() {
			public int getCount() {
				return pageViews.size();
			}

			public boolean isViewFromObject(View view, Object objcet) {
				return view == objcet;
			}

			// 这里会对需要进行水平切换的页面进行了加载和初始化 android:tileMode="repeat"
			public Object instantiateItem(View view, int id) {
				((ViewPager) view).addView(pageViews.get(id));
				return pageViews.get(id);
			}

			public void destroyItem(View view, int id, Object arg2) {
				((ViewPager) view).removeView(pageViews.get(id));
			}
		});
		viewPager.setCurrentItem(1);// 默认显示页面
		// ViewPager页面滑动事件
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
	 * 初始化控件
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
		View view1 = getLocalActivityManager().startActivity("公告信息", intent1).getDecorView();
		Intent intent2 = new Intent(VehicleInformation.this, BasicInformation.class);
		intent2.putExtra("C05DATA", data01C05);
		View view2 = getLocalActivityManager().startActivity("基本信息", intent2).getDecorView();
		Intent intent3 = new Intent(VehicleInformation.this, NoticePhoto.class);
		intent3.putExtra("C05DATA", data01C05);
		View view3 = getLocalActivityManager().startActivity("公告照片", intent3).getDecorView();
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
	 * 按钮点击事件
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
	 * 重置其他的Button
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < textlist.size(); i++) {
			textlist.get(i).setTextColor(Color.WHITE);
		}
	}
}
