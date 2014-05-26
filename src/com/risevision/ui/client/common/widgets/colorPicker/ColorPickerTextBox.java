package com.risevision.ui.client.common.widgets.colorPicker;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class ColorPickerTextBox extends Composite implements HasText, ClickHandler {
	private TextBox colorTextBox = new TextBox();
	private HTML colorPickerButton = new HTML();
	private ColorPickerDialog colorPicker = ColorPickerDialog.getInstance();
    private ColorPickerPreview previewWidget = ColorPickerPreview.getInstance();
    private Command hideCommand;
	
	public ColorPickerTextBox() {
		HorizontalPanel panel = new HorizontalPanel();
		
        DOM.setStyleAttribute(colorPickerButton.getElement(), "backgroundImage", "url(/images/color.png)");
        DOM.setStyleAttribute(colorPickerButton.getElement(), "cursor", "hand");
        colorPickerButton.setHeight("16px");
        colorPickerButton.setWidth("16px");
        
		panel.add(colorTextBox);
		panel.add(new SpacerWidget());
		panel.add(colorPickerButton);
		
		colorPickerButton.addClickHandler(this);
		hideCommand = new Command() {
			@Override
			public void execute() {
				setText(colorPicker.getColor(), true);
			}
		};
		
		colorTextBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				previewWidget.setPreview(colorTextBox.getText());
				setText(colorPicker.getColor(), true);
			}
		});
		
		initWidget(panel);
	}
	
	@Override
	public String getText() {
		return colorTextBox.getText();
	}

	@Override
	public void setText(String text) {
		setText(text, false);
	}
	
	private void setText(String text, boolean triggerEvents) {
		Color color = new Color(text);
		colorTextBox.getElement().getStyle().setColor((color.getValue() > 60 || color.getAlpha() < 0.4) ? "black " : "white");
		
		colorTextBox.getElement().getStyle().setBackgroundColor(text);
		colorTextBox.setValue(text, triggerEvents);
	}

	@Override
	public void onClick(ClickEvent event) {
		colorPicker.show(hideCommand, colorTextBox.getText());
	}
	
	@Override
	public void setStyleName(String style){
		colorTextBox.setStyleName(style);
	}

	@Override
	public void addStyleName(String style){
		colorTextBox.addStyleName(style);
	}
	
	public void addValueChangeHandler(ValueChangeHandler<String> handler) {
		colorTextBox.addValueChangeHandler(handler);
	}
}
