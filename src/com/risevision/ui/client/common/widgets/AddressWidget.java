// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.HasAddress;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.widgets.CountryWidget;
import com.risevision.ui.client.common.widgets.RegionWidget;
import com.risevision.ui.client.common.widgets.TimeZoneWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AddressWidget extends Grid {
	//UI pieces
	private int row = -1;
	private boolean isEnabled = true;
	//Customer fields
	private TextBox tbAddressDesc = new TextBox();
	private TextBox tbStreet = new TextBox();
	private TextBox tbUnit = new TextBox();
	private TextBox tbCity = new TextBox();
	private CountryWidget wgCountry = new CountryWidget();
	private RegionWidget wgRegion = new RegionWidget();	
	private TextBox tbPostalCode = new TextBox();
	private TimeZoneWidget wgTimeZone = new TimeZoneWidget();

	public AddressWidget() {
		this(false);
	}
	
	public AddressWidget(Boolean showDescription) {
		super(8, 2);

		//style the table	
		setCellSpacing(0);
		setCellPadding(0);
		setStyleName("rdn-Table");
		
		// add widgets
		if (showDescription) {
			gridAdd("Description:", tbAddressDesc, "rdn-TextBoxLong");
		}
		gridAdd("Street:", tbStreet, "rdn-TextBoxLong");
		gridAdd("Unit:", tbUnit, "rdn-TextBoxMedium");
		gridAdd("City:", tbCity, "rdn-TextBoxMedium");
		gridAdd("Country:", wgCountry, "rdn-ListBoxMedium");
		
		wgCountry.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				wgRegion.setCountry(wgCountry.getSelectedValue());
			}
		});
		
		gridAdd("State/Province/Region:", wgRegion, null); //Region is composite widget, no need to style 
		gridAdd("Postal Code:", tbPostalCode, "rdn-TextBoxMedium");
		gridAdd("Time Zone:", wgTimeZone, "rdn-ListBoxLong");
	}

	public void loadData(HasAddress addr) {
		if (addr == null || !isEnabled)
			return;

		tbAddressDesc.setText(addr.getAddressDescription());
		wgCountry.setSelectedValue(addr.getCountry());
		wgRegion.setCountry(addr.getCountry());
		wgRegion.setRegion(addr.getProvince());
		wgTimeZone.setSelectedValue(addr.getTimeZone());
		tbStreet.setText(addr.getStreet());
		tbUnit.setText(addr.getUnit());
		tbCity.setText(addr.getCity());
		tbPostalCode.setText(addr.getPostalCode());
	}

	public void saveData(HasAddress addr) {
		if (addr == null)
			return;
				
		addr.setAddressDescription(tbAddressDesc.getText());
		addr.setStreet(tbStreet.getText());
		addr.setUnit(tbUnit.getText());
		addr.setCity(tbCity.getText());

		addr.setCountry(wgCountry.getSelectedValue());
		addr.setProvince(wgRegion.getRegion());
		addr.setPostalCode(tbPostalCode.getText());
		addr.setTimeZone(wgTimeZone.getSelectedValue());
	}
	
	private void gridAdd(String label, Widget widget, String styleName) {
		row++;
		getCellFormatter().setStyleName(row, 0, "rdn-Column1");
		setText(row, 0, label);
		if (widget != null)
		{
			setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}
	
	public void setEnabled(boolean isEnabled) {
		if (isEnabled)
			removeStyleName("rdn-Disabled");
		else {
			addStyleName("rdn-Disabled");
			loadData(new DisplayInfo());
		}
		
		tbAddressDesc.setEnabled(isEnabled);
		wgCountry.setEnabled(isEnabled);
		wgRegion.setEnabled(isEnabled);
		tbStreet.setEnabled(isEnabled);
		tbUnit.setEnabled(isEnabled);
		tbCity.setEnabled(isEnabled);
		tbPostalCode.setEnabled(isEnabled);
		wgTimeZone.setEnabled(isEnabled);
		
		this.isEnabled = isEnabled;
	}
}