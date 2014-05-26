package com.risevision.ui.client.common.widgets.mediaLibrary;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class MediaLibraryTextBox extends Composite implements HasText, ClickHandler {
	private TextBox mediaUrlTextBox = new TextBox();
//	private final String googleDriveButtonName = "Google Drive\u2122 (beta)";
//	private Anchor googlePickerLink = new Anchor("<span style='white-space:nowrap;'>" + googleDriveButtonName + "</span>", true);
//	private HorizontalPanel mediaLibraryPanel = new HorizontalPanel();
//	private final String mediaLibraryButtonName = "Media Library";
	private final String mediaLibraryButtonName = "Storage";
	private Anchor mediaLibraryLink = new Anchor("<span style='white-space:nowrap;'>" + mediaLibraryButtonName + "</span>", true);

//	private GooglePickerWidget googlePickerWidget = GooglePickerWidget.getInstance();
	private StorageAppWidget mediaLibraryWidget = StorageAppWidget.getInstance();

//	private Command googlePickerSelectCommand;
	private Command mediaLibrarySelectCommand;
//	private String viewId = null;
	
	private String fileExtension;
	
	public MediaLibraryTextBox(String viewId) {
//		this.viewId = viewId;
		HorizontalPanel panel = new HorizontalPanel();
		        
		panel.add(mediaUrlTextBox);
		panel.add(new SpacerWidget());
//		panel.add(googlePickerLink);
		
//		mediaLibraryPanel.add(new SpacerWidget());
//		mediaLibraryPanel.add(new HTML("|"));
//		mediaLibraryPanel.add(new SpacerWidget());
//		mediaLibraryPanel.add(mediaLibraryLink);

//		panel.add(mediaLibraryPanel);
		
		panel.add(mediaLibraryLink);
		
//		googlePickerLink.addClickHandler(this);
		mediaLibraryLink.addClickHandler(this);
		
//		googlePickerSelectCommand = new Command() {
//			@Override
//			public void execute() {
//				setText(googlePickerWidget.getFileUrl());
//				fileExtension = googlePickerWidget.getFileExtension();
//			}
//		};
		
		mediaLibrarySelectCommand = new Command() {
			@Override
			public void execute() {
				setText(mediaLibraryWidget.getItemUrl());
			}
		};
		
		initWidget(panel);
	}
	
	protected void onLoad() {
//		boolean isEnabled = SelectedCompanyController.getInstance().isSelectedCompanyFeature(EnabledFeaturesInfo.FEATURE_MEDIA_LIBRARY);
//		mediaLibraryLink.setVisible(isEnabled);
	}
	
	@Override
	public String getText() {
		return mediaUrlTextBox.getText().trim();
	}

	public void setText(String text, String extension) {
		mediaUrlTextBox.setValue(text);
		fileExtension = extension;	
	}
	
	@Override
	public void setText(String text) {
		mediaUrlTextBox.setValue(text);
		fileExtension = "";
	}
	
	public String getFileExtension() {
		return fileExtension;
	}

	@Override
	public void onClick(ClickEvent event) {
//		if (event.getSource() == googlePickerLink) {
//			googlePickerWidget.show(googlePickerSelectCommand, viewId, true);
//		}
//		else {
			mediaLibraryWidget.show(mediaLibrarySelectCommand);
//		}
	}
	
	@Override
	public void setStyleName(String style){
		mediaUrlTextBox.setStyleName(style);
	}

	@Override
	public void addStyleName(String style){
		mediaUrlTextBox.addStyleName(style);
	}
	
	public void addChangeHandler(ChangeHandler handler) {
		mediaUrlTextBox.addChangeHandler(handler);
	}
}
