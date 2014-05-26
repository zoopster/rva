// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.ui.client.common.info.EmailInfo;
import com.risevision.ui.client.common.info.PrerequisitesInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.UserInfo;

/**
 * The async counterpart of <code>RiseUserServiceService</code>.
 */
public interface UserServiceAsync {
//	void getUsers(UsersInfo usersInfo, AsyncCallback<UsersInfo> callback);
	void getUser(String companyId, String userId, AsyncCallback<UserInfo> callback);
	void putUser(String companyId, UserInfo user, AsyncCallback<RpcResultInfo> callback);
	void updateLastLogin(String companyId, UserInfo user, AsyncCallback<RpcResultInfo> callback);
	void deleteUser(String companyId, String userId, AsyncCallback<RpcResultInfo> callback);
	
//	void getUser(String username, AsyncCallback<UserInfo> callback);	
	void getCurrent(String requestUrl, AsyncCallback<PrerequisitesInfo> callback);
	
	void sendEmail(EmailInfo email, AsyncCallback<Void> callback);
	void getLogoutURL(String URL, AsyncCallback<String> callback);
//	void getLoginURL(String URL, AsyncCallback<String> callback);

}
