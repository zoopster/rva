package com.risevision.ui.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.risevision.ui.client.common.service.LoginService;
import com.risevision.ui.server.utils.ServerUtils;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	public String getLoginURL(String URL) {
		return ServerUtils.getLoginURL(URL);
	}
		
}
