// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.utils;

import com.google.gwt.dom.client.Element;

public class HtmlUtils {
	public static native void writeHtml(Element myFrame, String html)  /*-{
//		try {
//			debugger; 
			
			var el = (myFrame.contentWindow) ? myFrame.contentWindow : (myFrame.contentDocument.document) ? myFrame.contentDocument.document : myFrame.contentDocument;
		    el.document.open();
		    el.document.write(html);
		    el.document.close();
//		} catch (err) {}
	}-*/;

	public static native void removePageMargins(Element myFrame)  /*-{
//		try {
//			debugger; 
			
			var el = (myFrame.contentWindow) ? myFrame.contentWindow : (myFrame.contentDocument.document) ? myFrame.contentDocument.document : myFrame.contentDocument;
		    el.document.body.style["padding"] = 0;
		    el.document.body.style["margin"] = 0;
		    
//		} catch (err) {}
	}-*/;
	
}
