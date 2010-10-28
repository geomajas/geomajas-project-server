package org.geomajas.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	private final String[] toCache = new String[] { ".cache.", "ISC_", ".png", ".jpg", ".gif" };

	// ------------------------------------------------------------------------
	// Filter implementation:
	// ------------------------------------------------------------------------

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		String requestUri = ((HttpServletRequest) request).getRequestURI();

		boolean notCached = false;
		for (String noc : noCache) {
			// Should we set the "no-cache" headers?
			if (requestUri.contains(noc)) {
				configureNoCaching((HttpServletResponse) response);
				notCached = true;
				continue;
			}
		}

		if (!notCached) {
			// Only check for cache headers, if no-cache hasn't been set:
			for (String c : toCache) {
				// Should we set the "cache" headers?
				if (requestUri.contains(c)) {
					configureCaching((HttpServletResponse) response);
					continue;
				}
			}
		}

		filterChain.doFilter(request, response);
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
}