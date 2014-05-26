// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

public class FormValidatorInfo {
	public static final String requiredFieldValidator = "requiredFieldValidator";
	public static final String emailValidator = "emailValidator";
	public static final String phoneNumberValidator = "phoneNumberValidator";
	public static final String dateValidator = "dateValidator";
	public static final String intValidator = "intValidator";
	public static final String urlValidator = "urlValidator";
	
	private Object uiElement;
	private String uiElementName;
	private String validationType;
	private boolean customMessage;
	private Object minValue = null;
	private Object maxValue = null;
	
	public FormValidatorInfo(Object uiElement, String uiElementName, String validationType, boolean customMessasge) {
		this.uiElement = uiElement;
		this.uiElementName = uiElementName;
		this.validationType = validationType;
		this.setCustomMessage(customMessasge);
	}
	public FormValidatorInfo(Object uiElement, String uiElementName, String validationType, boolean customMessasge, Object minValue, Object maxValue) {
		this(uiElement, uiElementName, validationType,customMessasge);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	public Object getUiElement() {
		return uiElement;
	}
	public void setUiElement(Object uiElement) {
		this.uiElement = uiElement;
	}
	public String getValidationType() {
		return validationType;
	}
	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}
	public void setUiElementName(String uiElementName) {
		this.uiElementName = uiElementName;
	}
	public String getUiElementName() {
		return uiElementName;
	}
	public void setCustomMessage(boolean customMessage) {
		this.customMessage = customMessage;
	}
	public boolean isCustomMessage() {
		return customMessage;
	}
	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}
	public Object getMinValue() {
		return minValue;
	}
	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}
	public Object getMaxValue() {
		return maxValue;
	}
}
