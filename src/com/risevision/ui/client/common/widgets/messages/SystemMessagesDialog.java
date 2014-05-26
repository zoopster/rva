package com.risevision.ui.client.common.widgets.messages;

import java.util.List;

import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.SystemMessageInfo;

public class SystemMessagesDialog {
	private static final String HTML_STRING = "<html>"
			+ "<head>"
			+ "<meta http-equiv='content-type' content='text/html; charset=UTF-8'>"
			+ "  <title>Notification Dialog</title>"
			+ "  <link rel='stylesheet' href='../bootstrap/css/bootstrap.css'>"
			+ "  <link rel='stylesheet' href='../style/popup.css'>"
			+ "</head>"
			+ "<body>"
			+ "	 <div class='container'>"
			+ "    <div class='alert alert-system-message alert-dismissable'>"
			+ "	     <div class='container'>"
			+ "        <button type='button' id='alert-close-btn'  class='close close-alert' onclick='parent.rdn2_dialogBox_close();'>"
			+ "          <img src='../images/close-btn.png' width='24' height='24'>"
			+ "        </button>"
			+ "        <div class='alert-text'>"
			+ "          %messages%"
			+ "        </div>"
			+ "      </div>"
			+ "    </div>"
			+ "  </div>"
			+ "</body>"
			+ "</html>";
	
	private static DialogBoxWidget dialogBox = DialogBoxWidget.getInstance();
	
	public static void show(List<SystemMessageInfo> systemMessages) {
		if (systemMessages != null && systemMessages.size() > 0) {
			String messages = "";
			
			for (SystemMessageInfo message : systemMessages) {
				if (message.canPlay()) {
					messages += message.getText() + "<hr>";
				}
			}
			
			if (!RiseUtils.strIsNullOrEmpty(messages)) {
				messages = messages.substring(0, messages.length() - 4);
				
				String htmlString = HTML_STRING.replace("%messages%", messages);
				
				dialogBox.showPanel(null, true, htmlString);
			}
		}
	}
	
}
