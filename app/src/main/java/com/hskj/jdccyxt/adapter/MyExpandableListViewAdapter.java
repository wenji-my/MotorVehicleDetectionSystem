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
	private LayoutInflater GroupInflater; // ���ڼ���group�Ĳ���xml
	private LayoutInflater ChildInflater; // ���ڼ���listitem�Ĳ���xm
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
		String[] cyxmlist = cyxmData.split(",");// ������Ŀ
		String[] bhgxlist = bhgx.split(" ");// ���ϸ���
		String[] hgxlist = hgx.split(" ");// �ϸ���
		String[] bcyxlist = bcyx.split(" ");// ��������
		bhgMap = new HashMap<String, String>();
		if (hgx.equals("") && bhgx.equals("") && bcyx.equals("")) {
			// ��һ�β���
			for (int i = 0; i < allNumbers.length; i++) {
				String allName = MutualConversionNameID.getCyxmName(inspectionItems, allNumbers[i]);
				bhgMap.put(allName, "3");// 1����ϸ�2�����ϸ�,3����δ��
				for (int j = 0; j < cyxmlist.length; j++) {
					String aCyxmName = MutualConversionNameID.getCyxmName(inspectionItems, cyxmlist[j]);
					if (aCyxmName.equals(allName)) {
						bhgMap.put(aCyxmName, "1");
						Log.i("AAA", aCyxmName + "qualified1");
					}
				}
			}
		} else {
			// ����
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
	public Object getChild(int arg0, int arg1) {// ������������item������ȡ��listitem
		return groups.get(arg0).children.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {// ����item����
		return arg1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ExpandableListHolder holder2 = null; // �����ʱ����
		if (convertView == null) {
			convertView = ChildInflater.inflate(R.layout.item_child_view, null);
			holder2 = new ExpandableListHolder();
			holder2.cyxmName = (TextView) convertView.findViewById(R.id.txt_cyxm_name);
			holder2.ra_cyxm = (RadioGroup) convertView.findViewById(R.id.ra_cyxm);
			holder2.qualified = (RadioButton) convertView.findViewById(R.id.qualified);
			holder2.unqualified = (RadioButton) convertView.findViewById(R.id.unqualified);
			holder2.notcheck = (RadioButton) convertView.findViewById(R.id.notcheck);
			convertView.setTag(holder2);
		} else { // �����ѳ�ʼ����ֱ�Ӵ�tag���Ի������ͼ������
			holder2 = (ExpandableListHolder) convertView.getTag();
		}
		// ��������ݣ�ģ�ͣ�
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
	public int getChildrenCount(int arg0) {// �������������ط������item��
		return groups.get(arg0).children.size();
	}

	@Override
	public Object getGroup(int arg0) {// ����������������
		return groups.get(arg0);
	}

	@Override
	public int getGroupCount() {// ���ط�����
		return groups.size();
	}

	@Override
	public long getGroupId(int arg0) {// ���ط�������
		return arg0;
	}

	@Override
	public View getGroupView(int position, boolean isExpanded, View view, ViewGroup parent) {
		ExpandableListHolder holder = null;
		if (view == null) {
			view = GroupInflater.inflate(R.layout.item_group_view, null);
			// ������Ҫ��ȡ����ĸ�����ͼ����������ͼ�����ԡ���tag�����������ͼ������
			holder = new ExpandableListHolder();
			holder.titleName = (TextView) view.findViewById(R.id.txt_title);
			view.setTag(holder);
		} else { // ��view��Ϊ�գ�ֱ�Ӵ�view��tag�����л�ø�����ͼ������
			holder = (ExpandableListHolder) view.getTag();
		}
		// ��Ӧ���������������ݣ�ģ�ͣ�
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
