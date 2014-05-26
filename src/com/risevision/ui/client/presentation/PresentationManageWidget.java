package com.risevision.ui.client.presentation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.common.client.json.DistributionJsonParser;
import com.risevision.common.client.utils.PresentationParser;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.types.PresentationRevisionStatus;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.HistoryTokenInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.PresentationService;
import com.risevision.ui.client.common.service.PresentationServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.MessageBoxWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerWidget;
import com.risevision.ui.client.presentation.common.HtmlEditorWidget;
import com.risevision.ui.client.presentation.common.RevisionsMenuWidget;
import com.risevision.ui.client.presentation.placeholder.PlaceholderManageWidget;
import com.risevision.ui.client.presentation.placeholder.PlaylistItemManageWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PresentationManageWidget extends Composite implements KeyDownHandler {
	private static final int LAYOUT_INDEX = 0;
	private static final int HTML_INDEX = 1;
//	private static final String COPY_BUTTON = "Copy";

	private static PresentationManageWidgetUiBinder uiBinder = GWT.create(PresentationManageWidgetUiBinder.class);
	interface PresentationManageWidgetUiBinder extends UiBinder<Widget, PresentationManageWidget> {}
	private static PresentationManageWidget instance;
	private PresentationInfo presentationInfo;
	private String presentationId;
	private String fromCompanyId;
//	private boolean isPreview = false;
	
	//RPC
	private final PresentationServiceAsync presentationService = GWT.create(PresentationService.class);
	
	//UI pieces
	@UiField ActionsWidget actionsWidget;
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	@UiField DeckPanel contentPanel = new DeckPanel();
	@UiField TabBar contentBar = new TabBar();

	@UiField PresentationLayoutWidget layoutWidget;
	@UiField HtmlEditorWidget htmlArea;

	private PopupPanel preOptionsPopupWidget = new PopupPanel(true, true);
	private PresentationOptionsWidget preOptionsWidget = new PresentationOptionsWidget();
	
//	//UI: Presentation fields
	@UiField Label nameLabel;
//	@UiField HorizontalPanel buttonsPanel;
//	private PushButton propertiesLink;
//	private PushButton addPlaceholderLink;
	private PushButton copyPlaceholderButton;
	private PushButton pastePlaceholderButton;
	private PushButton helpButton;
	
	private PushButton previewButton;
	private PushButton copyButton;
	private PushButton deleteButton;
	
	private RevisionsMenuWidget revisionsMenu;

	private LastModifiedWidget lastModifiedWidget = LastModifiedWidget.getInstance();
	
	private HandlerRegistration keyDownEventHandlerRegistration;
	
	public static PresentationManageWidget getInstance() {
		try {
			if (instance == null)
				instance = new PresentationManageWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public PresentationManageWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		layoutWidget = PresentationLayoutWidget.getInstance();
		
		preOptionsPopupWidget.add(preOptionsWidget);

		contentBar.addTab("Design");
		contentBar.addTab("HTML");
		contentBar.selectTab(LAYOUT_INDEX);

		contentPanel.showWidget(LAYOUT_INDEX);
		
//		propertiesLink = createPushButton("images/presentation/settings.png", "Presentation Settings");
		helpButton = createPushButton("images/presentation/help.png", "Presentation Help");
//		addPlaceholderLink = createPushButton("images/presentation/placeholder.png", "Add Placeholder");
		copyPlaceholderButton = createPushButton("images/presentation/copy.png", "Copy Placeholder (Ctrl+C)");
		pastePlaceholderButton = createPushButton("images/presentation/paste.png", "Paste Placeholder (Ctrl+V)");
		
//		buttonsPanel.add(propertiesLink);
//		buttonsPanel.add(helpLink);
//		buttonsPanel.add(addPlaceholderLink);
//		buttonsPanel.add(copyPlaceholderLink);
//		buttonsPanel.add(pastePlaceholderLink);
		
		copyButton = createPushButton("images/presentation/saveas.png", "Copy Presentation");
		previewButton = createPushButton("images/presentation/preview.png", "Preview");
		deleteButton = createPushButton("images/presentation/trash.png", "Delete Presentation");
		
		initClickEvents();
		initCommands();

		initActions();
		
		styleControls();
		initSelection();
		initValidator();
		applySecuritySettings();
	}
	
	private PushButton createPushButton(String url, String title) {
		Image image = new Image(url);
		image.setHeight("22px");
		image.setWidth("22px");
		PushButton button = new PushButton(image);
		
		button.getElement().setClassName("simple-PushButton");
		
		button.setTitle(title);
		
		return button;
	}

	private void styleControls() {	
		nameLabel.setStyleName("rdn-MainHead");
		
		actionsWidget.getElement().getStyle().setMarginBottom(5, Unit.PX);
		
//		propertiesLink.getElement().getStyle().setMarginLeft(4, Unit.PX);
//		helpLink.getElement().getStyle().setMarginLeft(4, Unit.PX);
//		addPlaceholderLink.getElement().getStyle().setMarginLeft(20, Unit.PX);
//		copyPlaceholderLink.getElement().getStyle().setMarginLeft(4, Unit.PX);
//		pastePlaceholderLink.getElement().getStyle().setMarginLeft(4, Unit.PX);
		
		pastePlaceholderButton.setVisible(false);
	}
	
	private void initClickEvents() {
		previewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doActionPreview();
			}
		});
		
