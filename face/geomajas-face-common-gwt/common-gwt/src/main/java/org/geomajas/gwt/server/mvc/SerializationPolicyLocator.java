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
package org.geomajas.gwt.server.mvc;

import javax.servlet.http.HttpServletRequest;

import org.geomajas.annotation.Api;

import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * Interface implemented by custom GWT-RPC serialization policy locators for {@link GeomajasController}.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api(allMethods = true)
public interface SerializationPolicyLocator {

	/**
	 * Loads a GWT-RPC serialization policy for the specified request, module base URL and strong name.
	 * 
	 * @param request the request
	 * @param moduleBaseURL the module base URL
	 * @param strongName the strong name
	 * @return the policy the policy
	 */
	SerializationPolicy loadPolicy(HttpServletRequest request, String moduleBaseURL, String strongName);
}
