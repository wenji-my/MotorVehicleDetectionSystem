package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import com.hskj.jdccyxt.domain.C02Domain;
import com.hskj.jdccyxt.domain.C04Domain;
import com.hskj.jdccyxt.domain.C05DomainBH;
import com.hskj.jdccyxt.domain.C05DomainBaseInfo;
import com.hskj.jdccyxt.domain.C05DomainNoticeInfo;
import com.hskj.jdccyxt.domain.C06DomainCLLX;
import com.hskj.jdccyxt.domain.C06DomainZPSM;
import com.hskj.jdccyxt.domain.C07Domain;
import com.hskj.jdccyxt.domain.C09Domain;
import com.hskj.jdccyxt.domain.C50Domain;
import com.hskj.jdccyxt.domain.CodeDomain;

/**
 * @类名：XMLParsingMethods
 * @author:广东泓胜科技有限公司
 * @实现功能：XML解析方法大汇总
 * @创建日期：2016.04.19
 */
public class XMLParsingMethods {

	/**
	 * 18C50-时间同步返回数据解析
	 */
	public static List<C50Domain> saxtime(String str) {
		List<C50Domain> timelists = new ArrayList<C50Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C50Xml handler = new My01C50Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			timelists = handler.getTimes();
			return timelists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 大多数返回内容数据解析
	 */
	public static List<CodeDomain> saxcode(String str) {
		List<CodeDomain> codelists = new ArrayList<CodeDomain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyCodeXml handler = new MyCodeXml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			codelists = handler.getCodes();
			return codelists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 01C09-用户登录返回数据解析
	 */
	public static List<C09Domain> saxuserlogin(String str) {
		List<C09Domain> operatorlists = new ArrayList<C09Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C09Xml handler = new My01C09Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			operatorlists = handler.getOperators();
			return operatorlists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 01C02-车辆查验列表返回数据解析
	 */
	public static List<C02Domain> saxVehicleQuerition(String str) {
		List<C02Domain> vehiclelists = new ArrayList<C02Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C02Xml handler = new My01C02Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			vehiclelists = handler.getVehicles();
			return vehiclelists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 01C06-照片种类返回数据解析
	 */
	public static List<C06DomainZPSM> saxPhotoType(String str) {
		List<C06DomainZPSM> zpsmslists = new ArrayList<C06DomainZPSM>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C06zpsmXml handler = new My01C06zpsmXml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			zpsmslists = handler.getZpsms();
			return zpsmslists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 01C06-车辆类型返回数据解析
	 */
	public static List<C06DomainCLLX> saxVehicleType(String str) {
		List<C06DomainCLLX> cllxslists = new ArrayList<C06DomainCLLX>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C06cllxXml handler = new My01C06cllxXml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			cllxslists = handler.getCllxs();
			return cllxslists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 01C05-车辆基本信息返回数据解析
	 */
	public static List<C05DomainBaseInfo> saxBasicInformation(String str) {
		List<C05DomainBaseInfo> baseInfolists = new ArrayList<C05DomainBaseInfo>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C05BaseInfoXml handler = new My01C05BaseInfoXml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			baseInfolists = handler.getBaseInfos();
			return baseInfolists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 01C05-车辆公告信息返回数据解析
	 */
	public static List<C05DomainNoticeInfo> saxNoticeInformation(String str) {
		List<C05DomainNoticeInfo> noticeInfolists = new ArrayList<C05DomainNoticeInfo>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C05NoticeInfoXml handler = new My01C05NoticeInfoXml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			noticeInfolists = handler.getNoticeInfos();
			return noticeInfolists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 01C05-车辆编号返回数据解析
	 */
	public static List<C05DomainBH> saxBH(String str) {
		List<C05DomainBH> bhlists = new ArrayList<C05DomainBH>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C05BHXml handler = new My01C05BHXml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			bhlists = handler.getBHs();
			return bhlists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 01C05-公告照片返回数据解析
	 */
	public static List<C04Domain> saxNoticePhoto(String str) {
		List<C04Domain> ggzplists = new ArrayList<C04Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C04Xml handler = new My01C04Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			ggzplists = handler.getGgzplists();
			return ggzplists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 01C07-外廓尺寸和整备质量返回数据解析
	 */
	public static List<C07Domain> saxWkccAndZbzl(String str) {
		List<C07Domain> wkzllists = new ArrayList<C07Domain>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			My01C07Xml handler = new My01C07Xml();
			parser.parse(new ByteArrayInputStream(str.getBytes()), handler);
			wkzllists = handler.getWkZls();
			return wkzllists;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
