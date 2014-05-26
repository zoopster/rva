// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.timeline;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.risevision.common.client.info.TimeLineInfo;

public class TimelineWidget extends HorizontalPanel {

	private CheckBox cbAlways = new CheckBox("Always");
	private Anchor hlTimeline = new Anchor("Edit");
	private Label lbDescription = new Label("");
	private TimeLineInfo timeline;

	public TimelineWidget() {
		styleControls();

		add(cbAlways);
		add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
		add(hlTimeline);
		add(new HTML("&nbsp;&nbsp;"));
		add(lbDescription);

		final Command onSaveCallBack = new Command() {
			public void execute() {
				setTimeline(TimelineSelectWidget.getInstance().getTimeline());
			}
		};

		hlTimeline.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				TimelineSelectWidget.getInstance().show(getTimeline(), onSaveCallBack);
			}
		});
		
		cbAlways.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				lbDescription.setText(timeline.getDescription(!cbAlways.getValue()));
			}
		});

	}

	private void styleControls() {
		this.setVerticalAlignment(ALIGN_MIDDLE);
		cbAlways.setStyleName("rdn-CheckBox");
		cbAlways.getElement().getStyle().setProperty("whiteSpace", "nowrap");
	}

	public void setTimeline(TimeLineInfo timeline) {
		if (timeline == null)
			timeline = new TimeLineInfo();
		
		timeline.parseDatesFromString();
		this.timeline = timeline;
		cbAlways.setValue(!timeline.getUseSchedule());
		lbDescription.setText(timeline.getDescription());
	}

	public TimeLineInfo getTimeline() {
		timeline.setUseSchedule(!cbAlways.getValue());
		timeline.updateDatesToString();
		return timeline;
	}
	
}