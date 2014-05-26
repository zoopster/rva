package com.risevision.ui.client.presentation.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.service.PresentationService;
import com.risevision.ui.client.common.service.PresentationServiceAsync;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.presentation.PresentationSelectPopupWidget;

public class PresentationSelectorWidget extends HorizontalPanel {
	private String selectedPresentationId;
	private String defaultPresentationId;
	
	// are Default/Delete buttons enabled
	private boolean enableDefault;
	
	//RPC
	private final PresentationServiceAsync presentationService = GWT.create(PresentationService.class);
	// UI Pieces
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private Label presentationLabel = new Label();
	private Anchor presentationChangeLink = new Anchor("Change");
	private Anchor presentationDeleteLink = new Anchor("Delete");
	private Anchor presentationDefaultLink = new Anchor("Default");
	private PresentationSelectPopupWidget selectWidget = PresentationSelectPopupWidget.getInstance();

	private Command selectCommand;
	
	public PresentationSelectorWidget() {
		this(true);
	}
	
	public PresentationSelectorWidget(boolean enableDefault) {
		this.enableDefault = enableDefault;
		
		if (enableDefault) {
			HorizontalPanel firstPanel = new HorizontalPanel();
	
			firstPanel.add(presentationLabel);
			firstPanel.setCellWidth(presentationLabel, "100%");
			presentationLabel.setWidth("275px");
			
			firstPanel.add(presentationChangeLink);
			firstPanel.add(new HTML("<span style='padding-left:20px'></span>"));
			firstPanel.add(presentationDeleteLink);
	
			add(firstPanel);
			setCellWidth(firstPanel, "450px");
			add(new HTML("<span style='padding-left:20px'></span>"));
			add(presentationDefaultLink);
		}
		else {
			add(presentationLabel);
			presentationLabel.setWidth("175px");

			add(new SpacerWidget());
			add(presentationChangeLink);
			setCellWidth(presentationChangeLink, "100%");
		}
		
		initActions();
		styleControls();
	}
	
	private void initActions() {
		selectCommand = new Command() {
			public void execute() {
				contentSelected();
			}
		};
		
		presentationChangeLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showSelectWidget();
			}
		});
		
		presentationDeleteLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectedPresentationId = "";
				loadPresentationName();
			}
		});
		
		presentationDefaultLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (defaultPresentationId != null && !defaultPresentationId.isEmpty()) {
					selectedPresentationId = defaultPresentationId;
					loadPresentationName();
				}
			}
		});
	}
	
	private void styleControls() {
		presentationLabel.addStyleName("rdn-OverflowElipsis");
	}

	public void init(String presentationId) {	
//		if (presentationId != null && !presentationId.isEmpty()) {
			selectedPresentationId = presentationId;
			loadPresentationName();
//		}
//		else {
//			presentationLabel.setText("None");
//		}
	}

	private void showSelectWidget() {
		selectWidget.show(selectCommand);
	}
	
	private void contentSelected() {		
		PresentationInfo presentation = selectWidget.getCurrentPresentation();
		
		if (presentation != null) {
			selectedPresentationId = presentation.getId();
			presentationLabel.setText(presentation.getName());
		}

		selectWidget.hide();
	}
	
	private void loadPresentationName(){
		if (selectedPresentationId != null && !selectedPresentationId.isEmpty() && selectedPresentationId.equals(defaultPresentationId)) {
			presentationLabel.setText("Default presentation");
		}
		else if (selectedPresentationId != null && !selectedPresentationId.isEmpty()) {
			statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
			presentationService.getPresentation(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
					selectedPresentationId, new RpcCallBackHandler());
		}
		else {
			presentationLabel.setText("None");
		}
	}
	
	class RpcCallBackHandler implements AsyncCallback<PresentationInfo> {
		public void onFailure(Throwable caught) {
			if (caught instanceof ServiceFailedException && ((ServiceFailedException)caught).getReason() == ServiceFailedException.NOT_FOUND) {
				if (enableDefault) {
					presentationLabel.setText("Parent Network Operator Presentation");
				}
				else {
					presentationLabel.setText("None");
				}
			}
			else {
				statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
			}
		}

		public void onSuccess(PresentationInfo result) {
			statusBox.clear();
			if (result != null)
				presentationLabel.setText(result.getName());
		}
	}
	
	public String getId() {
		return selectedPresentationId;
	}
	
	public void setDefaultPresentation(String presentationId){
		if (presentationId != null && !presentationId.isEmpty()) {
			defaultPresentationId = presentationId;
		}
	}
}
