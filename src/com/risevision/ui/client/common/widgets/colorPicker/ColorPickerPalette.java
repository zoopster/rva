// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.colorPicker;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ColorPickerPalette extends Composite implements ClickHandler {
	private String[] grayscaleColors = {"rgb(0, 0, 0)","rgb(68, 68, 68)","rgb(102, 102, 102)","rgb(153, 153, 153)","rgb(204, 204, 204)","rgb(238, 238, 238)","rgb(243, 243, 243)","rgb(255, 255, 255)"};
	private String[] basicColors = {"rgb(255, 0, 0)","rgb(255, 153, 0)","rgb(255, 255, 0)","rgb(0, 255, 0)","rgb(0, 255, 255)","rgb(0, 0, 255)","rgb(153, 0, 255)","rgb(255, 0, 255)"};
	private String[] colorRow1 = {"rgb(244, 204, 204)","rgb(252, 229, 205)","rgb(255, 242, 204)","rgb(217, 234, 211)","rgb(208, 224, 227)","rgb(207, 226, 243)","rgb(217, 210, 233)","rgb(234, 209, 220)"};
	private String[] colorRow2 = {"rgb(234, 153, 153)","rgb(249, 203, 156)","rgb(255, 229, 153)","rgb(182, 215, 168)","rgb(162, 196, 201)","rgb(159, 197, 232)","rgb(180, 167, 214)","rgb(213, 166, 189)"};
	private String[] colorRow3 = {"rgb(224, 102, 102)","rgb(246, 178, 107)","rgb(255, 217, 102)","rgb(147, 196, 125)","rgb(118, 165, 175)","rgb(111, 168, 220)","rgb(142, 124, 195)","rgb(194, 123, 160)"};
	private String[] colorRow4 = {"rgb(204, 0, 0)","rgb(230, 145, 56)","rgb(241, 194, 50)","rgb(106, 168, 79)","rgb(69, 129, 142)","rgb(61, 133, 198)","rgb(103, 78, 167)","rgb(166, 77, 121)"};
	private String[] colorRow5 = {"rgb(153, 0, 0)","rgb(180, 95, 6)","rgb(191, 144, 0)","rgb(56, 118, 29)","rgb(19, 79, 92)","rgb(11, 83, 148)","rgb(53, 28, 117)","rgb(116, 27, 71)"};
	private String[] colorRow6 = {"rgb(102, 0, 0)","rgb(120, 63, 4)","rgb(127, 96, 0)","rgb(39, 78, 19)","rgb(12, 52, 61)","rgb(7, 55, 99)","rgb(32, 18, 77)","rgb(76, 17, 48)"};

	private String[][] colorArray = new String[8][8];
	
	private FlexTable table = new FlexTable();
    
    private Widget currentSwab;

    private ColorPickerPreview previewWidget = ColorPickerPreview.getInstance();
    
    public ColorPickerPalette() {
        colorArray[0] = grayscaleColors;
        colorArray[1] = basicColors;
        colorArray[2] = colorRow1;
        colorArray[3] = colorRow2;
        colorArray[4] = colorRow3;
        colorArray[5] = colorRow4;
        colorArray[6] = colorRow5;
        colorArray[7] = colorRow6;

        for (int i = 0; i < colorArray.length; i++) {
        	int j = 0;
            for (String color : colorArray[i]) {
            	HTML colorSwab = new HTML();

            	colorSwab.setWidth("20px");
            	colorSwab.setHeight("20px");
            	
                colorSwab.getElement().getStyle().setProperty("border", "1px solid #666");
//                DOM.setStyleAttribute(getElement(), "cursor", "default");
                colorSwab.getElement().getStyle().setProperty("backgroundColor", color);
            	colorSwab.addClickHandler(this);
            	
            	table.setWidget(i, j, colorSwab);
            	j++;
            }
        }
        
        initWidget(table);
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    public void onClick(ClickEvent event) {
    	deselectSwab();

        currentSwab = (Widget)event.getSource();
        previewWidget.setPreview(currentSwab.getElement().getStyle().getBackgroundColor());  
        
        selectSwab();
    }
    
    private void selectSwab() {
//    	-moz-box-shadow: 0 0 5px 5px #888;
//    -webkit-box-shadow: 0 0 5px 5px#888;
//    box-shadow: 0 0 5px 5px #888;
    	if (currentSwab != null) {
    		currentSwab.getElement().addClassName("color-box-selected");

//	        DOM.setStyleAttribute(currentSwab.getElement(), "-moz-box-shadow", "0 0 5px 5px #888");
//	        DOM.setStyleAttribute(currentSwab.getElement(), "-webkit-box-shadow", "0 0 5px 5px #888");
//	        DOM.setStyleAttribute(currentSwab.getElement(), "box-shadow", "0 0 5px 5px #888");
    	}
    }
    
    private void deselectSwab() {
    	if (currentSwab != null) {
    		currentSwab.getElement().removeClassName("color-box-selected");
    		
//	        DOM.setStyleAttribute(currentSwab.getElement(), "-moz-box-shadow", "0");
//	        DOM.setStyleAttribute(currentSwab.getElement(), "-webkit-box-shadow", "0");
//	        DOM.setStyleAttribute(currentSwab.getElement(), "box-shadow", "0");
    	}
    }
    
    public void setColor(String color) {
    	deselectSwab();
        for (int i = 0; i < colorArray.length; i++) {
            for (int j = 0; j < colorArray[i].length; j++) {
            	if (colorArray[i][j].equals(color)) {
            		currentSwab = table.getWidget(i, j);
            		selectSwab();
            		
            		return;
            	}
            }
        }
    }
}