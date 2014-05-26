// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.colorPicker;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;

public class ColorPickerPreview extends HTML {
	private static ColorPickerPreview instance;
	
	Element previewEl;
	Command colorChanged;
	
	public static ColorPickerPreview getInstance() {
		try {
			if (instance == null)
				instance = new ColorPickerPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public ColorPickerPreview() {
		super();
		
		// Color preview at the top
        setWidth("54px");
        setHeight("50px");
        DOM.setStyleAttribute(getElement(), "backgroundImage", "url(/images/preview-opacity.png)");
        DOM.setStyleAttribute(getElement(), "border", "1px solid black");

        previewEl = DOM.createDiv();
        DOM.setStyleAttribute(previewEl, "cursor", "default");
        DOM.setStyleAttribute(previewEl, "width", "100%");
        DOM.setStyleAttribute(previewEl, "height", "100%");

        DOM.appendChild(getElement(), previewEl);
        setPreview("ff0000");
	}
	
	public void setCommand(Command command) {
		this.colorChanged = command;
	}
	
    public void setPreview(String color) {
        DOM.setStyleAttribute(previewEl, "backgroundColor", color);
        
        if (colorChanged != null) {
        	colorChanged.execute();
        }
    }
    
    public Color getColor() {
    	return new Color(previewEl.getStyle().getBackgroundColor());
    }
    
//    public String getCssColor() {
//    	return getColor().getColor();
//    }
}