//		propertiesLink.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				contentBar.selectTab(LAYOUT_INDEX);
//
//				layoutWidget.hideEditDialog();
//				preOptionsPopupWidget.show();
//				preOptionsPopupWidget.center();
//			}
//		});
		
//		addPlaceholderLink.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				contentBar.selectTab(LAYOUT_INDEX);
//				layoutWidget.addPlaceholder();
//			}
//		});
		
		copyPlaceholderButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				copyPlaceholder();
			}
		});
		
		pastePlaceholderButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pastePlaceholder();
			}
		});
		
		copyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doActionSaveAs();
			}
		});
		
		helpButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presentationInfo != null && presentationInfo.getHelpURL() != null && !presentationInfo.getHelpURL().isEmpty()) {
					Window.open(presentationInfo.getHelpURL(), "_blank", "");
				}
			}
		});
		
		deleteButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doActionDelete();
			}
		});
	}
	
	private void initCommands() {
		Command restoreCommand = new Command() {
			
			@Override
			public void execute() {
				restorePresentation();
			}
		};
		revisionsMenu = new RevisionsMenuWidget(restoreCommand);
	}

	private void initSelection() {
	    // Hook up a tab listener to do something when the user selects a tab.
		contentBar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				if (presentationInfo != null) {
					updateData();
				}
				
				int selectedIndex = event.getSelectedItem();
				contentPanel.showWidget(selectedIndex);
				
				// fixes issues with the editor when instance is first shown
				if (contentPanel.getVisibleWidget() == HTML_INDEX){
					htmlArea.onShow();
				}
				
				// update the layout every time
				layoutWidget.initWidget(presentationInfo);
			}
		});
	}
	
	private void updateData() {
		// called when saving and when switching layout/html panels
		if (contentPanel.getVisibleWidget() == HTML_INDEX){
			presentationInfo.setLayout(htmlArea.getText());
			PresentationParser.parsePresentation(presentationInfo);
		}
		else if (contentPanel.getVisibleWidget() == LAYOUT_INDEX) {
			PlaceholderManageWidget.getInstance().hide();
			PresentationParser.updatePresentation(presentationInfo);
			htmlArea.setText(presentationInfo.getLayout());
		}

	}
	
	private void initValidator() {

	}

	private void initActions() {
		Command cmdProperties = new Command() {
			public void execute() {
				contentBar.selectTab(LAYOUT_INDEX);

				layoutWidget.hideEditDialog();
				preOptionsPopupWidget.show();
				preOptionsPopupWidget.center();
			}
		};
		
		Command cmdAddPlaceholder = new Command() {
			public void execute() {
				contentBar.selectTab(LAYOUT_INDEX);
				layoutWidget.addPlaceholder();
			}
		};

		Command cmdSave = new Command() {
			public void execute() {
				doActionSave();
			}
		};
		
//		Command cmdSaveAs = new Command() {
//			public void execute() {
//				doActionSaveAs();
//			}
//		};
		
//		Command cmdDelete = new Command() {
//			public void execute() {
//				doActionDelete();
//			}
//		};
//		Command cmdPreview = new Command() {
//			public void execute() {
//				doActionPreview();
//			}
//		};
		
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};

