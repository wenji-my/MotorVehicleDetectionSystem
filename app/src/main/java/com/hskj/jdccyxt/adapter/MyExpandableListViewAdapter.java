package com.hskj.jdccyxt.adapter;

import java.util.HashMap;
import java.util.List;
import com.hskj.jdccyxt.R;
import com.hskj.jdccyxt.domain.MaulItemGroup;
import com.hskj.jdccyxt.domain.MaulListItem;
import com.hskj.jdccyxt.toolunit.MutualConversionNameID;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<MaulItemGroup> groups;
	private LayoutInflater GroupInflater; // 用于加载group的布局xml
	private LayoutInflater ChildInflater; // 用于加载listitem的布局xm
	private HashMap<String, String> bhgMap;

	class ExpandableListHolder {
		LinearLayout linearLayout;
		TextView titleName, cyxmName;
		RadioGroup ra_cyxm;
		RadioButton qualified, unqualified, notcheck;
	}

	public MyExpandableListViewAdapter(Context context, List<MaulItemGroup> groups, String cyxmData, String bhgx, String hgx,
			String bcyx, String[] inspectionItems, String[] allNumbers) {
		this.context = context;
		this.groups = groups;
		GroupInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ChildInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		String[] cyxmlist = cyxmData.split(",");// 查验项目
		String[] bhgxlist = bhgx.split(" ");// 不合格项
		String[] hgxlist = hgx.split(" ");// 合格项
		String[] bcyxlist = bcyx.split(" ");// 不查验项
		bhgMap = new HashMap<String, String>();
		if (hgx.equals("") && bhgx.equals("") && bcyx.equals("")) {
			// 第一次查验
			for (int i = 0; i < allNumbers.length; i++) {
				String allName = MutualConversionNameID.getCyxmName(inspectionItems, allNumbers[i]);
				bhgMap.put(allName, "3");// 1代表合格，2代表不合格,3代表未检
				for (int j = 0; j < cyxmlist.length; j++) {
					String aCyxmName = MutualConversionNameID.getCyxmName(inspectionItems, cyxmlist[j]);
					if (aCyxmName.equals(allName)) {
						bhgMap.put(aCyxmName, "1");
						Log.i("AAA", aCyxmName + "qualified1");
					}
				}
			}
		} else {
			// 复查
			if (!hgx.equals("")) {
				for (int i = 0; i < hgxlist.length; i++) {
					String aHgxName = MutualConversionNameID.getCyxmName(inspectionItems, (Integer.parseInt(hgxlist[i]) + ""));
					bhgMap.put(aHgxName, "1");
				}
			}
			if (!bhgx.equals("")) {
				for (int i = 0; i < bhgxlist.length; i++) {
					String aBhgxName = MutualConversionNameID.getCyxmName(inspectionItems, (Integer.parseInt(bhgxlist[i]) + ""));
					bhgMap.put(aBhgxName, "2");
				}
			}
			if (!bcyx.equals("")) {
				for (int i = 0; i < bcyxlist.length; i++) {
					String aBcyxName = MutualConversionNameID.getCyxmName(inspectionItems, (Integer.parseInt(bcyxlist[i]) + ""));
					bhgMap.put(aBcyxName, "3");
				}
			}
		}
	}

	@Override
	public Object getChild(int arg0, int arg1) {// 根据组索引和item索引，取得listitem
		return groups.get(arg0).children.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {// 返回item索引
		return arg1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ExpandableListHolder holder2 = null; // 清空临时变量
		if (convertView == null) {
			convertView = ChildInflater.inflate(R.layout.item_child_view, null);
			holder2 = new ExpandableListHolder();
			holder2.cyxmName = (TextView) convertView.findViewById(R.id.txt_cyxm_name);
			holder2.ra_cyxm = (RadioGroup) convertView.findViewById(R.id.ra_cyxm);
			holder2.qualified = (RadioButton) convertView.findViewById(R.id.qualified);
			holder2.unqualified = (RadioButton) convertView.findViewById(R.id.unqualified);
			holder2.notcheck = (RadioButton) convertView.findViewById(R.id.notcheck);
			convertView.setTag(holder2);
		} else { // 若行已初始化，直接从tag属性获得子视图的引用
			holder2 = (ExpandableListHolder) convertView.getTag();
		}
		// 获得行数据（模型）
		final MaulListItem info = this.groups.get(groupPosition).children.get(childPosition);
		if (info != null) {
			holder2.cyxmName.setText(info.name);
			if (bhgMap.get(info.name) == null) {
				holder2.notcheck.setChecked(true);
			} else if (bhgMap.get(info.name).equals("1")) {
				holder2.qualified.setChecked(true);
			} else if (bhgMap.get(info.name).equals("2")) {
				holder2.unqualified.setChecked(true);
			} else if (bhgMap.get(info.name).equals("3")) {
				holder2.notcheck.setChecked(true);
			}
			holder2.qualified.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					bhgMap.put(info.name, "1");
					Log.i("AAA", info.name + "qualified1");
				}
			});
			holder2.unqualified.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					bhgMap.put(info.name, "2");
					Log.i("AAA", info.name + "unqualified2");
				}
			});
			holder2.notcheck.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					bhgMap.put(info.name, "3");
					Log.i("AAA", info.name + "notcheck3");
				}
			});
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {// 根据组索引返回分组的子item数
		return groups.get(arg0).children.size();
	}

	@Override
	public Object getGroup(int arg0) {// 根据组索引返回组
		return groups.get(arg0);
	}

	@Override
	public int getGroupCount() {// 返回分组数
		return groups.size();
	}

	@Override
	public long getGroupId(int arg0) {// 返回分组索引
		return arg0;
	}

	@Override
	public View getGroupView(int position, boolean isExpanded, View view, ViewGroup parent) {
		ExpandableListHolder holder = null;
		if (view == null) {
			view = GroupInflater.inflate(R.layout.item_group_view, null);
			// 下面主要是取得组的各子视图，设置子视图的属性。用tag来保存各子视图的引用
			holder = new ExpandableListHolder();
			holder.titleName = (TextView) view.findViewById(R.id.txt_title);
			view.setTag(holder);
		} else { // 若view不为空，直接从view的tag属性中获得各子视图的引用
			holder = (ExpandableListHolder) view.getTag();
		}
		// 对应于组索引的组数据（模型）
		MaulItemGroup info = this.groups.get(position);
		if (info != null) {
			holder.titleName.setText(info.title);
		}
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	public HashMap<String, String> returnCyxmInformation() {
		return bhgMap;
	}
}
