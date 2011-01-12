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

package org.geomajas.servlet;

import org.geomajas.service.DispatcherUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * {@link org.geomajas.service.DispatcherUrlService} which tries to automatically detect the dispatcher server address.
 *
 * @author Joachim Van der Auwera
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AutomaticDispatcherUrlService implements DispatcherUrlService {

	private Logger log = LoggerFactory.getLogger(AutomaticDispatcherUrlService.class);

	public String getDispatcherUrl() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (null == requestAttributes || !(requestAttributes instanceof ServletRequestAttributes)) {
			log.warn("Trying to automatically get the dispatcher URL, but not running inside a servlet request. " +
					"You are recommended to use StaticDispatcherUrlService");
			return "./d/"; // use relative URL as back-up, will fail in many cases
		}

		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		String url = request.getScheme() + "://" + request.getServerName();
		if (80 != request.getServerPort()) {
			url += ":" + request.getServerPort();
		}
		String cp = request.getContextPath();
		if (null != cp && cp.length() > 0) {
			url += "/" + request.getContextPath();
		}
		url += "/d/";
		return url;
	}
}
