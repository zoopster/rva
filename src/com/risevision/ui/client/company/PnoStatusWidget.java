package com.risevision.ui.client.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.risevision.core.api.types.CompanyNetworkOperatorStatus;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class PnoStatusWidget extends HorizontalPanel {
	private Label statusLabel = new Label();
	private Anchor upgradeLink = new Anchor("Upgrade");
	
	private int status;
	
	public PnoStatusWidget() {
		add(statusLabel);
		add(new SpacerWidget());
		add(upgradeLink);
		
		initActions();
	}
	
	private void initActions() {
		upgradeLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (status == CompanyNetworkOperatorStatus.SUBSCRIBED) {
					status = CompanyNetworkOperatorStatus.NO;
				}
				else {
					status = CompanyNetworkOperatorStatus.SUBSCRIBED;
				}
				bindData();
			}
		});
	}

	public void setStatus(int status) {
		this.status = status;
		
		bindData();
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setEditable(boolean editable) {
		upgradeLink.setVisible(editable);
	}
	
	private void bindData() {
		if (status == CompanyNetworkOperatorStatus.SUBSCRIBED) {
			statusLabel.setText("Operator");
			upgradeLink.setText("Downgrade");
		}
		else {
			statusLabel.setText("Premium");
			upgradeLink.setText("Upgrade");
		}
	}

}