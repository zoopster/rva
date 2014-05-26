package com.risevision.ui.client.common.widgets;

import com.google.gwt.user.client.ui.HTML;
import com.risevision.common.client.utils.RiseUtils;

public class TooltipLabelWidget extends HTML {
	private static String TOOLTIP_HTML = "<div class='tooltip' href='#'>%label%" +
			"<span>%tooltip%</span></div>";
	
	public TooltipLabelWidget() {
		super();
	}
	
	public TooltipLabelWidget(String label, String tooltip) {
		super();

		setTooltip(label, tooltip);
	}
	
	public void setTooltip(String label, String tooltip) {
		if (label == null) {
			setHTML("");
		}
		else if (RiseUtils.strIsNullOrEmpty(tooltip)) {
			setHTML(label);
		}
		else {
			String html = TOOLTIP_HTML;
			html = html.replace("%label%", label);
			html = html.replace("%tooltip%", tooltip);
			
			setHTML(html);
		}
	}
	
}