// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.user;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Frame;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.info.PrerequisitesInfo;
import com.risevision.ui.client.common.widgets.messages.DialogBoxWidget;

public class UserTermsWidget extends Frame {

	private static String NEW_USER_TEXT = "It's doesn't look like we have seen you here before. " +
			"If it is okay with you and you are in agreement with our %termsUrl%, we will go " +
			"ahead and set up a User account and Company for you.";
	private static String NEW_USER_TEXT2 = "Thanks for registering! We really appreciate it.";

	private static String DELETED_USER_TEXT = "It looks like your User account " +
			"was removed from the Company that it previously belonged to. If you would like to "
			+ "re-register and are in agreement with our %termsUrl%, " +
			"we will go ahead and set up a User account and Company for you.";
//	private static String DELETED_USER_TEXT2 = "Either way, thanks for the visit!";
	
	private static String REGISTERED_USER_TEXT = "Please review the %termsUrl%  to proceed. ";
	
	//UI pieces
	private String TERMS_LINK_TEXT = "<a href='%termsUrl%' target='blank'>Terms of Service and Privacy</a>";
	
	private static final String HTML_STRING = "<html>"
			+ "<head>"
			+ "<meta http-equiv='content-type' content='text/html; charset=UTF-8'>"
			+ "  <title>Registration Dialog</title>"
			+ "  <link rel='stylesheet' href='../bootstrap/css/bootstrap.css'>"
			+ "  <link rel='stylesheet' href='../style/popup.css'>"
			+ "</head>"
			+ "<body>"
			+ "  <div class='container'>"
			+ "    <div class='alert alert-registration'>"
			+ "      <h3>Welcome to Rise Vision</h3>"
			+ "      <br>"
			+ "      <p>%text1%</p>"
			+ "      <p>%text2%</p>"
			+ "      <br>	"
			+ "      <input type='submit' class='btn btn-primary' value='I Agree'"
			+ "        onclick='parent.rdn2_dialogBox_okay();'> "
			+ "      <input type='submit' class='btn btn-primary' value='Cancel'"
			+ "        onclick='parent.rdn2_dialogBox_close();'>"
			+ "    </div>"
			+ "  </div>"
			+ "</body>"
			+ "</html>";
	
	private DialogBoxWidget dialogBox = DialogBoxWidget.getInstance();
	
	public void show(Command termsCommand, int status) {
		String text1 = "", text2 = "";
		String termsUrl = ConfigurationController.getInstance().getConfiguration().getTermsURL();
		
		String termsUrlLink = TERMS_LINK_TEXT.replace("%termsUrl%", termsUrl);
		
		if (status == PrerequisitesInfo.STATUS_OK) {
			text1 = REGISTERED_USER_TEXT.replace("%termsUrl%", termsUrlLink);
		}
		else if (status == PrerequisitesInfo.STATUS_DELETED) {
			text1 = DELETED_USER_TEXT.replace("%termsUrl%", termsUrlLink);
			text2 += NEW_USER_TEXT2;
		} 
		else {
			text1 = NEW_USER_TEXT.replace("%termsUrl%", termsUrlLink);
			text2 += NEW_USER_TEXT2;
		}		
	
		String htmlString = HTML_STRING.replace("%text1%", text1)
				.replace("%text2%", text2);
		
		dialogBox.showPanel(termsCommand, true, htmlString);
	}
	
	public boolean getAcceptance() {
		return dialogBox.getResult();
	}
}
