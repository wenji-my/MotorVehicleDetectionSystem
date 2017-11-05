package com.hskj.jdccyxt.intentunit.ksoap2;

import java.net.URLDecoder;
import org.ksoap2.serialization.SoapObject;


/**
 * @������ConnectMethods
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ�ksoap2��������
 * @�������ڣ�2016.04.16
 */
public class ConnectMethods {
	/**
	 * @ip:��������ַ
	 * @methodName���ӿ����
	 * @jkxlh���ӿ����к�
	 * @jkid���ӿ�ID
	 * @xmlDoc���ϴ���XML�ĵ�
	 * @ResultGs����������������ĸ�ʽ
	 */
	public static String connectWebService(String ip, String methodName, String jkxlh, String jkid, String xmlDoc,
			String ResultGs, int timeout, int cscs) {
		try {
			ConnectWeb wsh = new ConnectWeb(methodName, ip, timeout, cscs);
			wsh.addProperty("xtlb", "18");
			wsh.addProperty("jkxlh", jkxlh);
			wsh.addProperty("jkid", jkid);
			wsh.addProperty("xmlDoc", xmlDoc);
			SoapObject result = wsh.connectToWebService();
			String dataone = result.getProperty(ResultGs).toString();
			String datatwo = URLDecoder.decode(dataone, "utf-8");
			return datatwo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String connectWebServicetwo(String ip, String methodName, String jkxlh, String jkid, String xmlDoc,
			String ResultGs, int timeout, int cscs) {
		try {
			ConnectWeb wsh = new ConnectWeb(methodName, ip, timeout, cscs);
			wsh.addProperty("xtlb", "18");
			wsh.addProperty("jkxlh", jkxlh);
			wsh.addProperty("jkid", jkid);
			wsh.addProperty("xmlDoc", xmlDoc);
			SoapObject result = wsh.connectToWebService();
			String dataone = result.getProperty(ResultGs).toString();
			//String datatwo = URLDecoder.decode(dataone, "utf-8");
			return dataone;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
