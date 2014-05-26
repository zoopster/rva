package com.risevision.ui.client.presentation;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.risevision.common.client.info.PlaceholderInfo;

public class PlaceholderPanelWidget extends FocusPanel {
	private boolean useMouseOverEvents;
	private ItemPreviewWidget previewWidget;
	
	public PlaceholderPanelWidget() {
		this(null, false);
	}
	
	public PlaceholderPanelWidget(PlaceholderInfo placeholder, boolean useMouseOverEvents) {
		super();
		this.useMouseOverEvents = useMouseOverEvents;
		
		if (useMouseOverEvents) {
			getElement().getStyle().setProperty("border", "1px solid #999999");
		}
		
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEUP | Event.ONCONTEXTMENU); 
		
		if (placeholder != null) {
			previewWidget = new ItemPreviewWidget(placeholder); 
		}
	}
	
//	public PlaceholderPanelWidget(ClickHandler handler) {
//		getElement().getStyle().setProperty("border", "1px solid #999999");
////		MouseOverHandler mouseOverHandler = new MouseOverHandler() {
//
//			@Override
//			public void onMouseOver(MouseOverEvent event) {
//				getElement().getStyle().setProperty("border", "1px solid #000000");
//			}
//		};
//		MouseOutHandler mouseOutHandler = new MouseOutHandler() {
//
//			@Override
//			public void onMouseOut(MouseOutEvent event) {
//				getElement().getStyle().setProperty("border", "1px solid #999999");
//			}
//		};
//
//		addDomHandler(mouseOverHandler, MouseOverEvent.getType());
//		addDomHandler(mouseOutHandler, MouseOutEvent.getType());
//		addDomHandler(handler, ClickEvent.getType());
//	}
	
	public void onBrowserEvent(Event event) {
        event.stopPropagation(); //This will stop the event from being propagated to parent elements.
        switch (DOM.eventGetType(event)) {
	        case Event.ONCONTEXTMENU: //	        	GWT.log("Event.ONCONTEXTMENU", null);
	        	event.preventDefault();
	        	break;
	        case Event.ONMOUSEUP:
	        	if (DOM.eventGetButton(event) == Event.BUTTON_LEFT) {//	        		GWT.log("Event.BUTTON_LEFT", null);
	        		PresentationLayoutWidget.getInstance().onClick(this, event, Event.BUTTON_LEFT);
	        	}
	        	if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
		        	event.preventDefault();
//	        		GWT.log("Event.BUTTON_RIGHT", null);
	        		PresentationLayoutWidget.getInstance().onClick(this, event, Event.BUTTON_RIGHT);
	        	}
	        case Event.ONDBLCLICK: 
//        		GWT.log("Event.ONDBLCLICK", null);
	        	break;
	        case Event.ONMOUSEOVER:
	        	if (useMouseOverEvents)
	        		onMouseOver();
	        	break;
	        case Event.ONMOUSEOUT:
	        	if (useMouseOverEvents)
	        		onMouseOut();
	        	break;
	        default: 
	                // Do nothing
        }//end switch
	}

	public void onMouseOver() {
		getElement().getStyle().setProperty("border", "1px solid #000000");
	}

	public void onMouseOut() {
		getElement().getStyle().setProperty("border", "1px solid #999999");
	}

	public ItemPreviewWidget getPreviewWidget() {
		return previewWidget;
	}
	
	public void setPreviewWidget(ItemPreviewWidget previewWidget) {
		this.previewWidget = previewWidget;
	}
	
	public void setPixelSize(int width, int height) {
		super.setPixelSize(width, height);
		
		previewWidget.setPixelSize(width, height);
	}
}


