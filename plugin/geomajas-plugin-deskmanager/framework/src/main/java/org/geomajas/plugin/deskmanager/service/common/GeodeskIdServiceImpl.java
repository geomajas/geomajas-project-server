/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

//FIXME: move to other services

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GeodeskIdServiceImpl implements GeodeskIdService {

	/**
	 * Attention! Update set value in urlrewrite.xml. 
	 */
	private static final String DESK_ID = "desk";

	public String getGeodeskIdentifier() {
		RequestAttributes a = RequestContextHolder.getRequestAttributes();
		if (a != null) {
			return (String) a.getAttribute(DESK_ID, RequestAttributes.SCOPE_REQUEST);
		}
		return null;
	}

}
