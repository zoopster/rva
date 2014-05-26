package com.risevision.ui.client.presentation.placeholder;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.ImageItemInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.TooltipLabelWidget;
import com.risevision.ui.client.common.widgets.colorPicker.ColorPickerTextBox;
import com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerViewId;
import com.risevision.ui.client.common.widgets.mediaLibrary.MediaLibraryTextBox;
import com.risevision.ui.client.presentation.placeholder.PlaceholderManageWidget;
import com.risevision.ui.client.presentation.placeholder.PlaylistItemManageWidget;

public class ImageSettingsWidget {
	ImageItemInfo imageItem;
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private Label statusBox = new Label();
	private FlexTable mainGrid;
	private int gridSize, row = -1;
	//UI: Gadget fields
	private MediaLibraryTextBox urlTextBox = new MediaLibraryTextBox(GooglePickerViewId.DOCS_IMAGES);
	private ColorPickerTextBox backgroundColor = new ColorPickerTextBox();
	private CheckBox scaleToFitCheckBox = new CheckBox();

	public ImageSettingsWidget(FlexTable grid) {
		this.mainGrid = grid;
		gridSize = grid.getRowCount();

		mainPanel.add(statusBox);
		mainPanel.add(formValidator);
		
		styleControls();
		initValidator();
	}

	private void styleControls() {
	}
	
	private void initValidator() {
		formValidator.addValidationElement(urlTextBox, "URL", FormValidatorInfo.requiredFieldValidator);
	}
	
	public void setGadgetUrl(String gadgetUrl) {	
		formValidator.clear();
		show();
		
		bindData(gadgetUrl);
	}
	
	public String getGadgetUrl() {
		return imageItem.buildUrl();
	}
	
	private void show() {
		row = gridSize - 1;
//		mainGrid.resize(mainGrid.getRowCount() + 1, 2);
		gridAdd("<b>Settings<b>", null, mainPanel, null);
//		gridSize = row;
		gridAdd("URL*:",
				"The URL path where the Image resides", 
				urlTextBox, "rdn-TextBoxLong");
		gridAdd("Background Color:", 
				"The Background Color that shows if the Image does not fill the Placeholder",
				backgroundColor, "rdn-TextBoxShort");
		gridAdd("Scale to Fit:", 
				"Resize Image to fill Placeholder",
				scaleToFitCheckBox, "rdn-CheckBox");
			
		if (PlaylistItemManageWidget.getInstance().isShowing()) {
			PlaylistItemManageWidget.getInstance().center();
		}
		else {
			PlaceholderManageWidget.getInstance().center();
		}
	}
	
	private void bindData(String gadgetUrl) {
		if (gadgetUrl != null)
			gadgetUrl = gadgetUrl.trim();
		
		imageItem = new ImageItemInfo(gadgetUrl);

		urlTextBox.setText(imageItem.getUrl());
		backgroundColor.setText(imageItem.getBackgroundColor());
		scaleToFitCheckBox.setValue(imageItem.isScaleToFit());
	}
	
//	private void hide() {
//		for (int i = mainGrid.getRowCount() - 1; i >= gridSize + 1; i--) {
//			mainGrid.removeRow(i);
//		}
//		
//		row = gridSize;
//	}
	
	private void gridAdd(String label, String tooltip, Widget widget, String styleName) {
		row++;
		
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		TooltipLabelWidget tooltipWidget = new TooltipLabelWidget(label, tooltip);

		mainGrid.setWidget(row, 0, tooltipWidget);
		if (widget != null)
		{
			mainGrid.setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}

	public boolean save() {
		if (formValidator.validate()) {
			saveData();
//			hide();
//			if (onSave != null)
//				onSave.execute();
			
			return true;
		}
		
		return false;
	}
	
	private void saveData() {
		imageItem.setUrl(urlTextBox.getText());
		imageItem.setBackgroundColor(backgroundColor.getText());
		imageItem.setScaleToFit(scaleToFitCheckBox.getValue());
	}
	
}