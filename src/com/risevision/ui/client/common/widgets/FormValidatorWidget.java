// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.FormValidatorInfo;

public class FormValidatorWidget extends VerticalPanel {
	private ArrayList<FormValidatorInfo> formValidatorList;
	
	private RegExp urlValidator;
	private RegExp emailValidator;
	
	public FormValidatorWidget(){
		formValidatorList = new ArrayList<FormValidatorInfo>();
		
		setStyleName("rdn-Validator");
	}
	
	public void addValidationElement(Object uiElement, String uiElementName, String validationType){
		addValidationElement(uiElement, uiElementName, validationType, false);
	}
	
	public void addValidationElement(Object uiElement, String uiElementName, String validationType, boolean customMessage){ 
		addValidationElement(uiElement, uiElementName, validationType, customMessage, null, null);
	}

	public void addValidationElement(Object uiElement, String uiElementName, String validationType, boolean customMessage, Object minValue, Object maxValue){ 
		if (!elementExists(uiElement, validationType)){
			FormValidatorInfo formValidator = new FormValidatorInfo(uiElement, uiElementName, validationType, customMessage, minValue, maxValue);
			formValidatorList.add(formValidator);
		}
	}

	public void addErrorMessage(String errorMessage){
		Label errorLabel = new Label(errorMessage);
		add(errorLabel);
	}
	
