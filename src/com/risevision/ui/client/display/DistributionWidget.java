package com.risevision.ui.client.display;

import java.util.ArrayList;

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

public class DistributionWidget extends HorizontalPanel{

	private CheckBox cbAllDisplays = new CheckBox("All Displays");

	private Anchor hlDistribution = new Anchor("Edit");
	private Label lbDescription = new Label();
	private ArrayList<String> distribution;
	
	private boolean allowEmptyDistribution;
	
	public DistributionWidget() {
		this(true);
	}
	
	public DistributionWidget(boolean allowEmptyDistribution) {
		this.allowEmptyDistribution = allowEmptyDistribution;
		
		styleControls();
		
		add(cbAllDisplays);
		add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
		add(hlDistribution);
		add(new HTML("&nbsp;&nbsp;"));
		add(lbDescription);
		
		final Command onSaveCallBack = new Command() {
			public void execute() {
				setDistributionToAll(false);
				setDistribution(DistributionSelectWidget.getInstance().getDistribution());
			}
		};
		
		hlDistribution.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DistributionSelectWidget.getInstance().show(getDistribution(), onSaveCallBack);
			}
		});
		
		cbAllDisplays.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				updateDescription();
			}
		});
	}

	private void styleControls() {
		this.setVerticalAlignment(ALIGN_MIDDLE);
		cbAllDisplays.setStyleName("rdn-CheckBox");
		cbAllDisplays.getElement().getStyle().setProperty("whiteSpace", "nowrap");
	}

	public void setDistribution(ArrayList<String> distribution) {
		this.distribution = distribution;

		if (!allowEmptyDistribution) {
			setDistributionToAll(distribution == null || distribution.isEmpty());
		}
		else {
			updateDescription();
		}
	}

	public ArrayList<String> getDistribution() {
		if (!allowEmptyDistribution && cbAllDisplays.getValue()) {
			return new ArrayList<String>();
		}
		else {	
			return distribution;
		}
	}

	public void setDistributionToAll(boolean distributionToAll) {
		cbAllDisplays.setValue(distributionToAll);
		updateDescription();
	}

	public boolean getDistributionToAll() {
		return cbAllDisplays.getValue();
	}
	
	private void updateDescription() {
		String numberOfSelectedDisplays = "0";
		if (distribution != null)
			numberOfSelectedDisplays = Integer.toString(distribution.size());
		
		lbDescription.setText("(" + numberOfSelectedDisplays + " Displays Selected)");
		lbDescription.setVisible(!cbAllDisplays.getValue());				
	}

}