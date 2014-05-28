// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.colorPicker;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
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
        getElement().getStyle().setProperty("backgroundImage", "url(/images/preview-opacity.png)");
        getElement().getStyle().setProperty("border", "1px solid black");

        previewEl = DOM.createDiv();
        previewEl.getStyle().setProperty("cursor", "default");
        previewEl.getStyle().setProperty("width", "100%");
        previewEl.getStyle().setProperty("height", "100%");

        DOM.appendChild(getElement(), previewEl);
        setPreview("ff0000");
	}
	
	public void setCommand(Command command) {
		this.colorChanged = command;
	}
	
    public void setPreview(String color) {
        previewEl.getStyle().setProperty("backgroundColor", color);
        
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
