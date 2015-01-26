/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.security.BaseAuthorization;

/**
 * Authorization info, which needs to be convertible to a {@link org.geomajas.security.BaseAuthorization} class.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface AuthorizationInfo {

	/**
	 * Get authorization object.
	 *
	 * @return authorization
	 */
	BaseAuthorization getAuthorization();
}
