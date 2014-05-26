// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.gadget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.GadgetSettingsInfo;
import com.risevision.common.client.info.GadgetSettingsInfo.GadgetSetting;
import com.risevision.common.client.info.GadgetSettingsInfo.GadgetSettingEnumValue;
import com.risevision.common.client.info.GadgetSettingsInfo.GadgetXmlParserError;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.service.GadgetService;
import com.risevision.ui.client.common.service.GadgetServiceAsync;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.colorPicker.ColorPickerTextBox;
import com.risevision.ui.client.common.widgets.financial.StockSelectorTextBox;
import com.risevision.ui.client.common.widgets.mediaLibrary.MediaLibraryTextBox;
import com.risevision.ui.client.common.widgets.textStyle.TextStyleWidget;
import com.risevision.ui.client.presentation.placeholder.PlaceholderManageWidget;
import com.risevision.ui.client.presentation.placeholder.PlaylistItemManageWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GadgetCustomSettingsWidget {
	//private static GadgetCustomSettingsWidget instance;
//	private String gadgetUrl;
	private final GadgetServiceAsync gadgetService = GWT.create(GadgetService.class);
	private GadgetXmlRpcCallBackHandler rpcCallBackHandler = new GadgetXmlRpcCallBackHandler();
	private GadgetRpcCallBackHandler gadgetRpcCallbackHandler = new GadgetRpcCallBackHandler();
//	private ArrayList<GadgetXml> gadgets = new ArrayList<GadgetXml>();
	
	private GadgetSettingsInfo gadgetSettings;
//	HashMap<String, String> urlParams = new HashMap<String, String>();
//	private String titleUrl;
	//UI pieces
//	private ActionsWidget actionsWidget = new ActionsWidget();
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
//	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private Label statusBox = new Label();
	private FlexTable mainGrid;
	private int gridSize, row = -1;
	//UI: Gadget fields
//	private Label lbTitle = new Label("Settings");
//	private HorizontalPanel linksPanel = new HorizontalPanel();
//	private HorizontalPanel helpLinkPanel = new HorizontalPanel();
	private Anchor helpLink = new Anchor("Help");
//	private Anchor showLink = new Anchor("Show");
//	private Anchor hideLink = new Anchor("Hide");
	//last modified
//	private Command onSave;
//	private boolean isRiseGadget;
//	private boolean isCustomUI;
	private boolean isShowing = false;
	
	private Command onSave;
	
	private GadgetCustomUIWidget customUIWidget;

	public GadgetCustomSettingsWidget(FlexTable grid, Command onSave) {
		this.mainGrid = grid;
		this.onSave = onSave;
		
		gridSize = grid.getRowCount();
//		super(false, false);
		//initWidget(mainPanel);
//		add(mainPanel);
		
//		helpLinkPanel.add(helpLink);	
//		helpLinkPanel.add(new SpacerWidget());
//		helpLinkPanel.add(new HTML("|"));
//		helpLinkPanel.add(new SpacerWidget());
		helpLink.setTarget("_blank");
//		linksPanel.add(helpLinkPanel);
//		linksPanel.add(showLink);
//		linksPanel.add(hideLink);
//		helpLinkPanel.setVisible(false);
		
//		mainPanel.add(titlePanel);
		
//		mainPanel.add(linksPanel);
		mainPanel.add(helpLink);
		
		mainPanel.add(statusBox);
		mainPanel.add(formValidator);
//		mainPanel.add(mainGrid);
//		mainPanel.add(actionsWidget);
		
		styleControls();
				
		initActions();
		
		customUIWidget = new GadgetCustomUIWidget(onSave);
	}

	private void styleControls() {
		//style the table	
//		mainGrid.setCellSpacing(0);
//		mainGrid.setCellPadding(0);
//		mainGrid.setStyleName("rdn-Table");

//		lbTitle.setStyleName("rdn-Head");
//		this.getElement().getStyle().setProperty("padding", "10px");
//		actionsWidget.addStyleName("rdn-VerticalSpacer");
	}

	private void initActions() {
//		showLink.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				show();
//			}
//		});
		
//		hideLink.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				save();
//			}
//		});
		
//		Command cmdSave = new Command() {
//			public void execute() {
//				doActionSave();
//			}
//		};
//		Command cmdCancel = new Command() {
//			public void execute() {
//				doActionCancel();
//			}
//		};
//
//		actionsWidget.addAction("Save", cmdSave);
//		actionsWidget.addAction("Cancel", cmdCancel);

	}


//	public static GadgetCustomSettingsWidget getInstance() {
//		try {
//			if (instance == null)
//				instance = new GadgetCustomSettingsWidget();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return instance;
//	}

//	protected void onLoad() {
//		super.onLoad();
//	}

//	@Override
//	public boolean onKeyDownPreview(char key, int modifiers) {
//		// Use the popup's key preview hooks to close the dialog when either
//		// enter or escape is pressed.
//		switch (key) {
//			case KeyCodes.KEY_ESCAPE:
//				hide();
//				break;
//			}
//
//		return true;
//	}
	
	public void setGadgetId(String gadgetId) {
		loadGadgetName(gadgetId);
	}
	
	private void loadGadgetName(String id) {
		setStatus("Loading Gadget...");
		gadgetService.getGadget(SelectedCompanyController.getInstance().getSelectedCompanyId(), id, gadgetRpcCallbackHandler);
	}
	
	public void setGadgetParams(String gadgetUrl, String additionalParams) {
//		customUIWidget.init(gadgetUrl, additionalParams);

//		if (gadgetUrl != null)
//			gadgetUrl = gadgetUrl.trim();
//		this.gadgetUrl = gadgetUrl;
		
		gadgetSettings = new GadgetSettingsInfo(gadgetUrl, additionalParams);
		
		isShowing = true;
		
//		urlParams.clear();
		formValidator.clear();
		show();
	}
	
	public String getGadgetUrl() {
		if (!gadgetSettings.isCustomUI()) {
			return gadgetSettings.getGadgetUrl();
		}
		else {
			return customUIWidget.getGadgetUrl();
		}
	}
	
	public String getAdditionalParams() {
		if (!gadgetSettings.isCustomUI()) {
			return null;
		}
		else {
			return customUIWidget.getAdditionalParams();
		}
	}
	
	public void setShowing(boolean isShowing) {
		this.isShowing = isShowing;
	}
	
	private void show() {
//		super.show();
//		center();
		
//		this.setGadgetUrl(gadgetUrl);
//		this.onSave = onChange;		
//		actionsWidget.setEnabled(true);
		
		row = gridSize - 1;
//		mainGrid.resize(mainGrid.getRowCount() + 1, 2);
		gridAdd("<b>Settings<b>", mainPanel);
//		gridSize = row;

		setStatus("Loading Gadget...");
		loadDataRPC();
	}
	
	private void setStatus(String status) {
		statusBox.setText(status);
		if (status.isEmpty()) {
			statusBox.setVisible(false);
//			linksPanel.setVisible(true);
		}
		else {
			statusBox.setVisible(true);
//			linksPanel.setVisible(false);
		}
	}
	
//	private void hide() {
//		helpLinkPanel.setVisible(false);
//		showLink.setVisible(true);
//		hideLink.setVisible(false);
//		
//		gadgetSettings.getGadgetSettings().clear();
//		buildUI();
//	}
	
//	public void center() {
//		super.center();
//		movePopup(15, -5);
//	}

	// leftMove and topMove are defined as % values.
//	private void movePopup(int leftMove, int topMove) {
//		int left, top;
//		left = (int) (getAbsoluteLeft() + ((Window.getClientWidth() / 100.0) * leftMove));
//		top = (int) (getAbsoluteTop() + ((Window.getClientHeight() / 100.0) * topMove));
//		
//		if (top < 0) {
//			top = 0;
//		}
//		
//		setPopupPosition(left, top);
//	}
	
	private void bindData(String gadgetXml) {
//		if (gadgetInfo == null)
//			return;

		//tbName.setText(gadgetInfo.getName());
		//tbUrl.setText(gadgetInfo.getUrl());
		
		if (!isShowing) {
			return;
		}
		
		setStatus("");
		helpLink.setVisible(false);

		try {
			gadgetSettings.setGadgetXml(gadgetXml);
			
			if (!RiseUtils.strIsNullOrEmpty(gadgetSettings.getTitleUrl())) {
				helpLink.setHref(gadgetSettings.getTitleUrl());
				helpLink.setVisible(true);
			}
			
			int numberOfVisibleUserPreferences = 0;	

			if (gadgetSettings.isCustomUI()) {
				// clear grid
				gadgetSettings.getGadgetSettings().clear();
				buildUI();
				
				row = gridSize + 1;
				mainGrid.setWidget(row, 0, customUIWidget);
				mainGrid.setWidget(row, 1, null);
				mainGrid.getFlexCellFormatter().setColSpan(row, 0, 2);
				
//				customUIWidget.show(gadgetSettings.getGadgetUrl());
				customUIWidget.show(gadgetSettings);
				centerParentWidget();
			}
			else {
//				parseUrl();
				numberOfVisibleUserPreferences = buildUI();
				if (numberOfVisibleUserPreferences == 0)
					setStatus("This gadget has no Custom Settings ");
			}
		}
		catch (GadgetXmlParserError error) {
			setStatus(error.getMessage());
		}
		
//		showLink.setVisible(false);
//		hideLink.setVisible(true);
				
//		setStatus("");
	}
	
	private void centerParentWidget() {
		if (PlaylistItemManageWidget.getInstance().isShowing()) {
			PlaylistItemManageWidget.getInstance().center();
		}
		else {
			PlaceholderManageWidget.getInstance().center();
		}
	}
	
	private void loadDataRPC() {
		//fetch XML data using server proxy in order to resolve browsers' cross-domain security restrictions
//		setStatus("Data is loading...");
		String xmlUrl = gadgetSettings.getGadgetXmlUrl();
		xmlUrl = URL.decodeQueryString(xmlUrl);
		gadgetService.getGadgetXml(xmlUrl, rpcCallBackHandler);
		//http://www.roseindia.net/tutorials/gwt/retrieving-xml-data.shtml
//		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, gadgetUrl);
//		try {
//			requestBuilder.sendRequest(null, new RequestCallback() {
//				public void onResponseReceived(Request request, Response response) {
//					actionsWidget.setEnabled(true);
//					tbName.setText(response.getText());
//					bindData();
//					statusBox.clear();
//				}
//				public void onError(Request request, Throwable ex) {
//					actionsWidget.setEnabled(true);
//					statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error: " + ex.getMessage());
//				}
//			});			
//		} catch (RequestException ex) {
//			statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error: " + ex.getMessage());
//	    }
	}

	private void gridAdd(String label, Widget widget) {
		row++;
		
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-ColumnShort");
		mainGrid.setHTML(row, 0, label);
		if (widget != null) {
			mainGrid.setWidget(row, 1, widget);
		}
	}
	
	private void gridAdd(String label, Widget widget, String styleName) {
		row++;
		
//		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-Column1");
//		mainGrid.setHTML(row, 0, label);
		
		Label rowName = new Label(label, true);
		rowName.setWidth("175px");
		rowName.getElement().getStyle().setPropertyPx("lineHeight", 16);
		
		mainGrid.setWidget(row, 0, rowName);
//		mainGrid.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
//		mainGrid.getCellFormatter().getElement(row, 0).getStyle().setPaddingTop(8, Unit.PX);
		mainGrid.getCellFormatter().getElement(row, 0).getStyle().setHeight(24, Unit.PX);
		
		if (widget != null)	{
			mainGrid.setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
			
//			mainGrid.getCellFormatter().getElement(row, 1).getStyle().setPaddingTop(8, Unit.PX);
			mainGrid.getCellFormatter().setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
		}
	}

	public void save() {
		if (gadgetSettings.isCustomUI()) {
			customUIWidget.save();
		}
		else {
			if (formValidator.validate()) {
				buildUrl();
//				hide();
				
				if (onSave != null)
					onSave.execute();
				
			}
		
		}
	}
	
//	private void doActionCancel() {
//		hide();
//	}

	private int buildUI() {
//		mainGrid.resize(gridSize + 1 + gadgets.size(), 2);
		for (int i = mainGrid.getRowCount() - 1; i >= gridSize + 1; i--) {
			mainGrid.removeRow(i);
		}
		
		row = gridSize;
		int hiddenWidgetsCount = 0;
		formValidator.clearValidationElements();
		
		if (!gadgetSettings.isCustomUI()) {
			for (GadgetSetting g: gadgetSettings.getGadgetSettings()) {
				g.setWidget(null);
				
				if (g.getName().toLowerCase().endsWith("color")) {
					g.setWidget(new ColorPickerTextBox());
					if (!RiseUtils.strIsNullOrEmpty(g.getValue()))
						((HasText)g.getWidget()).setText(g.getDecodedValue());
					gridAdd(g.getHeader(), g.getWidget(), "rdn-TextBoxMedium");
				}
				else if (g.getName().toLowerCase().endsWith("font-style")) {
					TextStyleWidget widget = new TextStyleWidget(g.getName());
					g.setWidget(widget);
					if (!RiseUtils.strIsNullOrEmpty(g.getValue()))
						((HasText)g.getWidget()).setText(g.getDecodedValue());
					gridAdd(g.getHeader(), g.getWidget(), null);
				}
				else if (g.getName().toLowerCase().endsWith("stock-selector")) {
					StockSelectorTextBox widget = new StockSelectorTextBox();
					g.setWidget(widget);
					if (!RiseUtils.strIsNullOrEmpty(g.getValue()))
						((HasText)g.getWidget()).setText(g.getDecodedValue());
					gridAdd(g.getHeader(), g.getWidget(), "rdn-TextBoxMedium");
				}
				else if (g.getName().toLowerCase().endsWith("media-url")) {
					MediaLibraryTextBox widget = new MediaLibraryTextBox(null);
					g.setWidget(widget);
					if (!RiseUtils.strIsNullOrEmpty(g.getValue()))
						((HasText)g.getWidget()).setText(g.getDecodedValue());
					gridAdd(g.getHeader(), g.getWidget(), "rdn-TextBoxMedium");
				}
				else if ("bool".equals(g.getDatatype())) {
					g.setWidget(new CheckBox());
					((CheckBox) g.getWidget()).setValue("true".equals(g.getValue()));
					gridAdd(g.getHeader(), g.getWidget(), "rdn-CheckBox");
				}
				else if ("enum".equals(g.getDatatype())) {
					g.setWidget(new RiseListBox());
					for (GadgetSettingEnumValue v: g.getEnumValues())
						((RiseListBox)g.getWidget()).addItem(v.getDisplayValue(), v.getValue());
					if (g.getValue() != null)
						((RiseListBox)g.getWidget()).setSelectedValue(g.getValue());
					gridAdd(g.getHeader(), g.getWidget(), "rdn-ListBoxLong");
				}
				else if ("hidden".equals(g.getDatatype())) {
//					g.widget = new Hidden();
//					if (!RiseUtils.strIsNullOrEmpty(g.getValue()))
//						((Hidden)g.widget).setValue(g.getDecodedValue());
					hiddenWidgetsCount++;
				}
				else {
					g.setWidget(new TextBox());
					if (!RiseUtils.strIsNullOrEmpty(g.getValue()))
						((TextBox)g.getWidget()).setText(g.getDecodedValue());
					gridAdd(g.getHeader(), g.getWidget(), "rdn-TextBoxLong");
				}
				if ((g.isRequired()) && (g.isVisible()))
					formValidator.addValidationElement(g.getWidget(), g.getDisplayName(), FormValidatorInfo.requiredFieldValidator);
			}
//			if (hiddenWidgetsCount > 0)
//				mainGrid.resize(mainGrid.getRowCount()-hiddenWidgetsCount, 2);
		}
		
		centerParentWidget();
		
		return gadgetSettings.getGadgetSettings().size() - hiddenWidgetsCount;
	}

//	private void parseUrl() {
//		try {
//			String xmlUrl = gadgetSettings.getGadgetXmlUrl();
//			String[] ray = gadgetUrl.substring(xmlUrl.length()+1).split("&");
//			urlParams.clear();
//			for (int i = 0; i < ray.length; i++) {
//				String[] substrRay = ray[i].split("=");
//				if (substrRay.length == 2)
//					urlParams.put(substrRay[0], substrRay[1]);
//				else if (substrRay.length == 1)
//					urlParams.put(substrRay[0], "");
//			}
//		} catch(Exception ex) {}
//		
//		for (GadgetSetting g: gadgetSettings.getGadgetSettings()) {
//			g.setValue(urlParams.get("up_" + g.getName()));
//		}
//	}
	
	private void buildUrl() {
//		String params = "";
		
		if (gadgetSettings.getGadgetSettings().size() > 0 /* || urlParams.size() > 0 */) {
			for (GadgetSetting g: gadgetSettings.getGadgetSettings()) {
				String value = "";

				if (g.getWidget() instanceof CheckBox) {
					value = ((CheckBox) g.getWidget()).getValue() ? "true" : "false";
				}
				else if (g.getWidget() instanceof RiseListBox) {
					value = ((RiseListBox)g.getWidget()).getSelectedValue();
				}
//				else if (g.widget instanceof TextBox || g.widget instanceof ColorPickerTextBox || g.widget instanceof TextStyleWidget)
//					value = URL.encodeQueryString(((HasText)g.widget).getText()));
				else if (g.getWidget() instanceof HasText)
					value = URL.encodeQueryString(((HasText)g.getWidget()).getText());
//				else if (g.widget instanceof Hidden)
//					value = URL.encodeQueryString(((Hidden)g.widget).getValue());
				
//				urlParams.put("up_" + g.getName(), value);
				g.setValue(value);

			}
			
//			for (Map.Entry<String, String> obj: urlParams.entrySet()) {
//				if (obj.getValue() != null && !obj.getValue().isEmpty()) {
//					params += "&" + obj.getKey() + "=" + obj.getValue();
//				}
//			}
//	
//			if (params.length() > 0)
//				params = "&" + params.substring(1);
			
//			gadgetUrl = (gadgetSettings.getGadgetXmlUrl() + params).trim();
//			gadgetUrl = gadgetSettings.getGadgetUrl();
		}
	}

//	private String getGadgetXmlUrl() {
//		//handle improperly url that have "?"
//		String res = gadgetUrl.replaceFirst("\\?", "&");
//		//get the gadget URL part
//		int queryParamsStartPos = res.indexOf('&');
//		if (queryParamsStartPos > 0)
//			res = gadgetUrl.substring(0, queryParamsStartPos);
//		return res;
//	}

	
	class GadgetXmlRpcCallBackHandler extends RiseAsyncCallback<String> {
		public void onFailure() {
			setStatus("RPC Error...");
		}

		public void onSuccess(String result) {
			bindData(result);
		}
	}
	
	class GadgetRpcCallBackHandler extends RiseAsyncCallback<GadgetInfo> {
		public void onFailure() {
			setStatus("RPC Error...");
		}

		public void onSuccess(GadgetInfo result) {
			if (result != null) {
				setGadgetParams(result.getUrl(), null);
			}
		}
	}
	
}