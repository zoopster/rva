// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.socialConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.data.SocialConnectorDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo.NetworkType;
import com.risevision.ui.client.common.service.SocialConnectorService;
import com.risevision.ui.client.common.service.SocialConnectorServiceAsync;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.SimpleGridWidget;

public class SocialConnectorCompanyWidget extends VerticalPanel implements ClickHandler {
//	private static final int ACTION_SELECT = 1;
	private static final int ACTION_ADD = 2;
//	private static final int ACTION_INSERT = 3;
	private static final int ACTION_DELETE = 4;
	private static final int ACTION_RESET = 5;
	
	//RPC
	private final SocialConnectorServiceAsync socialConnectorService = GWT.create(SocialConnectorService.class);
	private SocialConnectorAccessController accessController = SocialConnectorAccessController.getInstance();
//	private String networkName;
	private String companyId;
	private String locationToken;
	int rowIndex;
	private boolean isNew = false;

	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();

//	private HorizontalPanel locationPanel = new HorizontalPanel();
	private TextBox locationTextBox = new TextBox();

	/*
	 * Be careful with saving UI components in datastructures like this: if you
	 * remove a button from the app, make sure you also remove its reference
	 * from buttonMap HashMap to avoid memory leaks.
	 */
	private Map<Anchor, String> actionMap = new HashMap<Anchor, String>();

	private ArrayList<SocialConnectorAccessInfo> accessList;
	
	private String[][] header = new String[][] {
			{"Connection", "100%"},
			{"Venue ID", "90px"},
			{"", "45px"},
			{"", "35px"},
			{"", "50px"}
		};
	
	private SimpleGridWidget bodyFlexTable = new SimpleGridWidget(header);
	
	public SocialConnectorCompanyWidget() {
		add(bodyFlexTable);
//		add(locationPanel);
//		
//		locationPanel.add(new Label("Location Token:"));
//		locationPanel.add(new SpacerWidget());
//		locationPanel.add(locationTextBox);
		
		locationTextBox.setStyleName("rdn-TextBoxShort");
		
		setWidth("450px");
		
		getElement().getStyle().setMarginTop(4, Unit.PX);
		getElement().getStyle().setMarginBottom(2, Unit.PX);
		
		Command accessTokenReady = new Command() {
			@Override
			public void execute() {
				onTokenReady();
			}
		};
		
		accessController.setReadyCommand(accessTokenReady);

	}
	
	public void initWidget(String companyId) {
		this.companyId = companyId;
		
		bodyFlexTable.clearGrid();
		locationTextBox.setText("");
		
		if (companyId != null && !companyId.isEmpty()) {
			SocialConnectorDataController controller = SocialConnectorDataController.getInstance();
			statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
			
			controller.getAccessTokensList(companyId, new GetAccessTokensRpcCallBackHandler());
			
//			socialConnectorService.getAccessTokens(companyId, new GetAccessTokensRpcCallBackHandler());
			
			getLocationToken();
		}
		else {
			addEmptyRow();
		}
	}
	
	protected void getLocationToken() {
		socialConnectorService.getLocationToken(companyId, 
				NetworkType.foursquare.toString(), new GetLocationTokensRpcCallBackHandler());
	}
	
//	public void initWidget(ArrayList<SocialConnectorAccessInfo> accessList) {
//		this.accessList = accessList;
////		this.networkName = networkName;
//		
////		socialConnectorService.getLocationToken(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
////				networkName, new GetLocationTokensRpcCallBackHandler());
//		updateTable();
//	}
	
