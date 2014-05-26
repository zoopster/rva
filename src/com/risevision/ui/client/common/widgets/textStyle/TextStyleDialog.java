package com.risevision.ui.client.common.widgets.textStyle;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.colorPicker.ColorPickerTextBox;

public class TextStyleDialog extends PopupPanel implements ChangeHandler {
	private static TextStyleDialog instance;
	
    private static final String[][] fontFamily = {{"", ""}, {"Arial", "Arial, Arial, Helvetica, sans-serif"}, 
    	{"Arial Black", "Arial Black, Arial Black, Gadget, sans-serif"}, {"Comic Sans MS", "Comic Sans MS, Comic Sans MS, cursive"}, 
    	{"Courier New", "Courier New, Courier New, Courier, monospace"}, {"Georgia", "Georgia, Georgia, serif"}, 
    	{"Impact", "Impact, Impact, Charcoal, sans-serif"}, {"Lucida Console", "Lucida Console, Monaco, monospace"},  
    	{"Lucida Sans Unicode", "Lucida Sans Unicode, Lucida Grande, sans-serif"}, {"Palatino Linotype", "Palatino Linotype, Book Antiqua, Palatino, serif"},
    	{"Tahoma", "Tahoma, Geneva, sans-serif"}, {"Times New Roman", "Times New Roman, Times, serif"}, 
    	{"Trebuchet MS", "Trebuchet MS, Helvetica, sans-serif"}, {"Verdana", "Verdana, Verdana, Geneva, sans-serif"}, 
    	{"MS Sans Serif", "MS Sans Serif, Geneva, sans-serif"}, {"MS Serif", "MS Serif, New York, serif"}};
    
    private static final String[] fontSizes = {"", "8px", "10px", "12px", "14px", "16px", "18px", "20px", 
    	"22px", "24px", "26px", "28px", "32px", "36px", "40px", "44px", "48px", "52px", "56px", "60px"};
    
	private String className;
    
    // Define the buttons
    HorizontalPanel buttonPanel = new HorizontalPanel();
    Button okButton = new Button("Save");   // ok button
    Button cancelButton = new Button("Cancel");   // cancel button
    	
    private TextStylePreview previewWidget = TextStylePreview.getInstance();
	
	private FlexTable mainGrid = new FlexTable();
	private int row = -1;
    
	private CheckBox useUrlCheckBox = new CheckBox();
	private TextBox fontUrlTextBox = new TextBox();
	private RiseListBox familyListBox = new RiseListBox();
	private ColorPickerTextBox colorTextBox = new ColorPickerTextBox();
	private RiseListBox sizeListBox = new RiseListBox();
	private CheckBox boldCheckBox = new CheckBox();
	private CheckBox italicCheckBox = new CheckBox();
	
	private Label cssLabel = new Label();
	private Anchor showCssLink = new Anchor("Edit");
	private TextBox cssTextBox = new TextBox();
	
	private Command onClose;
	
