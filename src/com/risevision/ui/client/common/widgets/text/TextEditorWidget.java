// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.text;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.ui.client.presentation.common.HtmlEditorWidget;

public class TextEditorWidget extends Composite {
	private DeckPanel editorPanel = new DeckPanel();
	private TabBar editorBar = new TabBar();
	private Grid textEditorGrid = new Grid(2, 1);
	private SimplePanel textAreaScrollPanel = new SimplePanel();
	private AbsolutePanel textAreaInnerPanel = new AbsolutePanel();
	private SimplePanel textAreaBackgroundPanel = new SimplePanel();
	private RichTextArea textArea = new RichTextArea();
	private RichTextToolbar textToolbar = new RichTextToolbar(textArea);
	private HtmlEditorWidget htmlEditor = new HtmlEditorWidget(true);
	
	private final String HTML = "" +
		"<html>\n" +
		"\t<head>\n" +
		"\t\t<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
		"\t\t<title></title>\n" +
		"\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"/style/richText.css\"\n" +
		"\t</head>\n" +
		"\n" +
		"\t<body class=\"ContentBordered\" >\n" +
		"\t</body>\n" +
		"</html>\n";

	public TextEditorWidget() {	
		// Create the text area and toolbar
		// area.ensureDebugId("cwRichText-area");
//		textArea.setSize("100%", "14em");
//		textArea.setStyleName("rdn-DeckPanel");
		// toolbar.ensureDebugId("cwRichText-toolbar");
		textToolbar.setWidth("465px");
		
		textAreaScrollPanel.add(textAreaInnerPanel);
		textAreaScrollPanel.setStyleName("rdn-DeckPanel");
		textAreaScrollPanel.getElement().getStyle().setBackgroundColor("#DDDDDD");
		textAreaScrollPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		
		textAreaInnerPanel.add(textAreaBackgroundPanel, 5, 5);

		textAreaBackgroundPanel.add(textArea);
		textAreaBackgroundPanel.getElement().getStyle().setBackgroundColor("#FFFFFF");
		
		textArea.getElement().getStyle().setBorderWidth(0, Unit.PX);
		
		textEditorGrid.setWidget(0, 0, textToolbar);
		textEditorGrid.setWidget(1, 0, textAreaScrollPanel);
		
		textEditorGrid.setCellSpacing(0);
		textEditorGrid.setCellPadding(0);
		
		editorPanel.add(textEditorGrid);
		editorPanel.add(htmlEditor);
		
		editorPanel.showWidget(0);
		
		editorBar.addTab("Design");
		editorBar.addTab("HTML");
		editorBar.selectTab(0);
		
	    // Hook up a tab listener to do something when the user selects a tab.
		editorBar.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (event.getSelectedItem() != editorPanel.getVisibleWidget()) {
					boolean showDesign = event.getSelectedItem() == 0;
					
					editorPanel.showWidget(showDesign ? 0 : 1);
					if (showDesign) {
						textArea.setHTML(htmlEditor.getText());
					}
					else {
						htmlEditor.setText(textArea.getHTML());
					}
				}
			}
		});
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(editorBar);
		editorBar.getElement().getParentElement().getStyle().setProperty("lineHeight", "normal !important");
		mainPanel.add(editorPanel);
		
		initWidget(mainPanel);
	}
	
	protected void onLoad() {
		super.onLoad();
		
		Element el = textArea.getElement();
		if (el != null) {
			updateEditorIFrame(el, HTML);
		}
		
		editorBar.selectTab(0, true);
	}
	
	private native void updateEditorIFrame(Element el, String html) /*-{
		try {
//			debugger;
			
			var elFrame = (el.contentWindow) ? el.contentWindow : (el.contentDocument.document) ? el.contentDocument.document : el.contentDocument;

			if (elFrame) {
			    elFrame.document.open();
			    elFrame.document.write(html);
			    elFrame.document.close();
			}	
	
//			var fileref = elFrame.document.createElement("link");
//			fileref.setAttribute("rel", "stylesheet");
//			fileref.setAttribute("type", "text/css");
//			fileref.setAttribute("href", "/style/richText.css");
//			
//			elFrame.document.body.className = "ContentBordered"; 
		}
		catch (err) {

		}
	}-*/;

	public String getHTML() {
		return editorPanel.getVisibleWidget() == 0 ? textArea.getHTML() : htmlEditor.getText();
	}

	public void setHTML(String html) {
		textArea.setHTML(html);
		htmlEditor.setText(html);
	}

	public void resizeTextArea(int width, int height) {
		int outerWidth, outerHeight;
		int verticalScroll = 0;
		if (height > Window.getClientHeight() - 320) {	
			outerHeight = Window.getClientHeight() - 320;
			verticalScroll = 16;
		}
		else {
			outerHeight = height + 10;
		}
		
		if (width < 465 - 10 - verticalScroll) {
			outerWidth = 465;
		}
		else if (width > Window.getClientWidth() - 160 - verticalScroll) {	
			outerWidth = Window.getClientWidth() - 160;
		}
		else {
			outerWidth = width + 10 + verticalScroll;
		}

		textAreaScrollPanel.setSize(outerWidth + "px", outerHeight + "px");
		
		textAreaInnerPanel.setPixelSize(width + 10, height + 10);
		textAreaBackgroundPanel.setPixelSize(width, height);
		textArea.setPixelSize(width, height);
		
		htmlEditor.setSize(Math.max(outerWidth, 500) + "px", Math.max((outerHeight + 53), 175) + "px");
	}

}
