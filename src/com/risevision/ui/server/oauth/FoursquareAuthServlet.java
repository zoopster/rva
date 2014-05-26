// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.oauth;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FoursquareAuthServlet extends HttpServlet {
	private enum RegisteredConnection {
		rdnTest("rdn-test.appspot.com",
				"OEDQAXEF33OZGGZ4IVWS0UVHVTPQU3AQ0NNY0HBROP3SJVTI"),
		rvaTest("rva-test.appspot.com",
				""),
		rvaUser("rvauser.appspot.com",
				"TXHP15KATLRC1SA0WXXQRYS2IFHJEUW1151WLQC54THSIX50"),
		rvaUser2("rvauser2.appspot.com",
				"HY1HT5FNAK5G0GYT515N3SQBM1UWANE12RBH0D5RQ13HTLCD"),
		rvaRisevision("rva.risevision.com",
				"5QZDOSP5TO4W32SSIEJXVNABDSZUIFIZNMBDTYWM3ZU2YFZQ")
				;
			
		private String domain;
		private String token;
		private RegisteredConnection(String domain, String token) {
			this.domain = domain;
			this.token = token;
		}
		
		public String getDomain() {
			return domain;
		}
		
		public String getRedirectUrl() {
			return "http://" + domain + "/response.html";
		}
		
		public String getToken() {
			return token;
		}
				
	}
	
	protected static final Logger log = Logger.getAnonymousLogger();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String clientId = "";
		String redirectUrl = "";

		String currentDomain = request.getServerName();
		for (RegisteredConnection connection: RegisteredConnection.values()) {
			if (connection.getDomain().equals(currentDomain)) {
				clientId = connection.getToken();
				redirectUrl = connection.getRedirectUrl();
				
				break;
			}
		}
		
		// redirect to authUrl
		response.sendRedirect("https://foursquare.com/oauth2/authenticate" +
				"?client_id=" + clientId +
				// used for AJAX requests
				"&response_type=token" +
				// used for Server requests
//				"&response_type=code" +
				"&redirect_uri=" + redirectUrl);
	}
}
