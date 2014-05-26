package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;

import com.risevision.common.client.info.PresentationInfo;


@SuppressWarnings("serial")
public class PresentationsInfo extends ScrollingGridInfo implements Serializable {
	private String companyId;
	private ArrayList<PresentationInfo> presentations;
//	private boolean isTemplate;
	
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public ArrayList<PresentationInfo> getPresentations() {
		return presentations;
	}
	
	public void setPresentations(ArrayList<PresentationInfo> presentations) {
		this.presentations = presentations;
	}
	
	public void clearData(){
		presentations = null;
	}

//	public void setIsTemplate(boolean isTemplate) {
//		this.isTemplate = isTemplate;
//	}

//	public boolean getIsTemplate() {
//		return isTemplate;
//	}
}
