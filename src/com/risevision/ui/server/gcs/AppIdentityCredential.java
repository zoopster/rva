package com.risevision.ui.server.gcs;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

/**
 * OAuth 2.0 credential in which a client Google App Engine application needs to access data that it
 * owns, based on <a href="http://code.google.com/appengine/docs/java/appidentity/overview.html
 * #Asserting_Identity_to_Google_APIs">Asserting Identity to Google APIs</a>.
 *
 * <p>
 * Sample usage:
 * </p>
 *
 * <pre>
  public static HttpRequestFactory createRequestFactory(
      HttpTransport transport, JsonFactory jsonFactory, TokenResponse tokenResponse) {
    return transport.createRequestFactory(
        new AppIdentityCredential("https://www.googleapis.com/auth/urlshortener"));
  }
 * </pre>
 *
 * <p>
 * Implementation is immutable and thread-safe.
 * </p>
 *
 * @since 1.7
 * @author Yaniv Inbar
 */
public class AppIdentityCredential implements HttpRequestInitializer, HttpExecuteInterceptor {

	/** OAuth scopes. */
	private final List<String> scopes;

	/**
	 * @param scopes
	 *            OAuth scopes
	 */
	public AppIdentityCredential(Iterable<String> scopes) {
		this.scopes = Lists.newArrayList(scopes.iterator());
	}

	/**
	 * @param scopes
	 *            OAuth scopes
	 */
	public AppIdentityCredential(String... scopes) {
		this.scopes = Lists.newArrayList(scopes);
	}

	@Override
	public void initialize(HttpRequest request) throws IOException {
		request.setInterceptor(this);
	}

	@Override
	public void intercept(HttpRequest request) throws IOException {
		String accessToken = AppIdentityServiceFactory.getAppIdentityService().getAccessToken(scopes).getAccessToken();
		BearerToken.authorizationHeaderAccessMethod().intercept(request,accessToken);
	}
}