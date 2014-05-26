package com.risevision.ui.client.common.widgets.messages;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.risevision.ui.client.common.info.SystemMessageInfo;

public class SystemMessagesWidget extends Anchor implements ClickHandler {
	private static SystemMessagesWidget instance;
	
	private List<SystemMessageInfo> systemMessages;
	
	private SystemMessagesWidget() {
		setVisible(false);
		
		setStyleName("mail-icon");
		
		addClickHandler(this);
	}
	
	public static SystemMessagesWidget getInstance() {
		try {
			if (instance == null)
				instance = new SystemMessagesWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return instance;
	}
	
	public void init(List<SystemMessageInfo> messages) {
		this.setVisible(false);

		this.systemMessages = new ArrayList<SystemMessageInfo>();
		for (SystemMessageInfo message: messages) {
			if (message.canPlay()) {
				this.systemMessages.add(message);
			}
		}
		
//		this.systemMessages = systemMessages;
		
		if (this.systemMessages.size() > 0) {
			this.setVisible(true);
			
			SystemMessagesDialog.show(this.systemMessages);
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		SystemMessagesDialog.show(systemMessages);
	}

}
