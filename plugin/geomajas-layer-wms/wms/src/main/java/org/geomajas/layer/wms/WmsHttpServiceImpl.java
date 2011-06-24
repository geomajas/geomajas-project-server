/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.wms;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of {@link WmsHttpService}.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class WmsHttpServiceImpl implements WmsHttpService {

	private static final String URL_PARAM_START = "?";
	private static final String URL_PARAM_SEPARATOR = "&";
	private static final String URL_PARAM_IS = "=";
	private static final String URL_PROTOCOL_SEPARATOR = "://";
	private static final int TIMEOUT = 5000;

	public String addCredentialsToUrl(String url, WmsAuthentication authentication) {
		if (null != authentication && WmsAuthenticationMethod.URL.equals(authentication.getAuthenticationMethod())) {
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

	public InputStream getStream(String url, WmsAuthentication authentication) throws IOException {
		// Create a HTTP client object, which will initiate the connection:
		HttpClient client = new HttpClient();
		client.setConnectionTimeout(TIMEOUT);

		// Preemptive: In this mode HttpClient will send the basic authentication response even before the server
		// gives an unauthorized response in certain situations, thus reducing the overhead of making the
		// connection.
		client.getState().setAuthenticationPreemptive(true);

		// Set up the WMS credentials:
		Credentials credentials = new UsernamePasswordCredentials(authentication.getUser(),
				authentication.getPassword());
		client.getState().setCredentials(authentication.getRealm(), parseDomain(url), credentials);

		// Create the GET method with the correct URL:
		GetMethod get = new GetMethod(url);
		get.setDoAuthentication(true);

		// Execute the GET:
		client.executeMethod(get);

		return new WmsHttpServiceStream(get);
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
	 * Delegating input stream which also closes the HTTP connection when closing the stream.
	 *
	 * @author Joachim Van der Auwera
	 */
	private class WmsHttpServiceStream extends InputStream {

		private GetMethod get;
		private InputStream inputStream;

		public WmsHttpServiceStream(GetMethod getMethod) throws IOException {
			this.get = getMethod;
			this.inputStream = get.getResponseBodyAsStream();
		}

		@Override
		public int read() throws IOException {
			return inputStream.read();
		}

		@Override
		public int read(byte[] bytes) throws IOException {
			return inputStream.read(bytes);
		}

		@Override
		public int read(byte[] bytes, int i, int i1) throws IOException {
			return inputStream.read(bytes, i, i1);
		}

		@Override
		public long skip(long l) throws IOException {
			return inputStream.skip(l);
		}

		@Override
		public int available() throws IOException {
			return inputStream.available();
		}

		@Override
		public void close() throws IOException {
			inputStream.close();
			get.releaseConnection();
		}

		@Override
		public void mark(int i) {
			inputStream.mark(i);
		}

		@Override
		public void reset() throws IOException {
			inputStream.reset();
		}

		@Override
		public boolean markSupported() {
			return inputStream.markSupported();
		}
	}
}
