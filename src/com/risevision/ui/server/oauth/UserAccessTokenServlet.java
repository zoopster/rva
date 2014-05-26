// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.oauth;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.risevision.ui.server.data.DataService;
import com.risevision.ui.server.data.PersistentConfigurationInfo;
import com.risevision.ui.server.data.PersistentOAuthInfo;
import com.risevision.ui.server.data.PersistentUserInfo;
import com.risevision.ui.server.data.PersistentOAuthInfo.OAuthType;
import com.risevision.ui.server.utils.ServerUtils;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

@SuppressWarnings("serial")
public class UserAccessTokenServlet extends HttpServlet {
	protected static final Logger log = Logger.getAnonymousLogger();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DataService service = DataService.getInstance();
		PersistentOAuthInfo oAuth = service.getOAuth(OAuthType.user);
		PersistentConfigurationInfo pConfig = service.getConfig();
		
		String oauth_verifier = request.getParameter("oauth_verifier");
		String token = (String) request.getSession().getAttribute("token");
		String tokenSecret = (String) request.getSession().getAttribute("tokenSecret");
		
		OAuthConsumer consumer = new DefaultOAuthConsumer(oAuth.getConsumerKey(), oAuth.getConsumerSecret());
		consumer.setTokenWithSecret(token, tokenSecret);
		
		OAuthProvider provider = new DefaultOAuthProvider(pConfig.getRequestTokenURL(), pConfig.getAccessTokenURL(), pConfig.getAuthorizeTokenURL());
		provider.setOAuth10a(true);

		try {
			provider.retrieveAccessToken(consumer, oauth_verifier);
		} catch (OAuthMessageSignerException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		
		saveUserAuthentication(request, response, consumer.getToken(), consumer.getTokenSecret());
		redirectResponse(request, response);
	}
	
	private void saveUserAuthentication(HttpServletRequest request, HttpServletResponse response, String userToken, String userTokenSecret) throws IOException {
//		request.getSession().setAttribute("userToken", userToken);
//		request.getSession().setAttribute("userTokenSecret", userTokenSecret);
//		request.getSession().setAttribute("userTokenOwner", ServerUtils.getGoogleUsername());

		PersistentUserInfo user = new PersistentUserInfo(userToken, userTokenSecret);
		
		if (user.getUserName() != null && !user.getUserName().isEmpty()) {
			DataService.getInstance().saveUser(user);
		}
		else {
			log.severe("Missing Username - null or empty; Redirect User to login page");
			response.sendRedirect(ServerUtils.getLoginURL(ServerUtils.getRequestURL(request)));
		}
	}
	
	private void redirectResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String redirectUrl = (String)request.getSession().getAttribute("redirect");
		
		redirectUrl = redirectUrl == null ? "" : redirectUrl;  
		redirectUrl = redirectUrl.contains("&") ? "?" + redirectUrl.replace("&", "#") : redirectUrl.replace("?", "#");
		redirectUrl = (redirectUrl.contains("#") ? "" : "#") + redirectUrl;
		response.sendRedirect("/" + redirectUrl);
	}

}
