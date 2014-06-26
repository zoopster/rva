// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.PlaylistItemInfo;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.common.client.info.PlaceholderInfo;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.NumericBoxWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.TooltipLabelWidget;
import com.risevision.ui.client.common.widgets.TransitionWidget;
import com.risevision.ui.client.common.widgets.UnitLabelWidget;
import com.risevision.ui.client.common.widgets.background.BackgroundWidget;
import com.risevision.ui.client.common.widgets.store.StoreFrameWidget;
import com.risevision.ui.client.common.widgets.timeline.TimelineWidget;
import com.risevision.ui.client.display.DistributionWidget;
import com.risevision.ui.client.gadget.GadgetSelectWidget;
import com.risevision.ui.client.presentation.PresentationLayoutWidget;
import com.risevision.ui.client.presentation.common.PlaceholderSelectListBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PlaceholderManageWidget extends PopupPanel {
	public static String VIA_STORE = "via_store";
	private static PlaceholderManageWidget instance;
	private PlaceholderInfo placeholder;
	private PresentationInfo presentation;
	private PlaylistItemInfo copiedPlaylistItem;
	//UI pieces
	private int row = -1;
	//UI: Item type select panel
	private DeckPanel contentDeckPanel = new DeckPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel managePanel = new VerticalPanel();
	private Label titleLabel = new Label("Placeholder");
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	//UI: Placeholder manage fields
	private FlexTable manageGrid = new FlexTable();

	private NumericBoxWidget heightTextBox = new NumericBoxWidget(true);
	private UnitLabelWidget heightUnitLabel = new UnitLabelWidget(UnitLabelWidget.PIXEL_UNIT);
	private NumericBoxWidget widthTextBox = new NumericBoxWidget(true);
	private UnitLabelWidget widthUnitLabel = new UnitLabelWidget(UnitLabelWidget.PIXEL_UNIT);
	private NumericBoxWidget topTextBox = new NumericBoxWidget(true);
	private UnitLabelWidget topUnitLabel = new UnitLabelWidget(UnitLabelWidget.PIXEL_UNIT);
	private NumericBoxWidget leftTextBox = new NumericBoxWidget(true);
	private UnitLabelWidget leftUnitLabel = new UnitLabelWidget(UnitLabelWidget.PIXEL_UNIT);
	
	private PlaceholderSelectListBox selectorListBox = new PlaceholderSelectListBox();
	private TextBox idTextBox = new TextBox();
	private Anchor renameIdLink = new Anchor("Rename");
	private Anchor saveIdLink = new Anchor("Save");
	private Anchor cancelIdLink = new Anchor("Cancel");
	
//	private HorizontalPanel orderPanel = new HorizontalPanel();
//	private Anchor orderDownLink = new Anchor("<");
//	private Label orderTextBox = new Label();
//	private Anchor orderUpLink = new Anchor(">");
	
	private TimelineWidget timelineWidget = new TimelineWidget();	
	private DistributionWidget distributionWidget = new DistributionWidget();	
	private CheckBox visibilityCheckBox = new CheckBox();
	private TransitionWidget transitionWidget = new TransitionWidget(false);
	private BackgroundWidget backgroundWidget = new BackgroundWidget();
	
	private PlaceholderItemListWidget listWidget = new PlaceholderItemListWidget();
	private PlaceholderItemManageWidget itemManageWidget;
	
	private Command onChange, singleItemSavedCommand;
	private ActionsWidget actionWidget = new ActionsWidget();
	private GadgetSelectWidget gadgetSelectWidget = new GadgetSelectWidget(actionWidget);
	
	private PlaylistItemManageWidget playlistItemWidget = PlaylistItemManageWidget.getInstance();
	
	private boolean isInsert = false;
	private PlaylistItemInfo insertItem;
	private int insertIndex;
	private Map<String, Object> insertData;
	
    public final static Logger LOGGER = Logger.getLogger(PlaceholderManageWidget.class.toString());
    
	public PlaceholderManageWidget() {
		super(false, false); //set auto-hide and modal
		add(mainPanel);
		
		mainPanel.add(titleLabel);
		mainPanel.add(formValidator);
		mainPanel.add(contentDeckPanel);		
		mainPanel.add(actionWidget);
		
		contentDeckPanel.add(gadgetSelectWidget);
		contentDeckPanel.add(managePanel);
		
		managePanel.add(manageGrid);
		managePanel.add(listWidget);
		
//		orderPanel.add(orderDownLink);
//		orderPanel.add(new HTML("&nbsp;&nbsp;"));
//		orderPanel.add(orderTextBox);
//		orderPanel.add(new HTML("&nbsp;&nbsp;"));
//		orderPanel.add(orderUpLink);
		
		HorizontalPanel idPanel = new HorizontalPanel();
		idPanel.add(selectorListBox);
		idPanel.add(idTextBox);
		idPanel.add(new SpacerWidget());
		idPanel.add(renameIdLink);
		idPanel.add(saveIdLink);
		idPanel.add(new SpacerWidget());
		idPanel.add(cancelIdLink);
		
		// add widgets
		gridAdd("Placeholder:", 
				"Placeholders show Playlists of content in your Presentation", 
				idPanel, null);
		gridAdd("Width*:", null,
				widthTextBox, "rdn-TextBoxShort", widthUnitLabel);
		gridAdd("Height*:", null,
				heightTextBox, "rdn-TextBoxShort", heightUnitLabel);
		gridAdd("Top*:", null,
				topTextBox, "rdn-TextBoxShort", topUnitLabel);
		gridAdd("Left*:", null,
				leftTextBox, "rdn-TextBoxShort", leftUnitLabel);
		gridAdd("Timeline:", 
				"When this Placeholder shows on Displays",
				timelineWidget, null);
		gridAdd("Distribution:", 
				"Which Displays show this Placeholder", 
				distributionWidget, null);
		gridAdd("Visible:", 
				"Select whether the Placeholder will be visible or not",
				visibilityCheckBox, "rdn-CheckBox");
		gridAdd("Transition:", 
				"How to Transition between Playlist Items", 
				transitionWidget, null);
//		gridAdd("Reference:", idLabel, null);
		gridAdd("Background:", 
				"An image or color background for the Placeholder",
				backgroundWidget, null);
		
		row++;
		manageGrid.setWidget(row, 0, listWidget);
		manageGrid.getFlexCellFormatter().setColSpan(row, 0, 2);
						
		styleControls();

		initActions();
		initValidator();
		
		itemManageWidget = new PlaceholderItemManageWidget(manageGrid, singleItemSavedCommand);
	}

	private void styleControls() {
		setSize("500px", "100%");
		
		//style the table	
		manageGrid.setCellSpacing(0);
		manageGrid.setCellPadding(0);
		manageGrid.setStyleName("rdn-Table");
		
		idTextBox.setStyleName("rdn-TextBoxMedium");
		
		titleLabel.setStyleName("rdn-Head");

		this.getElement().getStyle().setProperty("padding", "10px");

		actionWidget.addStyleName("rdn-VerticalSpacer");
	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(idTextBox, "Id", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(heightTextBox, "Height", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(widthTextBox, "Width", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(topTextBox, "Top", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(leftTextBox, "Left", FormValidatorInfo.requiredFieldValidator);
//		formValidator.addValidationElement(urlTextBox, "URL", FormValidatorInfo.requiredFieldValidator);
	}

	private void initActions() {
		Command cmdSave = new Command() {
			public void execute() {
				if (contentDeckPanel.getVisibleWidget() == 0) {
					contentSelected();
				}
				else {
					isInsert = false;
					
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
				if (contentDeckPanel.getVisibleWidget() == 0 && placeholder.getPlaylistItems().size() == 1) {
					listWidget.deleteItem(0);
					showSelectPanel(false, null);
				}
			}
		};		

		actionWidget.addAction("Save", cmdSave);
//		actionWidget.addAction("Delete", cmdDelete);
		actionWidget.addAction("Cancel", cmdCancel);
		
//		initOrderLinks();
		
		renameIdLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hideSelectorList(true);
			}
		});
		
		saveIdLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (saveNewId()) {
					selectorListBox.bindSelectListBox(presentation, placeholder.getId());
					hideSelectorList(false);
				}
			}
		});
		
		cancelIdLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				formValidator.clear();
				hideSelectorList(false);
				idTextBox.setText(placeholder.getId());
			}
		});
		
		selectorListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (saveData()) {
					onChange.execute();
					PresentationLayoutWidget.getInstance().changePlaceholder(selectorListBox.getSelectedValue());
				}
			}
		});
		
		Command selectCommand = new Command() {
			public void execute() {
				contentSelected();
			}
		};
		
		gadgetSelectWidget.init(selectCommand);
		
		Command playlistItemSavedCommand = new Command() {
			@Override
			public void execute() {
				playlistItemSaved();
			}
		};
		
		playlistItemWidget.setCommand(playlistItemSavedCommand);
		
		singleItemSavedCommand = new Command() {
			@Override
			public void execute() {
				itemSaved();
			}
		};
	}	
	
	private void playlistItemSaved() {
		if (playlistItemWidget.getItemIsNew()) {			
			listWidget.addItem(playlistItemWidget.getPlaylistItem(), playlistItemWidget.getItemIndex());
			bindItem();
		}
		else {
			listWidget.updateTable();
		}
	}
	
	private void contentSelected(){
		GadgetInfo gadget = null;
		if (VIA_STORE.equals(this.insertData.get("via"))) {
			gadget = StoreFrameWidget.getInstance().getSelectedGadget();			
		}
		else {
			gadget = gadgetSelectWidget.getCurrentGadget();
		}
		
		if (gadget != null) {
			// add Gadget here
	
			showSelectPanel(false, null);
			formValidator.clear();
		}
		
		itemManageWidget.show(placeholder.getPlaylistItems().get(0), gadget);
		listWidget.updateTable();
	}
	
	public void showSelectPanel(boolean show, GadgetSelectWidget.Content content) {
		
		actionWidget.setVisible(!show, "Save");
		actionWidget.setVisible(show, "Cancel");
		
		contentDeckPanel.showWidget(show ? 0 : 1);
		if (show) gadgetSelectWidget.show(content);	
	}
	
	private void hideSelectorList(boolean hide) {
		idTextBox.setVisible(hide);
		selectorListBox.setVisible(!hide);
		
		renameIdLink.setVisible(!hide);
		saveIdLink.setVisible(hide);
		cancelIdLink.setVisible(hide);
	}
	
