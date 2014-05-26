package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GadgetsInfo extends ScrollingGridInfo implements Serializable {
	private String companyId;
	private ArrayList<GadgetInfo> gadgets;
	
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public ArrayList<GadgetInfo> getGadgets() {
		return gadgets;
	}
	
	public void setGadgets(ArrayList<GadgetInfo> gadgets) {
		this.gadgets = gadgets;
	}
	
	public void clearData(){
		gadgets = null;
	}
}
