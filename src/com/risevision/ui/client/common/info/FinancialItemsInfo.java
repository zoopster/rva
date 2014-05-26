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