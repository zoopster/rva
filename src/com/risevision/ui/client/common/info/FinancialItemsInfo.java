// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class FinancialItemsInfo extends ScrollingGridInfo implements Serializable{
	//output
	private ArrayList<FinancialItemInfo> items;

	public void setItems(ArrayList<FinancialItemInfo> items) {
		this.items = items;
	}

	public ArrayList<FinancialItemInfo> getItems() {
		return items;
	}

}