	public static TextStyleDialog getInstance() {
		try {
			if (instance == null)
				instance = new TextStyleDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
    public TextStyleDialog() {
		super(false, true); //set auto-hide and modal
        
        buttonPanel.add(okButton);
        buttonPanel.add(new SpacerWidget());
        buttonPanel.add(cancelButton);
        
        HorizontalPanel stylePanel = new HorizontalPanel();
        stylePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        stylePanel.add(boldCheckBox);
        stylePanel.add(new Label("Bold"));
        stylePanel.add(new SpacerWidget());
        stylePanel.add(italicCheckBox);
        stylePanel.add(new Label("Italic"));
        
    	HorizontalPanel cssPanel = new HorizontalPanel();
		cssPanel.add(cssLabel);
		cssPanel.add(cssTextBox);
		cssPanel.add(new SpacerWidget());
		cssPanel.add(showCssLink);
        
        gridAdd("Use URL Font:", useUrlCheckBox, "rdn-CheckBox");
        gridAdd("Font URL:", fontUrlTextBox, "rdn-TextBoxMedium");
		mainGrid.getRowFormatter().setVisible(1, false);
        gridAdd("Font Family:", familyListBox, "rdn-ListBoxMedium");
        gridAdd("Font Color:", colorTextBox, "rdn-TextBoxMedium");
        gridAdd("Font Size:", sizeListBox, "rdn-ListBoxShort");
        gridAdd("", stylePanel, "");
        gridAdd("CSS Style:", cssPanel, "");

        
        mainGrid.getFlexCellFormatter().setColSpan(6, 1, 2);
        mainGrid.getFlexCellFormatter().setRowSpan(0, 2, 6);
        mainGrid.getCellFormatter().setWidth(0, 2, "100%");
        mainGrid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
        mainGrid.setWidget(0, 2, previewWidget);

        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.add(mainGrid);
        mainPanel.add(buttonPanel);
        
        setWidget(mainPanel);
        
        updateTextBoxes();
        styleControls();
        initButtons();
        initHandlers();
    }
    
    private void updateTextBoxes() {
    	for (String[] s : fontFamily) {
    		familyListBox.addItem(s[0], s[1]);
    	}
    	
    	for (String s : fontSizes) {
    		sizeListBox.addItem(s, s);
    	}
    }
    
    private void styleControls() {
		mainGrid.setCellSpacing(0);
		mainGrid.setCellPadding(0);
		mainGrid.setStyleName("rdn-Table");
		
		boldCheckBox.addStyleName("rdn-CheckBox");
		italicCheckBox.addStyleName("rdn-CheckBox");
		
		cssLabel.addStyleName("rdn-OverflowElipsis");
		cssLabel.setWidth("420px");
		cssTextBox.setStyleName("rdn-TextBoxLong");
		
		getElement().getStyle().setProperty("padding", "10px");
		getElement().getStyle().setZIndex(1000);
	
        buttonPanel.addStyleName("rdn-VerticalSpacer");
    }
    
    private void initButtons() {
        okButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
            	if (onClose != null) {
            		onClose.execute();
            	}
                hide();
            }
        });
        
        cancelButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                hide();
            }
        });
        
		showCssLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showCssTextBox(true);
			}
		});
    }
	
	protected void showCssTextBox(boolean visible) {
		showCssLink.setVisible(!visible);
		cssLabel.setVisible(!visible);
		cssTextBox.setVisible(visible);
		cssLabel.setText(cssTextBox.getText());
	}
    
	private void initHandlers() {
		cssTextBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				bindData(cssTextBox.getText());
			}
		});
		
		useUrlCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				mainGrid.getRowFormatter().setVisible(1, event.getValue());
				mainGrid.getRowFormatter().setVisible(2, !event.getValue());
				
				saveData();
			}
		});
		
		fontUrlTextBox.addChangeHandler(this);
		colorTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				saveData();
			}
		});
		
		familyListBox.addChangeHandler(this);
		sizeListBox.addChangeHandler(this);
		
		boldCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				saveData();
			}
		});
		
		italicCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				saveData();
			}
		});
	}
	
    public String getStyle() {
    	return cssTextBox.getText();
    }
    
    public void show(Command onClose, String style, String className) {
    	super.show();
    	this.onClose = onClose;
    	this.className = className;
    	
    	if (style == null) {
    		style = "";
    	}
    	bindData(style);
    	showCssTextBox(false);
    	
    	super.center();
    }
    
	@Override
	public void onChange(ChangeEvent event) {
		saveData();
	}
    
    private void bindData(String style) {
//    	style = "@font-face { font-Family: text_font-style; src: url('Delicious-Roman.otf'); } .text_font-style{font-face: text_font-style; color:red;font-size:24px;} ";
    	
    	clearData();
    	
    	String properties = getProperty(style, "." + className);
    	
    	if (getStyleProperty(properties, "font-family").equals(className) && className.equals(getFontFaceFamily(style))) {
    		useUrlCheckBox.setValue(true, true);
    		fontUrlTextBox.setText(getFontFaceUrl(style));
    	}
    	else {
    		useUrlCheckBox.setValue(false, true);
    	}
    	
    	if (!properties.isEmpty()) {
    		familyListBox.setSelectedValue(getStyleProperty(properties, "font-family"));
    		colorTextBox.setText(getStyleProperty(properties, "color"));
    		sizeListBox.setSelectedValue(getStyleProperty(properties, "font-size"));

    		boldCheckBox.setValue("bold".equals(getStyleProperty(properties, "font-weight")));
    		italicCheckBox.setValue("italic".equals(getStyleProperty(properties, "font-style")));
    	}

    	cssTextBox.setText(style);
    	cssLabel.setText(style);
		previewWidget.setPreview(style, className);
    }
    
	private void clearData() {
		familyListBox.setSelectedValue("");
		colorTextBox.setText("");
		sizeListBox.setSelectedValue("");

		boldCheckBox.setValue(false);
		italicCheckBox.setValue(false);
		
		cssTextBox.setText("");
		cssLabel.setText("");
		previewWidget.setPreview("", className);
	}
    
    private void saveData() {
    	String style = cssTextBox.getText();
    			    	
    	String properties = getProperty(style, "." + className);
    	int j = properties.isEmpty() ? -1 : style.indexOf(properties) + properties.length();
    	
    	if (properties.isEmpty()) {
    		style = "." + className;
    	}
	
    	if (!useUrlCheckBox.getValue()) {
    		properties = updateStyleProperty("font-family", familyListBox.getSelectedValue(), properties);
    	}
    	else {
    		properties = updateStyleProperty("font-family", className, properties);
    	}
    	
    	properties = updateStyleProperty("color", colorTextBox.getText(), properties);
    	properties = updateStyleProperty("font-size", sizeListBox.getSelectedValue(), properties);
    	
    	if (boldCheckBox.getValue()) {
    		properties = updateStyleProperty("font-weight", "bold", properties);
    	}
    	else if ("bold".equals(getStyleProperty(properties, "font-weight"))){
    		properties = updateStyleProperty("font-weight", "normal", properties);
    	}
    	
    	if (italicCheckBox.getValue()) {
    		properties = updateStyleProperty("font-style", "italic", properties);
    	}
    	else if ("italic".equals(getStyleProperty(properties, "font-style"))){
    		properties = updateStyleProperty("font-style", "normal", properties);
    	}
    		
		style = style.substring(0, style.indexOf("." + className) + className.length() + 1) + "{" + properties + "}" + style.substring(j == -1 ? style.length() : j + 1);
    	
    	properties = getProperty(style, "@font-face");
    	j = properties.isEmpty() ? -1 : style.indexOf(properties) + properties.length();
    	
    	if (useUrlCheckBox.getValue()) {
        	if (properties.isEmpty()) {
        		style = style + " @font-face";
        	}
        	
    		properties = updateStyleProperty("font-family", className, properties);
    		properties = updateStyleProperty("src", "url('" + fontUrlTextBox.getText() + "')", properties);
    		
    		style = style.substring(0, style.indexOf("@font-face") + "@font-face".length()) + "{" + properties + "}" + style.substring(j == -1 ? style.length() : j + 1);
    	}
    	else {
    		
    	}	
		
    	cssTextBox.setText(style);
    	cssLabel.setText(style);
		previewWidget.setPreview(style, className);
    }
    
    private static String getFontFaceFamily(String cssStyle) {
    	String properties = getProperty(cssStyle, "@font-face");
    	
    	if (!properties.isEmpty()) {
			return getStyleProperty(properties, "font-family");
		}
    	
    	return "";
    }
    
    // CSS example: @font-face { font-family: Delicious; src: url('Delicious-Roman.otf'); } 
    private static String getFontFaceUrl(String cssStyle) {
    	String properties = getProperty(cssStyle, "@font-face");
    	
    	if (!properties.isEmpty()) {
			String urlString = getStyleProperty(properties, "src");
			if (!urlString.isEmpty()) {
				int i = urlString.indexOf("url");
    		    if (i != -1) {
            		i = urlString.indexOf("(", i);
            		if (i != -1) {
                		int j = urlString.indexOf(")", i);
                		if (j != -1) {
                			return urlString.substring(i + 1, j).replace('\"', ' ').replace('\'', ' ').trim();
                		}
            		}
		    	}
			}
		}
    	
    	return "";
    }
    
    private static String getProperty(String style, String property) {
    	String properties = "";
    	// parse CSS and update style elements
    	int i = style.indexOf(property);
    	int j = -1;
    	if (i != -1) {
    		i = style.indexOf("{", i);
    		if (i != -1) {
        		j = style.indexOf("}", i);
        		if (j != -1) {
        			properties = style.substring(i + 1, j).trim();
        		}
    		}
    	}
    	
    	return properties;
    }
    
    private static String getStyleProperty(String cssStyle, String property) {
    	int i = cssStyle.toLowerCase().indexOf(property.toLowerCase());
    	if (i != -1) {
    		String valueStart = cssStyle.substring(i + property.length()).trim();
    		if (!valueStart.isEmpty() && valueStart.charAt(0) == ':') {
    			valueStart = valueStart.substring(1);
    			int end = valueStart.indexOf(";");
    			if (end != -1) {
    				return valueStart.substring(0, end).trim();
    			}
    			else return valueStart.trim();
    		}
    	}
    	
    	return "";
    }

	private String updateStyleProperty(String param, String value, String styleToken) {
		if (value.isEmpty()) {
			return styleToken;
		}
		
		String[] tokens = styleToken.split(";");
		boolean found = false;
		for (int x = 0; x < tokens.length; x++) {
			if (tokens[x].indexOf(":") != -1){			
				if (tokens[x].substring(0, tokens[x].indexOf(":")).trim().equalsIgnoreCase(param)){				
					tokens[x] = param + ":" + value;
					found = true;
				}
			}
		}
		if (!found)
			return toString(tokens, ";") + param + ":" + value + ";";
		return toString(tokens, ";");
	}
	
	private static String toString(String[] tokens, String divider) {
		String result = "";
		for (String token: tokens){
			if (!token.isEmpty())
				result += token + divider;
		}
		return result;
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

	public String getLabelText() {
		return getLabelText(cssTextBox.getText(), className);
	}
	
    public static String getLabelText(String style, String className) {
    	String returnString = "";
    	
        String properties = getProperty(style, "." + className);
		if (!properties.isEmpty()) {    		
	    	if (getStyleProperty(properties, "font-family").equals(className) && className.equals(getFontFaceFamily(style))) {
	    		returnString += "Custom font, ";
	    	}
	    	else if (!getStyleProperty(properties, "font-family").isEmpty()) {
	    		String font = getStyleProperty(properties, "font-family");
	    		boolean found = false;
	    		
	        	for (String[] s : fontFamily) {
	        		if (s[1].equals(font)) {
        	    		returnString += s[0] + ", ";
        	    		found = true;
	        		}
	        	}
	    		if (!found) {
	    			returnString += font + ", ";
	    		}
	    	}
	    	else if (!getStyleProperty(properties, "font").isEmpty()) {
	    		returnString += getStyleProperty(properties, "font") + ", ";
	    	}
//        	    	else {
//        	    		returnString += "Font: none" + ", ";
//        	    	}

	    	returnString += getStyleProperty(properties, "font-size").isEmpty() ? "" : getStyleProperty(properties, "font-size") + ", ";

	    	returnString += "bold".equals(getStyleProperty(properties, "font-weight")) ? "bold, " : "";
	    	returnString += "italic".equals(getStyleProperty(properties, "font-style")) ? "italic, " : "";
	    	returnString += getStyleProperty(properties, "color").isEmpty() ? "" : "color: " + getStyleProperty(properties, "color") + ", ";
	    	
			if (!returnString.isEmpty())
				returnString = returnString.substring(0, returnString.length() - 2);
		}

    	return returnString;
    }
	
}
