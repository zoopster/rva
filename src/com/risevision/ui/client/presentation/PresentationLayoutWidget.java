// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.common.client.info.PlaceholderInfo;
import com.risevision.common.client.utils.PresentationParser;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.dnd.ResizeDragController;
import com.risevision.ui.client.common.dnd.WindowController;
import com.risevision.ui.client.common.dnd.WindowPanel;
import com.risevision.ui.client.common.widgets.UnitLabelWidget;
import com.risevision.ui.client.presentation.placeholder.PlaceholderManageWidget;

public class PresentationLayoutWidget extends AbsolutePanel {
	private static PresentationLayoutWidget instance;

	//RPC
//	private final PlaylistServiceAsync playlistService = GWT.create(PlaylistService.class);
//	private PlaylistRpcCallBackHandler playlistRpcCallBackHandler = new PlaylistRpcCallBackHandler();
	
	private PresentationInfo presentation;
	private static Map<String, Widget> placeholderMap = new HashMap<String, Widget>();
	private WindowController windowController;
	private int height, width;
	private int widgetIndex = -1;
	private String selectedPlaceholder = "";
	private PlaceholderInfo copiedPlaceholder;
	private FocusPanel dragboxWidget;
	private WindowPanel selectedWindowPanel;
	private SimplePanel backgroundPanel = new SimplePanel();
	private SimplePanel presentationBackgroundPanel = new SimplePanel();
	
	private PlaceholderContextMenuWidget contextMenuWidget;
	private PlaceholderManageWidget optionsWidget = PlaceholderManageWidget.getInstance();
	
	private HandlerRegistration resizeHandler;
	
