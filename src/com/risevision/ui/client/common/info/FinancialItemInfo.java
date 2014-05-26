package com.risevision.ui.client.common.info;

public class FinancialItemInfo {
	public static final String CODE_ATTRIBUTE = "code";
	public static final String NAME_ATTRIBUTE = "name";
	
	private String code;
	private String name;
	
	public FinancialItemInfo() {
		
	}
	
	public FinancialItemInfo(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
