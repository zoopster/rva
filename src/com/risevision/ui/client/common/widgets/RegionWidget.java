package com.risevision.ui.client.common.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class RegionWidget extends Composite{
	private String country = "";
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private RiseListBox lsRegion = new RiseListBox();
	private TextBox tbRegion = new TextBox();
	
	public RegionWidget() {
		//set style
		lsRegion.setStyleName("rdn-ListBoxMedium");
		tbRegion.setStyleName("rdn-TextBoxMedium");
		//add elements to UI
		mainPanel.add(lsRegion);
		mainPanel.add(tbRegion);
		initWidget(mainPanel);
		//set defaults
		setCountry("US");
	}

	private void loadCA() {
		lsRegion.addItem("Alberta", "AB");
		lsRegion.addItem("British Columbia", "BC");
		lsRegion.addItem("Manitoba", "MB");
		lsRegion.addItem("New Brunswick", "NB");
		lsRegion.addItem("Newfoundland and Labrador", "NL");
		lsRegion.addItem("Northwest Territories", "NT");
		lsRegion.addItem("Nova Scotia", "NS");
		lsRegion.addItem("Nunavut", "NV");
		lsRegion.addItem("Ontario", "ON");
		lsRegion.addItem("Prince Edward Island", "PE");
		lsRegion.addItem("Quebec", "QC");
		lsRegion.addItem("Saskatchewan", "SK");
		lsRegion.addItem("Yukon Territory", "YT");	
		lsRegion.setSelectedIndex(8);			
	}

	private void loadUS() {
		lsRegion.addItem("Alabama", "AL");
		lsRegion.addItem("Alaska", "AK");
		lsRegion.addItem("Arizona", "AZ");
		lsRegion.addItem("Arkansas", "AR");
		lsRegion.addItem("California", "CA");
		lsRegion.addItem("Colorado", "CO");
		lsRegion.addItem("Connecticut", "CT");
		lsRegion.addItem("Delaware", "DE");
		lsRegion.addItem("District of Columbia", "DC");
		lsRegion.addItem("Florida", "FL");
		lsRegion.addItem("Georgia", "GA");
		lsRegion.addItem("Hawaii", "HI");
		lsRegion.addItem("Idaho", "ID");
		lsRegion.addItem("Illinois", "IL");
		lsRegion.addItem("Indiana", "IN");
		lsRegion.addItem("Iowa", "IA");
		lsRegion.addItem("Kansas", "KS");
		lsRegion.addItem("Kentucky", "KY");
		lsRegion.addItem("Louisiana", "LA");
		lsRegion.addItem("Maine", "ME");
		lsRegion.addItem("Maryland", "MD");
		lsRegion.addItem("Massachusetts", "MA");
		lsRegion.addItem("Michigan", "MI");
		lsRegion.addItem("Minnesota", "MN");
		lsRegion.addItem("Mississippi", "MS");
		lsRegion.addItem("Missouri", "MO");
		lsRegion.addItem("Montana", "MT");
		lsRegion.addItem("Nebraska", "NE");
		lsRegion.addItem("Nevada", "NV");
		lsRegion.addItem("New Hampshire", "NH");
		lsRegion.addItem("New Jersey", "NJ");
		lsRegion.addItem("New Mexico", "NM");
		lsRegion.addItem("New York", "NY");
		lsRegion.addItem("North Carolina", "NC");
		lsRegion.addItem("North Dakota", "ND");
		lsRegion.addItem("Ohio", "OH");
		lsRegion.addItem("Oklahoma", "OK");
		lsRegion.addItem("Oregon", "OR");
		lsRegion.addItem("Pennsylvania", "PA");
		lsRegion.addItem("Rhode Island", "RI");
		lsRegion.addItem("South Carolina", "SC");
		lsRegion.addItem("South Dakota", "SD");
		lsRegion.addItem("Tennessee", "TN");
		lsRegion.addItem("Texas", "TX");
		lsRegion.addItem("Utah", "UT");
		lsRegion.addItem("Vermont", "VT");
		lsRegion.addItem("Virginia", "VA");
		lsRegion.addItem("Washington", "WA");
		lsRegion.addItem("West Virginia", "WV");
		lsRegion.addItem("Wisconsin", "WI");
		lsRegion.addItem("Wyoming", "WY");
		lsRegion.setSelectedIndex(32);			
		}
	
	private boolean isUsOrCanada(String country)
	{
		return ((country != null) && (("US".equals(country)) || ("CA".equals(country))));
	}
	
	public void setCountry(String country)
	{
		boolean lShowList = isUsOrCanada(country);

		lsRegion.setVisible(lShowList);
		tbRegion.setVisible(!lShowList);
		if (lShowList) {
			if (!this.country.equals(country)) {
				lsRegion.clear();
				if (country.equals("CA"))
					loadCA();
				else if (country.equals("US"))
					loadUS();
			}
		}
		this.country = country;
	}

	public String getRegion()
	{
		if (isUsOrCanada(country))
			return lsRegion.getSelectedValue();
		else
			return tbRegion.getText();
	}

	public void setRegion(String region)
	{
		if (isUsOrCanada(country))
			lsRegion.setSelectedValue(region);
		else
			tbRegion.setText(region);
	}
	
	public void setEnabled(boolean enabled) {
		lsRegion.setEnabled(enabled);
		tbRegion.setEnabled(enabled);
	}


}