	public static PresentationLayoutWidget getInstance() {
		try {
			if (instance == null)
				return new PresentationLayoutWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public PresentationLayoutWidget() {	
		if (instance == null) {
			instance = this;
		}
		
		sinkEvents(Event.ONCONTEXTMENU); 
		
		windowController = new WindowController(this);
		
		PickupDragController dragController = windowController.getPickupDragController();
		
		dragController.setBehaviorDragStartSensitivity(1);
		dragController.setBehaviorScrollIntoView(false);
		
		DragHandler dragHandler = new DragHandler() {
			@Override
			public void onPreviewDragStart(DragStartEvent event)
					throws VetoDragException {
				// ;			
			}
			@Override
			public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
				// ;
			}
			@Override
			public void onDragStart(DragStartEvent event) {
				onDragStartEvent(event) ;			
			}
			@Override
			public void onDragEnd(DragEndEvent event) {
				onDragEndEvent(event);
			}
		};
		
		dragController.addDragHandler(dragHandler);
		windowController.getResizeDragController().addDragHandler(dragHandler);
		windowController.getResizeDragController().setBehaviorDragStartSensitivity(1);
//		windowController.getResizeDragController().setBehaviorScrollIntoView(false);
		
		dragboxWidget = new FocusPanel();
		
		selectedWindowPanel = new WindowPanel(windowController, dragboxWidget);
		
		dragboxWidget.getElement().getStyle().setProperty("border", "1px solid #000000");
		
//		ClickHandler clickHandler = new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				deselectPlaceholder();
//			}
//		};
//		
//		addDomHandler(clickHandler, ClickEvent.getType());
		
		Command onPlaceholderChanged = new Command() {
			public void execute() {
				onPlaceholderChanged();
			}
		};
		
		optionsWidget.setCommand(onPlaceholderChanged);
		
		initContextMenu();
	}
	
	private void initContextMenu() {
		Command bringToFrontCommand = new Command() {
			public void execute() {
				contextMenuWidget.hide();
				changeOrder(2);
			}
		};
		Command bringForwardCommand = new Command() {
			public void execute() {
				contextMenuWidget.hide();
				changeOrder(1);
			}
		};
		Command sendBackwardCommand = new Command() {
			public void execute() {
				contextMenuWidget.hide();
				changeOrder(-1);
			}
		};
		Command sendToBackCommand = new Command() {
			public void execute() {
				contextMenuWidget.hide();
				changeOrder(-2);
			}
		};
		Command propertiesCommand = new Command() {
			public void execute() {
				contextMenuWidget.hide();
				showEditDialog();
			}
		};
		Command deleteCommand = new Command() {
			public void execute() {
				contextMenuWidget.hide();
				doActionDelete();
			}
		};
		
		contextMenuWidget = new PlaceholderContextMenuWidget(bringToFrontCommand, bringForwardCommand, sendBackwardCommand,
								sendToBackCommand, propertiesCommand, deleteCommand);
	}
	
	private void changeOrder(int desiredOrder) {
		PlaceholderInfo placeholder = getSelectedPlaceholder();
		
		if (placeholder != null) {
			int orderNumber = placeholder.getzIndex();
			switch (desiredOrder) {
			case -2:
				orderNumber = 0;
				break;
			case -1:
				if (orderNumber > 0)
					orderNumber--;
				break;
			case +1:
				if (orderNumber < presentation.getPlaceholders().size() - 1)
					orderNumber++;
				break;
			case 2:
				orderNumber = presentation.getPlaceholders().size();
				break;
			}
			
			if (placeholder.getzIndex() != orderNumber) {
				placeholder.setzIndex(orderNumber);
				fixPlaceholderOrder(placeholder);
				bindData();
			}
		}
	}
	
	private void doActionDelete() {
		if (Window.confirm("Are you sure you want to delete this placeholder?")) {
			PlaceholderInfo placeholder = getSelectedPlaceholder();
			
			if (placeholder != null) {		
				placeholder.setDeleted(true);
				
				fixPlaceholderOrder(placeholder);
				bindData();
			}
		}
	}
	
	protected void onLoad() {
		super.onLoad();
		
		resizeHandler = Window.addResizeHandler(new ResizeHandler() {	
			@Override
			public void onResize(ResizeEvent event) {
			    getElement().getParentElement().getStyle().setPropertyPx("width", (int)(Window.getClientWidth() - 31));
				getElement().getParentElement().getStyle().setPropertyPx("height", (int)(Window.getClientHeight() - 206));
				
				resizeCanvas();
			}
		});
	}
	
	protected void onUnload() {
		super.onUnload();
		
		if (resizeHandler != null) {
			resizeHandler.removeHandler();
			resizeHandler = null;
		}
	}
	
	public void onBrowserEvent(Event event) {
        event.stopPropagation();
        
        switch (DOM.eventGetType(event)) {
        case Event.ONCONTEXTMENU: 
        	event.preventDefault();
        }
	}
	
	public void initWidget(PresentationInfo presentation){
		getElement().getStyle().setBackgroundColor("#DDDDDD");

		getElement().getParentElement().setPropertyString("className", "rdn-DeckPanel");
	    getElement().getParentElement().getStyle().setPropertyPx("width", (int)(Window.getClientWidth() - 31));
		getElement().getParentElement().getStyle().setPropertyPx("height", (int)(Window.getClientHeight() - 206));
		getElement().getParentElement().getStyle().setBackgroundColor("#DDDDDD");
		getElement().getParentElement().getStyle().setOverflow(Overflow.AUTO);
				
		if (presentation == null || presentation.getPlaceholders() == null){
			clear();
			return; 
		}
		
		this.presentation = presentation;
		fixPlaceholderOrder(null);

		bindData();
	}
	
	public void bindData(){
		deselectPlaceholder();
		
		resizeCanvas();
		
		clear();
		presentationBackgroundPanel.getElement().getStyle().setProperty("background", "");
        presentationBackgroundPanel.getElement().getStyle().setProperty("backgroundSize", "");
		
		add(backgroundPanel, 5, 5);
		add(presentationBackgroundPanel, 5, 5);
		
		backgroundPanel.getElement().getStyle().setBackgroundColor("#FFFFFF");				

		if (presentation.getBackgroundStyle() != null) {
			presentationBackgroundPanel.getElement().getStyle().setProperty("background", presentation.getBackgroundStyle());
	        presentationBackgroundPanel.getElement().getStyle().setProperty("backgroundSize", presentation.isBackgroundScaleToFit() ? "contain" : "");
		}
		
		bindPlaceholders();
	}
	
	private void resizeCanvas() {
		int parentHeight = 0, parentWidth = 0;
		
		for (int i = 0; i < 2; i++) {
			if (getElement().getParentElement() != null){
				parentHeight = getElement().getParentElement().getClientHeight();
				parentWidth = getElement().getParentElement().getClientWidth();
			}
			
			if (presentation.getWidthUnits().equals(UnitLabelWidget.PERCENT_UNIT)){
				width = parentWidth - 10;
			}
			else {
//				if (presentation.getWidthUnits().equals(UnitLabelWidget.PIXEL_UNIT)) {
				width = (int)(presentation.getWidth());
			}
//			else{
//				if (width != 0 && height != 0) {
//					width = parentWidth;
//					height = (int) (((double) presentation.getWidth()) / presentation.getHeight())
//							* parentHeight;
//				} 
//				else {
//					width = parentWidth;
//					height = parentHeight;
//				}
//			}
			
			if (presentation.getHeightUnits().equals(UnitLabelWidget.PERCENT_UNIT)){
				height = parentHeight - 10;
			}
			else {
//				if (presentation.getHeightUnits().equals(UnitLabelWidget.PIXEL_UNIT)) {
				height = (int)(presentation.getHeight());
			}
			
			setPixelSize(width + 10, height + 10);
				
			backgroundPanel.setPixelSize(width, height);
			presentationBackgroundPanel.setPixelSize(width, height);
		}
	}
	
	private void bindPlaceholders() {
		placeholderMap.clear();
		
		for (PlaceholderInfo placeholder : presentation.getPlaceholders()){
			if (!placeholder.isDeleted()) {
//				if (placeholder.getType() != null && !placeholder.getType().isEmpty() 
//						&& placeholder.getObjectRef() != null && !placeholder.getObjectRef().isEmpty() 
//						&& placeholder.getType().equals(PlaceholderInfo.TYPE_PLAYLIST)
//						&& placeholder.getPlaylistItems() == null){					
//					loadPlaylistInfo(placeholder.getObjectRef());
//				}
				
				if (getPixelSize(placeholder.getWidth(), width, placeholder.getWidthUnits()) > 10 
						&& getPixelSize(placeholder.getHeight(), height, placeholder.getHeightUnits()) > 10) {
					PlaceholderPanelWidget placeholderWidget = new PlaceholderPanelWidget(placeholder, true);

					setWidgetPixelSize(placeholderWidget, placeholder);
					
//					if (placeholder.getzIndex() != null && !placeholder.getzIndex().isEmpty()){
//						placeholderWidget.getElement().getStyle().setProperty("zIndex", placeholder.getzIndex());
//					}
										
					placeholderMap.put(placeholder.getId(), placeholderWidget);

//					add(placeholderWidget, left, top);
					
//					add(placeholderWidget.getPreviewWidget(), placeholder.getLeft() + 6, placeholder.getTop() + 6);
//					add(placeholderWidget, placeholder.getLeft() + 5, placeholder.getTop() + 5);
					
					add(placeholderWidget.getPreviewWidget());
					setWidgetPixelPosition(placeholderWidget.getPreviewWidget(), placeholder, 6);
					add(placeholderWidget);
					setWidgetPixelPosition(placeholderWidget, placeholder, 5);
				}
			}
		}
	}
	
	private void setWidgetPixelSize(Widget placeholderWidget, PlaceholderInfo placeholder) {
//	    DOM.setStyleAttribute(placeholderWidget.getElement(), "width", placeholder.getWidth() + placeholder.getWidthUnits());
//	    DOM.setStyleAttribute(placeholderWidget.getElement(), "height", placeholder.getHeight() + placeholder.getHeightUnits());

		int placeholderWidth = getPixelSize(placeholder.getWidth(), width, placeholder.getWidthUnits());
		int placeholderHeight = getPixelSize(placeholder.getHeight(), height, placeholder.getHeightUnits());
		
		placeholderWidget.setPixelSize(placeholderWidth - 2, placeholderHeight - 2); // 2px for border size
	}
	
	private void setWidgetPixelPosition(Widget placeholderWidget, PlaceholderInfo placeholder, int offset) {
//		if (placeholderWidget.getParent() != null) {
//			DOM.setStyleAttribute(placeholderWidget.getElement(), "position", "absolute");
//			DOM.setStyleAttribute(placeholderWidget.getElement(), "left", (placeholder.getLeft() + offset) + placeholder.getLeftUnits());
//			DOM.setStyleAttribute(placeholderWidget.getElement(), "top", (placeholder.getTop() + offset) + placeholder.getTopUnits());
//		}
	      
		int top = getPixelSize(placeholder.getTop(), height, placeholder.getTopUnits());
		int left = getPixelSize(placeholder.getLeft(), width, placeholder.getLeftUnits());
		
		if (placeholderWidget.getParent() != null) {
			AbsolutePanel parent = (AbsolutePanel) placeholderWidget.getParent();
	
			parent.setWidgetPosition(placeholderWidget, left + offset, top + offset);
		}
	}

//	private void onClickEvent(ClickEvent event) {
//		int xCoord = event.getNativeEvent().getClientX() - getAbsoluteLeft() - 5;
//        int yCoord = event.getNativeEvent().getClientY() - getAbsoluteTop() - 5;
//        String placeholderSelection = "";
//        int selectedIndex = -1;
//        
//        // check if the placeholder is selected already
//        if (!selectedPlaceholder.isEmpty()) {
//    		PresentationPlaceholderInfo placeholder = getSelectedPlaceholderInfo(selectedPlaceholder);
//			if (placeholder.getTop() < yCoord + 5 && placeholder.getTop() + placeholder.getHeight() > yCoord - 10
//					&& placeholder.getLeft() < xCoord + 5 && placeholder.getLeft() + placeholder.getWidth() > xCoord - 10) {
//				return;
//			}
//			else {
//	    		deselectPlaceholder();
//			}
//        }
//        
//		for (PresentationPlaceholderInfo placeholder : presentation.getPlaceholders()){
//			if (!placeholder.isDeleted()){
//				int top = placeholder.getTop();
//				int left = placeholder.getLeft();
//				int right = placeholder.getLeft() + placeholder.getWidth();
//				int bottom = placeholder.getTop() + placeholder.getHeight();
//				if ((((top + 2 >= yCoord && top - 2 <= yCoord) || (bottom + 2 >= yCoord && bottom - 2 <= yCoord)) 
//						&& (left - 2 <= xCoord && right + 2 >= xCoord)) || 
//					(((left + 2 >= xCoord && left - 2 <= xCoord) || (right + 2 >= xCoord && right - 2 <= xCoord)) 
//						&& (top - 2 <= yCoord && bottom + 2 >= yCoord))) {
//					placeholderSelection = placeholder.getId();
//					break;
//				}
//				else if (top < yCoord && bottom > yCoord && left < xCoord && right > xCoord) {
//					if (placeholder.getzIndex() > selectedIndex) {
//						placeholderSelection = placeholder.getId();
//						selectedIndex = placeholder.getzIndex();
//					}
//				}
//				else {				
//
//				}				
//			}
//		}
//		
//		if (!placeholderSelection.isEmpty()) {
//			selectedPlaceholder = placeholderSelection;
//			selectPlaceholder();
//		}
//	}
	
	public void onClick(Widget sender, Event event, int eventType) {
		Panel placeholderWidget = (Panel) sender;
		
		if (eventType == Event.BUTTON_RIGHT) {
	    	int x = event.getClientX() + 1;
	    	int y = event.getClientY() + 1;
	    	contextMenuWidget.setPopupPosition(x, y);
	    	contextMenuWidget.show();
		}
		else if (eventType == Event.BUTTON_LEFT) {
			contextMenuWidget.hide();
		}
		
		if (placeholderWidget != placeholderMap.get(selectedPlaceholder) && placeholderWidget != selectedWindowPanel) {
			Object[] items = placeholderMap.keySet().toArray();
			
			deselectPlaceholder();
			for (int i = 0; i < items.length; i++) {
				if (placeholderWidget == placeholderMap.get(items[i].toString())) {
					selectedPlaceholder = items[i].toString();
					selectPlaceholder();
				}
			}
		}
	}

	private void selectPlaceholder() {	
		if (!selectedPlaceholder.isEmpty()) {
			PlaceholderPanelWidget placeholderWidget = (PlaceholderPanelWidget)placeholderMap.get(selectedPlaceholder);
			
			if (placeholderWidget != null) {
				PlaceholderInfo placeholder = getSelectedPlaceholder();
				
				widgetIndex = getWidgetIndex(placeholderWidget);
				remove(placeholderWidget);
				
				setWidgetPixelSize(selectedWindowPanel, placeholder);
				
				// -5 px + 5 px because of border
				if (widgetIndex != -1) {
//					insert(selectedWindowPanel, placeholder.getLeft(), placeholder.getTop(), widgetIndex);

					insert(selectedWindowPanel, widgetIndex);
					setWidgetPixelPosition(selectedWindowPanel, placeholder, 0);

					selectedWindowPanel.setPreviewWidget(placeholderWidget.getPreviewWidget());
				}
								
//				showEditDialog();
			}
			
			selectionUpdated();
		}
	}
	
	private void deselectPlaceholder() {
		if (!selectedPlaceholder.isEmpty()) {
			PlaceholderPanelWidget placeholderWidget = (PlaceholderPanelWidget)placeholderMap.get(selectedPlaceholder);
			PlaceholderInfo placeholder = getSelectedPlaceholder();
			
			if (placeholderWidget != null) {
				//widgetIndex = -1;
				if (selectedWindowPanel != null) {
					//widgetIndex = getWidgetIndex(selectedWindowPanel);
					remove(selectedWindowPanel);
				}
				
				if (placeholder != null) {
					// + 5px border
					if (widgetIndex != -1) {
//						insert(placeholderWidget, placeholder.getLeft() + 5, placeholder.getTop() + 5, widgetIndex);
						
						insert(placeholderWidget, widgetIndex);
						setWidgetPixelPosition(placeholderWidget.getPreviewWidget(), placeholder, 6);
						setWidgetPixelPosition(placeholderWidget, placeholder, 5);
						
						selectedWindowPanel.setPreviewWidget(null);
//					} else {
//						add(placeholderWidget, placeholder.getLeft() + 5, placeholder.getTop() + 5);
					}
					
					setWidgetPixelSize(placeholderWidget, placeholder);
				}

				placeholderWidget.getElement().getStyle().setProperty("border", "1px solid #999999");
			}
			
			selectedPlaceholder = "";
			
			selectionUpdated();
		}
	}
	
	private PlaceholderInfo getSelectedPlaceholder() {
		for (PlaceholderInfo ph : presentation.getPlaceholders()){
			if (!ph.isDeleted() && ph.getId().equals(selectedPlaceholder)) {
				return ph;
			}
		}
		return null;
	}
	
	private void onDragStartEvent(DragStartEvent event) {
		try {
			if (!(event.getContext().dragController instanceof ResizeDragController) && selectedWindowPanel.getPreviewWidget() != null) {
				selectedWindowPanel.getPreviewWidget().setVisible(false);
			}
		} catch (Exception e) {
			
		}
	}
	
	private void onDragEndEvent(DragEndEvent event) {
		try {
			WindowPanel windowPanel;
			
			if (event.getContext().dragController instanceof ResizeDragController)
				windowPanel = (WindowPanel)((Widget)event.getSource()).getParent().getParent();
			else
				windowPanel = (WindowPanel)event.getSource();
						
			PlaceholderInfo placeholder = getSelectedPlaceholder();
			int widgetWidth = (int)RiseUtils.strToDouble(windowPanel.getContentWidth() + "", 0) + 2;
			int widgetHeight = (int)RiseUtils.strToDouble(windowPanel.getContentHeight() + "", 0) + 2;

			placeholder.setWidth(getAdjustedSize(widgetWidth, width, placeholder.getWidthUnits()));
			placeholder.setHeight(getAdjustedSize(widgetHeight, height, placeholder.getHeightUnits()));
			
			int widgetLeft = (int)RiseUtils.strToDouble(getWidgetLeft(windowPanel) + "", 0);
			int widgetTop = (int)RiseUtils.strToDouble(getWidgetTop(windowPanel) + "", 0);

			placeholder.setLeft(getAdjustedSize(widgetLeft, width, placeholder.getLeftUnits()));
			placeholder.setTop(getAdjustedSize(widgetTop, height, placeholder.getTopUnits()));
			
			if (selectedWindowPanel.getPreviewWidget() != null) {
				if (!(event.getContext().dragController instanceof ResizeDragController)) {
					selectedWindowPanel.getPreviewWidget().setVisible(true);
				}
				setWidgetPosition(selectedWindowPanel.getPreviewWidget(), widgetLeft + 6, widgetTop + 6);
			}
			
			optionsWidget.bindSize();			
		} catch (Exception e) {

		}
	}
	
	public void hideEditDialog() {
		optionsWidget.hide();
		deselectPlaceholder();
	}
	
	public void addPlaceholder() {
		PlaceholderInfo placeholder = new PlaceholderInfo(200, 400);
		
		addPlaceholder(placeholder);
	}
	
	public boolean copyPlaceholder() {
		if (getSelectedPlaceholder() != null) {
			copiedPlaceholder = getSelectedPlaceholder();
			return true;
		}
		return false;
	}
	
	public boolean pastePlaceholder() {
		if (copiedPlaceholder != null) {
			PlaceholderInfo newPlaceholder = copiedPlaceholder.copy();
			newPlaceholder.setId(null);
			addPlaceholder(newPlaceholder);	
			return true;
		}
		return false;
	}
	
	private void addPlaceholder(PlaceholderInfo placeholder) {
		centerPlaceholder(placeholder);
		
		presentation.getPlaceholders().add(placeholder);
		placeholder.setzIndex(getPlaceholderCount() - 1);
		PresentationParser.updatePresentation(presentation);

		bindData();
		selectedPlaceholder = placeholder.getId();
		selectPlaceholder();
		showEditDialog();
	}
	
	private void centerPlaceholder(PlaceholderInfo placeholder) {
		int placeholderPixelWidth = getPixelSize(placeholder.getWidth(), width, placeholder.getWidthUnits());
		if (placeholderPixelWidth > width) {
			placeholder.setWidth(presentation.getWidth());
			placeholder.setLeft(0);
		}
		else {
			int totalWidth = Math.min(width, getElement().getParentElement().getClientWidth());
			int scrollLeft = getElement().getParentElement().getScrollLeft();
	
			int left = getCenterPosition(placeholderPixelWidth, totalWidth, scrollLeft);
			
			placeholder.setLeft(getAdjustedSize(left, width, placeholder.getLeftUnits()));
		}
		
//		if (placeholder.getWidth() > presentation.getWidth()) {
//			placeholder.setWidth(presentation.getWidth());
//			placeholder.setLeft(0);
//		}
//		else {
//			int size = Math.min(presentation.getWidth(), getElement().getParentElement().getClientWidth());
//			int scrollLeft = getElement().getParentElement().getScrollLeft();
//			
//			int center = (size / 2) + scrollLeft;
//			placeholder.setLeft(center - (placeholder.getWidth() / 2));
//			
//			if (placeholder.getLeft() < 0)
//				placeholder.setLeft(0);
//			else if (placeholder.getLeft() + placeholder.getWidth() > presentation.getWidth()) {
//				placeholder.setLeft(presentation.getWidth() - placeholder.getWidth());
//			}
//		}
		
		int placeholderPixelHeight = getPixelSize(placeholder.getHeight(), height, placeholder.getHeightUnits());
		if (placeholderPixelHeight > height) {
			placeholder.setHeight(presentation.getHeight());
			placeholder.setTop(0);
		}
		else {
			int totalHeight = Math.min(height, getElement().getParentElement().getClientHeight());
			int scrollTop = getElement().getParentElement().getScrollTop();
	
			int top = getCenterPosition(placeholderPixelHeight, totalHeight, scrollTop);
			
			placeholder.setTop(getAdjustedSize(top, height, placeholder.getTopUnits()));
		}
		
//		if (placeholder.getHeight() > presentation.getHeight()) {
//			placeholder.setHeight(presentation.getHeight());
//			placeholder.setTop(0);
//		}
//		else {
//			int size = Math.min(presentation.getHeight(), getElement().getParentElement().getClientHeight());
//			int scrollTop = getElement().getParentElement().getScrollTop();
//			
//			int center = (size / 2) + scrollTop;
//			placeholder.setTop(center - (placeholder.getHeight() / 2));
//			
//			if (placeholder.getTop() < 0) 
//				placeholder.setTop(0);
//			else if (placeholder.getTop() + placeholder.getHeight() > presentation.getHeight()) {
//				placeholder.setTop(presentation.getHeight() - placeholder.getHeight());
//			}
//		}
	}
	
	private int getCenterPosition(int widgetSize, int totalPixelSize, int scroll) {	
		int center = (totalPixelSize / 2) + scroll;
		int position = center - (widgetSize / 2);
		
		if (position < 0) 
			position = 0;
		else if (position + widgetSize > totalPixelSize) {
			position = totalPixelSize - widgetSize;
		}
		
		return position;
	}
	
	private int getPixelSize(double size, int containerSize, String unit) {
		if (unit.equals(UnitLabelWidget.PERCENT_UNIT)) {
			return (int)(size * containerSize / 100);
		}
		else { 
//			if (placeholder.getLeftUnits().equals(UnitLabelWidget.PIXEL_UNIT))
			return (int)size;
		}
	}
	
	private double getAdjustedSize(int size, int containerSize, String unit) {
		if (unit.equals(UnitLabelWidget.PERCENT_UNIT)) {
			return size * 100.0 / containerSize;
		}
		else { 
//			if (placeholder.getLeftUnits().equals(UnitLabelWidget.PIXEL_UNIT))
			return size;
		}
	}
	
	public void changePlaceholder(String selectedPlaceholder) {
		deselectPlaceholder();
		this.selectedPlaceholder = selectedPlaceholder;
		selectPlaceholder();
		showEditDialog();
	}
	
	public int getPlaceholderWidth() {
		PlaceholderInfo placeholder = getSelectedPlaceholder();

		if (placeholder != null) {
			return getPixelSize(placeholder.getWidth(), width, placeholder.getWidthUnits());
		}
		return 0;
	}

	public int getPlaceholderHeight() {
		PlaceholderInfo placeholder = getSelectedPlaceholder();

		if (placeholder != null) {
			return getPixelSize(placeholder.getHeight(), height, placeholder.getHeightUnits());
		}
		return 0;
	}
	
	private void showEditDialog() {
		PlaceholderInfo placeholder = getSelectedPlaceholder();

		optionsWidget.show(placeholder, presentation);
	}
	
	private int getPlaceholderCount() {
		int size = 0;
		for (PlaceholderInfo ph: presentation.getPlaceholders()) {
			if (!ph.isDeleted()) {
				size ++;
			}
		}
		return size;
	}
	
	private void selectionUpdated() {
		PresentationManageWidget.getInstance().enableCopyButton(!selectedPlaceholder.isEmpty());
	}
	
	private void onPlaceholderChanged() {
//		if (optionsWidget.isSaved()) {
		if (presentation.getPlaceholders().contains(optionsWidget.getPlaceholder())) {
			fixPlaceholderOrder(optionsWidget.getPlaceholder());

			bindData();
		}
//		}
//		else {
//			deselectPlaceholder();
//		}
	}
	
	private void fixPlaceholderOrder(PlaceholderInfo changedPlaceholder) {
		orderItems(changedPlaceholder);
		for (int i = 0; i < presentation.getPlaceholders().size(); i++) {
			presentation.getPlaceholders().get(i).setzIndex(i);
		}
		
		return;
	}
	
	private void orderItems(PlaceholderInfo changedPlaceholder) {
		ArrayList<PlaceholderInfo> placeholders = presentation.getPlaceholders();
		
		for (int i = 0; i < Math.min(getPlaceholderCount(), placeholders.size() - 1); i++) {	
			// put deleted placeholder at the back
			if (placeholders.get(i).isDeleted()) {					
				PlaceholderInfo deleted = placeholders.get(i);
				placeholders.remove(i);
				placeholders.add(deleted);
			}
			
			if (placeholders.get(i) == changedPlaceholder && placeholders.get(i).getzIndex() == placeholders.get(i + 1).getzIndex()) {
				PlaceholderInfo smallest = placeholders.get(i + 1);
				placeholders.remove(i + 1);
				
				placeholders.add(i, smallest);
			}
			else {
				for (int j = i + 1; j < placeholders.size(); j++) {
					if ((placeholders.get(j) == changedPlaceholder && placeholders.get(j).getzIndex() <= placeholders.get(i).getzIndex()) ||
							(placeholders.get(j).getzIndex() < placeholders.get(i).getzIndex()) &&
							!placeholders.get(j).isDeleted()) {
						PlaceholderInfo smallest = placeholders.get(j);
						placeholders.remove(j);
						
						placeholders.add(i, smallest);
					}
				}
			}
		}
		
		if (changedPlaceholder != null && !changedPlaceholder.isDeleted() && changedPlaceholder.getzIndex() == getPlaceholderCount() - 1) {
			int index = getPlaceholderCount() - 1;
			placeholders.remove(changedPlaceholder);
			placeholders.add(index, changedPlaceholder);
		}
	}

//	private void loadPlaylistInfo(String id){
////		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
//		playlistService.getPlaylist(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
//				id, playlistRpcCallBackHandler);
//	}
	
//	class PlaylistRpcCallBackHandler extends RiseAsyncCallback<PlaylistInfo> {
//
//		public void onFailure() {
//			// Show the RPC error message to the user
////			statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error: " + caught.getMessage());
//		}
//
//		public void onSuccess(PlaylistInfo result) {
////			statusBox.clear();
//			if (result != null) {
//				for (PlaceholderInfo placeholder: presentation.getPlaceholders()) {
//					if (result.getId().equals(placeholder.getObjectRef())) {
//						placeholder.setPlaylist(result);
//					}
//				}
//			}
//		}
//	}
	
}
