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

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration object for the proxy servlet.
 * 
 * @author Pieter De Graef
 */
public class ConnectionConfiguration {

	// Connection parameters:

	private String targetUrl;

	private int connectionTimeout;

	private int maxConnectionsPerRoute;

	private int maxConnectionsTotal;

	private String proxyHost;

	private Integer proxyPort;

	private final Logger log = LoggerFactory.getLogger(ConnectionConfiguration.class);

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMaxConnectionsPerRoute() {
		return maxConnectionsPerRoute;
	}

	public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
		this.maxConnectionsPerRoute = maxConnectionsPerRoute;
	}

	public int getMaxConnectionsTotal() {
		return maxConnectionsTotal;
	}

	public void setMaxConnectionsTotal(int maxConnectionsTotal) {
		this.maxConnectionsTotal = maxConnectionsTotal;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	/** @return Returns a socket factory for plain http. */

	public SchemeSocketFactory getHTTPSocketFactory() {
		return PlainSocketFactory.getSocketFactory();
	}

	/**
	 * @return returns a HTTPS socket factory, that allows any certificate, whether it's valid or not.
	 * @throws NoSuchAlgorithmException
	 *             - very bad!
	 * @throws KeyManagementException
	 *             - even worse, shape on you!
	 */
	public SchemeSocketFactory getHTTPSSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {

		TrustManager easyTrustManager = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// Oh, I am easy!
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// Oh, I am easy!
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);

		// The following sslSocketFactory is created to connect to hosts with self signed https certificates.
		SSLSocketFactory sslSocketFactory;

		log.debug("Creating SSLSocketFactory");
		sslSocketFactory = new SSLSocketFactory(sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		return sslSocketFactory;
	}
}