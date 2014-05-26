// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.socialConnector;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.data.SocialConnectorDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo.NetworkType;
import com.risevision.ui.client.common.service.SocialConnectorService;
import com.risevision.ui.client.common.service.SocialConnectorServiceAsync;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.SuggestBoxWidget;
import com.risevision.ui.client.common.widgets.grid.SimpleGridWidget;

public class SocialConnectorDisplayWidget extends VerticalPanel {
	//RPC
	private final SocialConnectorServiceAsync socialConnectorService = GWT.create(SocialConnectorService.class);
	private ArrayList<SocialConnectorAccessInfo> socialConnectors;
	private HashMap<NetworkType, ArrayList<String>> displayTokens;
	
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();

//	private CheckBox useDefaultCheckBox = new CheckBox();
	
	private HashMap<NetworkType, RiseListBox> accessTokenListBoxes = new HashMap<SocialConnectorAccessInfo.NetworkType, RiseListBox>();
	
//	private	RiseListBox accessTokenList = new RiseListBox();

	private SuggestBoxWidget locationTextBox = new SuggestBoxWidget("Default");

	private String displayId;
	
	private String[][] header = new String[][] {
			{"Connection", "160px"},
			{"Token", "200px"},
			{"Venue ID", "85px"}
			};
	
	private SimpleGridWidget bodyFlexTable = new SimpleGridWidget(header);

	public SocialConnectorDisplayWidget() {	
		add(bodyFlexTable);
		
		initActions();
		styleControls();
	}
	
	private void initActions() {		
//		useDefaultCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
////				accessTokenPanel.setVisible(!event.getValue());
////				itemsFlexTable.getCellFormatter().setVisible(0, 2, !event.getValue());
//				
//				accessTokenList.setVisible(!event.getValue());
//			}
//		});
	}
	
	private void styleControls() {
		
//		useDefaultCheckBox.addStyleName("rdn-CheckBox");
//		accessTokenList.addStyleName("rdn-ListBoxMedium");

		locationTextBox.addStyleName("rdn-TextBoxShort");
		
		setWidth("450px");
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
		
		loadDisplayData();
	}
	
	protected void onLoad() {
		super.onLoad();
		
		clearData();
		
		SocialConnectorDataController controller = SocialConnectorDataController.getInstance();
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
		
		controller.getAccessTokensList(SelectedCompanyController.getInstance().getSelectedCompanyId(), new GetAccessTokensRpcCallBackHandler());
		
//		socialConnectorService.getAccessTokens(SelectedCompanyController.getInstance().getSelectedCompanyId(), new GetAccessTokensRpcCallBackHandler());
	}
	
	private void loadDisplayData() {
		if (!(displayId == null || displayId.isEmpty())) {
//			statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
			
			socialConnectorService.getDisplayTokens(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
					displayId, new GetDisplayTokensRpcCallBackHandler());
		}
	}

	private void clearData() {
		bodyFlexTable.clearGrid();
		
		displayId = "";
		socialConnectors = null;
		displayTokens = null;
		
		accessTokenListBoxes.clear();
		
//		useDefaultCheckBox.setValue(true, true);
//		accessTokenList.clear();
//		accessTokenList.addItem("Default", "");
//		accessTokenList.setSelectedValue("");
		
		locationTextBox.setText("");
	}
	
	public void save() {
		if (displayId != null && !displayId.isEmpty()) {
			statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);

			for (NetworkType networkType: NetworkType.values()) {
				if (accessTokenListBoxes.get(networkType) != null) {
//					String accessToken = useDefaultCheckBox.getValue() ? "": accessTokenList.getSelectedValue();
					String accessToken = accessTokenListBoxes.get(networkType).getSelectedValue();
					String locationToken = "";
					if (networkType == NetworkType.foursquare) {
						locationToken = locationTextBox.getText();
					}
			
					socialConnectorService.putDisplayTokens(SelectedCompanyController.getInstance().getSelectedCompanyId(), displayId, 
							networkType.toString(), accessToken, locationToken, 
							new SaveDisplayTokensRpcCallBackHandler());
				}
			}
		}
	}

	private void updateTable() {
		int row = 0;

		if (socialConnectors != null && socialConnectors.size() > 0 && displayTokens != null) {
			for (NetworkType networkType: NetworkType.values()) {
				boolean socialConnectorFound = false;
				RiseListBox accessTokenList = new RiseListBox();

				accessTokenList.addItem("Default", "");
				accessTokenList.addStyleName("rdn-ListBoxMedium");

				accessTokenListBoxes.put(networkType, accessTokenList);

				for (SocialConnectorAccessInfo item: socialConnectors) {
					if (item.getNetworkType() == networkType) {
						socialConnectorFound = true;
//						if (item.isDefault()) {
//							accessTokenList.addItem(item.getName() + " - Default", item.getId());
//						}
//						else {
							accessTokenList.addItem(item.getName(), item.getId());
//						}
					}
				}
				
				if (socialConnectorFound) {
					if (displayTokens.get(networkType) != null) {
						ArrayList<String> displayToken = displayTokens.get(networkType);
						
						if (displayToken != null && displayToken.size() == 2) {
							String accessToken = displayToken.get(0);
//							boolean found = false;
							if (!RiseUtils.strIsNullOrEmpty(accessToken)) {
								for (int i = 0; i < accessTokenList.getItemCount(); i++) {
									if (accessTokenList.getValue(i).equals(accessToken)) {
										accessTokenList.setSelectedIndex(i);
//										found = true;
										break;
									}
								}
							}
							
//							if (found) {
//								useDefaultCheckBox.setValue(false, true);
//							}
							
							if (networkType == NetworkType.foursquare) {
								locationTextBox.setText(displayToken.get(1));
							}
						}
					}
	
					bodyFlexTable.setText(row, 0, networkType.getDefaultName());
					bodyFlexTable.setWidget(row, 1, accessTokenList);
					
					if (networkType == NetworkType.foursquare) {
						bodyFlexTable.setWidget(row, 2, locationTextBox);
					}
					else {
						bodyFlexTable.setText(row, 2, "");
					}
					
					row++;
				}
				
			}
			
//			loadDisplayData();
		}
		
		if (row == 0) {
			addEmptyRow();
		}
	}

	private void addEmptyRow() {
		bodyFlexTable.setText(0, 0, "(No connections available.)");
	}

	class GetAccessTokensRpcCallBackHandler extends RiseAsyncCallback<ArrayList<SocialConnectorAccessInfo>> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(ArrayList<SocialConnectorAccessInfo> result) {
			statusBox.clear();

			socialConnectors = result;
			updateTable();
		}
	}
	
	class GetDisplayTokensRpcCallBackHandler extends RiseAsyncCallback<HashMap<NetworkType, ArrayList<String>>> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(HashMap<NetworkType, ArrayList<String>> result) {
			statusBox.clear();
			
			displayTokens = result;
			updateTable();
		}
	}
	
	class SaveDisplayTokensRpcCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			statusBox.clear();
			if (result != null) {
				
			}
		}
	}
	
}
