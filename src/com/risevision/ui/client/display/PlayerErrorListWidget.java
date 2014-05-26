// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.display;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.ui.client.common.data.PlayerErrorDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.PlayerErrorInfo;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.SimpleGridWidget;

public class PlayerErrorListWidget extends VerticalPanel {
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private List<PlayerErrorInfo> playerErrors;
	
	private String[][] header = new String[][] {
			{"Date", "120px"},
			{"Code and Description", "355px"},
		};
	
	private SimpleGridWidget bodyFlexTable = new SimpleGridWidget(header);
	
	private static final String SHOW_ERRORS_TEXT = "Show More";
	private static final String HIDE_ERRORS_TEXT = "Show Less";
	
	private Anchor showMoreErrorsLink = new Anchor();
	private boolean showingMoreErrors = false;
	private int errorRow = 0;
	
	public PlayerErrorListWidget() {
		add(bodyFlexTable);
		add(showMoreErrorsLink);
		
		setWidth("475px");
		
		getElement().getStyle().setMarginTop(4, Unit.PX);
		getElement().getStyle().setMarginBottom(2, Unit.PX);
		
		initActions();
		
	}
	
	private void initActions() {
		showMoreErrorsLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showMoreErrors();
			}
		});
	}
	
	public void initWidget(String displayId) {
		bodyFlexTable.clearGrid();
		
		if (displayId != null && !displayId.isEmpty()) {
			PlayerErrorDataController controller = PlayerErrorDataController.getInstance();
			statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
			
			controller.getPlayerErrors(displayId, new GetErrorsRpcCallBackHandler());
		}
		else {
			addEmptyRow();
		}
	}
	
	private void updateTable() {
		bodyFlexTable.clearGrid();
		errorRow = -1;
		
		if (playerErrors.size() == 0) {
			addEmptyRow();
		}
		else {
			for (int i = 0; i < playerErrors.size(); i++) {
				PlayerErrorInfo error = playerErrors.get(i);
				if (error.getStatusCode() < 1000 && errorRow == -1) {
					errorRow = i;
				}
				
				updateTableRow(error, i);
			}
		}
		
		showingMoreErrors = true;
		showMoreErrors();
	}
	
	private void addEmptyRow() {
		bodyFlexTable.setText(0, 1, "(No Errors)"); 
	}

	private void updateTableRow(final PlayerErrorInfo item, int row) {
		bodyFlexTable.setText(row, 0, item.getTimestamp());
		bodyFlexTable.getFlexTable().getCellFormatter().getElement(row, 0).getStyle().setVerticalAlign(VerticalAlign.TOP);
		bodyFlexTable.getFlexTable().getCellFormatter().getElement(row, 0).getStyle().setLineHeight(16, Unit.PX);

		String errorString = 
//				Integer.toString(item.getStatusCode()) + " - " + 
				item.getErrorMessage();
		if (item.getStatusCode() != 0 && (errorRow == -1 || (item.getStatusCode() == 1 && row == 0))) {
			errorString += " Click <a href='" + item.getHelpLink() + "' target='_blank'>here</a> for details on this error.";
		}
		bodyFlexTable.setHTML(row, 1, errorString); 
		
//		if (errorRow == -1 || (item.getStatusCode() == 1 && row == 0)) {
//			bodyFlexTable.getFlexTable().getCellFormatter().getElement(row, 1).getStyle().setColor("red");
//		}
		
		bodyFlexTable.getFlexTable().getCellFormatter().getElement(row, 1).getStyle().setWhiteSpace(WhiteSpace.NORMAL);
		bodyFlexTable.getFlexTable().getCellFormatter().getElement(row, 1).getStyle().setLineHeight(16, Unit.PX);
	}
	
	private void showMoreErrors() {
		showMoreErrorsLink.setVisible(errorRow != -1);
		if (errorRow == -1) return;
		else if (errorRow == 0) errorRow = 1;
		
		showingMoreErrors = !showingMoreErrors;
		showMoreErrorsLink.setText(showingMoreErrors ? HIDE_ERRORS_TEXT : SHOW_ERRORS_TEXT);
		
		for (int i = errorRow; i < playerErrors.size(); i++) {
			bodyFlexTable.getFlexTable().getRowFormatter().setVisible(i, showingMoreErrors);
		}
	}

	class GetErrorsRpcCallBackHandler extends RiseAsyncCallback<List<PlayerErrorInfo>> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(List<PlayerErrorInfo> result) {
			statusBox.clear();
			if (result != null) {
				playerErrors = result;
				updateTable();
			}
		}
	}

}
