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

package org.geomajas.plugin.wmsclient.server.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Central proxy servlet.
 * <p>
 * This servlet automatically picks up any {@link HttpRequestInterceptor}s and {@link HttpResponseInterceptor}s that
 * have been defined within the Spring context.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Controller("ProxyController")
@RequestMapping(value = "/*")
public class ProxyController extends HttpServlet {

	public static final String ORIGINAL_REQUEST = "orig_request";

	private static final long serialVersionUID = 100L;

	private final Logger log = LoggerFactory.getLogger(ProxyController.class);

	private ProxyService proxyService;

	private DefaultHttpClient httpClient;

	public void init(final ServletConfig config) throws ServletException {
		super.init(config);

		log.info("initializing ProxyController.");
		// Get the necessary Spring beans: configuration & monitor service.
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(config
				.getServletContext());
		proxyService = applicationContext.getBean(ProxyService.class);
		ConnectionConfiguration connectionConfig = applicationContext.getBean(ConnectionConfiguration.class);

		// Create a ThreadSafe way of communicating using a connection pool:
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, connectionConfig.getHTTPSocketFactory()));
		try {
			schemeRegistry.register(new Scheme("https", 443, connectionConfig.getHTTPSSocketFactory()));
		} catch (NoSuchAlgorithmException e) {
			log.error("No algorithm found to do https stuff...");
			throw new ServletException(e);
		} catch (KeyManagementException e) {
			log.error("No key management found...");
			throw new ServletException(e);
		}

		ThreadSafeClientConnManager pccm = new ThreadSafeClientConnManager(schemeRegistry);
		pccm.setDefaultMaxPerRoute(connectionConfig.getMaxConnectionsPerRoute());
		pccm.setMaxTotal(connectionConfig.getMaxConnectionsTotal());

		httpClient = new DefaultHttpClient(pccm);
		httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				connectionConfig.getConnectionTimeout());
		httpClient.getParams().setParameter(CoreConnectionPNames.TCP_NODELAY, true);

		// See if there are any request interceptors in the Spring configuration. If so add them:
		Map<String, HttpRequestInterceptor> requestInterceptors = applicationContext
				.getBeansOfType(HttpRequestInterceptor.class);
		if (requestInterceptors != null) {
			for (HttpRequestInterceptor interceptor : requestInterceptors.values()) {
				httpClient.addRequestInterceptor(interceptor);
			}
		}

		// See if there are any response interceptors in the Spring configuration. If so add them:
		Map<String, HttpResponseInterceptor> responseInterceptors = applicationContext
				.getBeansOfType(HttpResponseInterceptor.class);
		if (responseInterceptors != null) {
			for (HttpResponseInterceptor interceptor : responseInterceptors.values()) {
				httpClient.addResponseInterceptor(interceptor);
			}
		}

		// If a proxy has been configured, use it:
		if (connectionConfig.getProxyHost() != null && !"".equals(connectionConfig.getProxyHost())) {
			HttpHost proxy = new HttpHost(connectionConfig.getProxyHost(), connectionConfig.getProxyPort());
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			log.info("ProxyController initialized with proxy from config: " + connectionConfig.getProxyHost() + ":"
					+ connectionConfig.getProxyPort());

		}
		if (connectionConfig.getTargetUrl() != null && !"".equals(connectionConfig.getTargetUrl())) {
			log.info("ProxyController target url = {}", connectionConfig.getTargetUrl());
		}
		log.info("ProxyController is ready.");
	}

	// ------------------------------------------------------------------------
	// Servlet implementation:
	// ------------------------------------------------------------------------

	public String getServletInfo() {
		return "Proxy Servlet";
	}

	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		log.debug("DoGet");
		HttpGet proxyGet = proxyService.buildGetRequest(request);
		handleRequest(request, proxyGet, response);
	}

	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("DoPost");
		HttpPost proxyPost = proxyService.buildPostRequest(request);
		handleRequest(request, proxyPost, response);
	}

	protected void doDelete(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("DoDelete");
		HttpDelete proxyDelete = proxyService.buildDeleteRequest(request);
		handleRequest(request, proxyDelete, response);
	}

	protected void doPut(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		log.debug("DoPut");
		HttpPut proxyPut = proxyService.buildPutRequest(request);
		handleRequest(request, proxyPut, response);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private StatusLine handleRequest(HttpServletRequest servletRequest, HttpRequestBase request,
			HttpServletResponse response) throws IOException {
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ORIGINAL_REQUEST, servletRequest);
		// request.addHeader("X-Forwarded-Host", servletRequest.getRequestURL().toString());
		HttpResponse proxyResponse = httpClient.execute(request, localContext);
		// log.debug("response length: " + proxyResponse.getEntity().getContentLength());

		if (proxyService.isCopyHeaders(request)) {
			proxyService.copyHeaders(proxyResponse, response);
		} else if (proxyService.isCachedRequest(request)) {
			proxyService.addCacheHeaders(response);
		} else {
			proxyService.addNocacheHeaders(response);
		}
		response.setStatus(proxyResponse.getStatusLine().getStatusCode());
		write(proxyResponse, response);
		return proxyResponse.getStatusLine();
	}

	private void write(HttpResponse from, HttpServletResponse to) throws IOException {
		if (from.getEntity() != null) {
			InputStream inputStream = from.getEntity().getContent();
			OutputStream outputStream = to.getOutputStream();
			int b;
			while ((b = inputStream.read()) != -1) {
				outputStream.write(b);
			}
			outputStream.flush();
		}
	}
}