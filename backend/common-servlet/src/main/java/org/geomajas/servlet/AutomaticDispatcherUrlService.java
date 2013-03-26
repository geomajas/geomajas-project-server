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

package org.geomajas.servlet;

import javax.servlet.http.HttpServletRequest;

import org.geomajas.annotation.Api;
import org.geomajas.service.DispatcherUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * {@link org.geomajas.service.DispatcherUrlService} which tries to automatically detect the dispatcher server address.
 *
 * @author Joachim Van der Auwera
 * @author Oliver May
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Component
@Api
public class AutomaticDispatcherUrlService implements DispatcherUrlService {

	private static final long serialVersionUID = 110L;

	private static final String X_FORWARD_HOST_HEADER = "X-Forwarded-Host";
	private static final String X_GWT_MODULE_HEADER = "X-GWT-Module-Base";

	private final Logger log = LoggerFactory.getLogger(AutomaticDispatcherUrlService.class);
	
	private String localDispatcherUrl;

	/** {@inheritDoc} */
	public String getDispatcherUrl() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (null == requestAttributes || !(requestAttributes instanceof ServletRequestAttributes)) {
			log.warn("Trying to automatically get the dispatcher URL, but not running inside a servlet request. " +
					"You are recommended to use StaticDispatcherUrlService");
			return "./d/"; // use relative URL as back-up, will fail in many cases
		}

		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

		String serverName = request.getServerName();

		// X-Forwarded-Host if behind a reverse proxy, fallback to general method.
		// Alternative we could use the gwt module url to guess the real URL.
		if (null != request.getHeader(X_FORWARD_HOST_HEADER)) {
			log.warn("AutomaticDispatcherService detected a X-Forwarded-Host header which means the server is " +
					"accessed using a reverse proxy server. This might cause problems in some cases. You are " +
					"recommended to configure your tomcat connector to be aware of the original url. " +
					"(see http://tomcat.apache.org/tomcat-6.0-doc/proxy-howto.html )");
			String gwtModuleBase = request.getHeader(X_GWT_MODULE_HEADER);
			if (null != gwtModuleBase) {
				// Get last slash in the gwtModuleBase, ignoring the trailing slash.
				int contextEndIndex = gwtModuleBase.lastIndexOf("/", gwtModuleBase.length() - 2);
				if (contextEndIndex > -1) {
					return gwtModuleBase.substring(0, contextEndIndex) + "/d/";
				}
			} else {
				// else get the information from the X-forwarded-host header and default to the standard behaviour 
				serverName = request.getHeader(X_FORWARD_HOST_HEADER);
			}
		}

		return getBasePathForHostNamePort(request, serverName, request.getServerPort());
	}

	/** {@inheritDoc} */
	public String getLocalDispatcherUrl() {
		if (localDispatcherUrl == null) {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return getBasePathForHostNamePort(request, request.getLocalName(), request.getLocalPort());
		} else {
			return localDispatcherUrl;
		}
	}

	/**
	 * Set the local base URL for the dispatcher service.
	 * 
	 * @param localDispatcherUrl the local base URL
	 * @since 1.10.0
	 */
	@Api
	public void setLocalDispatcherUrl(String localDispatcherUrl) {
		this.localDispatcherUrl = localDispatcherUrl;
	}

	/** {@inheritDoc} */
	public String localize(String externalUrl) {
		String localBase = getLocalDispatcherUrl();
		String dispatcherBase = getDispatcherUrl();
		if (externalUrl.startsWith(dispatcherBase)) {
			return localBase + externalUrl.substring(dispatcherBase.length());
		} else {
			// not a dispatcher url, return the original one
			return externalUrl;
		}
	}
	
	private String getBasePathForHostNamePort(HttpServletRequest request, String hostName, int port) {
		StringBuilder url = new StringBuilder();
		url.append(request.getScheme());
		url.append("://");
		url.append(hostName);
		if (80 != port) {
			url.append(":");
			url.append(Integer.toString(port));
		}
		String cp = request.getContextPath();
		if (null != cp && cp.length() > 0) {
			url.append(request.getContextPath());
		}
		url.append("/d/");
		return url.toString();
	}

}
