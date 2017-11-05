package com.hskj.jdccyxt.toolunit;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @������CombinationXMLData
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ�XML�ĵ��������
 * @�������ڣ�2016.04.21
 */
public class CombinationXMLData {

	/**
	 * ��ѯ��������Ϸ���
	 * 
	 * @param fieldName
	 *            ���ֶ�����
	 * @param fieldDta
	 *            ���ύ��������
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
	 * д����������Ϸ���
	 * 
	 * @param fieldName
	 *            ���ֶ�����
	 * @param fieldDta
	 *            ���ύ��������
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
	 * �������
	 * 
	 * @param fieldName
	 *            ���ֶ�����
	 * @param fieldDta
	 *            ���ύ��������
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
	 * ��Ƭд����������Ϸ���
	 * 
	 * @param fieldName
	 *            ���ֶ�����
	 * @param fieldDta
	 *            ���ύ��������
	 * @param photoData
	 *            ����Ƭ��������
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
