package com.hskj.jdccyxt.toolunit;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @类名：CombinationXMLData
 * @author:广东泓胜科技有限公司
 * @实现功能：XML文档数据组合
 * @创建日期：2016.04.21
 */
public class CombinationXMLData {

	/**
	 * 查询类数据组合方法
	 * 
	 * @param fieldName
	 *            ：字段名称
	 * @param fieldDta
	 *            ：提交数据内容
	 * @return
	 */
	public static String getQueryData(String[] fieldName, List<String> fieldDta) {
		try {
			String queryData = null;
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
			sbuilder.append("<root>");
			sbuilder.append("<QueryCondition>");
			for (int i = 0; i < fieldName.length; i++) {
				sbuilder.append("<" + fieldName[i] + ">");
				sbuilder.append(URLEncoder.encode(fieldDta.get(i), "utf-8"));
				sbuilder.append("</" + fieldName[i] + ">");
			}
			sbuilder.append("</QueryCondition>");
			sbuilder.append("</root>");
			byte[] xmlData = sbuilder.toString().getBytes();
			queryData = new String(xmlData);
			return queryData;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 写入类数据组合方法
	 * 
	 * @param fieldName
	 *            ：字段名称
	 * @param fieldDta
	 *            ：提交数据内容
	 * @return
	 */
	public static String getWriteData(String[] fieldName, List<String> fieldDta) {
		try {
			String writeData = null;
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
			sbuilder.append("<root>");
			sbuilder.append("<vehispara>");
			for (int i = 0; i < fieldName.length; i++) {
				sbuilder.append("<" + fieldName[i] + ">");
				if (fieldName[i].equals("hgx") || fieldName[i].equals("bhgx") || fieldName[i].equals("bcyx")) {
					sbuilder.append(fieldDta.get(i));
				} else {
					sbuilder.append(URLEncoder.encode(fieldDta.get(i), "utf-8"));
				}
				sbuilder.append("</" + fieldName[i] + ">");
			}
			sbuilder.append("</vehispara>");
			sbuilder.append("</root>");
			byte[] xmlData = sbuilder.toString().getBytes();
			writeData = new String(xmlData);
			return writeData;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 组合数据
	 * 
	 * @param fieldName
	 *            ：字段名称
	 * @param fieldDta
	 *            ：提交数据内容
	 * @return
	 */
	public static String getGatherData(String[] fieldName, List<String> fieldDta) {
		try {
			String gatherData = null;
			StringBuilder sbuilder = new StringBuilder();
			for (int i = 0; i < fieldName.length; i++) {
				sbuilder.append("<" + fieldName[i] + ">");
				sbuilder.append(URLEncoder.encode(fieldDta.get(i), "utf-8"));
				sbuilder.append("</" + fieldName[i] + ">");
			}
			byte[] xmlData = sbuilder.toString().getBytes();
			gatherData = new String(xmlData);
			return gatherData;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 照片写入类数据组合方法
	 * 
	 * @param fieldName
	 *            ：字段名称
	 * @param fieldDta
	 *            ：提交数据内容
	 * @param photoData
	 *            ：照片数据内容
	 * @return
	 */
	public static String getPhotoWriteData(String[] fieldName, List<String> fieldDta, String photoData) {
		try {
			String writeData = null;
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
			sbuilder.append("<root>");
			sbuilder.append("<vehispara>");
			for (int i = 0; i < fieldName.length; i++) {
				sbuilder.append("<" + fieldName[i] + ">");
				if (fieldName[i].equals("zp")) {
					sbuilder.append(photoData);
				} else {
					sbuilder.append(URLEncoder.encode(fieldDta.get(i), "utf-8"));
				}
				sbuilder.append("</" + fieldName[i] + ">");
			}
			sbuilder.append("</vehispara>");
			sbuilder.append("</root>");
			byte[] xmlData = sbuilder.toString().getBytes();
			writeData = new String(xmlData);
			return writeData;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
