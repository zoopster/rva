// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.background;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.colorPicker.ColorPickerTextBox;
import com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerViewId;
import com.risevision.ui.client.common.widgets.mediaLibrary.MediaLibraryTextBox;

public class BackgroundDialog extends PopupPanel /* implements ChangeHandler */ {
	private static BackgroundDialog instance;
    
    private static final String[][] repeatOptions = {{"repeat", "Vertical and Horizontal"},
    									{"repeat-x", "Horizontal"},
    									{"repeat-y", "Vertical"},
    									{"no-repeat", "None"}};
    
    private static final String[][] positionOptions = {{"left top", "Top Left"},
								    	{"center top", "Top"},
								    	{"right top", "Top Right"},
								    	{"left center", "Left"},
								    	{"center center", "Center"},
								    	{"right center", "Right"},
								    	{"left bottom", "Bottom Left"},
								    	{"center bottom", "Bottom"},
										{"right bottom", "Bottom Right"}};

        
    // Define the buttons
    HorizontalPanel buttonPanel = new HorizontalPanel();
    Button okButton = new Button("Save");   // ok button
    Button cancelButton = new Button("Cancel");   // cancel button
    	
//    private BackgroundPreview previewWidget = BackgroundPreview.getInstance();
	
	private Grid mainGrid = new Grid(5, 2);
	private int row = -1;
    
	private ColorPickerTextBox colorTextBox = new ColorPickerTextBox();
	private MediaLibraryTextBox urlTextBox = new MediaLibraryTextBox(GooglePickerViewId.DOCS_IMAGES);
	private RiseListBox positionListBox = new RiseListBox();
	private CheckBox scaleToFitCheckBox = new CheckBox();
	private RiseListBox repeatListBox = new RiseListBox();

//	private Label cssLabel = new Label();
//	private Anchor showCssLink = new Anchor("Edit");
//	private TextBox cssTextBox = new TextBox();
	
	private Command onClose;
	
	public static BackgroundDialog getInstance() {
		try {
			if (instance == null)
				instance = new BackgroundDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
    public BackgroundDialog() {
		super(false, true); //set auto-hide and modal
        
        buttonPanel.add(okButton);
        buttonPanel.add(new SpacerWidget());
        buttonPanel.add(cancelButton);
        
//    	HorizontalPanel cssPanel = new HorizontalPanel();
//		cssPanel.add(cssLabel);
//		cssPanel.add(cssTextBox);
//		cssPanel.add(new SpacerWidget());
//		cssPanel.add(showCssLink);
        
        gridAdd("Background Color:", colorTextBox, "rdn-TextBoxMedium");
        gridAdd("Background Image URL:", urlTextBox, "rdn-TextBoxLong");
        gridAdd("Image Position:", positionListBox, "rdn-ListBoxMedium");
        gridAdd("Scale To Fit:", scaleToFitCheckBox, "rdn-CheckBox");
        gridAdd("Image Repeat:", repeatListBox, "rdn-ListBoxMedium");
//        gridAdd("CSS Style:", cssPanel, "");

//        mainGrid.getFlexCellFormatter().setColSpan(4, 1, 2);
//        mainGrid.getFlexCellFormatter().setRowSpan(0, 2, 4);
//        mainGrid.getCellFormatter().setWidth(0, 2, "100%");
//        mainGrid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
//        mainGrid.setWidget(0, 2, previewWidget);

        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.add(mainGrid);
        mainPanel.add(buttonPanel);
        
        setWidget(mainPanel);
        
        updateTextBoxes();
        styleControls();
        initButtons();
        initHandlers();
    }
    
    private void updateTextBoxes() {
    	for (String[] s : positionOptions) {
    		positionListBox.addItem(s[1], s[0]);
    	}
    	
    	for (String[] s : repeatOptions) {
    		repeatListBox.addItem(s[1], s[0]);
    	}
    }
    
    private void styleControls() {
		mainGrid.setCellSpacing(0);
		mainGrid.setCellPadding(0);
		mainGrid.setStyleName("rdn-Table");
		
//		cssLabel.addStyleName("rdn-OverflowElipsis");
//		cssLabel.setWidth("420px");
//		cssTextBox.setStyleName("rdn-TextBoxLong");
		
		getElement().getStyle().setProperty("padding", "10px");
	
        buttonPanel.addStyleName("rdn-VerticalSpacer");
    }
    
    private void initButtons() {
        okButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
            	if (onClose != null) {
            		onClose.execute();
            	}
                hide();
            }
        });
        
        cancelButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                hide();
            }
        });
        
//		showCssLink.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				showCssTextBox(true);
//			}
//		});
    }
	
//	protected void showCssTextBox(boolean visible) {
//		showCssLink.setVisible(!visible);
//		cssLabel.setVisible(!visible);
//		cssTextBox.setVisible(visible);
//		cssLabel.setText(cssTextBox.getText());
//	}
    
	private void initHandlers() {
//		cssTextBox.addChangeHandler(new ChangeHandler() {
//			@Override
//			public void onChange(ChangeEvent event) {
//				bindData(cssTextBox.getText());
//			}
//		});
		
//		urlTextBox.addChangeHandler(this);
//		colorTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {
//
//			@Override
//			public void onValueChange(ValueChangeEvent<String> event) {
//				saveData();
//			}
//		});
		
//		positionListBox.addChangeHandler(this);
//		repeatListBox.addChangeHandler(this);
		
		scaleToFitCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				repeatListBox.setEnabled(!event.getValue());
				if (event.getValue()) {
					repeatListBox.setSelectedValue("no-repeat");
				}
//				saveData();
			}
		});
	}
	
    public String getStyle() {
    	return saveData();
    	
//    	return cssTextBox.getText();
    }
    
    public boolean isScaleToFit() {
    	return scaleToFitCheckBox.getValue();
    }
    
    public void show(Command onClose, String style, boolean scaleToFit) {
    	super.show();
    	this.onClose = onClose;
    	clearData();
    	bindData(style, scaleToFit);
//    	showCssTextBox(false);
    	
    	super.center();
    }
    
//	@Override
//	public void onChange(ChangeEvent event) {
//		saveData();
//	}
	
	private void clearData() {
        colorTextBox.setText("");
        urlTextBox.setText("");
        positionListBox.setSelectedValue("left top");
        scaleToFitCheckBox.setValue(true);
        repeatListBox.setSelectedValue("no-repeat");
        repeatListBox.setEnabled(false);
//        cssTextBox.setText("");
//        cssLabel.setText("");
        
//        previewWidget.setPreview("", true);
	}
    
