// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.placeholder;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.PlaylistItemInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.DurationWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.TooltipLabelWidget;
import com.risevision.ui.client.common.widgets.timeline.TimelineWidget;
import com.risevision.ui.client.display.DistributionWidget;
import com.risevision.ui.client.gadget.GadgetSelectWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PlaylistItemManageWidget extends PopupPanel {
	private static PlaylistItemManageWidget instance;
	
	private PlaylistItemInfo playlistItem;
	private boolean itemIsNew;
	private int itemIndex;
	
//	private int durationRow;
	
	//UI pieces
	private int row = -1;
	//UI: Item type select panel
	private DeckPanel contentDeckPanel = new DeckPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label titleLabel = new Label("Item");
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	//UI: Placeholder manage fields
	private FlexTable mainGrid = new FlexTable();
	
	private TextBox nameTextBox = new TextBox();
	private DurationWidget durationWidget = new DurationWidget(false);
	private TimelineWidget wgTimeline = new TimelineWidget();
	private DistributionWidget wgDistribution = new DistributionWidget();
//	private TransitionWidget transitionWidget = new TransitionWidget();

	private PlaceholderItemManageWidget itemManageWidget;
	
	private Command onChange, itemSaved;
	private ActionsWidget actionWidget = new ActionsWidget();
	private GadgetSelectWidget gadgetSelectWidget = new GadgetSelectWidget(actionWidget);

	public PlaylistItemManageWidget() {
		super(false, false); //set auto-hide and modal
		add(mainPanel);
		
		mainPanel.add(titleLabel);
		mainPanel.add(statusBox);
		mainPanel.add(formValidator);
		mainPanel.add(contentDeckPanel);
		mainPanel.add(actionWidget);

		contentDeckPanel.add(gadgetSelectWidget);
		contentDeckPanel.add(mainGrid);
		
		// add widgets
		gridAdd("Name*:",
				null, 
				nameTextBox, "rdn-TextBoxMedium");
		gridAdd("Duration*:", 
				"The Duration in seconds that Content is shown or show until done", 
				durationWidget, null);
//		durationRow = row;
		gridAdd("Timeline:", 
				"When this content shows on Displays",
				wgTimeline, null);
		gridAdd("Distribution:", 
				"Which Displays show this content",
				wgDistribution, null);
//		gridAdd("Transition:", transitionWidget, null);
		
		styleControls();

		initActions();
		initValidator();
		
		itemManageWidget = new PlaceholderItemManageWidget(mainGrid, itemSaved);
	}

	private void styleControls() {
		setSize("500px", "100%");
		
		//style the table	
		mainGrid.setCellSpacing(0);
		mainGrid.setCellPadding(0);
		mainGrid.setStyleName("rdn-Table");

		titleLabel.setStyleName("rdn-Head");
		
		nameTextBox.setStyleName("rdn-TextBoxMedium");

		this.getElement().getStyle().setProperty("padding", "10px");

		actionWidget.addStyleName("rdn-VerticalSpacer");
	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(nameTextBox, "Name", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(durationWidget, "Duration", FormValidatorInfo.requiredFieldValidator);
	}

	private void initActions() {			
		Command cmdSave = new Command() {
			public void execute() {
				if (contentDeckPanel.getVisibleWidget() == 0) {
					contentSelected();
				}
				else {
					doActionSave();
				}
			}
		};		

//		Command cmdDelete = new Command() {
//			public void execute() {
//				doActionDelete();
//			}
//		};
		
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};		

//		actionWidget.addAction("Preview", cmdPreview);
		actionWidget.addAction("Save", cmdSave);
//		actionWidget.addAction("Delete", cmdDelete);
		actionWidget.addAction("Cancel", cmdCancel);
		
		Command selectCommand = new Command() {
			public void execute() {
				contentSelected();
			}
		};
		
		gadgetSelectWidget.init(selectCommand);
		
		itemSaved = new Command() {
			@Override
			public void execute() {
				onItemSaved();
			}
		};
	}
	
	private void contentSelected(){
		GadgetInfo gadget = gadgetSelectWidget.getCurrentGadget();
		
		if (gadget != null) {
			showSelectPanel(false);
			formValidator.clear();
		}
		
		itemManageWidget.show(playlistItem, gadget);
		bindData();
	}
	
	private void showSelectPanel(boolean show) {
		actionWidget.setVisible(!show, "Save");
		
		contentDeckPanel.showWidget(show ? 0 : 1);
		if (show) gadgetSelectWidget.show();	
	}
	
	public static PlaylistItemManageWidget getInstance() {
		try {
			if (instance == null)
				instance = new PlaylistItemManageWidget();
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
		gadgetSelectWidget.load();
	}
	
//	public void show(String itemType, int itemIndex) {
//		show(null, itemType, itemIndex);
//	}
	
	public void show(PlaylistItemInfo playlistItem, int itemIndex){
		show(playlistItem, itemIndex, false);	
	}
	
	public void show(PlaylistItemInfo playlistItem, int itemIndex, boolean itemIsNew){
		super.show();

		this.itemIsNew = itemIsNew;
		
		this.playlistItem = playlistItem;
		this.itemIndex = itemIndex;
		formValidator.clear();
		bindData();
		
		if (PlaylistItemInfo.TYPE_GADGET.equals(playlistItem.getType()) && 
				(playlistItem.getObjectData() == null || 
						playlistItem.getObjectData().isEmpty())){
			showSelectPanel(true);
		}
		else {
			showSelectPanel(false);
						
			itemManageWidget.show(playlistItem);
		}
		
		nameTextBox.setFocus(true);
		center();
	}
	
	public void hide() {
		super.hide();
		
		itemManageWidget.hide();
	}
	
	public void center() {
		super.center();
		movePopup(15, -5);
	}
	
	// leftMove and topMove are defined as % values.
	private void movePopup(int leftMove, int topMove) {
		int left, top;
		left = (int) (getAbsoluteLeft() + ((Window.getClientWidth() / 100.0) * leftMove));
		top = (int) (getAbsoluteTop() + ((Window.getClientHeight() / 100.0) * topMove));
		
		top = top < 0 ? 0 : top;
		left = left < 0 ? 0 : left;
		
		setPopupPosition(left, top);
	}

	private void bindData() {
		if (playlistItem == null)
			return;

		nameTextBox.setValue(playlistItem.getName());
		
//		mainGrid.getRowFormatter().setVisible(durationRow, !PlaylistItemInfo.TYPE_VIDEO.equals(playlistItem.getType()));
		durationWidget.setDuration(playlistItem.getDuration(), playlistItem.isPlayUntilDone());
		durationWidget.showPlayUntilDone(playlistItem.showPlayUntilDone());
		
		wgTimeline.setTimeline(playlistItem.getTimeline());

		wgDistribution.setDistributionToAll(playlistItem.getDistributionToAll());
		wgDistribution.setDistribution(playlistItem.getDistribution());
		
//		transitionWidget.setTransition(playlistItem.getTransition());
	}

	private boolean saveData() {
		if (playlistItem == null)
			return true;

		if (!formValidator.validate())
			return false;
		
//		playlistItem.setChanged(true);

		playlistItem.setName(nameTextBox.getText());
		playlistItem.setDuration(durationWidget.getDuration());
//		if (!PlaylistItemInfo.TYPE_VIDEO.equals(playlistItem.getType())) {
			playlistItem.setPlayUntilDone(durationWidget.getPlayUntilDone());
//		}

		playlistItem.setTimeline(wgTimeline.getTimeline());
		
		playlistItem.setDistributionToAll(wgDistribution.getDistributionToAll());
		playlistItem.setDistribution(wgDistribution.getDistribution());
//		playlistItem.setTransition(transitionWidget.getTransition());
		
		return true;
	}
	
	private void gridAdd(String label, String tooltip, Widget widget, String styleName) {
		row++;
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		TooltipLabelWidget tooltipWidget = new TooltipLabelWidget(label, tooltip);

		mainGrid.setWidget(row, 0, tooltipWidget);
		if (widget != null){
			mainGrid.setWidget(row, 1, widget);
			
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}


	private void doActionSave() {
		itemManageWidget.save();
	}
	
	private void onItemSaved() {
		if (saveData()) {
			
			hide();
			if (onChange != null)
				onChange.execute();
		}
	}
	
	private void doActionCancel() {
		hide();
	}

	public void setPlaylistItem(PlaylistItemInfo playlistItem) {
		this.playlistItem = playlistItem;
	}

	public PlaylistItemInfo getPlaylistItem() {
		return playlistItem;
	}

	public boolean getItemIsNew() {
		return itemIsNew;
	}

	public int getItemIndex() {
		return itemIndex;
	}
	
	public void setCommand(Command command) {
		this.onChange = command;
	}

}