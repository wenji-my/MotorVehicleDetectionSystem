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
		// ��ȡ�ı���01C06�ӿڵ�����
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
				noticeInfo.setInfonMame("�����ˮ��");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HPHM")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("���ƺ���");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HPZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��������");
				noticeInfo.setInfoMatter(MutualConversionNameID.getHpzlName(temp));
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("JYJGBH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLSBDH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����ʶ�����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("BH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLPP1")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����Ʒ��(����)");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLPP2")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����Ʒ��(Ӣ��)");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLXH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����ͺ�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QYID")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��ҵID");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("SCDZ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("������ַ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FDJXH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("ʶ���������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} /*
			 * else if (qName.equalsIgnoreCase("SBDHXL")) { this.noticeInfo =
			 * new C05DomainNoticeInfo(); noticeInfo.setInfonMame("SBDHXL");//
			 * noticeInfo.setInfoMatter(temp); noticeInfolists.add(noticeInfo);
			 * }
			 */else if (qName.equalsIgnoreCase("CLLX")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��������");
				noticeInfo.setInfoMatter(MutualConversionNameID.getCllxName(cllxslists, temp));
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZZG")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZXXS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("ת����ʽ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("RLZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("ȼ������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("PL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CWKC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CWKK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CWKG")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HXNBCD")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����ڲ�����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HXNBKD")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����ڲ����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HXNBGD")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����ڲ��߶�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GBTHPS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�ְ嵯��Ƭ��");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("���");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QLJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("ǰ�־�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HLJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("���־�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("LTS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��̥��");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("LTGG")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��̥���");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZBZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HDZZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZQYZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("׼ǣ��������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HDZK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��ؿ�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QPZK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��ʻ��ǰ������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HPZK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��ʻ�Һ�������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("PC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("DPID")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����ID");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HBDBQK")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CSLX")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("���淢������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GXRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("BZ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��ע");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZZCMC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����������ҵ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GGRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("���淢������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("SFMJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CXSXRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("������Ч����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("DPQYXH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("������ҵ���ͺ�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CPLB")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��Ʒ���");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} /*
			 * else if (qName.equalsIgnoreCase("JYW")) { this.noticeInfo = new
			 * C05DomainNoticeInfo(); noticeInfo.setInfonMame("JYW");//
			 * noticeInfo.setInfoMatter(temp); noticeInfolists.add(noticeInfo);
			 * }
			 */else if (qName.equalsIgnoreCase("CLGGBH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("���͹�����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("SFYXZC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�Ƿ�����ע��");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GGSXRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("������Ч����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GGYXQBJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("������Ч�ڱ��");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CXGGPC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("������������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CXGGRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�������淢������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("TZSCRQ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("ֹͣ��������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("YXQMS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��Ч������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZPS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��Ƭ��");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("MJYXQZ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����Ч��ֹ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FGBSXH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����ʶ�ͺ�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FGBSSB")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����ʶ�̱�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FGBSQY")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�����ʶ��ҵ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("���");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZZLLYXS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("����������ϵ��");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("BGAZZDYXZZL")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��Ұ����������������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("JJLQJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�ӽ���ȥ��");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QXHX")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("ǰ������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("JSSLX")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��ʻ������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CDXS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("������ʽ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("ZGCS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��߳���");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("YH")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�ͺ�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QZDFS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("ǰ�ƶ���ʽ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HZDFS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("���ƶ���ʽ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QZDCZFS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("ǰ�ƶ�������ʽ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("HZDCZFS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("���ƶ�������ʽ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FDJQY")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��������ҵ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("FDJSB")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�������̱�");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("YWABS")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("�Ƿ��������ϵͳ");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("CLMC")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("QYDM")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("��ҵ����");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			} else if (qName.equalsIgnoreCase("GGBJ")) {
				this.noticeInfo = new C05DomainNoticeInfo();
				noticeInfo.setInfonMame("������");
				noticeInfo.setInfoMatter(temp);
				noticeInfolists.add(noticeInfo);
			}
			temp = "";
		}
	}
}
