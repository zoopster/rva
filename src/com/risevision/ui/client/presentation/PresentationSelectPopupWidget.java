// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;

public class PresentationSelectPopupWidget extends PopupPanel {
	private static PresentationSelectPopupWidget instance;
	private VerticalPanel selectPanel = new VerticalPanel();
	private Label titleLabel = new Label("Select Presentation");
	private PresentationSelectWidget presentationSelectWidget = new PresentationSelectWidget(false);
	private ActionsWidget actionWidget = new ActionsWidget();
	
	public PresentationSelectPopupWidget() {
		super(true, true);
		
		selectPanel.add(titleLabel);
		selectPanel.add(presentationSelectWidget);
		selectPanel.add(actionWidget);
		
		add(selectPanel);
		
		styleControls();
		initActions();
	}
	
	private void styleControls() {
		titleLabel.setStyleName("rdn-Head");
		
		setSize("600px", "100%");
		getElement().getStyle().setProperty("padding", "10px");
		
		actionWidget.addStyleName("rdn-VerticalSpacer");
	}
	
	private void initActions() {
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};	
		actionWidget.addAction("Cancel", cmdCancel);
	}
	
	public static PresentationSelectPopupWidget getInstance() {
		try {
			if (instance == null)
				instance = new PresentationSelectPopupWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		// Use the popup's key preview hooks to close the dialog when either
		// enter or escape is pressed.
		switch (key) {
			case KeyCodes.KEY_ESCAPE:
				hide();
				break;
			}

		return true;
	}
	
	public void load() {
		presentationSelectWidget.load();
	}
	
	public void show(Command selectCommand) {
		presentationSelectWidget.init(selectCommand);

		show();
		center();
	}
	
	private void doActionCancel() {
		hide();
	}
	
	public PresentationInfo getCurrentPresentation() {
		return presentationSelectWidget.getCurrentPresentation();
	}
}
