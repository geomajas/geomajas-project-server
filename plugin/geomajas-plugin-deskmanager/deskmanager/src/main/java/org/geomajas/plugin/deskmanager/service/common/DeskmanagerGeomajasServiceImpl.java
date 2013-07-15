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
package org.geomajas.plugin.deskmanager.service.common;

import javax.servlet.http.HttpServletRequest;

import org.geomajas.gwt.server.GeomajasServiceImpl;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;

import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * Hack for. http://stackoverflow.com/questions/1517290/problem-with-gwt-behind-a-reverse-proxy-either-nginx-or-apache
 * http://code.google.com/p/google-web-toolkit/issues/detail?id=4817
 * 
 * @author Oliver May
 * 
 */
public class DeskmanagerGeomajasServiceImpl extends GeomajasServiceImpl {

	private static final long serialVersionUID = 1L;

	@Override
	protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL,
			String strongName) {
		// get the base url from the header instead of the body this way
		// apache reverse proxy with rewrite on the header can work
		String moduleBaseURLHdr = request.getHeader("X-GWT-Module-Base");
		if (moduleBaseURLHdr != null) {
			moduleBaseURL = moduleBaseURLHdr;
		}

		moduleBaseURL = moduleBaseURL.replaceFirst(GdmLayout.geodeskPrefix + "[^/]*", "");
		moduleBaseURL = moduleBaseURL.replaceFirst(GdmLayout.managerPrefix + "[^/]*", "");
		return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
	}

}
