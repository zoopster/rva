// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.schedule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.PlaylistItemInfo;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.service.PresentationService;
import com.risevision.ui.client.common.service.PresentationServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.NumericBoxWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.timeline.TimelineWidget;
import com.risevision.ui.client.display.DistributionWidget;
import com.risevision.ui.client.presentation.PresentationSelectWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ScheduleItemManageWidget extends PopupPanel {
	private static ScheduleItemManageWidget instance;
	private PlaylistItemInfo playListItem;
	private boolean itemIsNew;
	private String objectRef;
	private int itemIndex;
	//Get Presentation name Utils
	//RPC
	private final PresentationServiceAsync presentationService = GWT.create(PresentationService.class);
	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	//UI pieces
	private int row = -1;
	private VerticalPanel mainPanel = new VerticalPanel();
	//UI: Item type select panel
	private Label titleLabel = new Label("Content");
	private HorizontalPanel typePanel = new HorizontalPanel();
	private RadioButton presentationSelection = new RadioButton("itemTypeSelect", "Presentation");
	private RadioButton urlSelection = new RadioButton("itemTypeSelect", "URL");
	private PresentationSelectWidget presentationSelectWidget = new PresentationSelectWidget(true);
	//UI: Schedule Item manage fields
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private Grid topGrid = new Grid(1, 2);
	private Grid contentGrid = new Grid(2, 2);
	private Grid manageGrid = new Grid(2, 2);
	private TextBox tbName = new TextBox();
	private TextBox urlTextBox = new TextBox();
	private HorizontalPanel presentationPanel = new HorizontalPanel();
	private Label presentationLabel = new Label();
	private Anchor presentationChangeLink = new Anchor("change");
	private NumericBoxWidget tbDuration = new NumericBoxWidget();
	private Command onChange;
	
	private ActionsWidget actionWidget = new ActionsWidget();
	
	private TimelineWidget wgTimeline = new TimelineWidget();	
	private DistributionWidget wgDistribution = new DistributionWidget();	
	//last modified
//	private LastModifiedWidget wgLastModified = new LastModifiedWidget();

	public ScheduleItemManageWidget() {
		super(true, true); //set auto-hide and modal
		setSize("600px", "100%");
		add(mainPanel);
		
		typePanel.add(presentationSelection);
		typePanel.add(new SpacerWidget());
		typePanel.add(urlSelection);

		presentationPanel.add(presentationLabel);
		presentationPanel.add(new SpacerWidget());
		presentationPanel.add(presentationChangeLink);
		
		gridAdd(topGrid, "Name*:", tbName, "rdn-TextBoxMedium");
		row = -1;
		gridAdd(contentGrid, "URL*:", urlTextBox, "rdn-TextBoxMedium");
		gridAdd(contentGrid, "Presentation:", presentationPanel, null);
		row = -1;
		gridAdd(manageGrid, "Duration*:", tbDuration, "rdn-TextBoxShort");
		gridAdd(manageGrid, "Timeline:", wgTimeline, null);
//		gridAdd(manageGrid, "Distribution:", wgDistribution, null);
				
		mainPanel.add(titleLabel);
		mainPanel.add(statusBox);
		mainPanel.add(formValidator);
		mainPanel.add(topGrid);
		mainPanel.add(typePanel);
		mainPanel.add(contentGrid);
		mainPanel.add(presentationSelectWidget);
		mainPanel.add(manageGrid);
//		secondPanel.add(wgLastModified);
		mainPanel.add(actionWidget);
		mainPanel.setCellWidth(actionWidget, "350px");
		
		styleControls();

		initActions();
		initValidator();
	}

	private void styleControls() {
		//style the table	
		topGrid.setCellSpacing(0);
		topGrid.setCellPadding(0);
		topGrid.setStyleName("rdn-Table");
		contentGrid.setCellSpacing(0);
		contentGrid.setCellPadding(0);
		contentGrid.setStyleName("rdn-Table");
		manageGrid.setCellSpacing(0);
		manageGrid.setCellPadding(0);
		manageGrid.setStyleName("rdn-Table");
		
		presentationSelection.addStyleName("rdn-CheckBox");
		urlSelection.addStyleName("rdn-CheckBox");
		
		titleLabel.setStyleName("rdn-Head");
		
		urlTextBox.setStyleName("rdn-TextBoxMedium");
		
		presentationLabel.addStyleName("rdn-OverflowElipsis");
		presentationLabel.setWidth("175px");

		this.getElement().getStyle().setProperty("padding", "10px");
		actionWidget.addStyleName("rdn-VerticalSpacer");
		
		presentationSelectWidget.getElement().getStyle().setPaddingTop(6, Unit.PX);
		presentationSelectWidget.getElement().getStyle().setPaddingBottom(6, Unit.PX);
	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(tbName, "Name", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(tbDuration, "Duration", FormValidatorInfo.requiredFieldValidator);
	}

	private void initActions() {
		ValueChangeHandler<Boolean> radioSelected = new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				contentGrid.getRowFormatter().setVisible(0, !presentationSelection.getValue());
				presentationSelectWidget.setVisible(presentationSelection.getValue());
				
				formValidator.clear();
				if (presentationSelection.getValue())
					formValidator.removeValidationElement(urlTextBox);
				else
					formValidator.addValidationElement(urlTextBox, "URL", FormValidatorInfo.requiredFieldValidator);
			}
		};
		
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
		
		urlSelection.addValueChangeHandler(radioSelected);
		presentationSelection.addValueChangeHandler(radioSelected);
		presentationSelection.setValue(true, true);
		
		presentationSelectWidget.init(selectCommand);
		
		actionWidget.addAction("Okay", cmdSave);
		actionWidget.addAction("Cancel", cmdCancel);
		
		initChangeLink();
	}
	
	private void contentSelected() {
		PresentationInfo presentation = presentationSelectWidget.getCurrentPresentation();
		
		if (presentation != null) {
			objectRef = presentation.getId();
			presentationLabel.setText(presentation.getName());
			tbName.setText(presentation.getName());
			
			showPresentationLabel(presentationSelection.getValue());
		}

		center();
	}
	
	private void showSelectWidget(boolean isSelect) {
		boolean newItem = true;
		if ((objectRef != null && !objectRef.isEmpty()) || (urlTextBox.getText() != null && !urlTextBox.getText().isEmpty()))
			newItem = false;
		
		typePanel.setVisible(newItem && isSelect);
		
		if (isSelect) {
			presentationSelection.setValue(presentationSelection.getValue(), true);
			presentationSelectWidget.setVisible(presentationSelection.getValue());
		}
		else {
			presentationSelectWidget.setVisible(false);
		}
	}
	
	private void showPresentationLabel(boolean show){
		presentationSelection.setValue(show, true);
		showSelectWidget(false);
		
		contentGrid.getRowFormatter().setVisible(0, !show);
		contentGrid.getRowFormatter().setVisible(1, show);
	}
	
	private void initChangeLink() {
		presentationChangeLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showSelectWidget(true);
			}
		});
	}

	public static ScheduleItemManageWidget getInstance() {
		try {
			if (instance == null)
				instance = new ScheduleItemManageWidget();
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
	
	public void show(PlaylistItemInfo playListItem, Command onChange){
		this.show(playListItem, false, -1, onChange);
	}

	public void show(PlaylistItemInfo playListItem, boolean isNew, int itemIndex, Command onChange){
		super.show();
			
		this.playListItem = playListItem;
		this.itemIsNew = isNew;
		this.itemIndex = itemIndex;
		this.onChange = onChange;
		bindData();
		
		tbName.setFocus(true);
		center();
	}

	private void bindData() {
		if (getPlayListItem() == null)
			return;

		objectRef = "";
		urlTextBox.setValue("");
		formValidator.clear();
		if (playListItem.getType() != null){
			showSelectWidget(false);
			if (playListItem.getType().equals(PlaylistItemInfo.TYPE_PRESENTATION)) {
				objectRef = playListItem.getObjectRef();
				loadPresentationName();
				
				showPresentationLabel(true);
			} else {
				urlTextBox.setText(getPlayListItem().getObjectRef());
				showPresentationLabel(false);
			}
		}
		else {
			contentGrid.getRowFormatter().setVisible(1, false);
			showSelectWidget(true);
			presentationSelection.setValue(true, true);
		}
		
		tbName.setText(getPlayListItem().getName());
		tbDuration.setText(getPlayListItem().getDuration());
		
		wgTimeline.setTimeline(getPlayListItem().getTimeline());
		//TODO:remove description
//		wgTimeline.setTimelineDescription(getPlayListItem().getTimelineDescription());
		wgDistribution.setDistributionToAll(getPlayListItem().getDistributionToAll());
		wgDistribution.setDistribution(getPlayListItem().getDistribution());
		
//		wgLastModified.Initialize(getPlayListItem().getChangedBy(), getPlayListItem().getChangeDate());
	}

	private boolean saveData() {
		if (playListItem == null)
			return true;

		if (!formValidator.validate())
			return false;
		
//		playListItem.setChanged(true);
		
		playListItem.setName(tbName.getText());
		
		if (presentationSelection.getValue()){
			playListItem.setObjectRef(objectRef);
			playListItem.setType(PlaylistItemInfo.TYPE_PRESENTATION);
		} else {
			playListItem.setObjectRef(urlTextBox.getText());
			playListItem.setType(PlaylistItemInfo.TYPE_URL);
		}
		
		playListItem.setDuration(tbDuration.getText());

		playListItem.setTimeline(wgTimeline.getTimeline());
		
		// AD - Temporary All Displays until Distribution is re-enabled
		playListItem.setDistributionToAll(true);
//		getPlayListItem().setDistributionToAll(wgDistribution.getDistributionToAll());
//		getPlayListItem().setDistribution(wgDistribution.getDistribution());

		return true;
	}

	private void loadPresentationName(){
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		presentationService.getPresentation(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
				objectRef, rpcCallBackHandler);
	}
	
	class RpcCallBackHandler extends RiseAsyncCallback<PresentationInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(PresentationInfo result) {
			statusBox.clear();
			if (result != null)
				presentationLabel.setText(result.getName());
		}
	}
	
	private void gridAdd(Grid grid, String label, Widget widget, String styleName) {
		row++;
		grid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		grid.setText(row, 0, label);
		if (widget != null)
		{
			grid.setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
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

	public void setPlayListItem(PlaylistItemInfo playListItem) {
		this.playListItem = playListItem;
	}

	public PlaylistItemInfo getPlayListItem() {
		return playListItem;
	}

	public void setItemIsNew(boolean itemIsNew) {
		this.itemIsNew = itemIsNew;
	}

	public boolean getItemIsNew() {
		return itemIsNew;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public int getItemIndex() {
		return itemIndex;
	}
}