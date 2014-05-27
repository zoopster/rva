// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.textStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class TextStyleWidget extends Composite implements HasText, ClickHandler {
	private String propertyId, propertyValue;
	private Label styleLabel = new Label();
	private HTML editStyleButton = new HTML();
	private TextStyleDialog styleDialog = TextStyleDialog.getInstance();
    private Command hideCommand;
	
	public TextStyleWidget(String propertyId) {
		this.propertyId = propertyId;
		
		HorizontalPanel panel = new HorizontalPanel();
		
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        editStyleButton.getElement().getStyle().setProperty("backgroundImage", "url(/images/font.png)");
        editStyleButton.getElement().getStyle().setProperty("cursor", "hand");
        editStyleButton.setHeight("16px");
        editStyleButton.setWidth("16px");
        
        styleLabel.addStyleName("rdn-OverflowElipsis");
        styleLabel.setWidth("400px");
        
		panel.add(styleLabel);
		panel.add(new SpacerWidget());
		panel.add(editStyleButton);
		
		editStyleButton.addClickHandler(this);
		hideCommand = new Command() {
			@Override
			public void execute() {
				setText(styleDialog.getStyle());
			}
		};
		
		initWidget(panel);
	}
	
	@Override
	public String getText() {
		return propertyValue;
	}

	@Override
	public void setText(String text) {
		propertyValue = text;
		
		styleLabel.setText(TextStyleDialog.getLabelText(text, propertyId));
	}

	@Override
	public void onClick(ClickEvent event) {
		styleDialog.show(hideCommand, propertyValue, propertyId);
	}

}
