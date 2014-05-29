// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.iframe;

import com.google.gwt.dom.client.Element;
import com.risevision.common.client.utils.RiseUtils;

public abstract class RpcDialogBoxWidget extends IFramePanelWidget {	
	protected static final String HTML_STRING = "<html>" +
			"<head>" +
			"<meta http-equiv='content-type' content='text/html; charset=UTF-8'>" +
//			"<script type='text/javascript' language='javascript' src='http://ig.gmodules.com/gadgets/js/rpc.js'></script>" +
//			"<script type='text/javascript' language='javascript' src='/gadgets/globals.js'></script>" +
//			"<script type='text/javascript' language='javascript' src='/gadgets/urlparams.js'></script>" +
//			"<script type='text/javascript' language='javascript' src='/gadgets/config.js'></script>" +
//			"<script type='text/javascript' language='javascript' src='/gadgets/wpm.transport.js'></script>" +
//			"<script type='text/javascript' language='javascript' src='/gadgets/rpc.js'></script>" +
			"<script type='text/javascript' language='javascript' src='/gadgets/gadgets.min.js'></script>"
			+ "<script>"
			+ "function setUrl(url) {"
			+ "	var el = document.getElementById('if_divEditor');"
			+ "	if (el) el.src = url;"
			+ "}"
			+ "</script>"
			+ "%scripts%" +
			"</head>" +
			"<body style='margin:0px;'>" +
			"	<div id='divEditor' name='divEditor' style='width: 100%; height: 100%;'>" +
			"		<iframe id='if_divEditor' name='if_divEditor' allowTransparency='true' " +
			"			style='display:block;position:absolute;height:100%;width:100%;' " +
			"			frameborder=0 scrolling='no'>" +
			"		</iframe>" +
			"	</div>" +
			"</body>" +
			"</html>" +
			"";
	
	protected void init(String externalScript) {
		String htmlString = HTML_STRING.replace("%scripts%", externalScript);
		
		super.init(htmlString);
	}
	
	public void show(String url) {
		if (!RiseUtils.strIsNullOrEmpty(url)) {
			setUrlNative(iFrameElement.getElement(), url);
		}
		
		show();
	}
	
	private native void setUrlNative(Element iFrame, String url) /*-{	

		debugger;
		if (iFrame.contentWindow && iFrame.contentWindow.setUrl) {
			iFrame.contentWindow.setUrl(url);
		}

	}-*/;

}