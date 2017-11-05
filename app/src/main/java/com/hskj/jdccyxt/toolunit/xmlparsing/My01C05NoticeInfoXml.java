package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.hskj.jdccyxt.domain.C05DomainNoticeInfo;
import com.hskj.jdccyxt.domain.C06DomainCLLX;
import com.hskj.jdccyxt.toolunit.DocumentTool;
import com.hskj.jdccyxt.toolunit.MutualConversionNameID;
import com.hskj.jdccyxt.toolunit.StaticValues;

public class My01C05NoticeInfoXml extends DefaultHandler {
	private List<C05DomainNoticeInfo> noticeInfolists;
	private C05DomainNoticeInfo noticeInfo;
	private String temp = "";
	private List<C06DomainCLLX> cllxslists;

	public List<C05DomainNoticeInfo> getNoticeInfos() {
		return this.noticeInfolists;
	}

	@Override
	public void startDocument() throws SAXException {
		// 提取文本中01C06接口的数据
		String c06data = DocumentTool.readFileContent(StaticValues.rootDirectory + "/CllxEndZpsm/01C06.txt");
		cllxslists = XMLParsingMethods.saxVehicleType(c06data);
		noticeInfolists = new ArrayList<C05DomainNoticeInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("NoticeInfo")) {
			this.noticeInfo = new C05DomainNoticeInfo();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.noticeInfo != null) {
			if (qName.equalsIgnoreCase("JCLSH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("检测流水号");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HPHM")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("号牌号码");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HPZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("号牌种类");
				noticeInfo.setInfoMatter(MutualConversionNameID.getHpzlName(temp));
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("JYJGBH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("检验机构编号");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLSBDH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车辆识别代号");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("BH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("整车公告编号");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLPP1")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车辆品牌(中文)");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLPP2")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车辆品牌(英文)");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLXH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车辆型号");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QYID")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("企业ID");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("SCDZ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("生产地址");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FDJXH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("识别代号序列");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} /*
			 * else if (qName.equalsIgnoreCase("SBDHXL")) { this.noticeInfo =
			 * new C05DomainNoticeInfo(); noticeInfo.setInfonMame("SBDHXL");//
			 * noticeInfo.setInfoMatter(temp); noticeInfolists.add(noticeInfo);
			 * }
			 */else if (qName.equalsIgnoreCase("CLLX")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车辆类型");
				noticeInfo.setInfoMatter(MutualConversionNameID.getCllxName(cllxslists, temp));
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZZG")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("制造国");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZXXS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("转向形式");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("RLZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("燃料种类");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("PL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("排量");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("功率");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CWKC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车外廓长");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CWKK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车外廓宽");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CWKG")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车外廓高");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HXNBCD")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("货箱内部长度");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HXNBKD")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("货箱内部宽度");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HXNBGD")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("货箱内部高度");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GBTHPS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("钢板弹簧片数");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("轴数");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("轴距");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QLJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("前轮距");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HLJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("后轮距");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("LTS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("轮胎数");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("LTGG")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("轮胎规格");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("总质量");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZBZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("整备质量");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HDZZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("额定载质量");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZQYZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("准牵引总质量");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HDZK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("额定载客");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QPZK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("驾驶室前排人数");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HPZK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("驾驶室后排人数");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("PC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("批次");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("DPID")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("底盘ID");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HBDBQK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("环保达标情况");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CSLX")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("公告发布类型");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GXRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("更新日期");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("BZ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("备注");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZZCMC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车辆制造企业");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GGRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("公告发布日期");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("SFMJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("免检标记");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CXSXRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("撤销生效日期");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("DPQYXH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("底盘企业及型号");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CPLB")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("产品类别");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} /*
			 * else if (qName.equalsIgnoreCase("JYW")) { this.noticeInfo = new
			 * C05DomainNoticeInfo(); noticeInfo.setInfonMame("JYW");//
			 * noticeInfo.setInfoMatter(temp); noticeInfolists.add(noticeInfo);
			 * }
			 */else if (qName.equalsIgnoreCase("CLGGBH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车型公告编号");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("SFYXZC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("是否允许注册");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GGSXRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("公告生效日期");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GGYXQBJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("公告有效期标记");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CXGGPC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("撤销公告批次");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CXGGRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("撤销公告发布日期");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("TZSCRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("停止生产日期");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("YXQMS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("有效期描述");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZPS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("照片数");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("MJYXQZ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("免检有效期止");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FGBSXH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("反光标识型号");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FGBSSB")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("反光标识商标");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FGBSQY")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("反光标识企业");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("轴荷");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZZLLYXS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("载质量利用系数");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("BGAZZDYXZZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("半挂鞍座最大允许总质量");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("JJLQJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("接近离去角");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QXHX")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("前悬后悬");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("JSSLX")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("驾驶室类型");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CDXS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("传动型式");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZGCS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("最高车速");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("YH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("油耗");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QZDFS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("前制动方式");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HZDFS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("后制动方式");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QZDCZFS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("前制动操作方式");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HZDCZFS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("后制动操作方式");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FDJQY")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("发动机企业");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FDJSB")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("发动机商标");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("YWABS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("是否带防抱死系统");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLMC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("车辆名称");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QYDM")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("企业代码");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GGBJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("公告标记");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			}
			temp = "";
		}
	}
}
