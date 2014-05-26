package com.risevision.ui.client.common.widgets.textStyle;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.risevision.ui.client.common.utils.HtmlUtils;

public class TextStylePreview extends HTML {
	private static TextStylePreview instance;
	private static final String frameHtml = "" +
		"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">" +
		"<html>" +
		"<head>" +
		"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" +
		"<title></title>" +
		"<style>%css%</style>" +
		"</head>" +
		"<body style=\"height:100%;width:100%; margin: 0; overflow: hidden;text-align:center;\" >" +
		"<div class=\"%class%\">AaBb</div>" +
		"</body>" +
		"</html>"; 
	
	Element previewEl;
	
	public static TextStylePreview getInstance() {
		try {
			if (instance == null)
				instance = new TextStylePreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public TextStylePreview() {
		super();
		
		// Color preview at the top
        setWidth("108px");
        setHeight("50px");
        DOM.setStyleAttribute(getElement(), "border", "1px solid black");
	}

    public void setPreview(String font, String className) {
    	String finalHtml = frameHtml.replace("%css%", font);
    	finalHtml = finalHtml.replace("%class%", className);
    	
    	getElement().setInnerHTML("");
    	
        previewEl = DOM.createIFrame();
        previewEl.getStyle().setWidth(108, Unit.PX);
        previewEl.getStyle().setHeight(50, Unit.PX);
        previewEl.getStyle().setBorderWidth(0, Unit.PX);
        
        DOM.appendChild(getElement(), previewEl);
       
        HtmlUtils.writeHtml(previewEl, finalHtml);
    }
}
