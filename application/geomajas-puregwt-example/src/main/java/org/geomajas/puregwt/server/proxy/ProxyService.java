/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.server.proxy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link ProxyService} interface. It uses a {@link RequestConfiguration} object behind
 * the screens to determine whether or not to copy headers etc.
 * 
 * @author Pieter De Graef
 */
@Component
public class ProxyService {

	private static final String HTTP_LAST_MODIFIED_HEADER = "Last-Modified";

	private static final String HTTP_EXPIRES_HEADER = "Expires";

	private static final String HTTP_EXPIRES_HEADER_NOCACHE_VALUE = "Wed, 11 Jan 1984 05:00:00:GMT";

	private static final String HTTP_CACHE_CONTROL_HEADER = "Cache-Control";

	private static final String HTTP_CACHE_CONTROL_HEADER_NOCACHE_VALUE = "max-age=0, no-cache, no-store, "
			+ "must-revalidate";

	private static final String HTTP_CACHE_PRAGMA = "Pragma";

	private static final String HTTP_CACHE_PRAGMA_VALUE = "no-cache";

	private long cacheDurationInSeconds = 60 * 60 * 24 * 365; // One year

	private long cacheDurationInMilliSeconds = cacheDurationInSeconds * 1000;

	private final Logger log = LoggerFactory.getLogger(ProxyService.class);

	@Autowired
	private RequestConfiguration requestConfig;

	@Autowired
	private ConnectionConfiguration connectionConfiguration;