//		actionsWidget.clearActions();
		
		actionsWidget.addAction(revisionsMenu);
		
		actionsWidget.addAction("images/presentation/placeholder.png", "Add Placeholder", cmdAddPlaceholder);
		actionsWidget.addAction(copyPlaceholderButton);
		actionsWidget.addAction(pastePlaceholderButton);

//		actionsWidget.addAction("images/presentation/preview.png", "Preview", cmdPreview);
		actionsWidget.addAction(previewButton);
		
		actionsWidget.addAction("images/presentation/save.png", "Save", cmdSave);
		actionsWidget.addAction(copyButton);
		
		actionsWidget.addAction(deleteButton);
		actionsWidget.addAction("images/presentation/cancel.png", "Cancel", cmdCancel);
		
		actionsWidget.addAction("images/presentation/settings.png", "Presentation Settings", cmdProperties);
		actionsWidget.addAction(helpButton);
	}
	
	private void applySecuritySettings() {
		if (!UserAccountController.getInstance().userHasRoleContentPublisher()) {
			revisionsMenu.setPublishEnabled(false);
			
			deleteButton.setEnabled(false);
		}
	}

	protected void onLoad() {
		super.onLoad();
		
//		initActions();
		enableButtons();
		clearData();
		if (!RiseUtils.strIsNullOrEmpty(presentationId)) {
//			PresentationAddWidget addWidget = PresentationAddWidget.getInstance();
//			if (addWidget.getPresentationInfo() != null) {
//				presentationInfo = addWidget.getPresentationInfo();
//				PresentationParser.updatePresentationHeader(presentationInfo);
//				bindData();
//			}
//		}
//		else {
			if (!RiseUtils.strIsNullOrEmpty(fromCompanyId)) {
				loadTemplateDataRPC(presentationId);
			}
			else {
				loadDataRPC(presentationId);
			}
		}
		
		PlaceholderManageWidget.getInstance().load();
		PlaylistItemManageWidget.getInstance().load();
		
		PresentationSelectPopupWidget.getInstance().load();
		
//		StorageAppWidget.getInstance().load();
		GooglePickerWidget.getInstance();
		
		copyPlaceholderButton.setEnabled(false);		
		
		keyDownEventHandlerRegistration = RootPanel.get().addDomHandler(this, KeyDownEvent.getType());
	}
	
	protected void onUnload() {
		super.onUnload();
		
		PlaceholderManageWidget.getInstance().hide();
		
		keyDownEventHandlerRegistration.removeHandler();
	}
	
	@Override
	public void onKeyDown(KeyDownEvent event) {
		// Use the popup's key preview hooks to close the dialog when either
		// enter or escape is pressed.
		int eventCode = event.getNativeKeyCode();
		// check if CTRL key is pressed & another key is sending the event
		if (event.isControlKeyDown() && eventCode != 17 && !PlaceholderManageWidget.getInstance().isShowing() 
				&& !preOptionsPopupWidget.isShowing()) {
			// Code 67 = c
			if (eventCode == 67 && copyPlaceholderButton.isEnabled()) {
				if (copyPlaceholder()) {
					event.stopPropagation();
				}
			}
			// Code ?? = v
			else if (eventCode == 86 && contentBar.getSelectedTab() == LAYOUT_INDEX) {
				pastePlaceholder();
			}
		}
	}
	
