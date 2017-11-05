package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.hskj.jdccyxt.domain.C09Domain;

public class My01C09Xml extends DefaultHandler {
	private List<C09Domain> operatorlists;
	private C09Domain operator;
	private String temp;

	public List<C09Domain> getOperators() {
		return this.operatorlists;
	}

	@Override
	public void startDocument() throws SAXException {
		operatorlists = new ArrayList<C09Domain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("root")) {
			this.operator = new C09Domain();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.operator != null) {
			if (qName.equalsIgnoreCase("code")) {
				operator.setCode(temp);
			} else if (qName.equalsIgnoreCase("message")) {
				operator.setMessage(temp);
			} else if (qName.equalsIgnoreCase("username")) {
				operator.setUsername(temp);
			} else if (qName.equalsIgnoreCase("powers")) {
				operator.setPowers(temp);
			} else if (qName.equalsIgnoreCase("idenitycard")) {
				operator.setIdenitycard(temp);
			} else if (qName.equalsIgnoreCase("xszgy")) {
				operator.setXszgy(temp);
			} else if (qName.equalsIgnoreCase("root")) {
				operatorlists.add(operator);
			}
			temp = "";
		}
	}
}