	/**
	 * Given an HTTP request build a new one to the target URL, using HTTP GET.
	 * 
	 * @param request
	 *            The original request that came in.
	 * @return Returns a redirected HttpGet request.
	 * @throws IOException
	 *             In case something went wrong trying to create the new request object.
	 */
	@SuppressWarnings("unchecked")
	public HttpGet buildGetRequest(HttpServletRequest request) throws IOException {
		Map<String, String[]> requestParameters = request.getParameterMap();
		String url = getBaseUrl(requestParameters, request);

		// Add GET parameters to the URL:
		boolean firstParam = !url.contains("?");
		StringBuilder query = new StringBuilder(url);
		try {
			for (String name : requestParameters.keySet()) {
				for (String value : requestParameters.get(name)) {
					if (!"url".equalsIgnoreCase(name)) {
						if (firstParam) {
							query.append("?");
							firstParam = false;
						} else {
							query.append("&");
						}
						query.append(URLEncoder.encode(name, "UTF-8"));
						query.append("=");
						query.append(URLEncoder.encode(value, "UTF-8"));
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			log.error("Problems dude: " + e.getLocalizedMessage());
			throw new IOException(e.getLocalizedMessage());
		}
		String uri = query.toString();
		log.debug("buildGetRequest:url=" + uri);
		return new HttpGet(uri);
	}

	/**
	 * Given an HTTP request, build a new one to the target URL, using HTTP POST.
	 * 
	 * @param request
	 *            The original request that came in.
	 * @return Returns a redirected HttpPost request.
	 * @throws IOException
	 *             In case something went wrong trying to create the new request object.
	 */
	@SuppressWarnings("unchecked")
	public HttpPost buildPostRequest(HttpServletRequest request) throws IOException {
		Map<String, String[]> requestParameters = request.getParameterMap();
		String url = getBaseUrl(requestParameters, request);

		HttpPost proxyPost = new HttpPost(url);

		// Apply the same set of parameters:
		setParameters(proxyPost, requestParameters);

		// Copy headers:
		copyHeadersToProxy(request, proxyPost);

		/**
		 * The BufferedHttpEntity is used because a regular InputStreamEntity starts with an invalid character. The
		 * wrapping in a BufferedHttpEntity solves this issue. I guess this is a Java bug, but I'm not sure...
		 */
		BufferedHttpEntity entity;
		try {
			ServletInputStream inputStream = request.getInputStream();
			InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream, -1);
			inputStreamEntity.setChunked(true);
			inputStreamEntity.setContentType(request.getContentType());
			entity = new BufferedHttpEntity(inputStreamEntity);
		} catch (IOException e) {
			log.error("Error occurred reading post body: " + e.getLocalizedMessage());
			throw new IOException(e.getLocalizedMessage());
		}

		proxyPost.setEntity(entity);
		return proxyPost;
	}

	/**
	 * Given an HTTP request, build a new one to the target URL, using HTTP PUT.
	 * 
	 * @param request
	 *            The original request that came in.
	 * @return Returns a redirected HttpPut request.
	 * @throws IOException
	 *             In case something went wrong trying to create the new request object.
	 */
	@SuppressWarnings("unchecked")
	public HttpPut buildPutRequest(HttpServletRequest request) throws IOException {
		// TODO code duplication from buildPostRequest...
		Map<String, String[]> requestParameters = request.getParameterMap();
		String url = getBaseUrl(requestParameters, request);

		HttpPut proxyPut = new HttpPut(url);

		// Apply the same set of parameters:
		setParameters(proxyPut, requestParameters);

		// Copy headers:
		copyHeadersToProxy(request, proxyPut);

		// The BufferedHttpEntity is used because a regular InputStreamEntity starts with an invalid character. The
		// wrapping in a BufferedHttpEntity solves this issue. I guess this is a Java bug, but I'm not sure...
		BufferedHttpEntity entity;
		try {
			ServletInputStream inputStream = request.getInputStream();
			InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream, -1);
			inputStreamEntity.setChunked(true);
			inputStreamEntity.setContentType(request.getContentType());
			entity = new BufferedHttpEntity(inputStreamEntity);
		} catch (IOException e) {
			log.error("Error occurred reading post body: " + e.getLocalizedMessage());
			throw new IOException(e.getLocalizedMessage());
		}

		proxyPut.setEntity(entity);
		return proxyPut;
	}

	/**
	 * Given an HTTP request, build a new one to the target URL, using HTTP DELETE.
	 * 
	 * @param request
	 *            The original request that came in.
	 * @return Returns a redirected HttpDelete request.
	 * @throws IOException
	 *             In case something went wrong trying to create the new request object.
	 */
	@SuppressWarnings("unchecked")
	public HttpDelete buildDeleteRequest(HttpServletRequest request) throws IOException {
		// TODO this part supports URL encoding in the parameters, copy for other request types..GET,POST ,PUt..wx
		// TODO code duplication from buildPostRequest...
		HttpDelete proxyDelete;
		Map<String, String[]> requestParameters = request.getParameterMap();
		String url = getBaseUrl(requestParameters, request);
		log.debug("url = " + url);
		try {
			if (url.indexOf("?") > 0) {
				String urlHostPath = url.substring(0, url.indexOf("?"));
				String urlParameters = url.substring(url.indexOf("?") + 1, url.length());
				StringBuilder urlEncodedParameters = new StringBuilder();
				String[] parameters = urlParameters.split("&");
				for (int i = 0; i < parameters.length; i++) {
					String parameter = parameters[i];
					String[] components = parameter.split("=");
					for (int j = 0; j < components.length; j++) {
						String component = components[j];
						urlEncodedParameters.append(URLEncoder.encode(component, "UTF-8"));
						if (j < component.length() - 1) {
							urlEncodedParameters.append("=");
						}
					}
					if (i < parameter.length() - 1) {
						urlEncodedParameters.append("&");
					}
				}
				URI uri = new URI(urlHostPath + "?" + urlEncodedParameters);
				proxyDelete = new HttpDelete(uri);
			} else {
				proxyDelete = new HttpDelete(url);
			}
		} catch (URISyntaxException e) {
			log.error("fout bij maken uri =" + e.getMessage());
			throw new IOException("fout bij maken uri =" + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.error("fout bij maken uri =" + e.getMessage());
			throw new IOException("fout bij maken uri =" + e.getMessage());
		}

		// Apply the same set of parameters:
		setParameters(proxyDelete, requestParameters);

		// Copy headers:
		copyHeadersToProxy(request, proxyDelete);

		return proxyDelete;
	}

	/**
	 * Should the headers from the proxy request be copied to the real response or not?
	 * 
	 * @param request
	 *            The redirected request object.
	 * @return True or false, indicating whether or not to copy the original headers.
	 */
	public boolean isCopyHeaders(HttpRequestBase request) {
		return requestConfig.isCopyHeaders();
	}

	/**
	 * Execution method that copies all header from the proxy response to the final response.
	 * 
	 * @param from
	 *            The proxy response to copy from.
	 * @param to
	 *            The target HTTP response.
	 */
	public void copyHeaders(HttpResponse from, HttpServletResponse to) {
		for (Header header : from.getAllHeaders()) {
			to.addHeader(header.getName(), header.getValue());
		}
		// to.addHeader("X-Forwarded-Host", from.);
	}

	/**
	 * Should the given request be cached or not?
	 * 
	 * @param request
	 *            The redirected request object.
	 * @return True or false, indicating whether or not the request should be cached.
	 */
	public boolean isCachedRequest(HttpRequestBase request) {
		String uri = request.getURI().toString();
		for (String pattern : requestConfig.getCacheIdentifiers()) {
			if (pattern.length() > 0) {
				if (uri.contains(pattern)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add headers to the response object to make sure it is cached.
	 * 
	 * @param response
	 *            The response object to add the cache headers to.
	 */
	public void addCacheHeaders(HttpServletResponse response) {
		long now = System.currentTimeMillis();
		response.setDateHeader(HTTP_LAST_MODIFIED_HEADER, now);

		// HTTP 1.0 header
		response.setDateHeader(HTTP_EXPIRES_HEADER, now + cacheDurationInMilliSeconds);

		// HTTP 1.1 header
		response.setHeader(HTTP_CACHE_CONTROL_HEADER, "max-age=" + cacheDurationInSeconds);
	}

	/**
	 * Add headers to the response object to make sure it is NOT cached.
	 * 
	 * @param response
	 *            The response object to add the no-cache headers to.
	 */
	public void addNocacheHeaders(HttpServletResponse response) {
		// HTTP 1.0 header:
		response.setHeader(HTTP_EXPIRES_HEADER, HTTP_EXPIRES_HEADER_NOCACHE_VALUE);
		response.setHeader(HTTP_CACHE_PRAGMA, HTTP_CACHE_PRAGMA_VALUE);

		// HTTP 1.1 header:
		response.setHeader(HTTP_CACHE_CONTROL_HEADER, HTTP_CACHE_CONTROL_HEADER_NOCACHE_VALUE);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private String getBaseUrl(Map<String, String[]> parameters, HttpServletRequest request) throws IOException {
		String url;
		String[] urlArray = parameters.get("url");

		if (urlArray != null && urlArray.length > 0) {
			url = urlArray[0];
		} else if (connectionConfiguration != null && connectionConfiguration.getTargetUrl() != null) {
			url = connectionConfiguration.getTargetUrl() + request.getPathInfo();
		} else {
			throw new IOException("target url is not defined.");
		}
		return url;
	}

	private void setParameters(HttpRequestBase request, Map<String, String[]> parameters) {
		for (String name : parameters.keySet()) {
			for (String value : parameters.get(name)) {
				if (!"url".equalsIgnoreCase(name)) {
					request.getParams().setParameter(name, value);
				}
			}
		}
	}

	/** Copy headers from the original request to the proxy request. */
	private void copyHeadersToProxy(HttpServletRequest original, HttpRequestBase proxy) {
		Enumeration<?> enumeration = original.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String headerName = (String) enumeration.nextElement();

			// Never copy these headers:
			if ("Content-Length".equalsIgnoreCase(headerName) || "Host".equalsIgnoreCase(headerName)) {
				// The host header might actually be needed sometimes...
				continue;
			}
			proxy.addHeader(headerName, original.getHeader(headerName));
		}
	}
}