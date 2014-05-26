// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.placeholder;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.PlaylistItemInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.TooltipLabelWidget;
import com.risevision.ui.client.common.widgets.text.TextEditorWidget;
import com.risevision.ui.client.gadget.GadgetCustomSettingsWidget;
import com.risevision.ui.client.gadget.WidgetCustomUIWidget;
import com.risevision.ui.client.presentation.PresentationLayoutWidget;
import com.risevision.ui.client.presentation.PresentationSelectPopupWidget;
import com.risevision.ui.client.presentation.common.HtmlEditorWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PlaceholderItemManageWidget {	
//	private static final String IMAGE_GADGET_URL = "http://risegadgets.googlecode.com/svn/trunk/SingleImage/SingleImage.xml";
	private PlaylistItemInfo playlistItem;
//	private int itemIndex;
	
	//Get Presentation name Utils
	//UI pieces
	private int gridSize, row = -1;
	private Label titleLabel = new Label("Item");
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	//UI: Placeholder manage fields
	private FlexTable mainGrid;

	private HorizontalPanel presentationIdPanel = new HorizontalPanel();
	private TextBox presentationIdTextBox = new TextBox();
	private Anchor presentationSelectButton = new Anchor("Select");
	private TextBox urlTextBox = new TextBox();
	private TextEditorWidget bulletinTextArea = new TextEditorWidget();
	private HtmlEditorWidget htmlEditor = new HtmlEditorWidget(true);
	private GadgetCustomSettingsWidget gadgetCustomSettings;
	private WidgetSettingsWidget widgetSettings;
	private VideoSettingsWidget videoSettings;
	private ImageSettingsWidget imageSettings;
	
	private PresentationSelectPopupWidget presentationSelectWidget = PresentationSelectPopupWidget.getInstance();
	private Command presentationSelectCommand;
	
	private Command gadgetSavedCommand, saveCompleteCommand;

	public PlaceholderItemManageWidget(FlexTable grid, Command saveCompleteCommand) {
		this.mainGrid = grid;
		gridSize = grid.getRowCount();
		
		this.saveCompleteCommand = saveCompleteCommand;
		
		presentationIdPanel.add(presentationIdTextBox);
		presentationIdPanel.add(new SpacerWidget());
		presentationIdPanel.add(presentationSelectButton);
		
		styleControls();

		initValidator();
		
		initCommands();
		initActions();
		
		gadgetCustomSettings = new GadgetCustomSettingsWidget(mainGrid, gadgetSavedCommand);
		widgetSettings = new WidgetSettingsWidget(mainGrid);
		videoSettings = new VideoSettingsWidget(mainGrid);
		imageSettings = new ImageSettingsWidget(mainGrid);
	}

	private void styleControls() {		
		//style the table	
		mainGrid.setCellSpacing(0);
		mainGrid.setCellPadding(0);
		mainGrid.setStyleName("rdn-Table");
		
		presentationIdTextBox.setStyleName("rdn-TextBoxMedium");
		urlTextBox.setStyleName("rdn-TextBoxLong");
		
		htmlEditor.setSize("550px", "250px");

		titleLabel.setStyleName("rdn-Head");
	}

	private void initValidator() {
		formValidator.addValidationElement(presentationIdTextBox, "Presentation Id", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(urlTextBox, "URL", FormValidatorInfo.requiredFieldValidator);
	}
	
	private void initCommands() {
		gadgetSavedCommand = new Command() {
			@Override
			public void execute() {
				onGadgetSaved();
			}
		};
		
		presentationSelectCommand = new Command() {
			@Override
			public void execute() {
				if (presentationSelectWidget.getCurrentPresentation() != null) {
					String presentationId = presentationSelectWidget.getCurrentPresentation().getId();
					presentationIdTextBox.setText(presentationId);
					
					presentationSelectWidget.hide();
				}
			}
		};
	}
	
	private void initActions() {
		presentationSelectButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presentationSelectWidget.show(presentationSelectCommand);
			}
		});
	}

	public void show(PlaylistItemInfo playlistItem, GadgetInfo gadget){
		if (gadget == null) {
			return;
		}
		
		if (gadget.getName() != null && !gadget.getName().isEmpty()) {
			playlistItem.setName(gadget.getName());
		}
		
		playlistItem.setObjectData(gadget.getUrl());
		playlistItem.setObjectRef(gadget.getId());
		
		playlistItem.setType(gadget.getType().toLowerCase());
		
		show(playlistItem);
	}
	
