package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.hskj.jdccyxt.domain.C07Domain;

public class My01C07Xml extends DefaultHandler {
	private List<C07Domain> wkzllists;
	private C07Domain wkzl;
	private String temp = "";

	public List<C07Domain> getWkZls() {
		return this.wkzllists;
	}

	@Override
	public void startDocument() throws SAXException {
		wkzllists = new ArrayList<C07Domain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("root")) {
			this.wkzl = new C07Domain();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.wkzl != null) {
			if (qName.equalsIgnoreCase("hphm")) {
				wkzl.setHphm(temp);
			} else if (qName.equalsIgnoreCase("hpzl")) {
				wkzl.setHpzl(temp);
			} else if (qName.equalsIgnoreCase("jclsh")) {
				wkzl.setJclsh(temp);
			} else if (qName.equalsIgnoreCase("wkccsfjc")) {
				wkzl.setWkccsfjc(temp);
			} else if (qName.equalsIgnoreCase("zbzlsfjc")) {
				wkzl.setZbzlsfjc(temp);
			} else if (qName.equalsIgnoreCase("root")) {
				wkzllists.add(wkzl);
			}
			temp = "";
		}
	}
}
