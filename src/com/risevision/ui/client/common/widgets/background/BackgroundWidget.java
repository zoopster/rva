// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.background;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class BackgroundWidget extends Composite implements ClickHandler {
	private String propertyValue;
	private boolean scaleToFit;
	private Label styleLabel = new Label();
	private Anchor editLink = new Anchor("Edit");
	private BackgroundDialog backgroundDialog = BackgroundDialog.getInstance();
    private Command hideCommand;
	
	public BackgroundWidget() {		
		HorizontalPanel panel = new HorizontalPanel();
		
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        styleLabel.addStyleName("rdn-OverflowElipsis");
        styleLabel.setWidth("175px");
        
		panel.add(styleLabel);
		panel.add(new SpacerWidget());
		panel.add(editLink);
		
		editLink.addClickHandler(this);
		hideCommand = new Command() {
			@Override
			public void execute() {
				init(backgroundDialog.getStyle(), backgroundDialog.isScaleToFit());
			}
		};
		
		initWidget(panel);
	}
	
	public String getStyle() {
		return propertyValue;
	}

	public void init(String text, boolean scaleToFit) {
		propertyValue = text;
		this.scaleToFit = scaleToFit;
		
		styleLabel.setText(backgroundDialog.getLabelText(text, scaleToFit));
	}
	
	public boolean isScaleToFit() {
		return scaleToFit;
	}

	@Override
	public void onClick(ClickEvent event) {
		backgroundDialog.show(hideCommand, propertyValue, scaleToFit);
	}

}
