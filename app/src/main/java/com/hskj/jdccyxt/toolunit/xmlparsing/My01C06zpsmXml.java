package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import android.annotation.SuppressLint;
import com.hskj.jdccyxt.domain.C06DomainZPSM;

@SuppressLint("UseValueOf")
public class My01C06zpsmXml extends DefaultHandler {
	private List<C06DomainZPSM> zpsmslists;
	private C06DomainZPSM zpsms;
	private String temp;

	public List<C06DomainZPSM> getZpsms() {
		return this.zpsmslists;
	}

	@Override
	public void startDocument() throws SAXException {
		zpsmslists = new ArrayList<C06DomainZPSM>();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("zpsm")) {
			this.zpsms = new C06DomainZPSM();
			zpsms.setId(new Integer(attributes.getValue(0)));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.zpsms != null) {
			if (qName.equalsIgnoreCase("zpmc")) {
				zpsms.setZpmc(temp);
			} else if (qName.equalsIgnoreCase("zpzl")) {
				zpsms.setZpzl(temp);
			} else if (qName.equalsIgnoreCase("zpsm")) {
				zpsmslists.add(zpsms);
			}
			temp = "";
		}
	}
}