//	public void show(PlaylistItemInfo playlistItem, String itemType)	{
//		show(playlistItem, itemType, null);
//	}	
	
	public void show(PlaylistItemInfo playlistItem)	{		
		if (playlistItem == null){
//			playlistItem = new PlaylistItemInfo();
//			playlistItem.setType(itemType);
//			playlistItem.setName(RiseUtils.capitalizeFirstLetter(itemType));
//			
//			itemIsNew = true;
//			
//			if (itemType.equals(PlaylistItemInfo.TYPE_IMAGE)) {
//				playlistItem.setObjectData(IMAGE_GADGET_URL);
//			}
			return;
		}
		
		this.playlistItem = playlistItem;
		addGridWidgets();
	}
	
	private void addGridWidgets() {
		hide();
		row = gridSize - 1;
//		mainGrid.resize(gridSize, 2);
//		gridAdd("<b>Item<b>", formValidator, null);
		
		if (PlaylistItemInfo.TYPE_GADGET.equals(playlistItem.getType())) {
			if (playlistItem.getObjectData() != null && !playlistItem.getObjectData().isEmpty()) {
				gadgetCustomSettings.setGadgetParams(playlistItem.getObjectData(), playlistItem.getAdditionalParams());
			}
			else {
				gadgetCustomSettings.setGadgetId(playlistItem.getId());
			}
		}
		else if (PlaylistItemInfo.TYPE_WIDGET.equals(playlistItem.getType())) {
			widgetSettings.setWidgetParams(playlistItem.getObjectData(), playlistItem.getAdditionalParams(), playlistItem.getObjectRef());
		}
		else if (PlaylistItemInfo.TYPE_TEXT.equals(playlistItem.getType())) {
//			mainGrid.resize(gridSize + 1, 2);
//			gridAdd("Text:", bulletinTextArea, null);
			
			gridAdd("<b>Settings<b>", null, null, null);
			row++;
			mainGrid.setWidget(row, 0, bulletinTextArea);
			mainGrid.getFlexCellFormatter().setColSpan(row, 0, 2);
			
			bulletinTextArea.setHTML(playlistItem.getObjectData());
			bulletinTextArea.resizeTextArea(PresentationLayoutWidget.getInstance().getPlaceholderWidth(),
					PresentationLayoutWidget.getInstance().getPlaceholderHeight());
		}
		else if (PlaylistItemInfo.TYPE_PRESENTATION.equals(playlistItem.getType())) {
//			mainGrid.resize(gridSize + 1, 2);
			gridAdd("<b>Settings<b>", null, null, null);
			gridAdd("Presentation ID:", 
					"Select a Presentation from your Company or enter the identifier of another Presentation",
					presentationIdPanel, null);
			
			presentationIdTextBox.setText(playlistItem.getObjectData());
		}
		else if (PlaylistItemInfo.TYPE_VIDEO.equals(playlistItem.getType())) {
			videoSettings.setGadgetUrl(playlistItem.getObjectData());			
		}
		else if (PlaylistItemInfo.TYPE_IMAGE.equals(playlistItem.getType())) {
			imageSettings.setGadgetUrl(playlistItem.getObjectData());
		}
		else if (PlaylistItemInfo.TYPE_HTML.equals(playlistItem.getType())) {
			gridAdd("<b>Settings<b>", null, null, null);
			row++;
			mainGrid.setWidget(row, 0, htmlEditor);
			mainGrid.getFlexCellFormatter().setColSpan(row, 0, 2);
			
			htmlEditor.setText(playlistItem.getObjectData());

		}
		else if (PlaylistItemInfo.TYPE_URL.equals(playlistItem.getType())) {
			gridAdd("<b>Settings<b>", null, null, null);
			gridAdd("URL:", 
					"The URL of this Item",
					urlTextBox, null);
			
			urlTextBox.setText(playlistItem.getObjectData());
		}
		
		if (PlaylistItemManageWidget.getInstance().isShowing()) {
			PlaylistItemManageWidget.getInstance().center();
		}
		else {
			PlaceholderManageWidget.getInstance().center();
		}
	}
	
	protected void hide() {
		for (int i = mainGrid.getRowCount() - 1; i >= gridSize; i--) {
			mainGrid.removeRow(i);
		}
		
		gadgetCustomSettings.setShowing(false);
		WidgetCustomUIWidget.getInstance().hide();
//		mainGrid.resize(gridSize, 2);

		formValidator.clearValidationElements();

	}
	
	public boolean save() {
		if (playlistItem == null)
			return true;
		
		if (PlaylistItemInfo.TYPE_GADGET.equals(playlistItem.getType())) {
			gadgetCustomSettings.save();
//			if (!gadgetCustomSettings.save()) {
//				return false;
//			}
//			playlistItem.setObjectData(gadgetCustomSettings.getGadgetUrl());
		}
		else {
			if (PlaylistItemInfo.TYPE_WIDGET.equals(playlistItem.getType())) {
				playlistItem.setObjectData(widgetSettings.getWidgetUrl());
				playlistItem.setAdditionalParams(widgetSettings.getAdditionalParams());

			}
			else if (PlaylistItemInfo.TYPE_TEXT.equals(playlistItem.getType())) {
				playlistItem.setObjectData(bulletinTextArea.getHTML());
			}
			else if (PlaylistItemInfo.TYPE_PRESENTATION.equals(playlistItem.getType())) {
				if (!formValidator.validate(presentationIdTextBox)) {
					return false;
				}
				playlistItem.setObjectData(presentationIdTextBox.getText().trim());
			}
			else if (PlaylistItemInfo.TYPE_VIDEO.equals(playlistItem.getType())) {
				if (!videoSettings.save()) {
					return false;
				}
				playlistItem.setObjectData(videoSettings.getGadgetUrl());
				
				// Force Play Until Done for videos
				playlistItem.setPlayUntilDone(true);
			}
			else if (PlaylistItemInfo.TYPE_IMAGE.equals(playlistItem.getType())) {
				if (!imageSettings.save()) {
					return false;
				}
				playlistItem.setObjectData(imageSettings.getGadgetUrl());
			}
			else if (PlaylistItemInfo.TYPE_HTML.equals(playlistItem.getType())) {
				playlistItem.setObjectData(htmlEditor.getText());
			}
			else if (PlaylistItemInfo.TYPE_URL.equals(playlistItem.getType())) {
				if (!formValidator.validate(urlTextBox)) {
					return false;
				}
				playlistItem.setObjectData(urlTextBox.getText().trim());
			}
			
			onSaveComplete();
		}
		
		return true;
	}
	
	private void onGadgetSaved() {
		playlistItem.setObjectData(gadgetCustomSettings.getGadgetUrl());
		playlistItem.setAdditionalParams(gadgetCustomSettings.getAdditionalParams());
		
		onSaveComplete();
	}
	
	private void onSaveComplete() {
		if (saveCompleteCommand != null) {
			saveCompleteCommand.execute();
		}
	}
	
//	private void gridAdd(String label, Widget widget) {
//		gridAdd(label, widget, null, null);
//	}
	
	private void gridAdd(String label, String tooltip, Widget widget, String styleName) {
		row++;
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		TooltipLabelWidget tooltipWidget = new TooltipLabelWidget(label, tooltip);

		mainGrid.setWidget(row, 0, tooltipWidget);
		if (widget != null){
			mainGrid.setWidget(row, 1, widget);
			
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}

	public void setPlaylistItem(PlaylistItemInfo playlistItem) {
		this.playlistItem = playlistItem;
	}

	public PlaylistItemInfo getPlaylistItem() {
		return playlistItem;
	}
}