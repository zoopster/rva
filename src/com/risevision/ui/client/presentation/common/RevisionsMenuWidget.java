package com.risevision.ui.client.presentation.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.types.PresentationRevisionStatus;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.PresentationService;
import com.risevision.ui.client.common.service.PresentationServiceAsync;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;

public class RevisionsMenuWidget extends Button {
	private final PresentationServiceAsync presentationService = GWT.create(PresentationService.class);
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance(); 
	
	private PopupPanel menuPanel = new PopupPanel(true, false);
	private PresentationInfo presentation;
	private Command restoreCommand;
	
	private MenuItem publishItem;
	
	public RevisionsMenuWidget(Command restoreCommand) {
		super("Published");
		this.restoreCommand = restoreCommand;
		MenuBar popupMenuBar = new MenuBar(true);

		MenuItem restoreItem = new MenuItem(RiseUtils.capitalizeFirstLetter("Restore"), true, 
				new Command() {
			
					@Override
					public void execute() {
						restorePresentation();
						menuPanel.hide();
					}
				});
		
		publishItem = new MenuItem(RiseUtils.capitalizeFirstLetter("Publish"), true, 
				new Command() {
					
					@Override
					public void execute() {
						publishPresentation();
						menuPanel.hide();
					}
				});
		
		popupMenuBar.addItem(restoreItem);
		popupMenuBar.addItem(publishItem);
		
    	popupMenuBar.setVisible(true);
    	menuPanel.add(popupMenuBar);
    	
    	addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!menuPanel.isShowing()) {
					int x = getAbsoluteLeft();
			    	int y = getAbsoluteTop() + getOffsetHeight();
			    	menuPanel.setPopupPosition(x, y);
			    	menuPanel.show();	 
			    	
					setHTML("<span style='white-space:nowrap;'><span style='color:red;'>Revised</span> \u25B2</span>");
				}
				else {
					menuPanel.hide();
				}
			}
		});
    	
    	menuPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				setHTML("<span style='white-space:nowrap;'><span style='color:red;'>Revised</span> \u25BC</span>");
			}
		});
	}
	
	public void init(PresentationInfo presentation) {
		this.presentation = presentation;
		
		statusUpdated();
	}
	
	public void setPublishEnabled(boolean enabled) {
		publishItem.setEnabled(enabled);
	}
	
	public void statusUpdated() {
		if (presentation.getId() == null || presentation.getId().isEmpty()) { 
			setVisible(false);
		}
		else {
			setVisible(true);
			
			if (presentation.getRevisionStatus() == PresentationRevisionStatus.PUBLISHED) {
				setText("Published");
				setEnabled(false);
			}
			else {
				setHTML("<span style='white-space:nowrap;'><span style='color:red;'>Revised</span> \u25BC</span>");
				setEnabled(true);
			}
		}
	}
	
	public void setEnabled(boolean enabled) {
		if (enabled && presentation != null && presentation.getRevisionStatus() == PresentationRevisionStatus.PUBLISHED) {
			return;
		}
		super.setEnabled(enabled);
	}
	
	private void restorePresentation() {
		if (restoreCommand != null) {
			restoreCommand.execute();
		}
	}
	
	private void publishPresentation() {
		if (presentation != null) {
//			actionsWidget.setEnabled(false);
			
			// save presentation
			statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Publishing presentation...");
			presentationService.publishPresentation(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
					presentation.getId(), new RpcPublishPresentationCallBackHandler());
		}
	}
	
	class RpcPublishPresentationCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			statusBox.clear();
			presentation.setRevisionStatus(PresentationRevisionStatus.PUBLISHED);
			statusUpdated();
		}
	}
}
