package com.risevision.ui.client.common.widgets;

import com.google.gwt.user.client.ui.HTML;

public class StatusBoxWidget extends HTML {

	public static enum Status{OK, WARNING, ERROR};
	public static final String LOADING = "Data is loading...";
	public static final String SAVING = "Saving...";
	public static final String DELETING = "Deleting...";
	
	public static enum ErrorType{RPC};
	public static final String RPC_ERROR = "Error: Remote Procedure Call Failure";
	
	private static StatusBoxWidget instance;
	
	public StatusBoxWidget() {
		clear();
	}

	public static StatusBoxWidget getInstance() {
		try {
			if (instance == null)
				instance = new StatusBoxWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public void setStatus(Status status, String message){
		setHTML(message);
		switch (status){
		case ERROR:
			setStyleName("rdn-StatusBox rdn-StatusBox-ERROR");
			break;
		case OK:
			setStyleName("rdn-StatusBox rdn-StatusBox-OK");
			break;
		case WARNING:
			setStyleName("rdn-StatusBox rdn-StatusBox-WARNING");
			break;		
		}
		setVisible(true);
	}
	
	public void setStatus(ErrorType error) {
		switch (error){
		case RPC:
			setStatus(Status.ERROR, RPC_ERROR);
			break;
		}
	}
	
	public void clear(){
		setVisible(false);
		setHTML("");
	}
	
}