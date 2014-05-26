package com.risevision.ui.client.presentation.placeholder;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.VideoItemInfo;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.SliderBarWidget;
import com.risevision.ui.client.common.widgets.TooltipLabelWidget;
import com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerViewId;
import com.risevision.ui.client.common.widgets.mediaLibrary.MediaLibraryTextBox;
import com.risevision.ui.client.presentation.placeholder.PlaceholderManageWidget;
import com.risevision.ui.client.presentation.placeholder.PlaylistItemManageWidget;

public class VideoSettingsWidget {
	VideoItemInfo videoItem;
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private Label statusBox = new Label();
	private FlexTable mainGrid;
	private int gridSize, row = -1;
	//UI: Gadget fields
	private Anchor helpLink = new Anchor("Help");
	private MediaLibraryTextBox videoUrlTextBox = new MediaLibraryTextBox(GooglePickerViewId.DOCS_VIDEOS);
	private SliderBarWidget videoVolumeSlider = new SliderBarWidget(100, false);
//	private TextBox videoVolumeTextBox = new TextBox();
	private CheckBox videoAutoHideControlsCheckBox = new CheckBox();
	private CheckBox videoCarryOnCheckBox = new CheckBox();

	public VideoSettingsWidget(FlexTable grid) {
		this.mainGrid = grid;
		gridSize = grid.getRowCount();

		helpLink.setTarget("_blank");
		helpLink.setHref("http://www.risevision.com/help/users/what-are-gadgets/content/video-types-and-encoding/");

		mainPanel.add(helpLink);
		mainPanel.add(statusBox);
		mainPanel.add(formValidator);
		
		styleControls();
		initValidator();
	}

	private void styleControls() {
	}
	
	private void initValidator() {
		formValidator.addValidationElement(videoUrlTextBox, "URL", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(videoUrlTextBox, "URL", FormValidatorInfo.urlValidator);
//		formValidator.addValidationElement(videoVolumeTextBox, "Volume", FormValidatorInfo.requiredFieldValidator);
	}
	
	public void setGadgetUrl(String gadgetUrl) {	
		formValidator.clear();
		show();
		
		bindData(gadgetUrl);
	}
	
	public String getGadgetUrl() {
		return videoItem.buildUrl();
	}
	
	private void show() {
		row = gridSize - 1;
//		mainGrid.resize(mainGrid.getRowCount() + 1, 2);
		gridAdd("<b>Settings<b>", null, mainPanel, null);
//		gridSize = row;
		gridAdd("URL*:", 
				"The URL path where the Video resides",
				videoUrlTextBox, "rdn-TextBoxLong");
		gridAdd("Volume:", 
				"The sound level of the Video",
				videoVolumeSlider, null);
//		gridAdd("Volume (0-100)*:", videoVolumeTextBox, "rdn-TextBoxShort");
		gridAdd("Auto Hide Controls:",
				" Show or hide the video player controls at the bottom of the Video", 
				videoAutoHideControlsCheckBox, "rdn-CheckBox");
//		gridAdd("Carry On:", videoCarryOnCheckBox, "rdn-CheckBox");
			
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
		
		videoItem = new VideoItemInfo(gadgetUrl);

		videoUrlTextBox.setText(videoItem.getVideoUrl(), videoItem.getVideoExtension());
		videoVolumeSlider.setSliderPosition(RiseUtils.strToInt(videoItem.getVideoVolume(), 0));
//		videoVolumeTextBox.setText(videoItem.getVideoVolume());
		videoCarryOnCheckBox.setValue(videoItem.isCarryOn());
		videoAutoHideControlsCheckBox.setValue(videoItem.isAutoHide());
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
		videoItem.setVideoUrl(videoUrlTextBox.getText());
		videoItem.setVideoExtension(videoUrlTextBox.getFileExtension());
//		videoItem.setVideoVolume(videoVolumeTextBox.getText());
		videoItem.setVideoVolume(Integer.toString(videoVolumeSlider.getSliderPosition()));
		videoItem.setCarryOn(videoCarryOnCheckBox.getValue());
		videoItem.setAutoHide(videoAutoHideControlsCheckBox.getValue());
	}
	
}