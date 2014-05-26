package com.risevision.ui.client.common.widgets.colorPicker;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.ui.client.common.widgets.SliderBarWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class ColorPickerDialog extends PopupPanel {
	private static ColorPickerDialog instance;

	private static final int PALETTE_PANEL = 0;
	private static final int RGB_PANEL = 1;
	private static final int HSV_PANEL = 2;
	private static final int MAP_PANEL = 3;
	
    // Define the buttons
    HorizontalPanel buttonPanel = new HorizontalPanel();
    Button okButton = new Button("Save");   // ok button
    Button cancelButton = new Button("Cancel");   // cancel button
	
	private DeckPanel contentPanel = new DeckPanel();
	private TabBar contentBar = new TabBar();
	
	private ColorPickerPalette colorPalette = new ColorPickerPalette();
	private ColorPickerRGB colorRGB = new ColorPickerRGB();
	private ColorPickerHSV colorHSV = new ColorPickerHSV();
	private ColorPickerMap colorMap = new ColorPickerMap();

    private ColorPickerPreview previewWidget = ColorPickerPreview.getInstance();
	
	private HTML transparencySwab = new HTML();
	private TextBox colorTextBox = new TextBox();
	
	private ColorPickerHistory historyWidget = new ColorPickerHistory();
	
	private SliderBarWidget alphaSlider = new SliderBarWidget(100, false);
	private TextBox alphaTextBox = new TextBox();
	
	private Command onClose;
	
	public static ColorPickerDialog getInstance() {
		try {
			if (instance == null)
				instance = new ColorPickerDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
    public ColorPickerDialog() {
		super(false, true); //set auto-hide and modal

        contentPanel.add(colorPalette);
        contentPanel.add(colorRGB);
        contentPanel.add(colorHSV);
        contentPanel.add(colorMap);
        
        contentBar.addTab("Palette");
        contentBar.addTab("RGB");
        contentBar.addTab("HSV");
        contentBar.addTab("Map");
        
        contentPanel.showWidget(PALETTE_PANEL);
        contentBar.selectTab(PALETTE_PANEL);
        
        // Define the panels
        HorizontalPanel colorPanel = new HorizontalPanel();
        colorPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        colorPanel.add(new Label("Color:"));
        colorPanel.add(new SpacerWidget());
        colorPanel.add(colorTextBox);

        HorizontalPanel transparencyPanel = new HorizontalPanel();
        transparencyPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        transparencyPanel.add(new Label("None:"));
        transparencyPanel.add(new SpacerWidget());
    	transparencySwab.setWidth("20px");
    	transparencySwab.setHeight("20px");
        DOM.setStyleAttribute(transparencySwab.getElement(), "border", "1px solid #666");
//        DOM.setStyleAttribute(getElement(), "cursor", "default");
        DOM.setStyleAttribute(transparencySwab.getElement(), "backgroundImage", "url(/images/preview-opacity.png)");
        transparencyPanel.add(transparencySwab);
        
        VerticalPanel previewPanel = new VerticalPanel();
        previewPanel.setHeight("100%");
        previewPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        previewPanel.add(previewWidget);
        previewPanel.add(historyWidget);
        
        HorizontalPanel alphaPanel = new HorizontalPanel();
        alphaPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        alphaPanel.setWidth("100%");
        alphaPanel.add(new Label("Alpha:"));
        HTML spacer3 = new HTML("&nbsp;");
        alphaPanel.add(spacer3);
        alphaPanel.setCellWidth(spacer3, "100%");
        alphaPanel.add(alphaSlider);
        
        buttonPanel.add(okButton);
        buttonPanel.add(new SpacerWidget());
        buttonPanel.add(cancelButton);
        
        // Put it together      
        FlexTable middleTable = new FlexTable();
        middleTable.setCellSpacing(0);
        middleTable.setCellPadding(0);
        
        middleTable.setWidget(0, 0, colorPanel);
        middleTable.getCellFormatter().setWidth(0, 1, "5px");
        middleTable.setWidget(0, 2, transparencyPanel);
        
        middleTable.getCellFormatter().setHeight(1, 0, "5px");
        
        middleTable.setWidget(2, 0, contentBar);
        
        middleTable.setWidget(3, 0, contentPanel);
        middleTable.getCellFormatter().setVerticalAlignment(3, 2, HasVerticalAlignment.ALIGN_MIDDLE);
        middleTable.getCellFormatter().setHorizontalAlignment(3, 2, HasHorizontalAlignment.ALIGN_CENTER);
        middleTable.setWidget(3, 2, previewPanel);
        
        middleTable.getCellFormatter().setHeight(4, 0, "5px");
        
        middleTable.setWidget(5, 0, alphaPanel);
        middleTable.setWidget(5, 2, alphaTextBox);
        
        middleTable.setWidget(6, 0, buttonPanel);

        
        setWidget(middleTable);
        
        styleControls();
        initButtons();
        initHandlers();
        initCommands();
    }
    
    private void styleControls() {
		getElement().getStyle().setProperty("padding", "10px");
		getElement().getStyle().setZIndex(1000);

		colorTextBox.setStyleName("rdn-TextBoxMedium");
		alphaTextBox.setStyleName("rdn-TextBoxShort");
		
        contentPanel.getElement().getStyle().setProperty("border", "1px solid #999");
        contentBar.getElement().getStyle().setProperty("fontWeight", "normal !important");
        
        historyWidget.addStyleName("rdn-VerticalSpacer");
        buttonPanel.addStyleName("rdn-VerticalSpacer");
    }
    
    private void initButtons() {
        okButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
            	historyWidget.addColor(previewWidget.getColor().getColor());
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
    }
    
	private void initHandlers() {
	    // Hook up a tab listener to do something when the user selects a tab.
		contentBar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				updateColor();
				
				contentPanel.showWidget(contentBar.getSelectedTab());
			}
		});
		
		colorTextBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
//				String oldColor = previewWidget.getCssColor();
				previewWidget.setPreview(colorTextBox.getText());
//				String newColor = previewWidget.getCssColor();
//				if (newColor.isEmpty()) {
//					previewWidget.setPreview(oldColor);
//				}
//				else {
//					updateColor();
//				}
			}
		});
		
		alphaTextBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				updateAlpha();
			}
		});
		
    	transparencySwab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				previewWidget.setPreview("transparent");
			}
		});
	}
	
	private void initCommands() {
		Command colorChanged = new Command() {
			@Override
			public void execute() {
				updateColor();
			}
		};
		
		previewWidget.setCommand(colorChanged);
		
		Command sliderMoved = new Command() {
			@Override
			public void execute() {
				alphaTextBox.setText((alphaSlider.getSliderPosition() / 100.0) + "");
				updateAlpha();
			}
		};
		
		alphaSlider.init(sliderMoved);
	}
	
	private void updateColor() {
		int selectedIndex = contentBar.getSelectedTab();
		Color color = previewWidget.getColor();

		switch (selectedIndex) {
		case PALETTE_PANEL:
			colorPalette.setColor(color.getColor());
			break;
		case RGB_PANEL:
			colorRGB.setColor(color);
			break;
		case HSV_PANEL:
			colorHSV.setColor(color);
			break;
		case MAP_PANEL:
			colorMap.setColor(color);
			break;
		}
		
		colorTextBox.setText(color.getColor());
		alphaTextBox.setText(color.getAlpha() + "");
		alphaSlider.setSliderPosition((int)(color.getAlpha() * 100));
	}
	
	private void updateAlpha() {
		Color color = previewWidget.getColor();
		color.setAlpha(alphaTextBox.getText());
		alphaSlider.setSliderPosition((int)(color.getAlpha() * 100));
		previewWidget.setPreview(color.getColor());
	}
	
    public String getColor() {
    	return previewWidget.getColor().getColor();
    }
    
    public void show(Command onClose) {
    	this.onClose = onClose;

    	super.show();
    	super.center();
    }
    
    public void show(Command onClose, String color) {
    	show(onClose);
    	
    	previewWidget.setPreview(color);
    }
}
