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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

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

/**
 * {@link Filter} to add cache control headers for caching all files comply to one of the following:
 * <ul>
 * <li>contains: .cache.</li>
 * <li>contains: ISC_</li>
 * <li>ends with: .png</li>
 * <li>ends with: .jpg</li>
 * <li>ends with: .gif</li>
 * </ul>
 * All files containing ".nocache." will not be cached.
 * 
 * @author Pieter De Graef
 */
public class CacheFilter implements Filter {

	private static final long CACHE_DURATION_IN_SECOND = 60 * 60 * 24 * 3650; // One year

	private static final long CACHE_DURATION_IN_MS = CACHE_DURATION_IN_SECOND * 1000;

	private static final String HTTP_LAST_MODIFIED_HEADER = "Last-Modified";

	private static final String HTTP_EXPIRES_HEADER = "Expires";

	private static final String HTTP_CACHE_CONTROL_HEADER = "Cache-Control";

	private static final String HTTP_CACHE_PRAGMA = "Pragma";

	private final String[] noCache = new String[] { ".nocache." };

	private final String[] toCache = new String[] { ".cache.", ".js", ".png", ".jpg", ".gif", ".css", ".html" };

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

		boolean notCached = false;
		boolean tryToCompress = false;
		String requestUri = httpRequest.getRequestURI();

		for (String noc : noCache) {
			// Should we set the "no-cache" headers?
			if (requestUri.contains(noc)) {
				configureNoCaching(httpResponse);
				notCached = true;
				tryToCompress = true;
				continue;
			}
		}

		if (!notCached) {
			// Only check for cache headers, if no-cache hasn't been set:
			for (String c : toCache) {
				// Should we set the "cache" headers?
				if (requestUri.contains(c)) {
					configureCaching(httpResponse);
					tryToCompress = true;
					continue;
				}
			}
		}

		// Check if the file needs compression:
		String encodings = httpRequest.getHeader("Accept-Encoding");
		if (tryToCompress && encodings != null && encodings.indexOf("gzip") != -1) {
			GzipServletResponseWrapper responseWrapper = new GzipServletResponseWrapper(httpResponse);
			try {
				filterChain.doFilter(request, responseWrapper);
			} finally {
				responseWrapper.finish();
			}
		} else {
			filterChain.doFilter(request, response);
		}
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
	// Private class GzipResponseStream (stolen from the ResourceServlet)
	// ------------------------------------------------------------------------

	/**
	 * Assure resources are gzip compressed.
	 * 
	 * @author Pieter De Graef
	 */
	private class GzipResponseStream extends ServletOutputStream {

		private ByteArrayOutputStream byteStream;

		private GZIPOutputStream gzipStream;

		private boolean closed;

		private HttpServletResponse response;

		private ServletOutputStream servletStream;

		public GzipResponseStream(HttpServletResponse response) throws IOException {
			super();
			closed = false;
			this.response = response;
			this.servletStream = response.getOutputStream();
			byteStream = new ByteArrayOutputStream();
			gzipStream = new GZIPOutputStream(byteStream);
		}

		public void close() throws IOException {
			if (closed) {
				throw new IOException("This output stream has already been closed");
			}
			gzipStream.finish();

			byte[] bytes = byteStream.toByteArray();

			response.setContentLength(bytes.length);
			response.addHeader("Content-Encoding", "gzip");
			servletStream.write(bytes);
			servletStream.flush();
			servletStream.close();
			closed = true;
		}

		public void flush() throws IOException {
			if (closed) {
				throw new IOException("Cannot flush a closed output stream");
			}
			gzipStream.flush();
		}

		public void write(int b) throws IOException {
			if (closed) {
				throw new IOException("Cannot write to a closed output stream");
			}
			gzipStream.write((byte) b);
		}

		public void write(byte[] b) throws IOException {
			write(b, 0, b.length);
		}

		public void write(byte[] b, int off, int len) throws IOException {
			if (closed) {
				throw new IOException("Cannot write to a closed output stream");
			}
			gzipStream.write(b, off, len);
		}
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