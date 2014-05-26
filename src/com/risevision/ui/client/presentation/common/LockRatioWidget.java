// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.common;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
//import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.risevision.common.client.utils.RiseUtils;

public class LockRatioWidget extends CheckBox{
	private TextBox heightTextBox, widthTextBox;
	private double heightValue, widthValue;
	private ChangeHandler heightChangeHandler, widthChangeHandler;
//	private HandlerRegistration heightHandler, widthHandler;
	
	public LockRatioWidget(String label){
	    super(label);
	}
	
	public void initWidget(TextBox heightTextBox, TextBox widthTextBox){
		this.heightTextBox = heightTextBox;
		this.widthTextBox = widthTextBox;
		
		initHandlers();
	}
	
	private void initHandlers(){
		addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue() && isVisible()){
					initValues();
				}
//				else{
//					removeChangeHandlers();
//				}
			}
		});
		
		widthChangeHandler = new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (getValue() && isVisible())
					widthChanged();
			}
		};
		
		heightChangeHandler = new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (getValue() && isVisible())
					heightChanged();
			}
		};
		
		heightTextBox.addChangeHandler(heightChangeHandler);
		widthTextBox.addChangeHandler(widthChangeHandler);
		
		initValues();
	}
	
	private void initValues(){
		if (heightTextBox != null && widthTextBox != null){
//			heightHandler = heightTextBox.addChangeHandler(heightChangeHandler);
//			widthHandler = widthTextBox.addChangeHandler(widthChangeHandler);
			
			widthValue = RiseUtils.strToInt(widthTextBox.getValue(), 0);
			heightValue = RiseUtils.strToInt(heightTextBox.getValue(), 0);
		}
	}
	
//	private void removeChangeHandlers(){
//		try{
//			heightHandler.removeHandler();
//			widthHandler.removeHandler();
//		}
//		catch(Exception e){
//			
//		}
//	}
	
	private void widthChanged(){
		if (widthValue != 0 && heightValue != 0){
			int width = RiseUtils.strToInt(widthTextBox.getValue(), 0);
			if (width != 0) { 
				heightValue = (int) ((heightValue/widthValue) * width);
				widthValue = width;
				heightTextBox.setValue((int)heightValue + "");
			}
		}
		else
			widthValue = RiseUtils.strToInt(widthTextBox.getValue(), 0);
	}
	
	private void heightChanged(){
		if (widthValue != 0 && heightValue != 0){
			int height = RiseUtils.strToInt(heightTextBox.getValue(), 0);
			if (height != 0) { 
				widthValue = (int) ((widthValue/heightValue) * height);
				heightValue = height;
				widthTextBox.setValue((int)widthValue + "");
			}
		}
		else
			heightValue = RiseUtils.strToInt(heightTextBox.getValue(), 0);
	}
	
	public void setVisible(boolean visibility){
		super.setVisible(visibility);
		
		if (visibility && getValue()){
			initValues();
		}
//		else{
//			removeChangeHandlers();
//		}
	}

}
