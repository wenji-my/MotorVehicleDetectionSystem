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
		// ��ȡ�ı���01C06�ӿڵ�����
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
				baseInfo.setInfonMame("�����ˮ��");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HPHM")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("���ƺ���");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HPZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��������");
				baseInfo.setInfoMatter(MutualConversionNameID.getHpzlName(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("JYJGBH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HGZBBH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�ϸ�֤�汾��");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HGZLX")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�ϸ�֤����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HGZBH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�ϸ�֤���");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CCRQ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZZCMC")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("���쳧����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD1")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("Ӣ��Ʒ��");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLLX")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��������");
				baseInfo.setInfoMatter(MutualConversionNameID.getCllxName(cllxslists, temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLPP")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����Ʒ��");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLXH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�����ͺ�");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CSYS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("������ɫ");
				baseInfo.setInfoMatter(MutualConversionNameID.getCsysNames(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD2")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD3")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��ʻ��ǰ������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD4")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLSBDH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����ʶ�����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CLSBDH1")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����ʶ�����1");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("FDJH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("FDJXH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�������ͺ�");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("RLZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("ȼ������");
				baseInfo.setInfoMatter(MutualConversionNameID.getFuelName(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HBDBQK")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�ŷű�׼");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("PL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("GL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZXXS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("ת����ʽ");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("QLJ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("ǰ�־�");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HLJ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("���־�");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("LTS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��̥��");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("LTGG")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��̥���");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("GBTHPS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�ְ嵯��Ƭ��");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZJ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("���");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZH")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("���");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZS")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CWKC")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CWKK")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CWKG")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HXNBCD")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�����ڲ�����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HXNBKD")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�����ڲ����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HXNBGD")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�����ڲ��߶�");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HDZZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("�������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZBZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZDMCDD5")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����/����");
				baseInfo.setInfoMatter(MutualConversionNameID.getCDName(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("ZQYZL")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("׼ǣ��������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("HDZK")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��ؿ�");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} /*
			 * else if (qName.equalsIgnoreCase("ZDMCDD6")) { this.baseInfo = new
			 * C05DomainBaseInfo(); baseInfo.setInfonMame("ZDMCDD6");
			 * baseInfo.setInfoMatter(temp); baseInfolists.add(baseInfo); }
			 */else if (qName.equalsIgnoreCase("QPZK")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��ʻ��ǰ������");
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
				baseInfo.setInfonMame("������������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("BZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("��ע");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("SCDZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����������λ��ַ");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("SCDWMC")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����������λ����");
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
				baseInfo.setInfonMame("���εǼ�����");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("JYYXQZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("������Ч��ֹ");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("FDJRQ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("����������");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("CZDW")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("������λ");
				baseInfo.setInfoMatter(temp);
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("SYXZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("ʹ������");
				baseInfo.setInfoMatter(MutualConversionNameID.getSyxzName(temp));
				baseInfolists.add(baseInfo);
			} else if (qName.equalsIgnoreCase("QZBFQZ")) {
				this.baseInfo = new C05DomainBaseInfo();
				baseInfo.setInfonMame("ǿ�Ʊ�����ֹ");
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
