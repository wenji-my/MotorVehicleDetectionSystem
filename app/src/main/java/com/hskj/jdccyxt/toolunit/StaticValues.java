package com.hskj.jdccyxt.toolunit;

import android.os.Environment;

/**
 * @������StaticValues
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ���̬���������
 * @�������ڣ�2016.04.19
 */
public class StaticValues {
	// ������������
	public static String queryObject = "queryObjectOut";
	public static String writeObject = "writeObjectOut";
	public static String queryResult = "queryObjectOutResult";
	public static String writeResult = "writeObjectOutResult";
	public static String nameSpace = "http://gdhs.com/";
	// ��ʱʱ��
	public static int timeoutThree = 3000;
	public static int timeoutFive = 5000;
	public static int timeoutNine = 9000;
	public static int timeoutFifteen = 15000;
	public static int timeoutThirty = 30000;
	// �ӿڵ��ε��Ĵ���
	public static int numberTwo = 2;
	public static int numberThree = 3;
	public static int numberFour = 4;
	public static int numberFive = 5;
	// ����
	public static final String ADDRESS_SETTING = "address_setup";// �����������ñ����
	public static final String BASIC_SETTING = "basic_setup";// �������ñ����
	public static final int STATIC_REQUEST_CODE_SCAN = 0x0000;// ɨ��
	public static final String STATIC_DECODED_CONTENT_KEY = "codedContent";

