package com.hskj.jdccyxt.toolunit.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.annotation.SuppressLint;
import com.hskj.jdccyxt.domain.C04Domain;

@SuppressLint("UseValueOf")
public class My01C04Xml extends DefaultHandler {
	private List<C04Domain> ggzplists;
	private C04Domain ggzp;
	private String temp = "";
	private StringBuffer buffer = new StringBuffer();

	public List<C04Domain> getGgzplists() {
		return this.ggzplists;
	}

	@Override
	public void startDocument() throws SAXException {
		ggzplists = new ArrayList<C04Domain>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		buffer.setLength(0);
		if (qName.equalsIgnoreCase("vehispara")) {
			this.ggzp = new C04Domain();
			ggzp.setId(new Integer(attributes.getValue(0)));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// temp = new String(ch, start, length);
		temp = buffer.append(ch, start, length).toString();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (this.ggzp != null) {
			if (qName.equalsIgnoreCase("CK_ID")) {
				ggzp.setCk_id(temp);
			} else if (qName.equalsIgnoreCase("CREATETIME")) {
				ggzp.setCreatetime(temp);
			} else if (qName.equalsIgnoreCase("ZPXH")) {
				ggzp.setZpxh(temp);
			} else if (qName.equalsIgnoreCase("ZP")) {
				ggzp.setZp(temp);
			} else if (qName.equalsIgnoreCase("PSSJ")) {
				ggzp.setPssj(temp);
			} else if (qName.equalsIgnoreCase("ZPZL")) {
				ggzp.setZpzl(temp);
			} else if (qName.equalsIgnoreCase("WJYBH")) {
				ggzp.setWjybh(temp);
			} else if (qName.equalsIgnoreCase("JYLSH")) {
				ggzp.setJylsh(temp);
			} else if (qName.equalsIgnoreCase("vehispara")) {
				ggzplists.add(ggzp);
			}
			temp = "";
		}
	}
}
