/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * {@link Filter} that alters response in two ways: by adding cache control headers and by compressing the response.
 * This has been tuned toward GWT file-naming. Files that should not be cached are the following:
 * <ul>
 * <li>All files that contain ".nocache." in their name.</li>
 * </ul>
 * Files that should be cached included the following:
 * <ul>
 * <li>All files that contain ".cache." in their name.</li>
 * <li>All javascript files.</li>
 * <li>All CSS files.</li>
 * <li>All HTML files.</li>
 * <li>All image files.</li>
 * </ul>
 * Files that should be compressed include the following:
 * <ul>
 * <li>All files that contain ".nocache." in their name.</li>
 * <li>All files that contain ".cache." in their name.</li>
 * <li>All javascript files.</li>
 * <li>All CSS files.</li>
 * <li>All HTML files.</li>
 * </ul>
 * 
 * @author Pieter De Graef
 */
public class CacheFilter implements Filter {

	private static final long CACHE_DURATION_IN_SECOND = 60 * 60 * 24 * 365; // One year

	private static final long CACHE_DURATION_IN_MS = CACHE_DURATION_IN_SECOND * 1000;

	private static final String HTTP_LAST_MODIFIED_HEADER = "Last-Modified";

	private static final String HTTP_EXPIRES_HEADER = "Expires";

	private static final String HTTP_CACHE_CONTROL_HEADER = "Cache-Control";

	private static final String HTTP_CACHE_PRAGMA = "Pragma";

	private static final String[] NO_CACHE = new String[] { "/**/*.nocache.*" };

	private static final String[] TO_CACHE = new String[] { "/**/*.cache.*", "/**/*.js", "/**/*.png", "/**/*.jpg",
			"/**/*.gif", "/**/*.css", "/**/*.html" };

	private static final String[] TO_ZIP = new String[] { "/**/*.nocache.*", "/**/*.cache.*", "/**/*.js", "/**/*.css",
			"/**/*.html" };

	// ------------------------------------------------------------------------
	// Filter implementation:
	// ------------------------------------------------------------------------

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requestUri = httpRequest.getRequestURI();

		/*
		if ("localhost".equals(httpRequest.getServerName()) || "127.0.0.1".equals(httpRequest.getServerName())) {
			filterChain.doFilter(request, response);
			return;
		}
		*/

		if (shouldNotCache(requestUri)) {
			configureNoCaching(httpResponse);
		} else if (shouldCache(requestUri)) {
			configureCaching(httpResponse);
		}

		if (shouldCompress(requestUri)) {
			String encodings = httpRequest.getHeader("Accept-Encoding");
			if (encodings != null && encodings.indexOf("gzip") != -1) {
				GzipServletResponseWrapper responseWrapper = new GzipServletResponseWrapper(httpResponse);
				try {
					filterChain.doFilter(request, responseWrapper);
				} finally {
					responseWrapper.finish();
				}
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}

	public boolean shouldCache(String requestUri) {
		return check(requestUri, TO_CACHE);
	}

	public boolean shouldNotCache(String requestUri) {
		return check(requestUri, NO_CACHE);
	}

	public boolean shouldCompress(String requestUri) {
		return check(requestUri, TO_ZIP);
	}

	public boolean check(String requestUri, String[] patterns) {
		boolean res = false;
		PathMatcher pathMatcher = new AntPathMatcher();
		for (String pattern : patterns) {
			if (pathMatcher.match(pattern, requestUri)) {
				res = true;
			}
		}
		return res;
	}

	// ------------------------------------------------------------------------
	// Private methods for setting cache/no-cache:
	// ------------------------------------------------------------------------

	private void configureNoCaching(HttpServletResponse response) {
		long now = System.currentTimeMillis();
		response.setDateHeader("Date", now);

		// HTTP 1.0 header:
		response.setDateHeader(HTTP_EXPIRES_HEADER, now - 86400000L); // one day old
		response.setHeader(HTTP_CACHE_PRAGMA, "no-cache");

		// HTTP 1.1 header:
		response.setHeader(HTTP_CACHE_CONTROL_HEADER, "no-cache");
	}

	private void configureCaching(HttpServletResponse response) {
		long now = System.currentTimeMillis();
		response.setDateHeader(HTTP_LAST_MODIFIED_HEADER, now);

		// HTTP 1.0 header
		response.setDateHeader(HTTP_EXPIRES_HEADER, now + CACHE_DURATION_IN_MS);

		// HTTP 1.1 header
		response.setHeader(HTTP_CACHE_CONTROL_HEADER, "max-age=" + CACHE_DURATION_IN_SECOND);
	}

	// ------------------------------------------------------------------------
	// Private class GzipServletResponseWrapper
	// ------------------------------------------------------------------------

	/**
	 * Wrapper around a response that uses a GZIP stream.
	 * 
	 * @author Pieter De Graef
	 */
	private class GzipServletResponseWrapper extends HttpServletResponseWrapper {

		private ServletOutputStream stream;

		public GzipServletResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		public void finish() {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}

		public ServletOutputStream getOutputStream() throws IOException {
			if (stream == null) {
				stream = new GzipResponseStream((HttpServletResponse) getResponse());
			}
			return stream;
		}
	}
}
