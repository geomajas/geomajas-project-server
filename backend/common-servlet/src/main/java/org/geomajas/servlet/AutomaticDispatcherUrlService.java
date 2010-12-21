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
