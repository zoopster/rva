package com.risevision.ui.client.display;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.core.api.attributes.DisplayAttribute;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.directory.DisplayDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.info.DisplaysInfo;
import com.risevision.ui.client.common.info.GridInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

public class DistributionSelectWidget extends PopupPanel {
	
	private static DistributionSelectWidgetUiBinder uiBinder = GWT.create(DistributionSelectWidgetUiBinder.class);
	interface DistributionSelectWidgetUiBinder extends UiBinder<Widget, DistributionSelectWidget> {}
	private static DistributionSelectWidget instance;
	//RPC
//	private final DisplayServiceAsync rpc = GWT.create(DisplayService.class);
	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	// fields
	private ArrayList<String> distribution = new ArrayList<String>();
	private Command onSave;
	//UI pieces
	@UiField StatusBoxWidget statusBox;
	@UiField ScrollingGridWidget gr;
	@UiField ActionsWidget wgActions;
	
	public DistributionSelectWidget() {
		super(true, false); //set auto-hide and modal
		add(uiBinder.createAndBindUi(this));
		setWidth("620px");
		initActions();
		styleControls();
	}

	public static DistributionSelectWidget getInstance() {
		try {
			if (instance == null)
				instance = new DistributionSelectWidget();
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
	
	public void show(ArrayList<String> distribution, Command onSaveCallBack){
		super.show( );
		center();
		setDistribution(distribution);
		this.onSave = onSaveCallBack;
		gr.clear();
		loadGridDataRPC();
	}
	
	@UiFactory ScrollingGridWidget createGridWidget(){
		Command grCommand = new Command() {
			public void execute() {			
				processGridCommand();
			}
		};	
		
		String[][] headerDefinition = new String[][] {
				{"", GridInfo.CHECKBOX, "30px"},
				{"Name", DisplayAttribute.NAME, "100%"},
				//{"ID", DisplayAttribute.ID, "100px"},
				//{"Status", DisplayAttribute.STATUS, "50px"},
				{"Resolution", DisplayInfo.RESOLUTION, "80px"},
				{"Address", DisplayAttribute.STREET, "260px"},
				//{"PNO's", DisplayAttribute.CONTROLLED, "20px"},
				//{"Player Version", DisplayAttribute.PLAYERVERSION, "50px"},
//				{"Company", null, "180px"}
				};

		DisplaysInfo displaysInfo = new DisplaysInfo();
		displaysInfo.setSortByDefault(DisplayAttribute.NAME);
		displaysInfo.setSortDirection(ScrollingGridInfo.SORT_DOWN);
		return new ScrollingGridWidget(grCommand, displaysInfo, headerDefinition);		
	}

	private void styleControls() {
		this.getElement().getStyle().setProperty("padding", "10px");
		
		wgActions.addStyleName("rdn-VerticalSpacer");
		gr.setGridHeight("400px");
	}

	private void initActions(){
		Command cmdSave = new Command() {
			public void execute() {
				doActionSave();
			}
		};		

		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};		

		wgActions.addAction("Save", cmdSave);
		wgActions.addAction("Cancel", cmdCancel);
	}	

	private void doActionSave() {
		hide();
		if (onSave != null)
			onSave.execute();
	}

	private void doActionCancel() {
		hide();
	}
	
	private void loadGridDataRPC() {
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		
		DisplayDataController controller = DisplayDataController.getInstance();
		controller.getDisplays(getDisplaysInfo(), true, rpcCallBackHandler);
		
//		rpc.getDisplays(getDisplaysInfo(), true, rpcCallBackHandler);
	}

	private void processGridCommand(){
		int command = gr.getCurrentCommand();
					
		switch (command) {
		case GridInfo.CHECKALLACTION:
			selectAllDisplays(gr.getCurrentCheckBoxValue());
			gr.checkAll(gr.getCurrentCheckBoxValue());
			break;
		case GridInfo.CHECKACTION:
			selectDisplay(gr.getCurrentKey(), gr.getCurrentCheckBoxValue());
			break;
		case GridInfo.SELECTACTION:
			//AddToSelectedList(gr.getCurrentKey());
			break;
		case GridInfo.SEARCHACTION:
		case GridInfo.PAGEACTION:
		case GridInfo.SORTACTION:
			loadGridDataRPC();			
			break;
		default:
			break;
		}
	}

	private void selectDisplay(String currentKey, boolean currentCheckBoxValue) {
		if (currentCheckBoxValue) {
			if (!isDisplaySelected(currentKey))
				distribution.add(currentKey);
		}
		else
			distribution.remove(currentKey);
	}

	private void selectAllDisplays(boolean currentCheckBoxValue) {
		DisplaysInfo displaysInfo = getDisplaysInfo();
		if ((displaysInfo != null) && (displaysInfo.getDisplays() != null))
			for (DisplayInfo display : displaysInfo.getDisplays()) 
				selectDisplay(display.getId(), currentCheckBoxValue);
	}

	private void updateTable(DisplaysInfo displaysInfo) {
		if ((displaysInfo != null) && (displaysInfo.getDisplays() != null)) {
			int i = 0;
			if (gr.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				gr.clearGrid();
			}
			else {
				i = gr.getRowCount();
			}

			for ( ; i < displaysInfo.getDisplays().size(); i++) {
				updateTableRow(displaysInfo.getDisplays().get(i), i);
			}
		}
	}

	private void updateTableRow(final DisplayInfo display, int row) {
		int col = 0; 

		gr.setCheckBox(row, col++, isDisplaySelected(display.getId()), display.getId());
		//gr.setAction(row, col++, "Select", display.getId());
		gr.setText(row, col++, display.getName());
		//gr.setText(row, col++, display.getSubscriptionStatusName());
		gr.setText(row, col++, display.getWidth() +"x"+ display.getHeight());
		gr.setText(row, col++, display.getAddress());
//		gr.setText(row, col++, display.getCompanyName());
	}

	private Boolean isDisplaySelected(String displayId) {
			for (String s: distribution) 
				if (s != null && s.equals(displayId))
					return true;				
		return false;
	}

	private DisplaysInfo getDisplaysInfo(){
		DisplaysInfo gridInfo = (DisplaysInfo) gr.getGridInfo();
		
//		if (gridInfo == null){
//			gridInfo = new DisplaysInfo();
//		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}
	
//	public void selectDistributionDisplays() {
//			for (String s: distribution) {
//				selectDisplay(s, true);
//			}
//	}
	
	public void setDistribution(ArrayList<String> distribution) {
		this.distribution = new ArrayList<String>();
		if (distribution != null)
			this.distribution.addAll(distribution);
	}

	public ArrayList<String> getDistribution() {
		return distribution;
	}

	class RpcCallBackHandler extends RiseAsyncCallback<DisplaysInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(DisplaysInfo result) {
			statusBox.clear();
			gr.loadGrid(result);
			updateTable(result);
			
			center();
		}
	}
}