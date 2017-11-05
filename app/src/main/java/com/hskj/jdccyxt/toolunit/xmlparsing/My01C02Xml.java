package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.annotation.SuppressLint;
import com.hskj.jdccyxt.domain.C02Domain;

@SuppressLint("UseValueOf")
public class My01C02Xml extends DefaultHandler {
	private List<C02Domain> vehiclelists;
	private C02Domain vehicle;
	private String temp = "";

	public List<C02Domain> getVehicles() {
		return this.vehiclelists;
	}

	@Override
	public void startDocument() throws SAXException {
		vehiclelists = new ArrayList<C02Domain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("vehispara")) {
			this.vehicle = new C02Domain();
			vehicle.setId(new Integer(attributes.getValue(0)));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.vehicle != null) {
			if (qName.equalsIgnoreCase("ywlx")) {
				vehicle.setYwlx(temp);
			} else if (qName.equalsIgnoreCase("jclsh")) {
				vehicle.setJclsh(temp);
			} else if (qName.equalsIgnoreCase("hphm")) {
				vehicle.setHphm(temp);
			} else if (qName.equalsIgnoreCase("hpzl")) {
				vehicle.setHpzl(temp);
			} else if (qName.equalsIgnoreCase("vin")) {
				vehicle.setVin(temp);
			} else if (qName.equalsIgnoreCase("cllx")) {
				vehicle.setCllx(temp);
			} else if (qName.equalsIgnoreCase("syxz")) {
				vehicle.setSyxz(temp);
			} else if (qName.equalsIgnoreCase("csys")) {
				vehicle.setCsys(temp);
			} else if (qName.equalsIgnoreCase("hdzk")) {
				vehicle.setHdzk(temp);
			} else if (qName.equalsIgnoreCase("bhgx")) {
				vehicle.setBhgx(temp);
			} else if (qName.equalsIgnoreCase("hgx")) {
				vehicle.setHgx(temp);
			} else if (qName.equalsIgnoreCase("bcyx")) {
				vehicle.setBcyx(temp);
			} else if (qName.equalsIgnoreCase("cyxm")) {
				vehicle.setCyxm(temp);
			} else if (qName.equalsIgnoreCase("zpzl")) {
				vehicle.setZpzl(temp);
			} else if (qName.equalsIgnoreCase("beizhu")) {
				vehicle.setBeizhu(temp);
			} else if (qName.equalsIgnoreCase("sfxc")) {
				vehicle.setSfxc(temp);
			} else if (qName.equalsIgnoreCase("fzcyy")) {
				vehicle.setFzcyy(temp);
			} else if (qName.equalsIgnoreCase("ts")) {
				vehicle.setTs(temp);
			} else if (qName.equalsIgnoreCase("vehispara")) {
				vehiclelists.add(vehicle);
			}
			temp = "";
		}
	}
}
