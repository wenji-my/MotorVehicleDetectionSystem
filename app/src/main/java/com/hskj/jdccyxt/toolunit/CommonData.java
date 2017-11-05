package com.hskj.jdccyxt.toolunit;

public class CommonData {
	static String usingFileName = "";
	static String imei = "";
	static String[] plateProxs = new String[] { "¾©", "½ò", "»¦", "Óå", "¼½", "Ô¥", "ÔÆ", "ÁÉ", "ºÚ", "Ïæ", "Íî", "Â³", "ÐÂ", "ËÕ", "Õã", "¸Ó",
			"¶õ", "¹ð", "¸Ê", "½ú", "ÃÉ", "ÉÂ", "¼ª", "Ãö", "¹ó", "ÔÁ", "Çà", "²Ø", "´¨", "Äþ", "Çí" };

	public static String GetUsingFileName() {
		return usingFileName;
	}

	public static void SetUsingFileName(String value) {
		usingFileName = value;
	}

	public static String GetImei() {
		return imei;
	}

	public static void SetImei(String value) {
		imei = value;
	}

	public static String[] GetPlateProxs() {
		return plateProxs;
	}
}
