package com.hskj.jdccyxt.toolunit.sql;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;

public class SQLFuntion {

	static DBHelper dbManager;

	public static void initTable(Context context) {
		dbManager = new DBHelper(context);
		dbManager.getReadableDatabase();
		// dbManager.getWritableDatabase();
	}

	/**
	 * 添加记录
	 * 
	 * @param context
	 * @param data
	 */
	public static void insert(Context context, Object[] data) {
		dbManager = new DBHelper(context);
		dbManager.getWritableDatabase();
		String sql = "insert into photos(jylsh,pssj,wjybh,zpzl,zpmc,isupload) values(?,?,?,?,?,?)";
		Object[] bindArgs = data;
		DBManager manager = new DBManager(context);
		manager.updateBySQL(sql, bindArgs);
	}

	/**
	 * 更新记录
	 * 
	 * @param context
	 * @param data
	 */
	public static void update(Context context, Object[] data) {
		dbManager = new DBHelper(context);
		dbManager.getWritableDatabase();
		String sql = "update  photos set isupload = ? where jylsh = ? and zpzl= ?";
		DBManager manager = new DBManager(context);
		manager.updateBySQL(sql, data);
	}

	public static void updatetwo(Context context, Object[] data) {
		dbManager = new DBHelper(context);
		dbManager.getWritableDatabase();
		String sql = "update  photos set pssj = ? where jylsh = ? and zpzl= ?";
		DBManager manager = new DBManager(context);
		manager.updateBySQL(sql, data);
	}

	/**
	 * 删除记录
	 * 
	 * @param context
	 * @param data
	 */
	public static void delete(Context context, Object[] data) {
		dbManager = new DBHelper(context);
		dbManager.getWritableDatabase();
		String sql = "delete  from  photos  where jylsh = ? ";
		DBManager manager = new DBManager(context);
		manager.updateBySQL(sql, data);
	}
	
	public static void deletePhotoData(Context context, Object[] data) {
		dbManager = new DBHelper(context);
		dbManager.getWritableDatabase();
		String sql = "delete  from  photos  where jylsh = ? and zpzl= ? ";
		DBManager manager = new DBManager(context);
		manager.updateBySQL(sql, data);
	}

	/**
	 * 查找记录
	 * 
	 * @param context
	 * @param jylshData
	 * @param zpzlData
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> query(Context context, String jylshData, String zpzlData) {
		dbManager = new DBHelper(context);
		dbManager.getReadableDatabase();
		String sql = "select *   from  photos where jylsh like ?  and zpzl like?";
		DBManager manager = new DBManager(context);
		ArrayList<HashMap<String, String>> list = null;
		if (jylshData == null && zpzlData == null) {
			list = manager.queryMultiMaps(sql, new String[] { "%", "%" });
		} else if (jylshData != null && zpzlData == null) {
			list = manager.queryMultiMaps(sql, new String[] { jylshData, "%" });
		} else {
			list = manager.queryMultiMaps(sql, new String[] { jylshData, zpzlData });
		}
		return list;
	}

	public static ArrayList<HashMap<String, String>> querytwo(Context context, String jylshData, String isuploadData) {
		dbManager = new DBHelper(context);
		dbManager.getReadableDatabase();
		String sql = "select *   from  photos where jylsh like ?  and isupload like?";
		DBManager manager = new DBManager(context);
		ArrayList<HashMap<String, String>> list = null;
		if (jylshData != null) {
			jylshData = "%" + jylshData + "%";
			list = manager.queryMultiMaps(sql, new String[] { jylshData, isuploadData });
		} else {
			list = manager.queryMultiMaps(sql, new String[] { "%", isuploadData });
		}
		return list;
	}
}
