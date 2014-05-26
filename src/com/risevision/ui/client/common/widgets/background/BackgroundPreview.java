// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.background;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;

public class BackgroundPreview extends HTML {
	private static BackgroundPreview instance;
	Element previewEl;
	
	public static BackgroundPreview getInstance() {
		try {
			if (instance == null)
				instance = new BackgroundPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public BackgroundPreview() {
		super();
		
		// Color preview at the top
        setWidth("54px");
        setHeight("50px");
        DOM.setStyleAttribute(getElement(), "border", "1px solid black");

        previewEl = DOM.createDiv();
        DOM.setStyleAttribute(previewEl, "cursor", "default");
        DOM.setStyleAttribute(previewEl, "width", "100%");
        DOM.setStyleAttribute(previewEl, "height", "100%");

        DOM.appendChild(getElement(), previewEl);
	}
	
    public void setPreview(String style, boolean scaleToFit) {
        DOM.setStyleAttribute(previewEl, "background", style);
        DOM.setStyleAttribute(previewEl, "backgroundSize", scaleToFit ? "contain" : "");
    }
}
