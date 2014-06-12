// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.risevision.common.client.info.ImageItemInfo;
import com.risevision.common.client.info.PlaceholderInfo;
import com.risevision.common.client.info.PlaylistItemInfo;
import com.risevision.common.client.info.VideoItemInfo;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.utils.HtmlUtils;

public class ItemPreviewWidget extends SimplePanel {
	private PlaylistItemInfo item;
	private int width = 100, height = 100;
	
	private final String VIDEO_HTML = "" +
			"<html>" +
			"<head>" +
			"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" +
			"<title></title>" +
			"<script type=\"text/javascript\" src=\"../jwplayer/jwplayer.js\"></script>" +
			"<script>jwplayer.key=\"xBXyUtAJ+brFzwc2kNhDg/Sqk8W7rmktAYliYHzVgxo=\"</script>" +
			"<script type=\"text/javascript\" src=\"../scripts/videoScripts.js\"></script>" +
			"</head>" +
			"<body style=\"margin:0px;\">" +
			"<div id=\"flash\">Loading the player...</div>" +
			"<script language=\"javascript\">" +
			"loadVideo(\"%s1\", \"%s2\");" +
			"</script>" +
			"</body>" +
			"</html>";
	
	private final String IMAGE_HTML = "" +
			"<html>" +
			"<head>" +
			"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" +
			"<title></title>" +
			"<style>" +
			"	#cnt {width:100%; height:100%; background-color: %s3%;}" +
			"	#wrapper {width:100%; height:100%; display:table;}" +
			"	#cell {display:table-cell; vertical-align:middle;}" +
			"	#image {display: block; margin: auto;}" +
			"</style>" +
			"</head>" +
			"<body style=\"margin:0px;\">" +
			"<div id=\"cnt\">" +
			"<div id=\"wrapper\">" +
			"<div id=\"cell\">" +
			"<img id=\"image\" src=\"%s2%\">" +
			"</div>" +
			"</div>" +
			"</div>" +
			"<script type='text/javascript'>" +
			"(function() {" +
			"	var img = document.getElementById('image');" +
			"	img.onload = function() {" +
			"		resizeImage();" +
			"	};" +
			"}());" +
			"function resizeImage() {" +
			"	if (%s4%) {" +
			"		var width = Math.max(document.body.clientWidth, document.documentElement.clientWidth);" +
			"		var height = Math.max(document.body.clientHeight, document.documentElement.clientHeight);" +
			"		var img = document.getElementById('image');" +
			"		if(img.width/img.height < width/height) {" +
			"			img.style.width = 'auto';" +
			"			img.style.height = height;" +
			"		} else {" +
			"			img.style.width = '100%';" +
			"			img.style.height = 'auto';" +
			"		}" +
			"	}" +			
			"}" +
			"</script>" +
			"</body>" +
			"</html>";
	
	public ItemPreviewWidget(PlaceholderInfo placeholder) {
		if (placeholder != null) {
			if (placeholder.getBackgroundStyle() != null) {
				getElement().getStyle().setProperty("background", placeholder.getBackgroundStyle());
		        getElement().getStyle().setProperty("backgroundSize", placeholder.isBackgroundScaleToFit() ? "contain" : "");
			}
			
			if (placeholder.getPlaylistItems() != null && placeholder.getPlaylistItems().size() > 0) {
				item = placeholder.getPlaylistItems().get(0); 
			}
		}
	}
	
	public void onLoad() {
		if (DOM.getChildCount(getElement()) == 0) {
			renderItem();
		}
		
		resizeItem(width, height);
	}
	
