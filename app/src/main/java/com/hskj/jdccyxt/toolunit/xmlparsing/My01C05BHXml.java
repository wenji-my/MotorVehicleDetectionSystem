package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.annotation.SuppressLint;
import com.hskj.jdccyxt.domain.C02Domain;
import com.hskj.jdccyxt.domain.C05DomainBH;

@SuppressLint("UseValueOf")
public class My01C05BHXml extends DefaultHandler {
	private List<C05DomainBH> bhlists;
	private C05DomainBH bh;
	private String temp = "";

	public List<C05DomainBH> getBHs() {
		return this.bhlists;
	}

	@Override
	public void startDocument() throws SAXException {
		bhlists = new ArrayList<C05DomainBH>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("NoticeInfo")) {
			this.bh = new C05DomainBH();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.bh != null) {
			if (qName.equalsIgnoreCase("BH")) {
				bh.setBh(temp);
			} else if (qName.equalsIgnoreCase("NoticeInfo")) {
				bhlists.add(bh);
			}
			temp = "";
		}
	}
}
