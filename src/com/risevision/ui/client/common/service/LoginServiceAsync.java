// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>CompanyService</code>.
 */
public interface LoginServiceAsync {
	void getLoginURL(String URL, AsyncCallback<String> callback);
}
