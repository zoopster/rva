// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.demoContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.data.DemoContentDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.DemoContentInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.client.common.service.CompanyServiceAsync;
import com.risevision.ui.client.common.service.PresentationService;
import com.risevision.ui.client.common.service.PresentationServiceAsync;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.SimpleGridWidget;

public class DemoContentGridWidget extends SimplePanel implements ClickHandler {
	public static final int ACTION_SELECT = 1;
	public static final int ACTION_ADD = 2;
//	public static final int ACTION_INSERT = 3;
	public static final int ACTION_DELETE = 4;
	
	//RPC
	private final PresentationServiceAsync presentationService = GWT.create(PresentationService.class);
	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);

	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();

	private DemoContentManageWidget demoContentManageWidget = new DemoContentManageWidget();

	/*
	 * Be careful with saving UI components in datastructures like this: if you
	 * remove a button from the app, make sure you also remove its reference
	 * from buttonMap HashMap to avoid memory leaks.
	 */
	private Map<Anchor, String> actionMap = new HashMap<Anchor, String>();

	private ArrayList<DemoContentInfo> demoContentList;
	
	private String[][] header = new String[][] {
			{"Resolution", "100px"},
			{"Presentation", "100%"},
			{"", "35px"},
			{"", "50px"}
			};

	private SimpleGridWidget bodyFlexTable = new SimpleGridWidget(header);

	public DemoContentGridWidget() {
		add(bodyFlexTable);
		
		setWidth("510px");
	}

	protected void onLoad() {
		super.onLoad();
		
		demoContentManageWidget.load();
		loadDemoContentList();
	}

	private Command onPlayListItemChanged = new Command() {
		public void execute() {
			if (demoContentManageWidget.getItemIsNew()) {
				demoContentList.add(demoContentManageWidget.getDemoContentItem());
			}
			updateTable();
		}
	};
	
	// The shared ClickHandler code.
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		DemoContentInfo item;
		
		if (sender instanceof Anchor) {
			Anchor b = (Anchor) sender;
			String keyString = actionMap.get(b);
			
			if (keyString != null && !keyString.isEmpty()) {
				int currentCommand = RiseUtils.strToInt(keyString.substring(0, 1), -1);
				int rowIndex = RiseUtils.strToInt(keyString.substring(1), 0);
				
				if (currentCommand == ACTION_SELECT) {
					item = demoContentList.get(rowIndex);
					demoContentManageWidget.show(item, onPlayListItemChanged);
				}

				if (currentCommand == ACTION_ADD) {
					item = new DemoContentInfo();
					demoContentManageWidget.show(item, true, onPlayListItemChanged);
				}

				if (currentCommand == ACTION_DELETE) {
					if (Window.confirm("Are you sure you want to delete this item?")) {
						deleteDemoContentItem(rowIndex);
					}
				}
			}
		}
	}
	
	private void updateTable() {
		bodyFlexTable.clearGrid();
		if (demoContentList.size() == 0)
			addEmptyRow();
		else
			for (int i = 0; i < demoContentList.size(); i++)
				updateTableRow(demoContentList.get(i), i);
	}

	private void updateTableRow(final DemoContentInfo item, int row) {
		String rowId = Integer.toString(row);

		//setAction(row, 0, item.getName(), ACTION_SELECT, item.getId());
		setAction(row, 0, item.getWidth() + "x" + item.getHeight(), ACTION_SELECT, rowId);
		
		if (item.getObjectName() != null && !item.getObjectName().isEmpty()) {
			bodyFlexTable.setText(row, 1, item.getObjectName());		
		}
		else {
			bodyFlexTable.setText(row, 1, "");
			loadPresentationName(item.getObjectRef());
		}
		
		setAction(row, 2, "Add", ACTION_ADD, rowId);
		setAction(row, 3, "Delete", ACTION_DELETE, rowId);
		
	}

	private void addEmptyRow() {
		setAction(0, 0, "(List is empty.)", ACTION_ADD, "0");
		setAction(0, 3, "Add", ACTION_ADD, "0");
	}

	public void setAction(int row, int column, String text, int actionType,
			String id) {
		Anchor actionHyperlink = new Anchor(text);
		actionHyperlink.addClickHandler(this);
		bodyFlexTable.setWidget(row, column, actionHyperlink);
		actionMap.put(actionHyperlink, actionType + id);
	}
	
	public void setAction(int row, int column, String text, String id) {
		setAction(row, column, text, ACTION_SELECT, id);
	}

	private void loadDemoContentList(){
		DemoContentDataController controller = DemoContentDataController.getInstance();
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		
		controller.getDemoContentList(SelectedCompanyController.getInstance().getSelectedCompanyId(), new DemoContentListCallBackHandler());
		
//		companyService.getDemoContent(SelectedCompanyController.getInstance().getSelectedCompanyId(), new DemoContentListCallBackHandler());
	}
	
	class DemoContentListCallBackHandler extends RiseAsyncCallback<ArrayList<DemoContentInfo>> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(ArrayList<DemoContentInfo> result) {
			statusBox.clear();
			if (result != null) {
				demoContentList = result;
				updateTable();
			}
		}
	}
	
	private void deleteDemoContentItem(int itemIndex) {
		DemoContentInfo item = demoContentList.get(itemIndex);
		
		if (item != null) {
			statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.DELETING);
			companyService.deleteDemoContent(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
					item.getWidth(), item.getHeight(), new DeleteDemoContentCallBackHandler());
			
			demoContentList.remove(itemIndex);
			updateTable();
		}
	}
	
	class DeleteDemoContentCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			statusBox.clear();
		}
	}
	
	private void loadPresentationName(String objectRef){
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		presentationService.getPresentation(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
				objectRef, new PresentationNameRpcCallBackHandler());
	}
	
	class PresentationNameRpcCallBackHandler extends RiseAsyncCallback<PresentationInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(PresentationInfo result) {
			statusBox.clear();
			if (result != null) {
				for (int i = 0; i < demoContentList.size(); i++) {
					if (result.getId().equals(demoContentList.get(i).getObjectRef())) {
						demoContentList.get(i).setObjectName(result.getName());
						bodyFlexTable.setText(i, 1, result.getName());		
					}
				}
			}
		}
	}
}
