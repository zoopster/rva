package com.risevision.ui.client.common.widgets.demographics;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.DemographicsInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.grid.DoubleRowFormGridWidget;

public class DemographicsWidget extends Composite {
	private DemographicsInfo demographicsInfo;
	//UI pieces
	private DoubleRowFormGridWidget mainGrid = new DoubleRowFormGridWidget(7);
	//Customer fields
	private CompanyTypeWidget companyTypeWidget = new CompanyTypeWidget();
	private ServicesProvidedWidget servicesProvidedWidget = new ServicesProvidedWidget();
	private SimplePanel marketSegmentsPanel = new SimplePanel();
	private SimplePanel organizationTypePanel = new SimplePanel();
	private MarketSegmentsWidget marketSegmentsWidget = new MarketSegmentsWidget();
	private TargetGroupsWidget targetGroupsWidget = new TargetGroupsWidget();
//	private ViewsPerDisplayWidget viewsPerDisplayListBox = new ViewsPerDisplayWidget();
	private RiseListBox advertisingRevenueEarnListBox = new RiseListBox();
	private RiseListBox advertisingRevenueInterestedListBox = new RiseListBox();
	
	
	public DemographicsWidget() {
		mainGrid.addRow("Do you use Rise Vision to provide a service to your Customers, or are you an End User yourself?", //"", 
				companyTypeWidget, null);
		
		mainGrid.addRow("What types of service do you provide?", //"", 
				servicesProvidedWidget, null);
		
		mainGrid.addRow("What Markets do you serve?", //"", 
				marketSegmentsPanel, null);
		
		mainGrid.addRow("What type of organization are you?", //"", 
				organizationTypePanel, null);
		
		mainGrid.addRow("Who does your message target?", //"",
				targetGroupsWidget, null);
		
//		mainGrid.addRow("On average, how many views of 1 of your displays do people see per day? " +
//				"For example let's say you usually have 2,300 people come into your building every " +
//				"day and they typically see 1 of your displays 6 times in that day. That would " +
//				"give us 2,300 x 6 = 13,800 views of your displays per day.", //"", 
//				viewsPerDisplayListBox, "rdn-ListBoxMedium");
		
		advertisingRevenueEarnListBox.addItem("Yes", "yes");
		advertisingRevenueEarnListBox.addItem("No", "no");
		
		mainGrid.addRow("Do you earn display advertising revenue from your displays?", //"", 
				advertisingRevenueEarnListBox, "rdn-ListBoxShort");

		advertisingRevenueInterestedListBox.addItem("Yes", "yes");
		advertisingRevenueInterestedListBox.addItem("No", "no");
		
		mainGrid.addRow("Are you interested in adding display advertising revenue?", //"", 
				advertisingRevenueInterestedListBox, "rdn-ListBoxShort");

		initWidget(mainGrid);

		styleControls();
		initHandlers();
	}

	private void styleControls() {
//		addStyleName("rdn-Indent");
	}

	private void initHandlers() {
		companyTypeWidget.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				showServiceProviderFields();
			}
		});
		
		advertisingRevenueEarnListBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				showAdvertisingRevenueInterestedFields();
			}
		});
	}
	
	public void initValidator(FormValidatorWidget formValidator, boolean validate) {
		if (validate) {
			formValidator.addValidationElement(companyTypeWidget, 
					"Please fill in your Company Profile information at the bottom of the form.", 
					FormValidatorInfo.requiredFieldValidator, true);
		}
		else {
			formValidator.removeValidationElement(companyTypeWidget);
		}
		
	}
	
	private void showServiceProviderFields() {
		String value = companyTypeWidget.getSelectedValue();
		
		if (RiseUtils.strIsNullOrEmpty(value)) {
			mainGrid.setRowVisibility(1, false);
			mainGrid.setRowVisibility(2, false);
			mainGrid.setRowVisibility(3, false);
			mainGrid.setRowVisibility(4, false);
			mainGrid.setRowVisibility(5, false);
			mainGrid.setRowVisibility(6, false);
		}
		else if (value.equals("serviceProvider")) {
			mainGrid.setRowVisibility(1, true);
			marketSegmentsPanel.clear();
			marketSegmentsPanel.add(marketSegmentsWidget);
			mainGrid.setRowVisibility(2, true);
			mainGrid.setRowVisibility(3, false);
			mainGrid.setRowVisibility(4, false);
			mainGrid.setRowVisibility(5, false);
			mainGrid.setRowVisibility(6, false);
		}
		else {
			mainGrid.setRowVisibility(1, false);
			mainGrid.setRowVisibility(2, false);
			organizationTypePanel.clear();
			organizationTypePanel.add(marketSegmentsWidget);
			mainGrid.setRowVisibility(3, true);
			mainGrid.setRowVisibility(4, true);
			mainGrid.setRowVisibility(5, true);
			mainGrid.setRowVisibility(6, true);
		}
		
		showAdvertisingRevenueInterestedFields();
	}
	
	private void showAdvertisingRevenueInterestedFields() {
		if (mainGrid.getRowVisibility(5) && 
				advertisingRevenueEarnListBox.getSelectedValue().equals("no")) {
			mainGrid.setRowVisibility(6, true);
		}
		else {
			mainGrid.setRowVisibility(6, false);
		}
	}

	public void initWidget(DemographicsInfo demographicsInfo) {
		this.demographicsInfo = demographicsInfo;
		
		bindData();
	}
	
	private void bindData() {
		if (demographicsInfo == null) {
			return;
		}
		
		companyTypeWidget.setSelectedValue(demographicsInfo.getCompanyType(), "");
		servicesProvidedWidget.setSelectedValues(demographicsInfo.getServicesProvided());
		marketSegmentsWidget.setSelectedValues(demographicsInfo.getMarketSegments());
		targetGroupsWidget.setSelectedValues(demographicsInfo.getTargetGroups());
//		viewsPerDisplayListBox.setSelectedValue(demographicsInfo.getViewsPerDisplay());
		advertisingRevenueEarnListBox.setSelectedValue(demographicsInfo.isAdvertisingRevenueEarn() ? "yes" : "no");
		advertisingRevenueInterestedListBox.setSelectedValue(demographicsInfo.isAdvertisingRevenueInterested() ? "yes" : "no");
		
		showServiceProviderFields();		
	}

	public DemographicsInfo saveData() {
		if (demographicsInfo == null)
			return null;

		demographicsInfo.setCompanyType(companyTypeWidget.getSelectedValue());
		demographicsInfo.setServicesProvided(servicesProvidedWidget.getSelectedValues());
		demographicsInfo.setMarketSegments(marketSegmentsWidget.getSelectedValues());
		demographicsInfo.setTargetGroups(targetGroupsWidget.getSelectedValues());
//		demographicsInfo.setViewsPerDisplay(viewsPerDisplayListBox.getSelectedValue());
		demographicsInfo.setAdvertisingRevenueEarn(advertisingRevenueEarnListBox.getSelectedValue().equals("yes"));
		demographicsInfo.setAdvertisingRevenueInterested(advertisingRevenueInterestedListBox.getSelectedValue().equals("yes"));
		
		return demographicsInfo;
	}
	
	public void setFocus(boolean focused) {
		companyTypeWidget.setFocus(focused);
	}

}