//	private void initOrderLinks() {
//		orderDownLink.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				int orderNumber = RiseUtils.StrToInt(orderTextBox.getText(), 0);
//				if (orderNumber > 0)
//					orderTextBox.setText(orderNumber - 1 + "");
//			}
//		});
//		
//		orderUpLink.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				int orderNumber = RiseUtils.StrToInt(orderTextBox.getText(), 0);
//				if (orderNumber < placeholderCount - 1)
//					orderTextBox.setText(orderNumber + 1 + "");
//			}
//		});
//	}

	public static PlaceholderManageWidget getInstance() {
		try {
			if (instance == null)
				instance = new PlaceholderManageWidget();
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
				doActionSave();
				break;
			}

		return true;
	}
	
	public void load() {
		gadgetSelectWidget.load();
	}
	
	public void show(PlaceholderInfo placeholder, PresentationInfo presentation){
		super.show();

		if (placeholder == null){
			placeholder = new PlaceholderInfo();
		}
		
		if (placeholder.getPlaylistItems() == null) {
			placeholder.setPlaylistItems(new ArrayList<PlaylistItemInfo>());
		}
		
		this.placeholder = placeholder;
		this.presentation = presentation;
		formValidator.clear();
		bindData();
		
		// AD - Temporarily removed focus because of Ctrl+V functionality pasting into textbox
//		widthTextBox.setFocus(true);
		center();
	}
	
	public void center() {
		super.center();
		movePopup(-15, -5);
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
	
	public void addItem(String type, int itemIndex, Map<String, Object> data) {
		StatusBoxWidget.getInstance().clear();
		PlaylistItemInfo playlistItem = new PlaylistItemInfo();
		playlistItem.setType(type);
		playlistItem.setName(RiseUtils.capitalizeFirstLetter(type) + " Item");
		playlistItem.setPlaceholderItem(true);

//		if (type.equals(PlaylistItemInfo.TYPE_IMAGE)) {
//			playlistItem.setObjectData(IMAGE_GADGET_URL);
//		}
		
		if (type.equals(PlaylistItemInfo.TYPE_VIDEO)) {
			playlistItem.setPlayUntilDone(true);
		}
		
		insertItem(playlistItem, itemIndex, data);
	}
	
	public void copyItem(int itemIndex) {
		if (itemIndex >= 0 && itemIndex < placeholder.getPlaylistItems().size()) {
			copiedPlaylistItem = placeholder.getPlaylistItems().get(itemIndex);
		}
	}
	
	public void pasteItem(int itemIndex) {
		if (copiedPlaylistItem != null) {
			insertItem(copiedPlaylistItem.copy(), itemIndex, new HashMap<String, Object>());	
		}
	}
	
	private void insertItem(PlaylistItemInfo playlistItem, int itemIndex, Map<String, Object> data) {
		isInsert = true;
		
		this.insertItem = playlistItem;
		this.insertIndex = itemIndex;
		this.insertData = data;
		
		doActionSave();
	}
	
	private void insertItemSaveComplete() {
		isInsert = false;
		
//		saveData();
		if (placeholder.getPlaylistItems().size() == 0) {
			// [ad] bug fix
			listWidget.addItem(insertItem, insertIndex);
//			listWidget.addItem(playlistItem, playlistItemWidget.getItemIndex());
			bindItem();
		}
		else {
			playlistItemWidget.show(insertItem, insertIndex, true, this.insertData);
		}
	}

	private void bindData() {
		if (placeholder == null)
			return;
		
		hideSelectorList(false);
		timelineWidget.setTimeline(placeholder.getTimeline());
		distributionWidget.setDistributionToAll(placeholder.getDistributionToAll());
		distributionWidget.setDistribution(placeholder.getDistribution());
		visibilityCheckBox.setValue(placeholder.isVisible());
		transitionWidget.setTransition(placeholder.getTransition());
		backgroundWidget.init(placeholder.getBackgroundStyle(), placeholder.isBackgroundScaleToFit());

		listWidget.setPlaylistItems(placeholder.getPlaylistItems());
		
		if (placeholder.getNewId() != null && !placeholder.getNewId().isEmpty()) {
			idTextBox.setText(placeholder.getNewId());
		}
		else {
			idTextBox.setText(placeholder.getId());
		}
		
		bindItem();
		bindSize();
		selectorListBox.bindSelectListBox(presentation, placeholder.getId());
	}
	
	public void bindItem() {
		if (placeholder.getPlaylistItems().size() == 1) {
			if (PlaylistItemInfo.TYPE_GADGET.equals(placeholder.getPlaylistItems().get(0).getType()) && 
					(placeholder.getPlaylistItems().get(0).getObjectData() == null || 
							placeholder.getPlaylistItems().get(0).getObjectData().isEmpty())){
				if (VIA_STORE.equals(this.insertData.get("via"))) {
					this.loadStoreIframe();
				}
				else {
					showSelectPanel(true, (GadgetSelectWidget.Content) this.insertData.get("via"));					
				}
			}
			else {
				showSelectPanel(false, null);

				itemManageWidget.show(placeholder.getPlaylistItems().get(0));
			}
		}
		else {
			showSelectPanel(false, null);
			itemManageWidget.hide();
			
//			listWidget.setVisible(true);
		}
	}
		
	public void bindSize() {
		widthTextBox.setText(RiseUtils.doubleToString(placeholder.getWidth(), 5));
		heightTextBox.setText(RiseUtils.doubleToString(placeholder.getHeight(), 5));
		topTextBox.setText(RiseUtils.doubleToString(placeholder.getTop(), 5));
		leftTextBox.setText(RiseUtils.doubleToString(placeholder.getLeft(), 5));

		widthUnitLabel.setText(placeholder.getWidthUnits());
		heightUnitLabel.setText(placeholder.getHeightUnits());
		topUnitLabel.setText(placeholder.getTopUnits());
		leftUnitLabel.setText(placeholder.getLeftUnits());
		
//		orderTextBox.setText(placeholder.getzIndex() + "");
//		setUnits(pixelText);
	}
	
	private boolean saveData() {
		if (placeholder == null)
			return true;

		if (!formValidator.validate())
			return false;
		
		if (idTextBox.isVisible() && !saveNewId()) 
			return false;
		
//		if (placeholder.getPlaylistItems().size() == 1 && !itemManageWidget.save()) {
//			return false;
//		}
		
		placeholder.setHeight(RiseUtils.strToDouble(heightTextBox.getText(), 0));
		placeholder.setWidth(RiseUtils.strToDouble(widthTextBox.getText(), 0));
		placeholder.setTop(RiseUtils.strToDouble(topTextBox.getText(), 0));
		placeholder.setLeft(RiseUtils.strToDouble(leftTextBox.getText(), 0));

		placeholder.setHeightUnits(heightUnitLabel.getText());
		placeholder.setWidthUnits(widthUnitLabel.getText());
		placeholder.setTopUnits(topUnitLabel.getText());
		placeholder.setLeftUnits(leftUnitLabel.getText());
//		placeholder.setHeightUnits(pixelText);
//		placeholder.setWidthUnits(pixelText);
//		placeholder.setTopUnits(pixelText);
//		placeholder.setLeftUnits(pixelText);
		
//		placeholder.setzIndex(RiseUtils.StrToInt(orderTextBox.getText(), 0));
		
		placeholder.setTimeline(timelineWidget.getTimeline());
 
		placeholder.setDistributionToAll(distributionWidget.getDistributionToAll());
		placeholder.setDistribution(distributionWidget.getDistribution());
		placeholder.setVisible(visibilityCheckBox.getValue());
		placeholder.setTransition(transitionWidget.getTransition());
		placeholder.setBackgroundStyle(backgroundWidget.getStyle());
		placeholder.setBackgroundScaleToFit(backgroundWidget.isScaleToFit());
		
		return true;
	}
	
	private boolean	saveNewId() {
		// clear validator (not done automatically when a custom validation message is displayed)
		formValidator.clear();
		
		if (!formValidator.validate(idTextBox))
			return false;
		
		// validate search for not-allowed characters; (a to z, A to Z), digits (0 to 9), hyphens (-), underscores (_) 
		// are the only characters allowed
		String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
		for (int i = 0; i < idTextBox.getText().length(); i++) {
			if (validCharacters.indexOf(idTextBox.getText().charAt(i)) == -1) {
				formValidator.addErrorMessage("* Placeholder name contains invalid character.");
				return false;
			}
		}
		
		for (PlaceholderInfo ph: presentation.getPlaceholders()) {
			if (ph != placeholder && !ph.isDeleted() && (ph.getId().equals(idTextBox.getText()) || idTextBox.getText().equals(ph.getNewId()))) {
				formValidator.addErrorMessage("* Duplicate Placeholder names are not allowed.");
				return false;
			}
		}
		
		placeholder.setNewId(idTextBox.getText());
		
		return true;
	}
	
	private void gridAdd(String label, String tooltip, Widget widget, String styleName) {
		gridAdd(label, tooltip, widget, styleName, null);
	}
	
	private void gridAdd(String label, String tooltip, Widget widget, String styleName, Widget units) {
		row++;
		manageGrid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		TooltipLabelWidget tooltipWidget = new TooltipLabelWidget(label, tooltip);

		manageGrid.setWidget(row, 0, tooltipWidget);
		if (widget != null){
			if (styleName != null)
				widget.setStyleName(styleName);
			
			if (units == null) {
				manageGrid.setWidget(row, 1, widget);
			}
			else {
				HorizontalPanel panel = new HorizontalPanel();
				panel.add(widget);
				panel.add(new HTML("&nbsp;&nbsp;"));
				panel.add(units);
				manageGrid.setWidget(row, 1, panel);
			}
		}
	}

	public void hide() {
		super.hide();
		playlistItemWidget.hide();
		itemManageWidget.hide();
	}

	private void doActionSave() {
		if (placeholder.getPlaylistItems().size() == 1) {
			itemManageWidget.save();
		}
		else {
			itemSaved();
		}
	}
	
	private void itemSaved() {
		if (saveData()) {
			if (!isInsert) {
				hide();
				if (onChange != null)
					onChange.execute();
			}
			else {
				insertItemSaveComplete();
			}
		}
	}
	
	public void loadStoreIframe() {
		StoreFrameWidget.getInstance().show(new Command() {

			@Override
			public void execute() {
				contentSelected();
			}
			
		}, new Command() {
			public void execute() {
				if (placeholder.getPlaylistItems().size() == 1) {
					listWidget.deleteItem(0);
					showSelectPanel(false, null);
				}
			}
		});
	}

//	private void doActionCancel() {
//		hide();
//		if (onChange != null)
//			onChange.execute();
//	}

	public PlaceholderInfo getPlaceholder() {
		return placeholder;
	}
	
	public void setCommand(Command command) {
		this.onChange = command;
	}
	
}