// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.controller;

import com.risevision.core.api.types.UserRole;
import com.risevision.ui.client.common.info.UserInfo;

public class UserAccountController {
	private UserInfo userInfo;

	private static UserAccountController instance;

	public UserInfo getUserInfo() {
		return userInfo;
	}
	
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	public static UserAccountController getInstance() {
		try {
			if (instance == null)
				instance = new UserAccountController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
//	public boolean userHasRoleContentAdministrator() {
//		return userHasRole(UserRole.CONTENT_ADMINISTRATOR);
//	}
	
	public boolean userHasRoleContentEditor() {
		return userHasRole(UserRole.CONTENT_EDITOR);
	}
	
	public boolean userHasRoleContentPublisher() {
		return userHasRole(UserRole.CONTENT_PUBLISHER);
	}
	
	public boolean userHasRoleDisplayAdministrator() {
		return userHasRole(UserRole.DISPLAY_ADMINISTRATOR);
	}

	public boolean userHasRoleSystemAdministrator() {
		return userHasRole(UserRole.SYSTEM_ADMINISTRATOR);
	}

	public boolean userHasRoleUserAdministrator() {
		return userHasRole(UserRole.USER_ADMINISTRATOR);
	}

	public boolean userHasRole(String roleName) {
		if (null != userInfo)
			for (String r: userInfo.getRoles())
				if (r.equals(roleName))
					return true;
		
		return false;
	}
	
}