	// �ļ���Ŀ¼
	public static String rootDirectory = "/MyJDCCYXT";
	// �°汾�����ַ
	public static String UpdataSubcatalog = Environment.getExternalStorageDirectory() + "/MyJDCCYXT/APKVersion/";
	// ͼƬ��Ŀ¼
	public static String photoSubcatalog = Environment.getExternalStorageDirectory() + "/MyJDCCYXT/Photo/";
	// ��������
	public static String[] titleName = { "cy,�� �� �� ѯ", "fcy,�� Ƭ �� ��", "xsz,�� ʻ ֤ �� Ƭ �� ��" };
	// ����ߴ���
	public static String[] jcxdhData = { "1", "2", "3", "4", "5", "6" };
	// ��������
	public static String[] hpzlData = { "01,��������", "02,С������", "03,ʹ������", "04,�������", "05,��������", "06,�⼮����", "07,��ͨĦ�г�", "08,���Ħ�г�",
			"09,ʹ��Ħ�г�", "10,���Ħ�г�", "11,����Ħ�г�", "12,�⼮Ħ�г�", "13,���ٳ�", "14,������", "15,�ҳ�", "16,��������", "17,����Ħ�г�", "20,��ʱ�뾳����",
			"21,��ʱ�뾳Ħ�г�", "22,��ʱ��ʻ��", "23,��������", "24,����Ħ�г�" };
	// ������ɫ
	public static String[] csysData = { "", "A,��", "B,��", "C,��", "D,��", "E,��", "F,��", "G,��", "H,��", "I,��", "J,��" };
	// ʹ������
	public static String[] syxzData = { "A,��Ӫ��", "B,��·����", "C,��������", "D,�������", "E,���ο���", "F,����", "G,����", "H,����", "I,����", "J,�Ȼ�",
			"K,��������", "L,Ӫת��", "M,����ת��", "N,����", "O,�׶�У��", "P,СѧУ��", "Q,����У��", "R,Σ��Ʒ����", "Z,����", "1,Ĭ��" };
	// ҵ������
	public static String[] ywlxData = { "0,ע��Ǽ�", "1,ת��", "2,ת�ƵǼ�", "3,���Ǩ��", "4,���������ɫ", "6,����������߳���", "7,����������", "8,���ʹ������",
			"9,���´��VIN", "10,���´�̷�������", "11,��������", "12,��װ/������ݸ���װ��", "13,����Ǽ�֤��", "14,����Ǽ�֤��", "15,�ල����", "16,����У��ʹ�����",
			"17,��������У������", "18,��ר��У��������ΪУ��ʹ��", "19,����" };
	// ��ͨ�����������������
	public static String[] regularName = { "ͨ����Ŀ", "�����ҳ�", "�����Ϳͳ���Σ�ջ�ѧƷ���䳵��", "����" };
	// ��ͨ����������
	public static String[] regularInspectionItems = { "1,1,����ʶ�����", "1,2,�������ͺ�/����", "1,3,����Ʒ��/�ͺ�", "1,4,������ɫ", "1,5,�˶�������",
			"1,6,��������", "1,7,����/���������״", "1,8,��̥������", "1,9,��ȫ�������Ǿ�����", "2,10,�����ߴ硢���������", "2,11,��������", "2,12,��̥���",
			"2,13,��󲿷���װ��", "2,14,�������ʶ�ͳ���β����־�塢��Ϳ", "3,15,�����", "3,16,��ʻ��¼װ�á�������¼����װ��", "3,17,Ӧ������/Ӧ�������˿���",
			"3,18,�ⲿ��ʶ/���֡���Ϳ", "4,19,��־�ƾߡ�������", "4,20,����ϸ�֤��" };
	// ��ͨ����ȫ�����
	public static String[] regularAllNumbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19", "20" };
	// У���������������
	public static String[] busName = { "ͨ����Ŀ", "У��ר����Ŀ", "����" };
	// У��������
	public static String[] schoolBusInspectionItem = { "1,1,����ʶ�����", "1,2,�������ͺ�/����", "1,3,����Ʒ��/�ͺ�", "1,4,������ɫ",
			"1,5,�˶���������ѧ��/���ˣ�", "1,6,��������", "1,7,����/���������״", "1,8,��̥������", "1,9,���Ǿ�����", "2,10,У����־��", "2,11,ͣ��ָʾ��־",
			"2,12,������ʻ��¼���ܵ����Ƕ�λװ��", "2,13,Ӧ������/Ӧ����", "2,14,�ɷ������", "2,15,������", "2,16,������۱�ʶ", "2,17,�չ���Ա��λ", "2,18,������ȫ��",
			"2,19,������¼����ϵͳ", "2,20,��������װ��", "2,21,У������", "3,22,����ϸ�֤��" };
	// У����ȫ�����
	public static String[] schoolBusAllNumbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
			"15", "16", "17", "18", "19", "20", "21", "22" };
	// ����/����
	public static String[] isChina = { "A,����", "B,����" };
	// ȼ������
	public static String[] fuelData = { "A,����", "B,����", "C,��", "D,�����", "E,��Ȼ��", "F,Һ��ʯ����", "L,�״�", "M,�Ҵ�", "N,̫����", "O,��϶���",
			"P,��", "Q,����ȼ��", "Y,��" };

	// 01C50�ֶ�����
	public static String[] FieldName01C50 = { "pdaid" };
	// 01C09�ֶ�����
	public static String[] FieldName01C09 = { "userid", "password", "jyjgbh", "pdaid", "version" };
	// 01C02�ֶ�����
	public static String[] FieldName01C02 = { "jyjgbh", "jclsh", "cylx", "userid" };
	// 01C04�ֶ�����
	public static String[] FieldName01C04 = { "bh" };
	// 01C05�ֶ�����
	public static String[] FieldName01C05 = { "jclsh" };
	// 01C06�ֶ�����
	public static String[] FieldName01C06 = { "pdaid" };
	// 01C07�ֶ�����
	public static String[] FieldName01C07 = { "jyjgbh", "jclsh", "userid" };
	// 01J01�ֶ�����
	public static String[] FieldName01J01 = { "userid", "password", "newpassword", "username", "pdaid" };
	// 01J63�ֶ�����
	public static String[] FieldName01J63 = { "jyjgbh", "jylsh", "pssj", "wjybh", "zpzl", "zp" };
	// 01J11��01J12�ֶ�����
	public static String[] FieldName01J11end01J12 = { "jyjgbh", "jylsh", "hphm", "hpzl", "clsbdh", "gwxm", "jcxdh", "jycs" };
	// 01J02�ֶ�����
	public static String[] FieldName01J02one = { "csys", "hdzk", "cllx" };
	public static String[] FieldName01J02 = { "userid", "idenitycard", "beizhu", "biangeng", "username", "jclsh", "jyjgbh",
			"ywlx", "hgx", "bhgx", "bcyx", "cyzjl" };
	// 01J03�ֶ�����
	public static String[] FieldName01J03 = { "jyjgbh", "jclsh" };
}