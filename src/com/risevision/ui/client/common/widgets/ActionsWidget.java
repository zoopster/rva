// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class ActionsWidget extends HorizontalPanel implements ClickHandler {
	private static ActionsWidget instance;	
	/*
	 * Be careful with saving UI components in datastructures like this: if you
	 * remove a button from the app, make sure you also remove its reference
	 * from buttonMap HashMap to avoid memory leaks.
	 */
	private Map<ButtonBase, Command> buttonMap = new HashMap<ButtonBase, Command>();

	public static ActionsWidget getInstance() {
		try {

			if (instance == null)
				instance = new ActionsWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	

	public ActionsWidget() {
		this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void addAction(String imageUrl, String title, Command command) {
		Image image = new Image(imageUrl);
		image.setHeight("22px");
		image.setWidth("22px");
		
		PushButton button = new PushButton(image);		
		
		button.addClickHandler(this);
		button.setTitle(title);
		
		button.getElement().setClassName("simple-PushButton");
		
		addButton(button);
		buttonMap.put(button, command);
	}
	
	public void addAction(String text, Command command) {
		Button button = new Button("<span style='white-space:nowrap;'>" + text + "</span>");
		button.addClickHandler(this);

		addButton(button);
		buttonMap.put(button, command);
	}
	
	public void addAction(ButtonBase button) {
		addButton(button);
		buttonMap.put(button, null);
	}
	
	private void addButton(ButtonBase button) {
		if (buttonMap.size() != 0) {
			button.getElement().getStyle().setMarginLeft(6, Unit.PX);
		}
		add(button);
	}
	
	public void clearActions(){
		buttonMap.clear();
		clear();
	}
	
	public void setEnabled(boolean enabled) {
		for (ButtonBase button : buttonMap.keySet()){
			button.setEnabled(enabled);
		}
	}
	
	public void setEnabled(boolean enabled, String text){
		for (ButtonBase button : buttonMap.keySet()){
			if (button.getText().equals(text))
				button.setEnabled(enabled);
		}
	}
	
	public void setVisible(boolean visible, String text){
		for (ButtonBase button : buttonMap.keySet()){
			if (button.getText().equals(text))
				button.setVisible(visible);
		}
	}
	
	// The shared ClickHandler code.
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if (sender instanceof ButtonBase) {
			ButtonBase b = (ButtonBase) sender;
			Command command = buttonMap.get(b);
			if (command != null) {
				command.execute();
			}
		}
	}
}
