package com.risevision.ui.client.common.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DefaultSettingWidget extends Composite implements HasText {
	private HasText textBox;
	private Anchor defaultHyperlink = new Anchor("Default");
	private String defaultText = "";
	
	public DefaultSettingWidget() {
		this(new TextBox());
	}
	
	public DefaultSettingWidget(HasText textBox) {	
		this.textBox = textBox;
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add((Widget) textBox);
		mainPanel.setCellWidth((Widget) textBox, "450px");
		mainPanel.add(new HTML("<span style='padding-left:20px'></span>"));
		mainPanel.add(defaultHyperlink);

		initHandlers();
		
		initWidget(mainPanel);
	}
	
	private void initHandlers() {
		defaultHyperlink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (defaultText != null && !defaultText.isEmpty())
					textBox.setText(defaultText);
			}
		});
	}
	
	public void setStyleName(String styleName){
		// don't want to override Color Picker's original style
		((Widget)textBox).addStyleName(styleName);
	}
	
	public void setFocus(boolean focused) {
		if (textBox instanceof TextBox) {
			((TextBox)textBox).setFocus(focused);
		}
	}
	
	public String getText(){
		return textBox.getText();
	}
	
	public void setText(String text){
		if (textBox instanceof ColorPickerWidget) {
			((ColorPickerWidget) textBox).setColor(text);
		}
		else {
			textBox.setText(text); 
		}
	}
	
//	public Boolean isDefaultText() {
//		return !textBox.getText().isEmpty() && textBox.getText().equals(defaultText);
//	}
	
	public void setDefaultText(String text){
		defaultText = text;
//		if (textBox.getText() == null || textBox.getText().isEmpty()){
//			textBox.setText(text);
//		}
	}
}
