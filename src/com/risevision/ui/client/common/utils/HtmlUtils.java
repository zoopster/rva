package com.risevision.ui.client.common.utils;

import com.google.gwt.user.client.Element;

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
}
