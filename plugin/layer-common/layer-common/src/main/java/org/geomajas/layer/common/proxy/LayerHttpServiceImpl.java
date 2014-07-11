/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.common.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.geomajas.layer.RasterLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link LayerHttpService}. Non-caching implementation, see {@link CachingLayerHttpService} for the
 * component bean.
 * 
 * @author Joachim Van der Auwera
 * @author Kristof Heirwegh
 * @author Jan De Moerloose
 */
public class LayerHttpServiceImpl implements LayerHttpService {

	private final Logger log = LoggerFactory.getLogger(LayerHttpServiceImpl.class);

	private static final String URL_PARAM_START = "?";
	private static final String URL_PARAM_SEPARATOR = "&";
	private static final String URL_PARAM_IS = "=";
	private static final String URL_PROTOCOL_SEPARATOR = "://";
	private static final int TIMEOUT = 5000;
	private static final int URL_DEFAULT_PORT = 80;
	private static final int URL_DEFAULT_SECURE_PORT = 443;

	@Autowired(required = false)
	private LayerHttpServiceInterceptors interceptors;
	
	private AbstractHttpClient client;
	
	public LayerHttpServiceImpl() {
		// Create a HTTP client object, which will initiate the connection:
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		client = new SystemDefaultHttpClient(httpParams);		
	}

	public String addCredentialsToUrl(final String url, final ProxyAuthentication authentication) {
		if (null != authentication && ProxyAuthenticationMethod.URL.equals(authentication.getMethod())) {
			StringBuilder res = new StringBuilder(url);
			if (res.indexOf(URL_PARAM_START) >= 0) {
				res.append(URL_PARAM_SEPARATOR);
			} else {
				res.append(URL_PARAM_START);
			}
			res.append(authentication.getUserKey());
			res.append(URL_PARAM_IS);
			res.append(authentication.getUser());
			res.append(URL_PARAM_SEPARATOR);
			res.append(authentication.getPasswordKey());
			res.append(URL_PARAM_IS);
			res.append(authentication.getPassword());
			return res.toString();
		}
		return url;
	}

	public InputStream getStream(String baseUrl, RasterLayer layer)
			throws IOException {		
		String url = baseUrl;		
		if (layer instanceof ProxyLayerSupport) {			
			// handle proxy authentication and interceptors			
			ProxyLayerSupport proxyLayer = (ProxyLayerSupport) layer;
			ProxyAuthentication authentication = proxyLayer.getProxyAuthentication();
			url = addCredentialsToUrl(baseUrl, authentication);

			// -- add basic authentication
			if (null != authentication 
					&& (ProxyAuthenticationMethod.BASIC.equals(authentication.getMethod()) || 
							ProxyAuthenticationMethod.DIGEST.equals(authentication.getMethod()))) {
				// Set up the credentials:
				Credentials creds = new UsernamePasswordCredentials(authentication.getUser(), authentication.getPassword());
				AuthScope scope = new AuthScope(parseDomain(url), parsePort(url), authentication.getRealm());
				client.getCredentialsProvider().setCredentials(scope, creds);
			}

			// -- add interceptors if any --
			addInterceptors(client, baseUrl, proxyLayer.getId());
		}
		
		
		// Create the GET method with the correct URL:
		HttpGet get = new HttpGet(url);

		// Execute the GET:
		HttpResponse response = client.execute(get);
		log.debug("Response: {} - {}", response.getStatusLine().getStatusCode(), response.getStatusLine()
				.getReasonPhrase());

		return response.getEntity().getContent();
	}

	/**
	 * Check if there are interceptors & add to client if any.
	 * 
	 * @param client
	 * @param baseUrl
	 */
	private void addInterceptors(AbstractHttpClient client, String baseUrl, String layerId) {
		try {
			if (interceptors != null && baseUrl != null) {
				for (Entry<String, List<HttpRequestInterceptor>> entry : interceptors.getMap().entrySet()) {
					String key = entry.getKey();
					if ("".equals(key) || layerId.equals(key) || baseUrl.startsWith(key)) {
						for (HttpRequestInterceptor inter : entry.getValue()) {
							client.addRequestInterceptor(inter);
						}
					}
				}
			}
		} catch (Exception e) {
			log.warn("Error adding interceptors: " + e.getMessage());
		}
	}
	
	/**
	 * Get the domain out of a full URL.
	 *
	 * @param url base url
	 * @return domain name
	 */
	private String parseDomain(String url) {
		int index = url.indexOf(URL_PROTOCOL_SEPARATOR);
		String domain = url.substring(index + URL_PROTOCOL_SEPARATOR.length());
		domain = domain.substring(0, domain.indexOf('/'));
		int colonPos = domain.indexOf(':');
		if (colonPos >= 0) {
			domain = domain.substring(0, colonPos);
		}
		return domain;
	}

	/**
	 * Get the port out of a full URL.
	 * <p>
	 * Note that we only take https & http into account if you are using a non-default port/protocol you will need to
	 * add the port to your baseUrl.
	 * 
	 * @param url base url
	 * @return domain name
	 */
	private int parsePort(String url) {
		try {
			URL u = new URL(url);
			int defaultport = "https".equalsIgnoreCase(u.getProtocol()) ? URL_DEFAULT_SECURE_PORT : URL_DEFAULT_PORT;
			return (u.getPort() == -1 ? defaultport : u.getPort());
		} catch (MalformedURLException e) {
			return URL_DEFAULT_SECURE_PORT;
		}
	}

}
