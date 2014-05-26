// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

import com.risevision.core.api.types.UserStatus;

@SuppressWarnings("serial")
public class UserInfo implements Serializable {
//	public static final String USERNAME = "username";
//	public static final String LASTNAME = "lastName";
//	public static final String FIRSTNAME = "firstName";
//	public static final String LASTLOGIN = "lastLogin";
	
//	public static final String INACTIVE = "0";
//	public static final String ACTIVE = "1";

	private String id;

	private String userName;
	private String firstName;
	private String lastName;

	private String telephone;
	private String email;	
	private boolean mailSyncEnabled = true;
	
	private Date lastLogin;	
	private boolean termsAccepted;
	private Date termsAcceptedDate;
	private boolean showTutorial = true;
	
	private String company;

	private int status;

	private ArrayList<String> roles;
	
	private String changedBy;
	private Date changedDate;
	
	public UserInfo() {
		status = UserStatus.ACTIVE;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}	

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}	
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public boolean isMailSyncEnabled() {
		return mailSyncEnabled;
	}

	public void setMailSyncEnabled(boolean mailSyncEnabled) {
		this.mailSyncEnabled = mailSyncEnabled;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return company;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}	

	public String getStatusText() {
		if (status == UserStatus.ACTIVE)
			return "Active";
		else
			return "Inactive";
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}

	public Date getChangedDate() {
		return changedDate;
	}

	public void setTermsAccepted(boolean termsAccepted) {
		this.termsAccepted = termsAccepted;
	}

	public boolean isTermsAccepted() {
		return termsAccepted;
	}

	public void setTermsAcceptedDate(Date termsAcceptedDate) {
		this.termsAcceptedDate = termsAcceptedDate;
	}

	public Date getTermsAcceptedDate() {
		return termsAcceptedDate;
	}

	public void setShowTutorial(boolean showTutorial) {
		this.showTutorial = showTutorial;
	}

	public boolean isShowTutorial() {
		return showTutorial;
	}
	
}