	private boolean elementExists(Object uiElement, String validationType) {
		for (FormValidatorInfo validatorInfo : formValidatorList) {
			if (validatorInfo.getUiElement() == uiElement && validatorInfo.getValidationType().equals(validationType)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeValidationElement(Object uiElement){
		for (int i = 0; i < formValidatorList.size(); i++) {
			FormValidatorInfo validatorInfo = formValidatorList.get(i);
			if (validatorInfo.getUiElement() == uiElement){
				formValidatorList.remove(validatorInfo);
			}
		}
	}
	
	public void clearValidationElements() {
		formValidatorList.clear();
	}
	
	public boolean validate(){
		boolean result = true;
		
		clear();
		
		for (FormValidatorInfo formValidator : formValidatorList) {
			if (formValidator.getValidationType().equals(FormValidatorInfo.urlValidator)) {
				result = validateItemUrl(formValidator) && result;
			}
			else {
				if (formValidator.getUiElement() instanceof CheckBox) {
					result = validate(((CheckBox)formValidator.getUiElement()).getValue(), formValidator.getUiElementName(), formValidator.getValidationType(), formValidator.isCustomMessage()) && result;
				}
				else if (formValidator.getUiElement() instanceof RiseListBox) {
					result = validate(((RiseListBox)formValidator.getUiElement()).getSelectedValue(), formValidator.getUiElementName(), formValidator.getValidationType(), formValidator.isCustomMessage()) && result;
				}
				else if (formValidator.getUiElement() instanceof HasText) {
					result = validate(((HasText)formValidator.getUiElement()).getText(), formValidator.getUiElementName(), formValidator.getValidationType(), formValidator.isCustomMessage()) && result;
				}
			}
		}
		return result;
	}

	public boolean validate(Object uiElement){
		boolean result = true;
		
		for (FormValidatorInfo formValidator : formValidatorList) {
			if (formValidator.getUiElement() == uiElement) 
				result = validateItem(formValidator);
		}
		return result;
	}

	private boolean validateItem(FormValidatorInfo formValidator){
		boolean result = true;
		
		if (formValidator.getValidationType().equals(FormValidatorInfo.requiredFieldValidator)) 
			result = validateItemRequired(formValidator);
		if (formValidator.getValidationType().equals(FormValidatorInfo.dateValidator)) 
			result = validateItemDate(formValidator);
		if (formValidator.getValidationType().equals(FormValidatorInfo.intValidator)) 
			result = validateItemInt(formValidator);
		if (formValidator.getValidationType().equals(FormValidatorInfo.urlValidator))
			result = validateItemUrl(formValidator);
		
		return result;
	}

	private boolean validateItemRequired(FormValidatorInfo formValidator){
		boolean result = true;
		
		if (formValidator.getUiElement() instanceof HasText)
			result = !((HasText)formValidator.getUiElement()).getText().isEmpty();
		else if (formValidator.getUiElement() instanceof HasValue<?>)
				result = (((HasValue<?>)formValidator.getUiElement()).getValue() != null);

		if (!result) {
			if (!formValidator.isCustomMessage())
				addErrorMessage("* " + formValidator.getUiElementName() + " is a required field.");
			else
				addErrorMessage(formValidator.getUiElementName());
		}
			
		return result;
	}

	private boolean validateItemDate(FormValidatorInfo formValidator) {
		//not very accurate implementation because it doesn't take date format in consideration
		boolean result = true;
		Object value = getUiElementValue(formValidator);
		
		if ((value != null) && !(value instanceof Date)) {
			if (value instanceof String)
				try {
					DateTimeFormat.getFormat(RiseUtils.SHORT_DATE_FORMAT).parse((String)value);
				} catch (Exception e) {
					result = false;
				}
			else
				result = false;
		}

		if (!result) {
			addErrorMessage("* " + formValidator.getUiElementName() + " is a valid date.");
		}
			
		return result;
	}

	private boolean validateItemInt(FormValidatorInfo formValidator) {
		boolean result = true;
		int intResult = 0;
		Object value = getUiElementValue(formValidator);
		
		if ((value != null) && !(value instanceof Integer)) {
			if (value instanceof String)
				try {
					intResult = Integer.parseInt((String)value);
				} catch (Exception e) {
					result = false;
				}
			else
				result = false;
		}

		if (!result) {
			addErrorMessage("* " + formValidator.getUiElementName() + " is not a valid integer value.");
		} else {
			//range validation
			try {
				if ((formValidator.getMinValue() != null) && (formValidator.getMaxValue() != null)) {
					int minValue = (Integer) formValidator.getMinValue();
					int maxValue = (Integer) formValidator.getMaxValue();
					result = ((intResult >= minValue) && (intResult <= maxValue));
					if (!result)
						addErrorMessage("* " + formValidator.getUiElementName() + " value must be between " +Integer.toString(minValue)+ " and "+Integer.toString(maxValue)+".");
				}
			} catch (Exception e) {}
		}
		
		return result;
	}
	
	private boolean validateItemUrl(FormValidatorInfo formValidator) {
		if (urlValidator == null) {
			// [AD] Added \\[ \\] \\( \\) - brackets for the URL validation
//			urlValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+\\.[a-zA-Z]{2,}(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\[\\]\\(\\)\\_\\-/]+)*){1}$");
		        
			// regex where top level domain is not required
			urlValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+(:\\d{1,5})?(/[\\w #!:.?+=&%@!\\[\\]\\(\\)\\_\\-/]+)*){1}$");
		        
		}
		Object value = getUiElementValue(formValidator);
		
		if (value != null && value instanceof String) {
			String s = (String) value;
			
			if (urlValidator.exec(s) != null) {
				return true;
			}
				
			addErrorMessage("* " + formValidator.getUiElementName() + " is not a valid URL.");
		}
		
		return false;
	}
	
	private Object getUiElementValue(FormValidatorInfo formValidator) {
		Object value = null;
		
		if (formValidator.getUiElement() instanceof HasValue<?>)
			value = ((HasValue<?>)formValidator.getUiElement()).getValue();
		else if (formValidator.getUiElement() instanceof HasText)
			value = ((HasText)formValidator.getUiElement()).getText();
		
		return value;
	}

	private boolean validate(String text, String elementName, String validationType, boolean customMessage) {
		if (validationType.equals(FormValidatorInfo.requiredFieldValidator) && (text == null || text.isEmpty())) {
			if (!customMessage)
				addErrorMessage("* " + elementName + " is a required field.");
			else
				addErrorMessage(elementName);
			return false;
		}
//		else if (validationType.equals(FormValidatorInfo.phoneNumberValidator) && textBox.getText().isEmpty()){
//			//Initialize reg ex for phone number.
//			String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
//			CharSequence inputStr = textBox.getText();
//			
//			Pattern pattern = Pattern.compile(expression);
//			Matcher matcher = pattern.matcher(inputStr);
//			if(!matcher.matches()){
//				Label errorLabel = new Label("* " + textBoxName + " is not a valid Phone Number.");
//				
//				validationPanel.add(errorLabel);
//				return false;
//			}
//		}
		else if (validationType.equals(FormValidatorInfo.emailValidator) && !RiseUtils.strIsNullOrEmpty(text)) {
			if (emailValidator == null) {
//				emailValidator = RegExp.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$");
				emailValidator = RegExp.compile("^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$");
			}

			if (emailValidator.exec(text) != null) {
				return true;
			}
					
			addErrorMessage("* " + elementName + " is not a valid Email.");

			return false;
		}
		
		return true;
	}	
	
	private boolean validate(boolean value, String elementName, String validationType, boolean customMessage) {
		if (validationType.equals(FormValidatorInfo.requiredFieldValidator) && !value) {
			if (!customMessage)
				addErrorMessage("* " + elementName + " is a required field.");
			else
				addErrorMessage(elementName);
			return false;
		}

		return true;
	}	

}
