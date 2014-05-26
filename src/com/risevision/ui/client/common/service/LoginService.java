package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("loginHelper")
public interface LoginService extends RemoteService {
	public String getLoginURL(String URL);
}
