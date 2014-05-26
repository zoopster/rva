package com.risevision.ui.client.gadget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.types.GadgetType;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.info.HistoryTokenInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.GadgetService;
import com.risevision.ui.client.common.service.GadgetServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.FormGridWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GadgetManageWidget extends Composite {
	private static GadgetManageWidget instance;
	private GadgetInfo gadgetInfo;
	private String gadgetId;
	//RPC
	private final GadgetServiceAsync gadgetService = GWT.create(GadgetService.class);
	private RpcGetGadgetCallBackHandler rpcGetGadgetCallBackHandler = new RpcGetGadgetCallBackHandler();
	private RpcPutGadgetCallBackHandler rpcPutGadgetCallBackHandler = new RpcPutGadgetCallBackHandler();
	private RpcDeleteGadgetCallBackHandler rpcDeleteGadgetCallBackHandler = new RpcDeleteGadgetCallBackHandler();
	//UI pieces
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private FormGridWidget mainGrid = new FormGridWidget(11, 2);
	//UI: Gadget fields
	private Label lbGadgetId = new Label();
	private TextBox tbName = new TextBox();
	private GadgetTypeListBox typeListBox = new GadgetTypeListBox();
	private int typeRow = -1;
	private TextBox tbUrl = new TextBox();
	private TextBox uiUrlTextBox = new TextBox();
	private TextBox helpUrlTextBox = new TextBox();
	private TextBox tbScreenshotUrl = new TextBox();
	private TextBox tbThumbnailUrl = new TextBox();
	private TextBox tbDescription = new TextBox();
	private TextBox tbAuthor = new TextBox();
	private TextBox tbAuthorUrl = new TextBox();
	private CheckBox cbShared = new CheckBox();
	//last modified
	private LastModifiedWidget wgLastModified = LastModifiedWidget.getInstance();

	public GadgetManageWidget() {
		initWidget(mainPanel);
		mainPanel.add(formValidator);
		mainPanel.add(mainGrid);

		styleControls();
		
		// add widgets
		mainGrid.addRow("Name*:", tbName, "rdn-TextBoxLong");
		
		mainGrid.addRow("Type:", typeListBox, null);
		typeRow = mainGrid.getRow();
		
		mainGrid.addRow("URL:", tbUrl, "rdn-TextBoxLong");
		mainGrid.addRow("Custom UI URL:", uiUrlTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Help URL:", helpUrlTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Shared:", cbShared, "rdn-CheckBox");
		mainGrid.addRow("Description:", tbDescription, "rdn-TextBoxLong");
		mainGrid.addRow("Author:", tbAuthor, "rdn-TextBoxLong");
		mainGrid.addRow("Author URL:", tbAuthorUrl, "rdn-TextBoxLong");
		mainGrid.addRow("Screenshot URL:", tbScreenshotUrl, "rdn-TextBoxLong");
		mainGrid.addRow("Thumbnail URL:", tbThumbnailUrl, "rdn-TextBoxLong");
		
		initValidator();
	}

	private void styleControls() {

	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(tbName, "Gadget Name", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(tbUrl, "URL", FormValidatorInfo.requiredFieldValidator);
	}

	private void initActions() {
		@SuppressWarnings("unused")
		Command cmdPreview = new Command() {
			public void execute() {
				doActionPreview();
			}
		};
		Command cmdSave = new Command() {
			public void execute() {
				doActionSave();
			}
		};
		Command cmdDelete = new Command() {
			public void execute() {
				doActionDelete();
			}
		};
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};

		actionsWidget.clearActions();
		actionsWidget.addAction("Save", cmdSave);
		actionsWidget.addAction("Delete", cmdDelete);
//		actionsWidget.addAction("Preview", cmdPreview);
		actionsWidget.addAction("Cancel", cmdCancel);
		
		typeListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				typeValueChanged();
			}
		});
	}

	
	public static GadgetManageWidget getInstance() {
		try {
			if (instance == null)
				instance = new GadgetManageWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	protected void onLoad() {
		super.onLoad();
		initActions();
		actionsWidget.setEnabled(true);
		clearData();
		if (!(gadgetId == null || gadgetId.isEmpty()))
			loadDataRPC(gadgetId);
		tbName.setFocus(true);
	}

	private void clearData() {
		gadgetInfo = new GadgetInfo();
		gadgetInfo.setType(GadgetType.WIDGET);
		
		bindData();
	}
	
	private void bindData() {
		if (gadgetInfo == null)
			return;

		lbGadgetId.setText(gadgetInfo.getId());
		tbName.setText(gadgetInfo.getName());

		typeListBox.setSelectedValue(gadgetInfo.getType());
		typeValueChanged();

		tbUrl.setText(gadgetInfo.getUrl());
		uiUrlTextBox.setText(gadgetInfo.getUiUrl());
		helpUrlTextBox.setText(gadgetInfo.getHelpUrl());
		tbScreenshotUrl.setText(gadgetInfo.getScreenshotUrl());
		tbThumbnailUrl.setText(gadgetInfo.getThumbnailUrl());
		tbDescription.setText(gadgetInfo.getDescription());
		tbAuthor.setText(gadgetInfo.getAuthorName());
		tbAuthorUrl.setText(gadgetInfo.getAuthorUrl());
		cbShared.setValue(gadgetInfo.isShared());
				
		wgLastModified.Initialize(gadgetInfo.getChangedBy(), gadgetInfo.getChangeDate());
		statusBox.clear();
	}

	private void saveData() {
		if (gadgetInfo == null)
			return;

		if (!formValidator.validate())
			return;
		
		//gadgetInfo.setId(lbGadgetId.getText()); //ID for new gadget will be generated on server (web app, not core API)
		
		gadgetInfo.setName(tbName.getText());
		gadgetInfo.setType(typeListBox.getSelectedValue());
		gadgetInfo.setUrl(tbUrl.getText());
		gadgetInfo.setUiUrl(uiUrlTextBox.getText());
		gadgetInfo.setHelpUrl(helpUrlTextBox.getText());
		gadgetInfo.setScreenshotUrl(tbScreenshotUrl.getText());
		
		gadgetInfo.setThumbnailUrl(tbThumbnailUrl.getText());
		gadgetInfo.setDescription(tbDescription.getText());
		gadgetInfo.setAuthorName(tbAuthor.getText());
		gadgetInfo.setAuthorUrl(tbAuthorUrl.getText());
		gadgetInfo.setShared(cbShared.getValue());
		
		saveDataRPC(gadgetInfo);
	}

	private void deleteData() {
		deleteDataRPC(gadgetId);
	}
	
	private void loadDataRPC(String gadgetId) {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
		gadgetService.getGadget(SelectedCompanyController.getInstance().getSelectedCompanyId(), gadgetId, rpcGetGadgetCallBackHandler);
	}

	private void saveDataRPC(GadgetInfo si) {
		//we need to clear items to prevent RPC from serializing it and sending to server
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
		//save gadget only
		gadgetService.putGadget(SelectedCompanyController.getInstance().getSelectedCompanyId(), si, rpcPutGadgetCallBackHandler);
	}

	private void deleteDataRPC(String gadgetId) {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.DELETING);
		gadgetService.deleteGadget(SelectedCompanyController.getInstance().getSelectedCompanyId(), gadgetId, rpcDeleteGadgetCallBackHandler);
	}

	private void doActionPreview() {
		String url = tbUrl.getText();
		if (!RiseUtils.strIsNullOrEmpty(url)) {
			url = tbUrl.getText().replace("?", "&");
			Window.open(ConfigurationController.getInstance().getConfiguration().getViewerURL() + "Gadget.html?" +
					"&url=" + url,"_blank", "");
			/*Window.open(Globals.VIEWER_URL + "Gadget.html?" +
					"&url=" + url,"_blank", "");*/
		}
	}

	private void doActionSave() {
		saveData();
	}

	private void doActionDelete() {
		if (Window.confirm("Are you sure you want to delete this gadget?")) {
			deleteData();
		}
	}
	
	private void doActionCancel() {
		UiEntryPoint.loadContentStatic(ContentId.GADGETS);
	}
	
	private void typeValueChanged() {
		if (typeListBox.getSelectedValue().equals(GadgetType.GADGET)) {
			mainGrid.getRowFormatter().setVisible(typeRow + 2, false);
			mainGrid.getRowFormatter().setVisible(typeRow + 3, false);
		}
		else if (typeListBox.getSelectedValue().equals(GadgetType.URL)) {
			mainGrid.getRowFormatter().setVisible(typeRow + 2, false);
			mainGrid.getRowFormatter().setVisible(typeRow + 3, false);
		}
		else {
			mainGrid.getRowFormatter().setVisible(typeRow + 2, true);
			mainGrid.getRowFormatter().setVisible(typeRow + 3, true);
		}
	}

	public void setToken(HistoryTokenInfo tokenInfo) {
		gadgetId = tokenInfo.getId();
	}

	//--------- RPC CLASSES ---------------//
	class RpcGetGadgetCallBackHandler extends RiseAsyncCallback<GadgetInfo> {

		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(GadgetInfo result) {
			actionsWidget.setEnabled(true);
			if (result == null)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error retrieving Gadget data. Please try again.");
			else {
				gadgetInfo = result;
				bindData();
			}
		}
	}

	class RpcPutGadgetCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {

		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			actionsWidget.setEnabled(true);
			if (result == null)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error retrieving Gadget data. Please try again.");
			else {
				if (result != null) {
					//update ID
					gadgetId = result.getId();
					gadgetInfo.setId(gadgetId);
					lbGadgetId.setText(gadgetId);
					statusBox.clear();
				}
			}
		}
	}

	class RpcDeleteGadgetCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {

		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			//if Delete action succeeds, simply close the page
			doActionCancel();
			actionsWidget.setEnabled(true);
		}
	}
}