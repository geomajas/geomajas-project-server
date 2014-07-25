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
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
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

	private static final int TIMEOUT = 5000;

	@Autowired(required = false)
	private LayerHttpServiceInterceptors interceptors;

	private AbstractHttpClient client;

	public LayerHttpServiceImpl() {
		// Create a HTTP client object, which will initiate the connection:
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		setClient(new SystemDefaultHttpClient(httpParams));
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

	public InputStream getStream(String baseUrl, RasterLayer layer) throws IOException {
		String url = baseUrl;
		HttpContext context = null;
		if (layer instanceof ProxyLayerSupport) {
			context = new BasicHttpContext();
			// pass url and layer id to the CompositeInterceptor
			context.setAttribute(CompositeInterceptor.BASE_URL, baseUrl);
			context.setAttribute(CompositeInterceptor.LAYER_ID, layer.getId());
			// handle proxy authentication and interceptors
			ProxyLayerSupport proxyLayer = (ProxyLayerSupport) layer;
			ProxyAuthentication authentication = proxyLayer.getProxyAuthentication();
			url = addCredentialsToUrl(baseUrl, authentication);

			// -- add basic authentication
			if (null != authentication
					&& (ProxyAuthenticationMethod.BASIC.equals(authentication.getMethod()) ||
						ProxyAuthenticationMethod.DIGEST.equals(authentication.getMethod()))) {
				// Set up the credentials:
				Credentials creds = new UsernamePasswordCredentials(authentication.getUser(),
						authentication.getPassword());
				URL u = new URL(url);
				AuthScope scope = new AuthScope(u.getHost(), u.getPort(), authentication.getRealm());
				// thread-safe, this is ok
				client.getCredentialsProvider().setCredentials(scope, creds);
			}
		}

		// Create the GET method with the correct URL:
		HttpGet get = new HttpGet(url);

		// Execute the GET:
		HttpResponse response = client.execute(get, context);
		log.debug("Response: {} - {}", response.getStatusLine().getStatusCode(), response.getStatusLine()
				.getReasonPhrase());

		return response.getEntity().getContent();
	}

	@Override
	public void setClient(AbstractHttpClient client) {
		this.client = client;
		// add an interceptor that picks up the autowired interceptors, remove first to avoid doubles
		client.removeRequestInterceptorByClass(CompositeInterceptor.class);
		client.addRequestInterceptor(new CompositeInterceptor());
	}

	@Override
	public AbstractHttpClient getClient() {
		return client;
	}

	/**
	 * This interceptor will call the autowired interceptor(s) that apply to the current layer.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class CompositeInterceptor implements HttpRequestInterceptor {

		private static final String LAYER_ID = "LayerHttpServiceLayerId";

		private static final String BASE_URL = "LayerHttpServiceBaseUrl";

		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
			String baseUrl = (String) context.getAttribute(BASE_URL);
			String layerId = (String) context.getAttribute(LAYER_ID);
			try {
				if (interceptors != null && baseUrl != null) {
					for (Entry<String, List<HttpRequestInterceptor>> entry : interceptors.getMap().entrySet()) {
						String key = entry.getKey();
						if ("".equals(key) || (layerId != null && layerId.equals(key)) || baseUrl.startsWith(key)) {
							for (HttpRequestInterceptor inter : entry.getValue()) {
								inter.process(request, context);
							}
						}
					}
				}
			} catch (Exception e) {
				log.warn("Error processing interceptors: " + e.getMessage());
			}
		}

	}

}
