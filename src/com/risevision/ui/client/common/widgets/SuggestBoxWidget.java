package com.risevision.ui.client.common.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.TextBox;

public class SuggestBoxWidget extends TextBox{
	private String messageString;
	private boolean grayedOut;
	
	public SuggestBoxWidget(String messageString) {
		super();
		
		this.messageString = messageString;
		setText("");
		
		addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				setFocusEvent(true);
			}
		});
		
		addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				setFocusEvent(false);
			}
		});
	}
	
	private void setFocusEvent(boolean focus) {
		if (focus) {
			if (grayedOut) {
				grayedOut = false;
				setGrayedOut();
				super.setText("");
			}
		}
		else
			if (getText().isEmpty()) {
				setText("");
			}
	}
	
	public void setText(String text) {
		if (text == null || text.isEmpty()) {
			grayedOut = true;
			super.setText(messageString);
			setGrayedOut();
		}
		else {
			grayedOut = false;
			super.setText(text);
			setGrayedOut();
		}
	}
	
	public String getText() {
		if (messageString != null && super.getText().equals(messageString)) {
			return "";
		}
		return super.getText();
	}

	private void setGrayedOut() {
		if (grayedOut) {
			getElement().getStyle().setColor("gray");
		}
		else {
			getElement().getStyle().setColor("black");
		}
	}
	
}