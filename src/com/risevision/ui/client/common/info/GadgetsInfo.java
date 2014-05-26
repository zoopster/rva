// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

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