//	private void bindData(String style) {
//		bindData(style, scaleToFitCheckBox.getValue());
//	}
	
    private void bindData(String style, boolean scaleToFit) {
//    	style = "#ffffff url('/images/preview-opacity.png') repeat-x right top";
    	
    	if (style != null && !style.isEmpty()) {
    		
    		// Extract URL at the beginning and remove
    		// Otherwise spaces in the URL cause split(" ") to fail
    		String imageUrl = getImageUrl(style);
    		if (!imageUrl.isEmpty()) {
    			style = style.replace(imageUrl, "");
    		}
    		
	    	String[] styleTokens = style.split(" ");
	    	// tokens have to be in order
	    	String token1 = null;
	    	int location = styleTokens.length - 1;
	    	// see if last token is a position token
	    	if (location >= 0) {
	    		for (String[] position: positionOptions) {
	    			if (position[0].contains(styleTokens[location].toLowerCase())) {
	    				token1 = styleTokens[location].toLowerCase();
	    				location--;
	    				break;
	    			}
	    		}
	    	}
	   
	    	// see if second last token is a position token
	    	if (token1 != null && location >= 0) {
	    		boolean found = false;
	    		if ("center".equals(styleTokens[location].toLowerCase())) {
	    			positionListBox.setSelectedValue("center center");
	    			location--;
	    		}
	    		else {
		    		for (String[] position: positionOptions) {
		    			if (position[0].contains(token1) && position[0].contains(styleTokens[location].toLowerCase())) {
		    				positionListBox.setSelectedValue(position[0]);
		    				location--;
		    				found = true;
		    				break;
		    			}
		    		}
		    		if (!found) {
		        		for (String[] position: positionOptions) {
		        			if (position[0].contains("center") && position[0].contains(styleTokens[location].toLowerCase())) {
		        				positionListBox.setSelectedValue(position[0]);
		        				location--;
		        				break;
		        			}
		        		}
		    		}
	    		}
	    	}
	    	
	    	// search for repeat token
	    	if (location >= 0) {
	    		for (String[] repeat: repeatOptions) {
	    			if (repeat[0].contains(styleTokens[location].toLowerCase())) {
	    				repeatListBox.setSelectedValue(repeat[0]);
	    				location--;
	    			}
	    		}
	    	}
	    	
	    	// search for url token
	    	if (location >= 0) {
				if (!imageUrl.isEmpty()) {
			    	urlTextBox.setText(imageUrl);
					location--;
	    		}
	    	}
	    	
	    	// first token has to be color (if available)
	    	if (location >= 0) {
	    		String color = "";
	    		for (int i = 0; i <= location; i++) {
	    			color = color + styleTokens[i] + " ";
	    		}
	    		colorTextBox.setText(color.trim());
	    	}
	
	    	scaleToFitCheckBox.setValue(scaleToFit);
	    	repeatListBox.setEnabled(!scaleToFit);
	    	
//	    	cssTextBox.setText(style);
//	    	cssLabel.setText(style);
//			previewWidget.setPreview(style, scaleToFitCheckBox.getValue());
    	}
    }
    
    private String saveData() {   	
    	String properties = "";

    	if (!colorTextBox.getText().isEmpty()) {
    		properties = colorTextBox.getText() + " ";
    	}
    	
    	if (!urlTextBox.getText().isEmpty()) {
    		properties = properties + "url('" + urlTextBox.getText() + "') ";
    		
        	properties = properties + repeatListBox.getSelectedValue() + " ";
    		properties = properties + positionListBox.getSelectedValue();
    	}
    	
    	properties = properties.trim();

    	return properties;
    	
//    	cssTextBox.setText(properties);
//    	cssLabel.setText(properties);
//		previewWidget.setPreview(properties, scaleToFitCheckBox.getValue());
    }
    
    // CSS example: url('/images/preview-opacity.png') 
    private static String getImageUrl(String cssStyle) {
		int i = cssStyle.indexOf("url");
	    if (i != -1) {
    		i = cssStyle.indexOf("(", i);
    		if (i != -1) {
        		int j = cssStyle.lastIndexOf(")");
        		if (j != -1) {
        			return cssStyle.substring(i + 1, j).replace('\"', ' ').replace('\'', ' ').trim();
        		}
    		}
    	}
    	
    	return "";
    }

	private void gridAdd(String label, Widget widget, String styleName) {
		row++;
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		mainGrid.setText(row, 0, label);
		if (widget != null)
		{
			mainGrid.setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}

    public String getLabelText(String style, boolean scaleToFit) {
    	String returnString = "";

    	if (style != null && !style.isEmpty()) {
    		bindData(style, scaleToFit);
    		
        	if (!colorTextBox.getText().isEmpty()) {
        		returnString = colorTextBox.getText();
        	}
        	
        	if (!urlTextBox.getText().isEmpty()) {
        		if (!returnString.isEmpty())
        			returnString = returnString + ", ";
        		returnString = returnString + "Image, ";
        	
				returnString = returnString + repeatListBox.getItemText(repeatListBox.getSelectedIndex()) + ", ";
				returnString = returnString + positionListBox.getItemText(positionListBox.getSelectedIndex());
        	}
        	
        	return returnString;
    	}
    	
    	return "None";
    }
	
}
