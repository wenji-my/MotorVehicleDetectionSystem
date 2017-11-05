package com.hskj.jdccyxt.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class C09Domain implements Serializable {
	private String code;
	private String message;
	private String username;// 登录账号姓名
	private String powers;// 权限
	private String idenitycard;// 身份证
	private String xszgy;// 行驶证公用

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
