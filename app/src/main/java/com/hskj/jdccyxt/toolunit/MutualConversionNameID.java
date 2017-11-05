package com.hskj.jdccyxt.toolunit;

import java.util.List;
import com.hskj.jdccyxt.domain.C06DomainCLLX;
import com.hskj.jdccyxt.domain.C06DomainZPSM;

/**
 * @������MutualConversionNameID
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ����ƣ�ID�໥ת��
 * @�������ڣ�2016.04.18
 */
public class MutualConversionNameID {

	/**
	 * ��������IDת��Ϊ����
	 */
	public static String getHpzlName(String ID) {
		String hpzlName = "";
		for (int i = 0; i < StaticValues.hpzlData.length; i++) {
			if (StaticValues.hpzlData[i].split(",")[0].equals(ID)) {
				hpzlName = StaticValues.hpzlData[i].split(",")[1];
			}
		}
		return hpzlName;
	}

	/**
	 * ������������ת��ΪID
	 */
	public static String getHpzlID(String name) {
		String hpzlID = "";
		for (int i = 0; i < StaticValues.hpzlData.length; i++) {
			if (StaticValues.hpzlData[i].split(",")[1].equals(name)) {
				hpzlID = StaticValues.hpzlData[i].split(",")[0];
			}
		}
		return hpzlID;
	}

	/**
	 * ������ɫIDת��Ϊ����
	 */
	public static String getCsysName(String ID) {
		String csysName = "";
		for (int i = 0; i < StaticValues.csysData.length; i++) {
			if (StaticValues.csysData[i].split(",")[0].equals(ID)) {
				csysName = StaticValues.csysData[i].split(",")[1];
			}
		}
		return csysName;
	}

	/**
	 * ���������ɫIDת��Ϊ����
	 */
	public static String getCsysNames(String ID) {
		String csysName = "";
		if (ID.length() > 1) {
			String[] idlist = ID.split("");
			for (int i = 1; i < idlist.length; i++) {
				csysName = csysName + getCsysName(idlist[i]);
			}
		} else {
			csysName = getCsysName(ID);
		}
		return csysName;
	}

	/**
	 * ������ɫ����ת��ΪID
	 */
	public static String getCsysID(String name) {
		String csysID = "";
		for (int i = 0; i < StaticValues.csysData.length; i++) {
			if (StaticValues.csysData[i].split(",")[1].equals(name)) {
				csysID = StaticValues.csysData[i].split(",")[0];
			}
		}
		return csysID;
	}

	/**
	 * ������ɫͨ��ID��ȡλ��
	 */
	public static int getCsysIDLocation(String ID) {
		int csysLocation = 0;
		for (int i = 0; i < StaticValues.csysData.length; i++) {
			if (StaticValues.csysData[i].split(",")[0].equals(ID)) {
				csysLocation = i;
			}
		}
		return csysLocation;
	}

	/**
	 * ʹ������IDת��Ϊ����
	 */
	public static String getSyxzName(String ID) {
		String syxzName = "";
		for (int i = 0; i < StaticValues.syxzData.length; i++) {
			if (StaticValues.syxzData[i].split(",")[0].equals(ID)) {
				syxzName = StaticValues.syxzData[i].split(",")[1];
			}
		}
		return syxzName;
	}

	/**
	 * ʹ����������ת��ΪID
	 */
	public static String getSyxzID(String name) {
		String syxzID = "";
		for (int i = 0; i < StaticValues.syxzData.length; i++) {
			if (StaticValues.syxzData[i].split(",")[1].equals(name)) {
				syxzID = StaticValues.syxzData[i].split(",")[0];
			}
		}
		return syxzID;
	}

	/**
	 * ҵ������IDת��Ϊ����
	 */
	public static String getYwlxName(String ID) {
		String ywlxName = "";
		for (int i = 0; i < StaticValues.ywlxData.length; i++) {
			if (StaticValues.ywlxData[i].split(",")[0].equals(ID)) {
				ywlxName = StaticValues.ywlxData[i].split(",")[1];
			}
		}
		return ywlxName;
	}

	/**
	 * ҵ����������ת��ΪID
	 */
	public static String getYwlxID(String name) {
		String ywlxID = "";
		for (int i = 0; i < StaticValues.ywlxData.length; i++) {
			if (StaticValues.ywlxData[i].split(",")[1].equals(name)) {
				ywlxID = StaticValues.ywlxData[i].split(",")[0];
			}
		}
		return ywlxID;
	}

	/**
	 * ��Ƭ����IDת��Ϊ����
	 */
	public static String getZpzlName(List<C06DomainZPSM> zpsmlist, String ID) {
		String zpzlName = "";
		for (int i = 0; i < zpsmlist.size(); i++) {
			if (zpsmlist.get(i).getZpzl().equals(ID)) {
				zpzlName = zpsmlist.get(i).getZpmc();
			}
		}
		return zpzlName;
	}

	/**
	 * ��Ƭ��������ת��ΪID
	 */
	public static String getZpzlID(String[] photoTypes, String[] photoID, String name) {
		String zpzlID = "";
		for (int i = 0; i < photoTypes.length; i++) {
			if (photoTypes[i].equals(name)) {
				zpzlID = photoID[i];
			}
		}
		return zpzlID;
	}

	/**
	 * ��������IDת��Ϊ����
	 */
	public static String getCllxName(List<C06DomainCLLX> cllxlist, String ID) {
		String cllxName = "";
		for (int i = 0; i < cllxlist.size(); i++) {
			if (cllxlist.get(i).getCllxbs().equals(ID)) {
				cllxName = cllxlist.get(i).getCllxmc();
			}
		}
		return cllxName;
	}

	/**
	 * ��������IDת��Ϊ����
	 */
	public static String getCllxID(List<C06DomainCLLX> cllxlist, String name) {
		String cllxID = "";
		for (int i = 0; i < cllxlist.size(); i++) {
			if (cllxlist.get(i).getCllxmc().equals(name)) {
				cllxID = cllxlist.get(i).getCllxbs();
			}
		}
		return cllxID;
	}

	/**
	 * ��������ͨ��ID��ȡλ��
	 */
	public static int getCllxIDLocation(List<C06DomainCLLX> cllxlist, String ID) {
		int cllxLocation = 0;
		for (int i = 0; i < cllxlist.size(); i++) {
			if (cllxlist.get(i).getCllxbs().equals(ID)) {
				cllxLocation = i;
			}
		}
		return cllxLocation;
	}

	/**
	 * ������Ŀ���IDתΪ����
	 */
	public static String getCyxmName(String[] cyxmlist, String ID) {
		String cyxmName = "";
		for (int i = 0; i < cyxmlist.length; i++) {
			if (cyxmlist[i].split(",")[1].equals(ID)) {
				cyxmName = cyxmlist[i].split(",")[2];
			}
		}
		return cyxmName;
	}

	/**
	 * �������ǽ����ж�
	 */
	public static String getCDName(String ID) {
		String cdName = "";
		for (int i = 0; i < StaticValues.isChina.length; i++) {
			if (StaticValues.isChina[i].split(",")[0].equals(ID)) {
				cdName = StaticValues.isChina[i].split(",")[1];
			}
		}
		return cdName;
	}

	/**
	 * ȼ�������ж�
	 */
	public static String getFuelName(String ID) {
		String fuelName = "";
		for (int i = 0; i < StaticValues.fuelData.length; i++) {
			if (StaticValues.fuelData[i].split(",")[0].equals(ID)) {
				fuelName = StaticValues.fuelData[i].split(",")[1];
			}
		}
		return fuelName;
	}
}
