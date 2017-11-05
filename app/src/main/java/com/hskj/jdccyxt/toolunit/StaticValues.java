package com.hskj.jdccyxt.toolunit;

import android.os.Environment;

/**
 * @类名：StaticValues
 * @author:广东泓胜科技有限公司
 * @实现功能：静态常量保存点
 * @创建日期：2016.04.19
 */
public class StaticValues {
	// 联网参数配置
	public static String queryObject = "queryObjectOut";
	public static String writeObject = "writeObjectOut";
	public static String queryResult = "queryObjectOutResult";
	public static String writeResult = "writeObjectOutResult";
	public static String nameSpace = "http://gdhs.com/";
	// 超时时间
	public static int timeoutThree = 3000;
	public static int timeoutFive = 5000;
	public static int timeoutNine = 9000;
	public static int timeoutFifteen = 15000;
	public static int timeoutThirty = 30000;
	// 接口单次调的次数
	public static int numberTwo = 2;
	public static int numberThree = 3;
	public static int numberFour = 4;
	public static int numberFive = 5;
	// 常量
	public static final String ADDRESS_SETTING = "address_setup";// 联网参数设置保存符
	public static final String BASIC_SETTING = "basic_setup";// 操作设置保存符
	public static final int STATIC_REQUEST_CODE_SCAN = 0x0000;// 扫描
	public static final String STATIC_DECODED_CONTENT_KEY = "codedContent";