	public void renderItem() {
		if (item != null && item.getType() != null && !item.getType().isEmpty()) {
			if (item.getType().equals(PlaylistItemInfo.TYPE_IMAGE)) {
				Frame textFrame = new Frame();
				textFrame.getElement().getStyle().setBorderWidth(0, Unit.PX);
				textFrame.getElement().setAttribute("scrolling", "no");
				
				add(textFrame);

				HtmlUtils.writeHtml(textFrame.getElement(), createImageHTML(item.getObjectData()));	
			}
			else if (item.getType().equals(PlaylistItemInfo.TYPE_VIDEO)) {
				Frame textFrame = new Frame();
				textFrame.getElement().getStyle().setBorderWidth(0, Unit.PX);
				textFrame.getElement().setAttribute("scrolling", "no");
				
				add(textFrame);

				HtmlUtils.writeHtml(textFrame.getElement(), createVideoHTML(item.getObjectData()));	
			}
			else if (item.getType().equals(PlaylistItemInfo.TYPE_TEXT) || item.getType().equals(PlaylistItemInfo.TYPE_HTML)) {
				Frame textFrame = new Frame();
				textFrame.getElement().getStyle().setBorderWidth(0, Unit.PX);
				textFrame.getElement().setAttribute("scrolling", "no");
				
				add(textFrame);

				HtmlUtils.writeHtml(textFrame.getElement(), item.getObjectData());				
			}
//			if (item.getType().equals(PlaylistItemInfo.TYPE_GADGET) 
//					|| item.getType().equals(PlaylistItemInfo.TYPE_URL)
//					|| item.getType().equals(PlaylistItemInfo.TYPE_PRESENTATION)
//					|| item.getType().equals(PlaylistItemInfo.TYPE_WIDGET)) {
			else {
				HTML itemLabel = new HTML("<b>" + RiseUtils.capitalizeFirstLetter(item.getType()) + "</b><br>" + item.getName());
				itemLabel.getElement().getStyle().setWidth(100, Unit.PCT);
				itemLabel.getElement().getStyle().setHeight(100, Unit.PCT);
				itemLabel.getElement().getStyle().setProperty("textAlign", "center");

				add(itemLabel);
			}
		}
	}
	
	private String createImageHTML(String imageData) {
		ImageItemInfo imageItem = new ImageItemInfo(item.getObjectData());
			
		String htmlString = IMAGE_HTML;
//		htmlString = htmlString.replace("%s0%", Integer.toString(width));
//		htmlString = htmlString.replace("%s1%", Integer.toString(height));
		htmlString = htmlString.replace("%s2%", imageItem.getUrl());
		htmlString = htmlString.replace("%s3%", imageItem.getBackgroundColor());
		htmlString = htmlString.replace("%s4%", Boolean.toString(imageItem.isScaleToFit()));
		
		return htmlString;
	}
	
	private String createVideoHTML(String videoData) {
		VideoItemInfo videoItem = new VideoItemInfo(item.getObjectData());
			
		String htmlString = VIDEO_HTML.replace("%s1", videoItem.getVideoUrl());
		htmlString = htmlString.replace("%s2", videoItem.getVideoExtension());
		
		return htmlString;
	}
	
	private void resizeItem(int width, int height) {
		if (item != null && item.getType() != null && !item.getType().isEmpty()) {
			if (item.getType().equals(PlaylistItemInfo.TYPE_GADGET) || item.getType().equals(PlaylistItemInfo.TYPE_URL)
					|| item.getType().equals(PlaylistItemInfo.TYPE_PRESENTATION)) {
				//do nothing
			}
			else if (item.getType().equals(PlaylistItemInfo.TYPE_TEXT) || item.getType().equals(PlaylistItemInfo.TYPE_IMAGE)
					|| item.getType().equals(PlaylistItemInfo.TYPE_VIDEO) || item.getType().equals(PlaylistItemInfo.TYPE_HTML)) {
				// resize iframe
				if (DOM.getChild(getElement(), 0) != null) {
					DOM.getChild(getElement(), 0).getStyle().setWidth(width, Unit.PX);
					DOM.getChild(getElement(), 0).getStyle().setHeight(height, Unit.PX);
					
					if (item.getType().equals(PlaylistItemInfo.TYPE_IMAGE)) {
						resizeImage(DOM.getChild(getElement(), 0));
					}
				}
			}
		}
	}
	
	public void setPixelSize(int width, int height) {
		super.setPixelSize(width, height);
		
		this.width = width;
		this.height = height;
		
		if (isAttached()) {
			resizeItem(width, height);
		}
	}
	
	public static native void resizeImage(Element myFrame)  /*-{
		try {
//			debugger; 
			myFrame.contentWindow.resizeImage();
//			var el = (myFrame.contentWindow) ? myFrame.contentWindow : (myFrame.contentDocument.document) ? myFrame.contentDocument.document : myFrame.contentDocument;
//		    el.resizeImage(width, height);
		} catch (err) {}
	}-*/;

}
