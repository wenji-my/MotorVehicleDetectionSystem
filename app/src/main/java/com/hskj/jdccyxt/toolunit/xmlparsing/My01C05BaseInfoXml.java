package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.hskj.jdccyxt.domain.C05DomainBaseInfo;
import com.hskj.jdccyxt.domain.C06DomainCLLX;
import com.hskj.jdccyxt.toolunit.DocumentTool;
import com.hskj.jdccyxt.toolunit.MutualConversionNameID;
import com.hskj.jdccyxt.toolunit.StaticValues;

public class My01C05BaseInfoXml extends DefaultHandler {
	private List<C05DomainBaseInfo> baseInfolists;
	private C05DomainBaseInfo baseInfo;
	private String temp = "";
	private List<C06DomainCLLX> cllxslists;

	public List<C05DomainBaseInfo> getBaseInfos() {
		return this.baseInfolists;
	}

	@Override
	public void startDocument() throws SAXException {
		// 提取文本中01C06接口的数据
		String c06data = DocumentTool.readFileContent(StaticValues.rootDirectory + "/CllxEndZpsm/01C06.txt");
		cllxslists = XMLParsingMethods.saxVehicleType(c06data);
		baseInfolists = new ArrayList<C05DomainBaseInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("BaseInfo")) {
			this.baseInfo = new C05DomainBaseInfo();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.baseInfo != null) {
			if (qName.equalsIgnoreCase("JCLSH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("检测流水号");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HPHM")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("号牌号码");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HPZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("号牌种类");
				baseInfo.setInfoMatter(MutualConversionNameID.getHpzlName(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("JYJGBH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("检验机构编号");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HGZBBH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("合格证版本号");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HGZLX")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("合格证类型");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HGZBH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("合格证编号");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CCRQ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("出厂日期");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZZCMC")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("制造厂名称");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD1")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("英文品牌");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLLX")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车辆类型");
				baseInfo.setInfoMatter(MutualConversionNameID.getCllxName(cllxslists, temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLPP")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车辆品牌");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLXH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车辆型号");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CSYS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车身颜色");
				baseInfo.setInfoMatter(MutualConversionNameID.getCsysNames(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD2")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("产地");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD3")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("驾驶室前排人数");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD4")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车辆所有人");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLSBDH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车辆识别代号");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLSBDH1")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车辆识别代号1");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("FDJH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("发动机号");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("FDJXH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("发动机型号");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("RLZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("燃料种类");
				baseInfo.setInfoMatter(MutualConversionNameID.getFuelName(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HBDBQK")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("排放标准");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("PL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("排量");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("GL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("功率");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZXXS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("转向形式");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("QLJ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("前轮距");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HLJ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("后轮距");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("LTS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("轮胎数");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("LTGG")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("轮胎规格");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("GBTHPS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("钢板弹簧片数");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZJ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("轴距");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("轴荷");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("轴数");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CWKC")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车外廓长");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CWKK")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车外廓宽");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CWKG")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车外廓高");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HXNBCD")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("货箱内部长度");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HXNBKD")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("货箱内部宽度");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HXNBGD")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("货箱内部高度");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("总质量");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HDZZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("额定载质量");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZBZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("整备质量");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD5")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("国产/进口");
				baseInfo.setInfoMatter(MutualConversionNameID.getCDName(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZQYZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("准牵引总质量");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HDZK")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("额定载客");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} /*
			 * else if (qName.equalsIgnoreCase("ZDMCDD6")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD6");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); }
			 */else if (qName.equalsIgnoreCase("QPZK")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("驾驶室前排人数");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} /*
			 * else if (qName.equalsIgnoreCase("ZDMCDD7")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD7");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD8")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD8");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD9")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD9");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD10")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD10");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD11")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD11");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); }
			 */else if (qName.equalsIgnoreCase("SYQ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车辆制造日期");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("BZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("备注");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("SCDZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车辆生产单位地址");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("SCDWMC")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车辆生产单位名称");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} /*
			 * else if (qName.equalsIgnoreCase("ZDMCDD12")) { this.baseInfo =
			 * new C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD12");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD13")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD13");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD14")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD14");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD15")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD15");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("HGZDYSJ")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("HGZDYSJ");//
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD16")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD16");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD17")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD17");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("ZDMCDD18")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD18");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("DYWY")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("DYWY");//
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("UDXLH")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("UDXLH");//
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); } else
			 * if (qName.equalsIgnoreCase("CZWJ")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("CZWJ");//
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); }
			 */else if (qName.equalsIgnoreCase("CCDJRQ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("初次登记日期");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("JYYXQZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("检验有效期止");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("FDJRQ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("发动机日期");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CZDW")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("车主单位");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("SYXZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("使用性质");
				baseInfo.setInfoMatter(MutualConversionNameID.getSyxzName(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("QZBFQZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("强制报废期止");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZT")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("ZT");//
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			}
			temp = "";
		}
	}
}
