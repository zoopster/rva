// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.user;

import java.util.Date;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.attributes.UserAttribute;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.directory.UserDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.info.UsersInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

public class UsersWidget extends Composite {
//	private UserServiceAsync userService = (UserServiceAsync) GWT.create(UserService.class);
//	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	
	private static UsersWidget instance;

	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	private ScrollingGridWidget usersGridWidget;
	private Command usersGridCommand;

	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();

	public static UsersWidget getInstance() {
		try {

			if (instance == null)
				instance = new UsersWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public UsersWidget() {		
		initGridWidget();
		initWidget(usersGridWidget);
	}
	
	private void initGridWidget(){
		// * Username (link to User Edit)
		// * Name (concatenated first and last name)
		// * Status
		// * Last Login
		
		usersGridCommand = new Command() {
			public void execute() {			
				processGridCommand();
			}
		};			
		
		String[][] headerDefinition = new String[][] {
				{"Username", UserAttribute.USERNAME, "100%"},
				{"Name", UserAttribute.LAST_NAME, "200px"},
				{"Status", null, "80px"},
				{"Last Login", UserAttribute.LAST_LOGIN, "150px"}
				};
		
		UsersInfo usersInfo = new UsersInfo();
		usersInfo.setSortByDefault(UserAttribute.USERNAME);
		usersInfo.setSortDirection(ScrollingGridInfo.SORT_DOWN);
		usersGridWidget = new ScrollingGridWidget(usersGridCommand, usersInfo, headerDefinition);
	}
	
	protected void onLoad() {
		super.onLoad();

		initActions();	
		usersGridWidget.clear();
		loadGridDataRPC();
	}

	private void initActions() {	
		Command cmdAdd = new Command() {
			public void execute() {
				doActionAdd();
			}
		};		

		actionsWidget.addAction("Add", cmdAdd);
	}
	
	private void doActionAdd(){
//		HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
//		tokenInfo.setId("");
//		tokenInfo.setContentId(ContentId.USER_MANAGE);
		
//		String[] params = {""};
		UiEntryPoint.loadContentStatic(ContentId.USER_MANAGE);
	}
	
	private void processGridCommand(){
		int command = usersGridWidget.getCurrentCommand();
					
		switch (command) {
//		case ScrollingGridInfo.SELECTACTION:
//			HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
//			tokenInfo.setId(usersGridWidget.getCurrentKey());
//			tokenInfo.setContentId(ContentId.USER_MANAGE);
//			
//			UiEntryPoint.loadContentStatic(tokenInfo);
//			break;
		case ScrollingGridInfo.SEARCHACTION:
		case ScrollingGridInfo.PAGEACTION:
		case ScrollingGridInfo.SORTACTION:
			loadGridDataRPC();
			break;
		default:
			break;
		}
	}
	
	private void loadGridDataRPC(){
		UserDataController controller = UserDataController.getInstance();
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);	
		
		controller.getUsers(getUsersInfo(), new RpcCallBackHandler());
//		userService.getUsers(getUsersInfo(), rpcCallBackHandler);
	}
	
	private void updateTable(UsersInfo usersInfo) {
		if ((usersInfo != null) && (usersInfo.getUsers() != null)) {
			int i = 0;
			if (usersGridWidget.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				usersGridWidget.clearGrid();
			}
			else {
				i = usersGridWidget.getRowCount();
			}

			for ( ; i < usersInfo.getUsers().size(); i++) {
				updateTableRow(usersInfo.getUsers().get(i), i);
			}
		}
	}
	
	private void updateTableRow(final UserInfo user, int index) {
		// * Name (concatenated first and last name, link to User Edit)
		// * Username
		// * Status
		// * Last Login
//		usersGridWidget.setAction(index, 0, user.getUserName(), user.getId());
		usersGridWidget.setHyperlink(index, 0, user.getUserName(), ContentId.USER_MANAGE, user.getId());
		usersGridWidget.setText(index, 1, (user.getFirstName() == null ? "" : user.getFirstName()+ " ") 
				+ (user.getLastName() == null ? "" : user.getLastName()));		usersGridWidget.setText(index, 2, user.getStatusText());

		Date lastLogin = user.getLastLogin();
		if (lastLogin != null && lastLogin.getTime() != 0)			usersGridWidget.setText(index, 3, RiseUtils.dateToString(lastLogin));
		else			usersGridWidget.setText(index, 3, "N/A");
	}

	private UsersInfo getUsersInfo() {
		UsersInfo usersInfo = (UsersInfo) usersGridWidget.getGridInfo();
		
		if (usersInfo == null){
			usersInfo = new UsersInfo();
		}
		
		usersInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		return usersInfo;
	}
	

	class RpcCallBackHandler extends RiseAsyncCallback<UsersInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(UsersInfo result) {
			statusBox.clear();
			usersGridWidget.loadGrid(result);
			updateTable(result);
		}
	}
}
