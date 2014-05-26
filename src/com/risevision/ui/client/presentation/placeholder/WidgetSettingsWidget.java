// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.placeholder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.info.WidgetSettingsInfo;
import com.risevision.ui.client.common.service.GadgetService;
import com.risevision.ui.client.common.service.GadgetServiceAsync;
import com.risevision.ui.client.gadget.WidgetCustomUIWidget;
import com.risevision.ui.client.presentation.placeholder.PlaceholderManageWidget;
import com.risevision.ui.client.presentation.placeholder.PlaylistItemManageWidget;

public class WidgetSettingsWidget {
	
	private final GadgetServiceAsync gadgetService = GWT.create(GadgetService.class);
	private GadgetRpcCallBackHandler gadgetRpcCallbackHandler = new GadgetRpcCallBackHandler();

	WidgetSettingsInfo widgetSettings;
	String gadgetId;
	
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
//	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private Label statusBox = new Label();
	private FlexTable mainGrid;
	private int gridSize, row = -1;
	//UI: Gadget fields
	private Label nameLabel = new Label();
	private Anchor configureLink = new Anchor("Configure");

	private Anchor helpLink = new Anchor("Help");
	
	private Command onSave = new Command() {
		@Override
		public void execute() {
			saveData();
		}
	};

	public WidgetSettingsWidget(FlexTable grid) {
		this.mainGrid = grid;
		gridSize = grid.getRowCount();

		helpLink.setTarget("_blank");

		mainPanel.add(helpLink);
		mainPanel.add(statusBox);
//		mainPanel.add(formValidator);
		
		styleControls();
		initValidator();
		
		initActions();
	}

	private void styleControls() {
	}
	
	private void initValidator() {
//		formValidator.addValidationElement(urlTextBox, "URL", FormValidatorInfo.requiredFieldValidator);
	}
	
	private void initActions() {
		configureLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (widgetSettings != null) {
					WidgetCustomUIWidget.getInstance().show(onSave, widgetSettings);
				}
			}
		});
	}
	
	public void setWidgetParams(String widgetUrl, String additionalParams, String gadgetId) {	
		this.gadgetId = gadgetId;
		widgetSettings = new WidgetSettingsInfo(widgetUrl, additionalParams);

//		formValidator.clear();
		
		show();		
	}
	
	public String getWidgetUrl() {
		return widgetSettings.getWidgetUrl();
	}
	
	public String getAdditionalParams() {
		return widgetSettings.getAdditionalParams();
	}
	
	private void show() {
		
		row = gridSize - 1;
//		mainGrid.resize(mainGrid.getRowCount() + 1, 2);
		gridAdd("<b>Settings<b>", mainPanel, null);
//		gridSize = row;
		gridAdd("Name:", nameLabel, null);
		gridAdd(configureLink, null);
		
		helpLink.setVisible(false);
		configureLink.setVisible(false);
	
		if (PlaylistItemManageWidget.getInstance().isShowing()) {
			PlaylistItemManageWidget.getInstance().center();
		}
		else {
			PlaceholderManageWidget.getInstance().center();
		}
		
		setStatus("Loading Widget...");
		loadDataRPC();
//		bindData(new GadgetInfo());
	}
	
	private void bindData(GadgetInfo gadgetInfo) {
		if (gadgetInfo == null)
			return;
		
		setStatus("");
		
		widgetSettings.setWidgetUIUrl(gadgetInfo.getUiUrl());
//		widgetSettings.setWidgetUIUrl("http://commondatastorage.googleapis.com/risemedialibrary-17899fe3-db05-4ecd-ade4-a7106fe53784/SpreadsheetWidgetUI.html");
		nameLabel.setText(gadgetInfo.getName());
		configureLink.setVisible(true);
		
		if (!RiseUtils.strIsNullOrEmpty(gadgetInfo.getHelpUrl())) {
			helpLink.setHref(gadgetInfo.getHelpUrl());
			helpLink.setVisible(true);
		}
		
//		backgroundColor.setText(gadgetInfo.getBackgroundColor());
//		scaleToFitCheckBox.setValue(gadgetInfo.isScaleToFit());
	}
	
//	private void hide() {
//		for (int i = mainGrid.getRowCount() - 1; i >= gridSize + 1; i--) {
//			mainGrid.removeRow(i);
//		}
//		
//		row = gridSize;
//	}
	
	private void loadDataRPC() {
//		String id = widgetSettings.getWidgetHtmlUrl();
		gadgetService.getGadget(gadgetId, gadgetRpcCallbackHandler);
	}
	
	private void gridAdd(String label, Widget widget, String styleName) {
		row++;
		
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		mainGrid.setHTML(row, 0, label);
		if (widget != null)
		{
			mainGrid.setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
			}
	}
	
	private void gridAdd(Widget widget, String styleName) {
		row++;
		
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		if (widget != null)
		{
			mainGrid.setWidget(row, 0, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}

//	public boolean save() {
//		if (formValidator.validate()) {
//			saveData();
////			hide();
////			if (onSave != null)
////				onSave.execute();
//			
//			return true;
//		}
//		
//		return false;
//	}
	
	private void saveData() {

	}
	
	private void setStatus(String status) {
		statusBox.setText(status);
		if (status.isEmpty()) {
			statusBox.setVisible(false);
//			linksPanel.setVisible(true);
		}
		else {
			statusBox.setVisible(true);
//			linksPanel.setVisible(false);
		}
	}
	
	class GadgetRpcCallBackHandler extends RiseAsyncCallback<GadgetInfo> {
		public void onFailure() {
			setStatus("RPC Error...");
		}

		public void onSuccess(GadgetInfo result) {
			if (result != null) {
				bindData(result);
			}
		}
	}
	
}