package com.hskj.jdccyxt.intentunit.ksoap2;

import java.net.URLDecoder;
import org.ksoap2.serialization.SoapObject;


/**
 * @类名：ConnectMethods
 * @author:广东泓胜科技有限公司
 * @实现功能：ksoap2联网部分
 * @创建日期：2016.04.16
 */
public class ConnectMethods {
	/**
	 * @ip:服务器地址
	 * @methodName：接口类别
	 * @jkxlh：接口序列号
	 * @jkid：接口ID
	 * @xmlDoc：上传的XML文档
	 * @ResultGs：返回数据需解析的格式
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
