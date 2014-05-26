package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>CompanyService</code>.
 */
public interface LoginServiceAsync {
	void getLoginURL(String URL, AsyncCallback<String> callback);
}
