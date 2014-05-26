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
import com.risevision.ui.server.data.PersistentOAuthInfo;
import com.risevision.ui.server.data.PersistentOAuthInfo.OAuthType;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

@SuppressWarnings("serial")
public class TwitterAccessTokenServlet extends HttpServlet {
	protected static final Logger log = Logger.getAnonymousLogger();
	
	private static final String RESPONSE_HTML = "	<script type='text/javascript'>" +
			"    var d = window.opener || window.parent;" +
			"    if (d) {" +
			"       	d.parent._setTwitterToken('%token%');" +
			" " +
			"	    if (window.opener) {" +
			"	        window.close();" +
			"	        if (window.self) {" +
			"	            window.self.close();" +
			"	        }" +
			"	    }" +
			"    }" +
			"	</script>";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DataService service = DataService.getInstance();
		PersistentOAuthInfo twitterOAuth = service.getOAuth(OAuthType.twitter);
//		PersistentConfigurationInfo pConfig = service.getConfig();
		
		String oauth_verifier = request.getParameter("oauth_verifier");
		String token = (String) request.getSession().getAttribute("twitter_token");
		String tokenSecret = (String) request.getSession().getAttribute("twitter_tokenSecret");
		
		OAuthConsumer consumer = new DefaultOAuthConsumer(twitterOAuth.getConsumerKey(), twitterOAuth.getConsumerSecret());
		consumer.setTokenWithSecret(token, tokenSecret);
		
//		OAuthProvider provider = new DefaultOAuthProvider(pConfig.getRequestTokenURL(), pConfig.getAccessTokenURL(), pConfig.getAuthorizeTokenURL());
		OAuthProvider provider = new DefaultOAuthProvider(PersistentOAuthInfo.TWITTER_REQUEST_TOKEN_URL, 
				PersistentOAuthInfo.TWITTER_ACCESS_TOKEN_URL, 
				PersistentOAuthInfo.TWITTER_AUTHORIZE_TOKEN_URL);
		
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
		
		saveTwitterAuthentication(request, response, consumer.getToken(), consumer.getTokenSecret());
	}
	
	private void saveTwitterAuthentication(HttpServletRequest request, HttpServletResponse response, String twitterToken, String twitterTokenSecret) throws IOException {
		// use Javascript to return values to the RVA
		String responseString = RESPONSE_HTML.replace("%token%", "[\"" + twitterToken + "\",\"" + twitterTokenSecret + "\"]");
		
		response.getWriter().println(responseString);

	}

}
