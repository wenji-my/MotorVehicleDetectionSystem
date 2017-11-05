package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.hskj.jdccyxt.domain.CodeDomain;

public class MyCodeXml extends DefaultHandler {
	private List<CodeDomain> codelists;
	private CodeDomain code;
	private String temp;

	public List<CodeDomain> getCodes() {
		return this.codelists;
	}

	@Override
	public void startDocument() throws SAXException {
		codelists = new ArrayList<CodeDomain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("root")) {
			this.code = new CodeDomain();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		temp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.code != null) {
			if (qName.equalsIgnoreCase("code")) {
				code.setCode(temp);
			} else if (qName.equalsIgnoreCase("message")) {
				code.setMessage(temp);
			} else if (qName.equalsIgnoreCase("root")) {
				codelists.add(code);
			}
			temp = "";
		}
	}
}