//	@Override
//	public void onKeyUp(KeyUpEvent event) {
//		// Use the popup's key preview hooks to close the dialog when either
//		// enter or escape is pressed.
//		if (event.isControlKeyDown()) {
//			controlPressed = false;
//		}
//	}
	
	private boolean copyPlaceholder() {
		boolean copy = layoutWidget.copyPlaceholder(); 					
				
		if (copy) {
			pastePlaceholderButton.setVisible(true);
		}

		return copy;
	}
	
	private void pastePlaceholder() {
		contentBar.selectTab(LAYOUT_INDEX);

		layoutWidget.pastePlaceholder();
	}
	
	public void enableCopyButton(boolean enabled) {
		copyPlaceholderButton.setEnabled(enabled);
	}
	
	private void enableButtons() {
		actionsWidget.setEnabled(true);

		if (RiseUtils.strIsNullOrEmpty(presentationId)) {
			copyButton.setEnabled(false);
			previewButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
		
		applySecuritySettings();
	}
	
	private void restorePresentation() {
		clearData();
		if (!RiseUtils.strIsNullOrEmpty(presentationId)) {
			restorePresentationRPC();
		}
	}
	
	private void clearData() {
		contentBar.selectTab(LAYOUT_INDEX);
		presentationInfo = new PresentationInfo();
		bindData();
	}

	private void bindData() {
		if (presentationInfo == null)
			return;
		
		PresentationParser.parsePresentation(presentationInfo);
		
		helpButton.setVisible(presentationInfo.getHelpURL() != null && !presentationInfo.getHelpURL().isEmpty());
		
		DistributionJsonParser.parseDistributionData(presentationInfo);
		layoutWidget.initWidget(presentationInfo);
		
		nameLabel.setText(presentationInfo.getName() + " (" + presentationInfo.getWidth() + "x" + presentationInfo.getHeight() + ")");
		htmlArea.setText(presentationInfo.getLayout());
						
		initPresentationProperties();
		revisionsMenu.init(presentationInfo);
		lastModifiedWidget.Initialize(presentationInfo.getChangedBy(), presentationInfo.getChangeDate());
		
		statusBox.clear();
		copyPlaceholderButton.setEnabled(false);		
	}
	
	protected void initPresentationProperties() {
		Command editCommand = new Command(){
			public void execute(){
				preOptionsPopupWidget.hide();
				if (preOptionsWidget.isSaveData()) {
					PresentationParser.updatePresentationHeader(presentationInfo);
					
					if (contentPanel.getVisibleWidget() == LAYOUT_INDEX){
						PresentationParser.updatePresentation(presentationInfo);
						htmlArea.setText(presentationInfo.getLayout());
					}
					
					bindData();
				}
			}
		};
		
		preOptionsWidget.init(presentationInfo, editCommand);
	}

	private void saveData() {
		updateData();
//		presentationInfo.setLayout(htmlArea.getText());
		DistributionJsonParser.updateDistributionData(presentationInfo);
	}
	
	private void loadDataRPC(String presentationId) {		
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
		presentationService.getPresentation(SelectedCompanyController.getInstance().getSelectedCompanyId(), presentationId, new RpcGetPresentationCallBackHandler());
	}
	
	private void loadTemplateDataRPC(String presentationId) {		
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
		presentationService.getTemplate(fromCompanyId, presentationId, new RpcGetPresentationCallBackHandler());
	}
	
	private void restorePresentationRPC() {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Restoring Presentation...");
		presentationService.restorePresentation(SelectedCompanyController.getInstance().getSelectedCompanyId(), presentationId, new RpcGetPresentationCallBackHandler());
	}
	
	private void saveDataRPC() {
		actionsWidget.setEnabled(false);
		
		// save presentation
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
		presentationService.putPresentation(SelectedCompanyController.getInstance().getSelectedCompanyId(), presentationInfo, new RpcPutPresentationCallBackHandler());
		
//		if (presentationId != null && !presentationId.isEmpty()) {
//			presentationService.deletePlaceholders(SelectedCompanyController.getInstance().getSelectedCompanyId(), presentationId, new RpcDeletePlaceholdersCallBackHandler());
//		}
	}
	
	private void deletePresentationRPC() {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.DELETING);
		
		presentationService.deletePresentation(SelectedCompanyController.getInstance().getSelectedCompanyId(), presentationId, new RpcDeletePresentationCallBackHandler());
	}

	private void doActionSaveAs() {
		if (presentationInfo == null)
			return;
		
		saveData();

		presentationId = "";
		presentationInfo.setId("");
		presentationInfo.setName("Copy of " + presentationInfo.getName());
//		PresentationAddWidget.getInstance().setPresentationInfo(presentationInfo);
		
		HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
		tokenInfo.setContentId(ContentId.PRESENTATION_MANAGE);
		
		UiEntryPoint.loadContentStatic(tokenInfo, false);
		
		PresentationInfo tempPresentationInfo = presentationInfo;
		
		enableButtons();
		clearData();
		
		presentationInfo = tempPresentationInfo;
		bindData();
	}
	
	private void doActionSave() {
		if (presentationInfo == null)
			return;
		
		saveData();
		
		saveDataRPC();
	}

	private void doActionDelete() {
		if (Window.confirm("Are you sure you want to delete this presentation?")) {
			deletePresentationRPC();
		}
	}
	
	private void doActionPreview() {
		launchPreview();
		
//		isPreview = true; 
//		
//		doActionSave();
	}
	
	private void launchPreview() {
		Window.open(ConfigurationController.getInstance().getConfiguration().getViewerURL() + "Viewer.html?type=presentation&id=" + presentationId, "_blank", "");
//		Window.open(Globals.VIEWER_URL + "Viewer.html?type=presentation&id=" + presentationId, "_blank", "");
		
//		if (presentationInfo == null)
//			return;
//		
//		saveData();
//		
//		PresentationPreviewController.getInstance().launchPreview(presentationInfo);
	}

	private void doActionCancel() {
		UiEntryPoint.loadContentStatic(ContentId.PRESENTATIONS);
	}

	public void setToken(HistoryTokenInfo tokenInfo) {
		presentationId = tokenInfo.getId();
		fromCompanyId = tokenInfo.getFromCompanyId();
	}

	//--------- PRESENTATION RPC CLASSES ---------------//
	class RpcGetPresentationCallBackHandler extends RiseAsyncCallback<PresentationInfo> {
		public void onFailure() {
			enableButtons();
			if (caught instanceof ServiceFailedException 
					&& ((ServiceFailedException)caught).getReason() == ServiceFailedException.NOT_FOUND) {
//				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "The Presentation was not found, or " +
//						"you do not have access to it from your Company.");
				
				statusBox.clear();
				
				MessageBoxWidget.getInstance().show("Very sorry but we can't find that Presentation. " +
						"It has either been deleted or you don't have access to it from your Company. ");
				UiEntryPoint.loadContentStatic(ContentId.HOME);
			}
			else {
				statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
			}
		}

		public void onSuccess(PresentationInfo result) {
			if (result == null) {
				if (!RiseUtils.strIsNullOrEmpty(fromCompanyId)) {
//					statusBox.setStatus(StatusBoxWidget.Status.ERROR, "This Template is not available to you. " +
//							"If you really want to use it send us the link to this preview " +
//							"at <a href='mailto:support@risevision.com' target='_blank'>support@risevision.com</a> " +
//							"and we will see if we can hook you up with the creator.");
					
					statusBox.clear();
					
					MessageBoxWidget.getInstance().show("Very sorry but this Template is not available to you. " +
							"If you really want to use it send us the link to this preview " +
							"at <a href='mailto:support@risevision.com' target='_blank'>support@risevision.com</a> " +
							"and we will see if we can hook you up with the creator.");
					
					UiEntryPoint.loadContentStatic(ContentId.HOME);
				}
				else {
					statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error retrieving Presentation data. Please try again.");
				}
			}
			else {
				presentationInfo = result;

				if (!RiseUtils.strIsNullOrEmpty(fromCompanyId)) {
					presentationInfo.setName("Copy of " + presentationInfo.getName());
					presentationInfo.setId("");
					presentationInfo.setChangedBy("");
					presentationInfo.setTemplate(false);
					presentationInfo.setDistributionString("");
					
					presentationId = "";
				}
				
				bindData();
			}
			
			enableButtons();
		}
	}

	class RpcPutPresentationCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			enableButtons();
			
			if (fromCompanyId != null && !fromCompanyId.isEmpty() && caught instanceof ServiceFailedException 
						&& ((ServiceFailedException)caught).getReason() == ServiceFailedException.FORBIDDEN) {
				statusBox.clear();
				
				MessageBoxWidget.getInstance().show("Very sorry but you don't have rights to Edit this " +
						"Presentation. Please contact your Administrator to find out about having your rights changed.");
				
				UiEntryPoint.loadContentStatic(ContentId.HOME);
			}
			else {
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error saving Presentation data. Please try again.");
			}
		}

		public void onSuccess(RpcResultInfo result) {
			if (result == null) {
				// unreachable code; result should never be null
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error saving Presentation data. Please try again.");
			}
			else {
				if (result.getErrorMessage() != null) {
					statusBox.setStatus(StatusBoxWidget.Status.ERROR, result.getErrorMessage());
				}
				else {
					//update ID
					presentationId = result.getId();
					
					if (presentationInfo.getId() == null || presentationInfo.getId().isEmpty()) {
	//					savePresentationDataRPC();
						
						HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
						tokenInfo.setId(result.getId());
						tokenInfo.setContentId(ContentId.PRESENTATION_MANAGE);
						
//						String[] params = {result.getId()};
						UiEntryPoint.loadContentStatic(tokenInfo, false);
						
						presentationInfo.setRevisionStatus(PresentationRevisionStatus.PUBLISHED);
					}		
					else {
						presentationInfo.setRevisionStatus(PresentationRevisionStatus.REVISED);
					}

					presentationInfo.setId(presentationId);

					revisionsMenu.init(presentationInfo);
					
					statusBox.clear();
				}
				enableButtons();
				copyPlaceholderButton.setEnabled(false);
			}
		}
	}

	class RpcDeletePresentationCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			if (caught instanceof ServiceFailedException && ((ServiceFailedException)caught).getReason() == ServiceFailedException.CONFLICT) {
				// Delete failed (409 error)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error: Presentation cannot be deleted since it is used in one or more Schedule Items.");
			}
			else {
				statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
			}
			enableButtons();
		}

		public void onSuccess(RpcResultInfo result) {
//			if (result != null && result.getErrorMessage() != null && result.getErrorMessage().equals("409")) {
//				// Submit failed (409 error)
//				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error: Presentation cannot be deleted since it is used in one or more Schedule Items.");
//			}
			if (result != null && result.getErrorMessage() != null) {
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, result.getErrorMessage());
			}
			else if (result == null) {
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error deleting Presentation. Please try again.");
			}
			else {
				statusBox.clear();

				doActionCancel();	
			}
			enableButtons();
		}
	}
	
//	class RpcDeletePlaceholdersCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
//		public void onFailure() {
//			enableButtons();
//			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
//		}
//
//		public void onSuccess(RpcResultInfo result) {
//
//		}
//	}

}