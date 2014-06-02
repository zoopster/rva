// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import com.google.gwt.user.client.ui.TextBox;

public class ColorPickerWidget extends TextBox {
	private String id;
	
	public ColorPickerWidget(String id) {
		super();

		this.id = id;
		this.getElement().setAttribute("id", id);
		
		this.setStyleName("color {required:false, pickerPosition:'right', hash:true, adjust:false}");
	}
	
	protected void onLoad() {
		super.onLoad();
		
		init();
	}
	
	private native void init() /*-{
		$wnd.jscolor.init();
	}-*/;
	
	public void setColor(String color) {
		if (color != null)
			setColorNative(color);
	}
	
	private native void setColorNative(String color) /*-{
		var id = this.@com.risevision.ui.client.common.widgets.ColorPickerWidget::id;
		var picker = $wnd.document.getElementById(id);
		
		picker.color.fromString(color);
	}-*/;

}





