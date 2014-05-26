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
public class TwitterRequestTokenServlet extends HttpServlet {
	protected static final Logger log = Logger.getAnonymousLogger();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DataService service = DataService.getInstance();
		PersistentOAuthInfo twitterOAuth = service.getOAuth(OAuthType.twitter);
//		PersistentConfigurationInfo pConfig = service.getConfig();
		
		OAuthConsumer consumer = new DefaultOAuthConsumer(twitterOAuth.getConsumerKey(), twitterOAuth.getConsumerSecret());
		
//		OAuthProvider provider = new DefaultOAuthProvider(pConfig.getRequestTokenURL(), pConfig.getAccessTokenURL(), pConfig.getAuthorizeTokenURL());
		OAuthProvider provider = new DefaultOAuthProvider(PersistentOAuthInfo.TWITTER_REQUEST_TOKEN_URL, 
				PersistentOAuthInfo.TWITTER_ACCESS_TOKEN_URL, 
				PersistentOAuthInfo.TWITTER_AUTHORIZE_TOKEN_URL);
		
		String queryString = request.getQueryString();
		request.getSession().setAttribute("redirect", queryString);
		
		String authUrl = null;
		try {
			authUrl = provider.retrieveRequestToken(consumer, request.getRequestURL().toString().replace("request", "access"));
			request.getSession().setAttribute("twitter_token", consumer.getToken());
			request.getSession().setAttribute("twitter_tokenSecret", consumer.getTokenSecret());
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

		// redirect to authUrl
		response.sendRedirect(authUrl);
	}
}