	// The shared ClickHandler code.
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if (sender instanceof Anchor) {
			Anchor b = (Anchor) sender;
			String keyString = actionMap.get(b);
			
			if (keyString != null && !keyString.isEmpty()) {
				int currentCommand = RiseUtils.strToInt(keyString.substring(0, 1), -1);
				rowIndex = RiseUtils.strToInt(keyString.substring(1), 0);
				isNew = false;
				
				if (currentCommand == ACTION_RESET) {
					// wait for token
				}

				if (currentCommand == ACTION_ADD) {
					// add item at the end of list
					rowIndex = accessList.size();
					isNew = true;
					// wait for token
				}

				if (currentCommand == ACTION_DELETE) {
					if (Window.confirm("Are you sure you want to delete this item?")) {
						deleteAccessToken();
					}
				}
			}
		}
	}
	
	private void onTokenReady() {
		String accessToken = accessController.getAccessToken();
		
		if (!RiseUtils.strIsNullOrEmpty(accessToken)) {
			SocialConnectorAccessInfo item;
			if (!isNew && !accessList.isEmpty()) {
				item = accessList.get(rowIndex);
			}
			else {
				NetworkType networkType = accessController.getNetworkType();
				
				item = new SocialConnectorAccessInfo();
				item.setNetworkType(networkType);
				item.setName(networkType.getDefaultName() + " " + findNextIndex(networkType));
				
				boolean hasDefault = false;
				for (SocialConnectorAccessInfo listItem: accessList) {
					if (listItem.getNetworkType() == networkType) {
						hasDefault = true;
					}
				}
				
				item.setDefault(accessList.size() == 0 || !hasDefault);
				
				accessList.add(item);
			}
					
			item.setValue(accessToken);
			saveAccessToken();
		}
	}
	
	private int findNextIndex(NetworkType networkType) {
		if (accessList.size() > 0) {
			int index = 0;
			for (SocialConnectorAccessInfo item: accessList) {
				if (item.getNetworkType() == networkType) {
					String[] tokens = item.getName().split(" ");
					if (RiseUtils.strToInt(tokens[tokens.length - 1], 0) > index);
						index = RiseUtils.strToInt(tokens[tokens.length - 1], 0);
				}
			}
			
			return index + 1;
		}
		else {
			return 1;
		}
	}
	
	private void updateTable() {
		bodyFlexTable.clearGrid();
		if (accessList.size() == 0) {
//			locationPanel.setVisible(false);
			
			addEmptyRow();
		}
		else {
//			locationPanel.setVisible(true);
			
			for (int i = 0; i < accessList.size(); i++)
				updateTableRow(accessList.get(i), i);
		}
	}
	
	private void addEmptyRow() {
		bodyFlexTable.setText(0, 0, "(No connection set up.)"); 
	
//		Anchor addKeyLink = new Anchor("Add", SocialConnectorAccessInfo.REQUEST_URL, "_blank");
//		setAction(1, 4, addKeyLink, ACTION_ADD, "0");
		if (companyId != null && !companyId.isEmpty()) {
			setAction(0, 4, new NetworkTypeSelectWidget(), ACTION_ADD, "0");
		}
	}

	private void updateTableRow(final SocialConnectorAccessInfo item, int row) {
		String rowId = Integer.toString(row);

		bodyFlexTable.setText(row, 0, item.getName() + (item.isDefault() ? " - Default": "")); 
		
		if (item.isDefault() && item.getNetworkType() == NetworkType.foursquare) {
			if (accessList.size() > 1) {
				// TODO: add default selector here
			}
			
//			setText(row, 1, "Default");
			bodyFlexTable.setWidget(row, 1, locationTextBox);
		}
		
		Anchor resetKeyLink = new Anchor("Reset", item.getNetworkType().getRequestUrl(), "_blank");
		setAction(row, 2, resetKeyLink, ACTION_RESET, rowId);
		
//		Anchor addKeyLink = new Anchor("Add", SocialConnectorAccessInfo.REQUEST_URL, "_blank");
//		setAction(row, 4, addKeyLink, ACTION_ADD, rowId);
		setAction(row, 3, new NetworkTypeSelectWidget(), ACTION_ADD, rowId);

		Anchor deleteKeyLink = new Anchor("Delete");
		setAction(row, 4, deleteKeyLink, ACTION_DELETE, rowId);	
	}

	private void setAction(int row, int column, Anchor actionHyperlink, int actionType,
			String id) {
		actionHyperlink.addClickHandler(this);
		bodyFlexTable.setWidget(row, column, actionHyperlink);
		actionMap.put(actionHyperlink, actionType + id);
	}
	
	private void saveAccessToken()	{
		SocialConnectorAccessInfo item = accessList.get(rowIndex);
		
		if (item != null) {
			statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
			socialConnectorService.putAccessToken(companyId, 
					item, new PutAccessTokenCallBackHandler());
		}
	}
	
	private void deleteAccessToken() {
		SocialConnectorAccessInfo item = accessList.get(rowIndex);
		
		if (item != null) {
			statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.DELETING);
			socialConnectorService.deleteAccessToken(companyId, 
					item, new DeleteAccessTokenCallBackHandler());
		}
	}
	
	public void save() {
		if (!locationTextBox.getText().equals(locationToken) && !RiseUtils.strIsNullOrEmpty(companyId)) {
			locationToken = locationTextBox.getText();

			statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
			socialConnectorService.putLocationToken(companyId,  
					NetworkType.foursquare.toString(), locationToken, new PutLocationTokenCallBackHandler());
		}
	}
	
	class GetAccessTokensRpcCallBackHandler extends RiseAsyncCallback<ArrayList<SocialConnectorAccessInfo>> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(ArrayList<SocialConnectorAccessInfo> result) {
			statusBox.clear();
			if (result != null) {
				accessList = result;
				updateTable();
			}
		}
	}
	
	class DeleteAccessTokenCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			statusBox.clear();

			// set new "Default" access token
			if (accessList.get(rowIndex).isDefault() && accessList.size() > 1) {
				for (int i = 0; i < accessList.size(); i++) {
					if (i != rowIndex && accessList.get(i).getNetworkType() == accessList.get(rowIndex).getNetworkType()) {
						accessList.get(i).setDefault(true);
						
						statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
						socialConnectorService.putAccessToken(companyId, 
								accessList.get(i), new PutAccessTokenCallBackHandler());
						
						break;
					}
				}
			}
			
			accessList.remove(rowIndex);
			updateTable();
		}
	}
	
	class PutAccessTokenCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			if (isNew) {
				accessList.remove(rowIndex);
				isNew = false;
			}
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			if (isNew) {
				accessList.get(rowIndex).setId(result.getId());
				isNew = false;
			}
			
			updateTable();
			statusBox.clear();
		}
	}
	
	class GetLocationTokensRpcCallBackHandler extends RiseAsyncCallback<String> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(String result) {
			statusBox.clear();
			if (result != null) {
				locationToken = result;
				locationTextBox.setText(locationToken);
			}
		}
	}
	
	class PutLocationTokenCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			statusBox.clear();
		}
	}
}
