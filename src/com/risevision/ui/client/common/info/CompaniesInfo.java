package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CompaniesInfo extends ScrollingGridInfo implements Serializable{

	public CompaniesInfo() {
	}
	
	public CompaniesInfo(String companyId, String sortBy) {
		this.companyId = companyId;
//		this.setDepth(depth);
		this.setSortBy(sortBy);
	}

	//input parameters
	private String companyId;
//	private int depth;
	//output
	private ArrayList<CompanyInfo> companies;

	public void setCompanies(ArrayList<CompanyInfo> companies) {
		this.companies = companies;
	}

	public ArrayList<CompanyInfo> getCompanies() {
		return companies;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

//	public void setDepth(int depth) {
//		this.depth = depth;
//	}

//	public int getDepth() {
//		return depth;
//	}
}