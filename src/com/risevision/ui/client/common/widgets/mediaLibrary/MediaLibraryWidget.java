package com.risevision.ui.client.common.widgets.mediaLibrary;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.EnabledFeaturesInfo;
import com.risevision.ui.client.common.info.GridInfo;
import com.risevision.ui.client.common.info.MediaItemInfo;
import com.risevision.ui.client.common.info.MediaItemsInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.lists.SearchSortController;
import com.risevision.ui.client.common.lists.SearchSortable;
import com.risevision.ui.client.common.service.MediaLibraryService;
import com.risevision.ui.client.common.service.MediaLibraryServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MediaLibraryWidget extends PopupPanel {
	private static String ACKNOWLEDGE_TEXT = "I acknowledge that there is currently no charge " +
			"for the use of the Media Library and that at an as of yet to be determined date in the " +
			"future, there will be a cost and at that time I will have 30 days to decide if I wish to " +
			"continue using this Media Library or not.";
	
//	private static String NOTIFICATION_TEXT = "We are replacing the Media Library with Google " +
//			"Drive. From this point forward please use the Drive option rather than Media Library" +
//			" for your media storage purposes.";
	
	private static MediaLibraryWidget instance;
	//RPC
	private MediaLibraryServiceAsync mediaLibraryService = GWT.create(MediaLibraryService.class);
	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private Label titleLabel = new Label("Media Library");
	private ActionsWidget actionsWidget = new ActionsWidget();
	
	private HorizontalPanel acknoweledgePanel = new HorizontalPanel();
	private CheckBox acknowledgeCheckBox = new CheckBox();
	private Label acknoweledgeLabel = new Label(ACKNOWLEDGE_TEXT);
//	private Label notificationLabel = new Label(NOTIFICATION_TEXT, true);
	
	private MediaLibraryUploadWidget uploadWidget = MediaLibraryUploadWidget.getInstance();
	//grid
	private ScrollingGridWidget gr;
	private Command grCommand;
	private Command selectCommand;
	private String bucketName;
	
	private ArrayList<String> selectedItems = new ArrayList<String>();
	
	public MediaLibraryWidget() {
		super(true, false); //set auto-hide and modal
		
		mainPanel.add(titleLabel);
		mainPanel.setCellHeight(titleLabel, "20px");
		
		mainPanel.add(statusBox);
		mainPanel.setCellHeight(statusBox, "1px");

//		mainPanel.add(notificationLabel);
//		mainPanel.setCellHeight(notificationLabel, "40px");

		initGridWidget();
		mainPanel.add(gr);
		mainPanel.add(actionsWidget);

		acknoweledgePanel.add(acknowledgeCheckBox);
		acknoweledgePanel.add(acknoweledgeLabel);
		
		mainPanel.add(acknoweledgePanel);
		
		add(mainPanel);

		styleControls();
		initActions();	
		
		initWidget();
	}
	
	private void styleControls() {
		setPixelSize(600, 400);
		getElement().getStyle().setZIndex(1000);
		
		titleLabel.setStyleName("rdn-Head");
		
		this.getElement().getStyle().setProperty("padding", "10px");

		acknowledgeCheckBox.setStyleName("rdn-CheckBox");
//		notificationLabel.addStyleName("rdn-Validator");
		
		actionsWidget.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
	
	}
	
	private void initActions() {
		Command deleteCommand = new Command() {
			@Override
			public void execute() {
				deleteItems();
			}
		};
		
		Command closeCommand = new Command() {
			@Override
			public void execute() {
				hide();
			}
		};
		
		actionsWidget.add(uploadWidget);
		actionsWidget.addAction("Delete", deleteCommand);
		actionsWidget.addAction("Cancel", closeCommand);

		uploadWidget.setStatusBox(statusBox);
		
		acknowledgeCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					SelectedCompanyController.getInstance().saveSelectedCompanyFeature(EnabledFeaturesInfo.FEATURE_MEDIA_LIBRARY, true);
					
					initWidget();
				}
			}
		});
	}

	private void initGridWidget(){
		grCommand = new Command() {
			public void execute() {			
				processGridCommand();
			}
		};			
		
		String[][] headerDefinition = new String[][] {
//				{"", null, "50px"},
				{"", GridInfo.CHECKBOX, "30px"},
				{"File Name", MediaItemInfo.KEY_ATTRIBUTE, "100%"},
				{"Size", MediaItemInfo.SIZE_ATTRIBUTE, "100px"},
				{"Last Modified", MediaItemInfo.LAST_MODIFIED_ATTRIBUTE, "100px"},
//				{"", null, "50px"}
				};

		gr = new ScrollingGridWidget(grCommand, new MediaItemsInfo(), headerDefinition);
		
		gr.setGridHeight("400px");
	}	
	
	public static MediaLibraryWidget getInstance() {
		try {
			if (instance == null)
				instance = new MediaLibraryWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	

	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		// Use the popup's key preview hooks to close the dialog when either
		// enter or escape is pressed.
		switch (key) {
			case KeyCodes.KEY_ESCAPE:
				hide();
				break;
			}

		return true;
	}
	
	public void load() {
		gr.clear();
		
		selectedItems.clear();
		
		bucketName = "risemedialibrary-" + SelectedCompanyController.getInstance().getSelectedCompanyId();
		
		uploadWidget.setBucketName(bucketName);
		uploadWidget.setRequireBucketCreation(false);
		
		loadGridDataRPC();
	}

	public void show() {
		show(null);
	}
	
	public void show(Command selectCommand){
		this.selectCommand = selectCommand;
				
		statusBox.clear();

		initWidget();
		
		super.show();
		
		center();
	}
	
	private void initWidget() {
		boolean isEnabled = SelectedCompanyController.getInstance().isSelectedCompanyFeature(EnabledFeaturesInfo.FEATURE_MEDIA_LIBRARY);

		acknowledgeCheckBox.setValue(isEnabled);
		acknowledgeCheckBox.setEnabled(!isEnabled);
		
		actionsWidget.setEnabled(isEnabled, "Delete");
		uploadWidget.setEnabled(isEnabled);
	}
	
	public String getItemUrl() {
		String url = "";
		
		if (!RiseUtils.strIsNullOrEmpty(gr.getCurrentKey())) {
			url = MediaItemInfo.MEDIA_LIBRARY_URL + bucketName + "/" + URL.encode(gr.getCurrentKey());
		}
		
		return url;
	}
	
	private void loadGridDataRPC() {
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		
//		String parentCompanyId;
//		CompanyInfo selectedCompany = SelectedCompanyController.getInstance().getSelectedCompany();
//		if (selectedCompany.isPno())
//			parentCompanyId = selectedCompany.getId();
//		else
//			parentCompanyId = selectedCompany.getParentId();
//		// parentCompanyId = "" for the top level PNO only.
//		if (RiseUtils.strIsNullOrEmpty(parentCompanyId))
//			parentCompanyId = SelectedCompanyController.getInstance().getUserCompanyId();

		mediaLibraryService.getBucketItems(bucketName, rpcCallBackHandler);
	}

	private void processGridCommand(){
		int command = gr.getCurrentCommand();
					
		switch (command) {
		case GridInfo.CHECKALLACTION:
			selectAllItems(gr.getCurrentCheckBoxValue());
			break;
		case GridInfo.CHECKACTION:
			selectItem(gr.getCurrentKey(), gr.getCurrentCheckBoxValue());
			break;
//		case ScrollingGridInfo.DELETEACTION:
//			deleteItems(gr.getCurrentKey());
//			break;
		case ScrollingGridInfo.SELECTACTION:
			if (selectCommand != null) {
				selectCommand.execute();
			}
			hide();
			break;
		case ScrollingGridInfo.SEARCHACTION:
		case ScrollingGridInfo.SORTACTION:
//		case ScrollingGridInfo.PAGEACTION:
			performSearchSort();
//			loadGridDataRPC();			
			break;
		default:
			break;
		}
	}
	
	private void performSearchSort() {
		MediaItemsInfo mediaItems = getMediaItemsInfo();
		ArrayList<? extends SearchSortable> items = mediaItems.getMediaItems();
		String searchFor = mediaItems.getSearchFor() != null ? mediaItems.getSearchFor() : "";
		searchFor = searchFor.replaceAll(" ", "+");
		
		items = SearchSortController.search(items, searchFor);
		items = SearchSortController.sort(items, mediaItems.getSortBy(), mediaItems.getSortDirection().equals(ScrollingGridInfo.SORT_UP));
		
		updateTable(items);
	}
	
	private void selectItem(String currentKey, boolean currentCheckBoxValue) {
		if (currentCheckBoxValue) {
			if (!isItemSelected(currentKey))
				selectedItems.add(currentKey);
		}
		else
			selectedItems.remove(currentKey);
	}

	private void selectAllItems(boolean currentCheckBoxValue) {
		gr.checkAll(currentCheckBoxValue);

		MediaItemsInfo mediaItems = getMediaItemsInfo();
		if ((mediaItems != null) && (mediaItems.getMediaItems() != null))
			for (MediaItemInfo mediaItem : mediaItems.getMediaItems()) 
				selectItem(mediaItem.getKey(), currentCheckBoxValue);
	}

	private Boolean isItemSelected(String displayId) {
		for (String s: selectedItems) 
			if (s.equals(displayId))
				return true;				
		return false;
	}
	
	private void updateTable(ArrayList<? extends SearchSortable> items) {
		if (getMediaItemsInfo().getMediaItems() != null) {
			int i = 0;
			if (gr.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				gr.clearGrid();
			}
			else {
				i = gr.getRowCount();
			}

			for ( ; i < items.size(); i++) {
				updateTableRow((MediaItemInfo) items.get(i), i);
			}
		}
	}

	private void updateTableRow(final MediaItemInfo mediaItem, int row) {
		int col = 0;
		gr.setCheckBox(row, col++, isMediaItemSelected(mediaItem.getKey()), mediaItem.getKey());
//		gr.setAction(row, col++, "Select", mediaItem.getKey());
		gr.setAction(row, col++, mediaItem.getKey().replace("+", " "), mediaItem.getKey());
		gr.setText(row, col++, mediaItem.getSizeString());
		gr.setText(row, col++, LastModifiedWidget.getLastModified(mediaItem.getLastModified()));
//		gr.setAction(row, col++, "Delete", ScrollingGridInfo.DELETEACTION, mediaItem.getKey());
	}
	
	private Boolean isMediaItemSelected(String displayId) {
		for (String s: selectedItems) 
			if (s.equals(displayId))
				return true;				
		return false;
	}
	
//	private void deleteItem(String itemName) {
//		if (Window.confirm("Are you sure you want to delete the following file: " + itemName + "?")) {
//			statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Deleting Item...");
//	
//			mediaLibraryService.deleteMediaItem(bucketName, itemName, new RpcDeleteCallBackHandler());
//		}
//	}
	
	private void deleteItems() {
		if (selectedItems.size() > 0) {
			if (Window.confirm("Are you sure you want to delete these " + selectedItems.size() + " files?")) {
				statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Deleting Items...");
		
				mediaLibraryService.deleteMediaItems(bucketName, selectedItems, new RpcDeleteCallBackHandler());
			}
		}
	}
	
	private MediaItemsInfo getMediaItemsInfo(){
		MediaItemsInfo gridInfo = (MediaItemsInfo) gr.getGridInfo();
		
//		if (gridInfo == null){
//			gridInfo = new MediaItemsInfo();
//		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}

	class RpcCallBackHandler implements AsyncCallback<MediaItemsInfo> {
		public void onFailure(Throwable caught) {
			if (caught instanceof ServiceFailedException 
					&& ((ServiceFailedException)caught).getReason() == ServiceFailedException.NOT_FOUND) {
				// bucket not found
				uploadWidget.setRequireBucketCreation(true);
			}
			else {
				statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
			}
		}
		
		public void onSuccess(MediaItemsInfo result) {	
			statusBox.clear();
			gr.loadGrid(result);
			updateTable(result.getMediaItems());
		}
	}
	
	class RpcDeleteCallBackHandler implements AsyncCallback<Void> {
		public void onFailure(Throwable caught) {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}
		
		public void onSuccess(Void result) {	
			statusBox.clear();
			
			load();
		}
	}

}