	// 文件根目录
	public static String rootDirectory = "/MyJDCCYXT";
	// 新版本软件地址
	public static String UpdataSubcatalog = Environment.getExternalStorageDirectory() + "/MyJDCCYXT/APKVersion/";
	// 图片子目录
	public static String photoSubcatalog = Environment.getExternalStorageDirectory() + "/MyJDCCYXT/Photo/";
	// 标题名称
	public static String[] titleName = { "cy,车 辆 查 询", "fcy,照 片 补 拍", "xsz,行 驶 证 照 片 拍 摄" };
	// 检测线代号
	public static String[] jcxdhData = { "1", "2", "3", "4", "5", "6" };
	// 号牌种类
	public static String[] hpzlData = { "01,大型汽车", "02,小型汽车", "03,使馆汽车", "04,领馆汽车", "05,境外汽车", "06,外籍汽车", "07,普通摩托车", "08,轻便摩托车",
			"09,使馆摩托车", "10,领馆摩托车", "11,境外摩托车", "12,外籍摩托车", "13,低速车", "14,拖拉机", "15,挂车", "16,教练汽车", "17,教练摩托车", "20,临时入境汽车",
			"21,临时入境摩托车", "22,临时行驶车", "23,警用汽车", "24,警用摩托车" };
	// 车身颜色
	public static String[] csysData = { "", "A,白", "B,灰", "C,黄", "D,粉", "E,红", "F,紫", "G,绿", "H,蓝", "I,棕", "J,黑" };
	// 使用性质
	public static String[] syxzData = { "A,非营运", "B,公路客运", "C,公交客运", "D,出租客运", "E,旅游客运", "F,货运", "G,租赁", "H,警用", "I,消防", "J,救护",
			"K,工程抢险", "L,营转非", "M,出租转非", "N,教练", "O,幼儿校车", "P,小学校车", "Q,其他校车", "R,危险品运输", "Z,其他", "1,默认" };
	// 业务类型
	public static String[] ywlxData = { "0,注册登记", "1,转入", "2,转移登记", "3,变更迁出", "4,变更车身颜色", "6,更换车身或者车架", "7,更换发动机", "8,变更使用性质",
			"9,重新打刻VIN", "10,重新打刻发动机号", "11,更换整车", "12,加装/拆除操纵辅助装置", "13,申领登记证书", "14,补领登记证书", "15,监督解体", "16,申请校车使用许可",
			"17,期满换发校车标牌", "18,非专用校车不再作为校车使用", "19,其他" };
	// 普通车辆查验项归类名称
	public static String[] regularName = { "通用项目", "货车挂车", "大中型客车、危险化学品运输车等", "其他" };
	// 普通车辆查验项
	public static String[] regularInspectionItems = { "1,1,车辆识别代号", "1,2,发动机型号/号码", "1,3,车辆品牌/型号", "1,4,车身颜色", "1,5,核定载人数",
			"1,6,车辆类型", "1,7,号牌/车辆外观形状", "1,8,轮胎完好情况", "1,9,安全带、三角警告牌", "2,10,外廓尺寸、轴数、轴距", "2,11,整备质量", "2,12,轮胎规格",
			"2,13,侧后部防护装置", "2,14,车身反光标识和车辆尾部标志板、喷涂", "3,15,灭火器", "3,16,行驶记录装置、车内外录像监控装置", "3,17,应急出口/应急锤、乘客门",
			"3,18,外部标识/文字、喷涂", "4,19,标志灯具、警报器", "4,20,检验合格证明" };
	// 普通车辆全部编号
	public static String[] regularAllNumbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19", "20" };
	// 校车查验项归类名称
	public static String[] busName = { "通用项目", "校车专用项目", "其他" };
	// 校车查验项
	public static String[] schoolBusInspectionItem = { "1,1,车辆识别代号", "1,2,发动机型号/号码", "1,3,车辆品牌/型号", "1,4,车身颜色",
			"1,5,核定载人数（学生/成人）", "1,6,车辆类型", "1,7,号牌/车辆外观形状", "1,8,轮胎完好情况", "1,9,三角警告牌", "2,10,校车标志灯", "2,11,停车指示标志",
			"2,12,具有行驶记录功能的卫星定位装置", "2,13,应急出口/应急锤", "2,14,干粉灭火器", "2,15,急救箱", "2,16,车身外观标识", "2,17,照管人员座位", "2,18,汽车安全带",
			"2,19,车内外录像监控系统", "2,20,辅助倒车装置", "2,21,校车标牌", "3,22,检验合格证明" };
	// 校车辆全部编号
	public static String[] schoolBusAllNumbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
			"15", "16", "17", "18", "19", "20", "21", "22" };
	// 国产/进口
	public static String[] isChina = { "A,国产", "B,进口" };
	// 燃料种类
	public static String[] fuelData = { "A,汽油", "B,柴油", "C,电", "D,混合油", "E,天然气", "F,液化石油气", "L,甲醇", "M,乙醇", "N,太阳能", "O,混合动力",
			"P,氢", "Q,生物燃料", "Y,无" };

	// 01C50字段名称
	public static String[] FieldName01C50 = { "pdaid" };
	// 01C09字段名称
	public static String[] FieldName01C09 = { "userid", "password", "jyjgbh", "pdaid", "version" };
	// 01C02字段名称
	public static String[] FieldName01C02 = { "jyjgbh", "jclsh", "cylx", "userid" };
	// 01C04字段名称
	public static String[] FieldName01C04 = { "bh" };
	// 01C05字段名称
	public static String[] FieldName01C05 = { "jclsh" };
	// 01C06字段名称
	public static String[] FieldName01C06 = { "pdaid" };
	// 01C07字段名称
	public static String[] FieldName01C07 = { "jyjgbh", "jclsh", "userid" };
	// 01J01字段名称
	public static String[] FieldName01J01 = { "userid", "password", "newpassword", "username", "pdaid" };
	// 01J63字段名称
	public static String[] FieldName01J63 = { "jyjgbh", "jylsh", "pssj", "wjybh", "zpzl", "zp" };
	// 01J11和01J12字段名称
	public static String[] FieldName01J11end01J12 = { "jyjgbh", "jylsh", "hphm", "hpzl", "clsbdh", "gwxm", "jcxdh", "jycs" };
	// 01J02字段名称
	public static String[] FieldName01J02one = { "csys", "hdzk", "cllx" };
	public static String[] FieldName01J02 = { "userid", "idenitycard", "beizhu", "biangeng", "username", "jclsh", "jyjgbh",
			"ywlx", "hgx", "bhgx", "bcyx", "cyzjl" };
	// 01J03字段名称
	public static String[] FieldName01J03 = { "jyjgbh", "jclsh" };
}