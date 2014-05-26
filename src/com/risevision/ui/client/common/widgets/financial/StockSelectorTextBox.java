// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.financial;

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

public class StockSelectorTextBox extends Composite implements HasText, ClickHandler {
	private TextBox instrumentsTextBox = new TextBox();
	private final String buttonName = "Stock Selector";
	private Anchor stockSelectorLink = new Anchor("<span style='white-space:nowrap;'>" + buttonName + "</span>", true);
	
	private InstrumentListWidget itemsWidget = InstrumentListWidget.getInstance();

	private Command selectCommand;
	
	public StockSelectorTextBox() {
		HorizontalPanel panel = new HorizontalPanel();
		        
		panel.add(instrumentsTextBox);
		panel.add(new SpacerWidget());
		panel.add(stockSelectorLink);
		
		stockSelectorLink.addClickHandler(this);
		selectCommand = new Command() {
			@Override
			public void execute() {
				setText(itemsWidget.getItemsList());
			}
		};
		
		initWidget(panel);
	}
	
	protected void onLoad() {
//		stockSelectorLink.setVisible(SelectedCompanyController.getInstance().isSelectedCompanyFeature(EnabledFeaturesInfo.FEATURE_MEDIA_LIBRARY));
	}
	
	@Override
	public String getText() {
		return instrumentsTextBox.getText();
	}

	@Override
	public void setText(String text) {
		instrumentsTextBox.setValue(text);
	}

	@Override
	public void onClick(ClickEvent event) {
		itemsWidget.show(selectCommand, instrumentsTextBox.getText());
	}
	
	@Override
	public void setStyleName(String style){
		instrumentsTextBox.setStyleName(style);
	}

	@Override
	public void addStyleName(String style){
		instrumentsTextBox.addStyleName(style);
	}
	
	public void addChangeHandler(ChangeHandler handler) {
		instrumentsTextBox.addChangeHandler(handler);
	}
}
