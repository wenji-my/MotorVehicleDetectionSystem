package com.hskj.jdccyxt.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class C09Domain implements Serializable {
	private String code;
	private String message;
	private String username;// ��¼�˺�����
	private String powers;// Ȩ��
	private String idenitycard;// ���֤
	private String xszgy;// ��ʻ֤����

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPowers() {
		return powers;
	}

	public void setPowers(String powers) {
		this.powers = powers;
	}

	public String getIdenitycard() {
		return idenitycard;
	}

	public void setIdenitycard(String idenitycard) {
		this.idenitycard = idenitycard;
	}

	public String getXszgy() {
		return xszgy;
	}

	public void setXszgy(String xszgy) {
		this.xszgy = xszgy;
	}

}
