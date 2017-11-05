package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import android.annotation.SuppressLint;
import com.hskj.jdccyxt.domain.C06DomainCLLX;

@SuppressLint("UseValueOf")
public class My01C06cllxXml extends DefaultHandler {
	private List<C06DomainCLLX> cllxslists;
	private C06DomainCLLX cllxs;
	private String temp;

	public List<C06DomainCLLX> getCllxs() {
		return this.cllxslists;
	}

	@Override
	public void startDocument() throws SAXException {
		cllxslists = new ArrayList<C06DomainCLLX>();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("cllxsm")) {
			this.cllxs = new C06DomainCLLX();
			cllxs.setId(new Integer(attributes.getValue(0)));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.cllxs != null) {
			if (qName.equalsIgnoreCase("cllxbs")) {
				cllxs.setCllxbs(temp);
			} else if (qName.equalsIgnoreCase("cllxmc")) {
				cllxs.setCllxmc(temp);
			} else if (qName.equalsIgnoreCase("cllxsm")) {
				cllxslists.add(cllxs);
			}
			temp = "";
		}
	}
}
