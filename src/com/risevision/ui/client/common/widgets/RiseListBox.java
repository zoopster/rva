package com.risevision.ui.client.common.widgets;

import com.google.gwt.user.client.ui.ListBox;

public class RiseListBox extends ListBox{

//	public RiseListBox() {
//	}

	public String getSelectedValue()
	{
		return getSelectedValue("");	
	};

	public String getSelectedValue(String defaultValue)
	{
		if (getSelectedIndex() >= 0)
			return getValue(getSelectedIndex());			
		else
			return defaultValue;
	}
	
	public String getSelectedText(String defaultText) {
		if (getSelectedIndex() != -1) {
			return getItemText(getSelectedIndex());
		}
		else {
			return defaultText;
		}
	}

	public void setSelectedValue(int value) {
		setSelectedValue(Integer.toString(value));
	}
	
	public void setSelectedValue(String value) {
		setSelectedValue(value, null);
	}
	
	public void setSelectedValue(String value, String defaultValue)
	{
		for (int i = 0; i < getItemCount(); i++) {
			if (getValue(i).equals(value)) {
				setSelectedIndex(i);
				return;
			}
		}
		
		if (defaultValue != null) {
			setSelectedValue(defaultValue);
		}
	}
	
	public void addItemAlphabetically(String item, String value) {
		for (int i = 0; i < getItemCount(); i++) {
			if (getItemText(i).compareTo(item) > 0) {
				insertItem(item, value, i);
				return;
			}
		}
		addItem(item, value);
	}

}