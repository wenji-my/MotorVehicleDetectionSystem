package com.hskj.jdccyxt.toolunit;

public class CommonData {
	static String usingFileName = "";
	static String imei = "";
	static String[] plateProxs = new String[] { "��", "��", "��", "��", "��", "ԥ", "��", "��", "��", "��", "��", "³", "��", "��", "��", "��",
			"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��" };

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
