package com.risevision.ui.client.common.widgets.demoContent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.DemoContentInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.client.common.service.CompanyServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.DefaultResolutionWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.presentation.PresentationSelectWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DemoContentManageWidget extends PopupPanel {
	private static DemoContentManageWidget instance;
	private DemoContentInfo demoContentItem;
	private boolean itemIsNew;
	private String objectRef;
	//Get Presentation name Utils
	//RPC
	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	//UI pieces
	private int row = -1;
	private VerticalPanel mainPanel = new VerticalPanel();
	//UI: Item type select panel
	private Label titleLabel = new Label("Content");
	//UI: Schedule Item manage fields
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private Grid mainGrid = new Grid(2, 2);
	private DefaultResolutionWidget resolutionWidget = new DefaultResolutionWidget();
	private HorizontalPanel presentationPanel = new HorizontalPanel();
	private TextBox selectedContentIdBox = new TextBox();
	private Label presentationLabel = new Label();
	private Anchor presentationChangeLink = new Anchor("change");
	private Anchor presentationCancelLink = new Anchor("cancel");
	private PresentationSelectWidget presentationSelectWidget = new PresentationSelectWidget(true);
	private Command onChange;
	
	private ActionsWidget actionWidget = new ActionsWidget();

	//last modified
	private LastModifiedWidget wgLastModified = new LastModifiedWidget();

	public DemoContentManageWidget() {
		super(true, false); //set auto-hide and modal
		setSize("600px", "100%");
		add(mainPanel);

		presentationPanel.add(presentationLabel);
		presentationPanel.add(new SpacerWidget());
		presentationPanel.add(presentationChangeLink);
		presentationPanel.add(presentationCancelLink);
		
		gridAdd("Resolution*:", resolutionWidget, null);
		gridAdd("Presentation*:", presentationPanel, null);
				
		mainPanel.add(titleLabel);
		mainPanel.add(statusBox);
		mainPanel.add(formValidator);
		mainPanel.add(mainGrid);
		mainPanel.add(presentationSelectWidget);
//		secondPanel.add(wgLastModified);
		mainPanel.add(actionWidget);
		mainPanel.setCellWidth(actionWidget, "350px");
		
		styleControls();

		initActions();
		initValidator();
	}

	private void styleControls() {
		//style the table	
		mainGrid.setCellSpacing(0);
		mainGrid.setCellPadding(0);
		mainGrid.setStyleName("rdn-Table");

		titleLabel.setStyleName("rdn-Head");
				
		presentationLabel.addStyleName("rdn-OverflowElipsis");
		presentationLabel.setWidth("175px");

		this.getElement().getStyle().setProperty("padding", "10px");
		actionWidget.addStyleName("rdn-VerticalSpacer");
	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(selectedContentIdBox, "No Content has been selected!", FormValidatorInfo.requiredFieldValidator, true);
		resolutionWidget.setValidator(formValidator);
	}

	private void initActions() {		
		Command selectCommand = new Command() {
			public void execute() {
				contentSelected();
			}
		};
		
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
		
		presentationSelectWidget.init(selectCommand);
		
		actionWidget.addAction("Save", cmdSave);
		actionWidget.addAction("Cancel", cmdCancel);
		
		initChangeLink();
	}
	
	private void contentSelected() {
		PresentationInfo presentation = presentationSelectWidget.getCurrentPresentation();
		
		if (presentation != null) {
			objectRef = presentation.getId();
			selectedContentIdBox.setText(objectRef);
			presentationLabel.setText(presentation.getName());
		}

		showSelectPanel(false);
		
		center();
	}
	
	private void initChangeLink() {
		presentationChangeLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showSelectPanel(true);
			}
		});
		
		presentationCancelLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showSelectPanel(false);
			}
		});
	}

	public static DemoContentManageWidget getInstance() {
		try {
			if (instance == null)
				instance = new DemoContentManageWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

//	protected void onLoad() {
//		super.onLoad();
//	}
	
	public void load() {
		presentationSelectWidget.load();
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
	
	public void show(DemoContentInfo demoContentItem, Command onChange){
		this.show(demoContentItem, false, onChange);
	}

	public void show(DemoContentInfo demoContentItem, boolean isNew, Command onChange){
		super.show();
			
		this.demoContentItem = demoContentItem;
		this.itemIsNew = isNew;
		this.onChange = onChange;
		bindData();
		
		center();
	}

	private void bindData() {
		if (demoContentItem == null)
			return;

		objectRef = "";
		selectedContentIdBox.setValue("");
		formValidator.clear();
		if (demoContentItem.getObjectRef() != null && !demoContentItem.getObjectRef().isEmpty()){
			showSelectPanel(false);
			
			objectRef = demoContentItem.getObjectRef();
			selectedContentIdBox.setText(demoContentItem.getObjectRef());
			presentationLabel.setText(demoContentItem.getObjectName());
		}
		else {
			showSelectPanel(true);
			presentationCancelLink.setVisible(false);
			
			presentationLabel.setText("select presentation");
		}
		resolutionWidget.setValue(demoContentItem.getWidth(), demoContentItem.getHeight());
		
		wgLastModified.Initialize(demoContentItem.getChangedBy(), demoContentItem.getChangeDate());
	}
	
	private void showSelectPanel(boolean show) {
		presentationSelectWidget.setVisible(show);
		presentationCancelLink.setVisible(show);
		
		presentationChangeLink.setVisible(!show);
		
		center();
	}

	private boolean saveData() {
		if (demoContentItem == null)
			return true;

		if (!formValidator.validate())
			return false;

		demoContentItem.setWidth(resolutionWidget.getWidth());
		demoContentItem.setHeight(resolutionWidget.getHeight());
		demoContentItem.setObjectName(presentationLabel.getText());
		demoContentItem.setObjectRef(objectRef);
		
		saveDataRPC();

		return true;
	}

	private void gridAdd(String label, Widget widget, String styleName) {
		row++;
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		mainGrid.setText(row, 0, label);
		if (widget != null)
		{
			mainGrid.setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}

	private void saveDataRPC() {
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
		companyService.saveDemoContent(SelectedCompanyController.getInstance()
				.getSelectedCompanyId(), demoContentItem, new SaveRpcCallBackHandler());
	}

	class SaveRpcCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			statusBox.clear();
		}
	}
	
	private void doActionSave() {
		if (saveData()) {
			hide();
			if (onChange != null)
				onChange.execute();
		}
	}

	private void doActionCancel() {
		hide();
	}

	public void setDemoContentItem(DemoContentInfo demoContentItem) {
		this.demoContentItem = demoContentItem;
	}

	public DemoContentInfo getDemoContentItem() {
		return demoContentItem;
	}

	public void setItemIsNew(boolean itemIsNew) {
		this.itemIsNew = itemIsNew;
	}

	public boolean getItemIsNew() {
		return itemIsNew;
	}

}